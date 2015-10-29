package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.javafx.collections.MappingChange.Map;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.DeliverCallback;
import messages.engine.Engine;
import messages.engine.Server;

public class EnginePingPong extends Engine {
	public Selector selector;
	public HashMap<SelectionKey, ServerPingPong> listeServer;
	public HashMap<SelectionKey, ClientPingPong> listeClient;
	public HashMap<SelectionKey, ChannelPingPong> listeServerChannel;
	
	public EnginePingPong() {
		this.listeServer = new HashMap<SelectionKey, ServerPingPong>();
		this.listeClient = new HashMap<SelectionKey, ClientPingPong>();
		this.listeServerChannel = new HashMap<SelectionKey, ChannelPingPong>();
		
		try {
			this.selector = SelectorProvider.provider().openSelector();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void mainloop() {
		    for (;;) {
		    	try {
		    		// Wait for an event one of the registered channels
		    		selector.select();    		
		    		// Some events have been received
		    		Iterator<?> selectedKeys = selector.selectedKeys().iterator();
		    		
		    		while (selectedKeys.hasNext()) {
		    			SelectionKey key = (SelectionKey) selectedKeys.next();
		    		    selectedKeys.remove();
		    		    if (!key.isValid()) { 
		    		    	System.out.println("[ERREUR] Key invalide");
		    		    	System.exit(-1);
		    		    } // Handle the event
		    		    else if (key.isAcceptable()){
		    		    	handleAccept(key);
		    		    } else if (key.isConnectable()){
		    		    	handleConnect(key);
		    		    } else if (key.isReadable()){
		    		    	handleRead(key);
		    		    } else if (key.isWritable()) {
		    		    	handleWrite(key);
		    		    } else {
							System.out.println("[ERREUR] Key inconnu");
							System.exit(-1);
						}
		    		    	
		    		}
		    	} catch (IOException e) { 
		    		e.printStackTrace();	    		
		    	}	    	  
		    }
	}

	private void handleAccept(SelectionKey key) {
		ServerSocketChannel serverSocketChannel =  (ServerSocketChannel) key.channel();
	    SocketChannel socketChannel = null;
	    SelectionKey m_key = null;
		try {
			// Accept the connection and make it non-blocking
			socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);	
			
			// Notifie "EN ATTENTE DE DONNEES"
			m_key = socketChannel.register(selector, SelectionKey.OP_READ);
			
			// Création d'un Channel entre Client et Server
			ChannelPingPong ch = new ChannelPingPong(socketChannel);
			
			//AcceptCallback : Callback to notify about an accepted connection
			AcceptCallback ac = listeServer.get(key).getAcceptCallback();
			ac.accepted(listeServer.get(m_key),ch);		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleConnect(SelectionKey key) {
		SocketChannel socketChannel =  (SocketChannel) key.channel();
	    
		// finish the connection   
		try {
			socketChannel.finishConnect();
			// Change interest
			socketChannel.register(selector, SelectionKey.OP_WRITE); //key.interestOps(SelectionKey.OP_WRITE);
			
			//ConnectCallback : Callback to notify about an connection channel has succeeded
			ConnectCallback cc = listeClient.get(key).getConnectCallback();
			cc.connected(listeServerChannel.get(key));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void handleRead(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		listeServerChannel.get(key).read();
	}
	
	private void handleWrite(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		listeServerChannel.get(key).write();
		key.interestOps(SelectionKey.OP_READ);
	}

	
	public Server listen(int port, AcceptCallback callback) throws IOException {
		SelectionKey m_key = null;
		ServerPingPong s = null;
		
		// Création du Server
		s = new ServerPingPong(port, callback);				
		m_key = s.getServerSocket().register(selector, SelectionKey.OP_ACCEPT);
		listeServer.put(m_key, s);

		return s;
	}

	/**
	   * Ask this NioEngine to connect to the given port on the given host.
	   * The callback will be notified when the connection will have succeeded.
	   * @param hostAddress
	   * @param port
	   * @param callback
	   */
	public void connect(InetAddress hostAddress, int port, ConnectCallback callback) throws UnknownHostException, SecurityException, IOException {
		SelectionKey m_key = null;
		ClientPingPong c = null;
		
		c = new ClientPingPong(hostAddress, port, callback);
		m_key = c.getSocketChannel().register(selector, SelectionKey.OP_CONNECT);
		//Stockage dans la HashMap listeChannel
		listeClient.put(m_key, c);
	}

}
