package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;

import messages.engine.AcceptCallback;
import messages.engine.ConnectCallback;
import messages.engine.DeliverCallback;
import messages.engine.Engine;
import messages.engine.Server;

public class EnginePingPong extends Engine {
	private Selector selector ;
	private HashMap<SelectionKey, ChannelPingPong> listeServerChannel;
	private ServerPingPong server = null;
	private ClientPingPong client = null; 
	
	private DeliverCallback dc = new DeliverCallBack();
	
	public EnginePingPong() {
		// create the list of channelPingPong
		this.listeServerChannel = new HashMap<SelectionKey, ChannelPingPong>();
		try {
			this.selector = SelectorProvider.provider().openSelector();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * this loop provide all the event case, for example, if a client is in a connect state, or in a write state
	 */
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
		    		    	System.out.println("[ERREUR] invalid key");
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
							System.out.println("[ERREUR] unknown key");
							System.exit(-1);
						}
		    		    	
		    		}
		    	} catch (IOException e) { 
		    		e.printStackTrace();	    		
		    	}	    	  
		    }
	}

	/**
	 * this is the part corresponding to the server
	 * @param key to know in wich state we are 
	 */
	private void handleAccept(SelectionKey key) {
		ServerSocketChannel serverSocketChannel =  (ServerSocketChannel) key.channel();
	    SocketChannel socketChannel = null;
	    SelectionKey m_key = null;
		try {
			// Accept the connection and make it non-blocking
			socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);	
			
			// Notification "WAITING FOR DATA"
			m_key = socketChannel.register(selector, SelectionKey.OP_READ);
			
			//Channel created between server and client 
			ChannelPingPong ch = new ChannelPingPong(socketChannel);
			//Add a ServerChannel 
			listeServerChannel.put(m_key,ch);
			
			//AcceptCallback : Callback to notify about an accepted connection
			AcceptCallback ac =  server.getAcceptCallback();
			ac.accepted(server,ch);	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * To know if we are in the connection state
	 * @param key to know in wich state we are 
	 */
	private void handleConnect(SelectionKey key) {
		SocketChannel socketChannel =  (SocketChannel) key.channel();
	    // finish the connection   
		try {
			socketChannel.finishConnect();
			// Change interest
			socketChannel.register(selector, SelectionKey.OP_WRITE);
			//ConnectCallback : Callback to notify about an connection channel has succeeded
			ConnectCallback cc = client.getConnectCallback();
			cc.connected(listeServerChannel.get(key));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * this methode is used to do everithing in the read state ( reading a message from the client)
	 * @param key to know in wich state we are
	 */
	private void handleRead(SelectionKey key) {
		// Change interest
		SocketChannel socketChannel = (SocketChannel) key.channel();
		listeServerChannel.get(key).read();
		dc.deliver(listeServerChannel.get(key),listeServerChannel.get(key).writeBuffer());
		try {
			socketChannel.register(selector, SelectionKey.OP_WRITE);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}
	}
	/**
	 * this methode is used to do everithing in the write state ( writing a message from the client)
	 * @param key to know in wich state we are
	 */
	private void handleWrite(SelectionKey key) {
		boolean end = listeServerChannel.get(key).write();
		SocketChannel socketChannel = (SocketChannel) key.channel();
		if (end){
			// STOP WRITING Change interest
			try {
				socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		}
		
		 
	}

	/**
	 * @param port 
	 * @param callback
	 * this methode is use at the creation of the server
	 */
	public Server listen(int port, AcceptCallback callback) throws IOException {
		SelectionKey m_key = null;
		// Creation du Server
		server = new ServerPingPong(port, callback);				
		m_key = server.getServerSocket().register(selector, SelectionKey.OP_ACCEPT);
		return server;
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
		client = new ClientPingPong(hostAddress, port, callback);
		m_key = client.getSocketChannel().register(selector, SelectionKey.OP_CONNECT);
		
		// channel created between client and server
		ChannelPingPong ch = new ChannelPingPong(client.getSocketChannel());
		//add to the server channel list
		listeServerChannel.put(m_key,ch);
		
	}

}
