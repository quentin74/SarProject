package messages.broadcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.DeliverCallback;
import messages.engine.Engine;
import messages.engine.Server;
import messages.broadcast.ChannelPingPong;

public class EnginePingPong extends Engine {
	private Selector selector ;
	private HashMap<SelectionKey, ChannelPingPong> listeServerChannel;
	private ServerPingPong server = null;
	
	private DeliverCallback dc = new DeliverCallBack();
	
	public EnginePingPong() {
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

	// Côté Server
	private void handleAccept(SelectionKey key) {
		ServerSocketChannel serverSocketChannel =  (ServerSocketChannel) key.channel();
	    SocketChannel socketChannel = null;
	    SelectionKey m_key = null;
		try {
				socketChannel = serverSocketChannel.accept();
				socketChannel.configureBlocking(false);	
			 
				//AcceptCallback : Callback to notify about an accepted connection
			    // Server notifie "PRET POUR LECTURE"
				m_key = socketChannel.register(selector, SelectionKey.OP_READ);
				
				// Creation d'un Channel entre Client et Server
				ConnectCallBack cc = new ConnectCallBack();
				ChannelPingPong ch = new ChannelPingPong(socketChannel,cc);
				//Ajout a la liste ServerChannel 
				listeServerChannel.put(m_key,ch);
				
				AcceptCallback ac =  server.getAcceptCallback();
				ac.accepted(server,ch);
					
				
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Coté client
	private void handleConnect(SelectionKey key) {
		SocketChannel socketChannel =  (SocketChannel) key.channel();
		
		// finish the connection   
		try {
			socketChannel.finishConnect();
			// Change interest
			socketChannel.register(selector, SelectionKey.OP_WRITE);
			
			//ConnectCallback : Callback to notify about an connection channel has succeeded
			ConnectCallback cc = listeServerChannel.get(key).getConnectCallback();
			cc.connected(listeServerChannel.get(key));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void handleRead(SelectionKey key) {
		// Change interest
		SocketChannel socketChannel = (SocketChannel) key.channel();
		listeServerChannel.get(key).read();
		dc.deliver(listeServerChannel.get(key),listeServerChannel.get(key).writeBuffer());
		try {
			socketChannel.register(selector, SelectionKey.OP_WRITE);
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleWrite(SelectionKey key) {
		boolean end = listeServerChannel.get(key).write();
		SocketChannel socketChannel = (SocketChannel) key.channel();
		if (end){
			// STOP WRITING Change interest
			try {
				socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (ClosedChannelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		 
	}

	 /**
	   * Ask for this NioEngine to accept connections on the given port,
	   * calling the given callback when a connection has been accepted.
	   * @param port
	   * @param callback
	   * @return an NioServer wrapping the server port accepting connections.
	   * @throws IOException if the port is already used or can't be bound.
	   */
	public ServerPingPong listen(int port, AcceptCallback callback) throws IOException {
		SelectionKey m_key = null;
		// Creation du Server "socket bind au port"
		this.server = new ServerPingPong(port, callback);
		    //SocketChannel socketChannel = server.getServerSocket().accept();
		   
		    	// Mise de l'interet à ACCEPT -> se déclenche si vérifié
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
		SocketChannel socketChannel = null;
		
		// create a non-blocking socket channel
				try{
					socketChannel = SocketChannel.open();
					// You can set a SocketChannel into non-blocking mode. When you do so, you can call connect(), read() and write() in asynchronous mode. 
					socketChannel.configureBlocking(false);
					// connect socketChannel to a remote SocketAddress
					socketChannel.connect(new InetSocketAddress(hostAddress, port));
					System.out.println("Initialisation Client "+socketChannel.socket());
					m_key = socketChannel.register(selector, SelectionKey.OP_CONNECT);
					// Creation d'un Channel entre Client et Server
					ChannelPingPong ch = new ChannelPingPong(socketChannel,callback);
					//Ajout a la liste ServerChannel 
					listeServerChannel.put(m_key,ch);
				}catch(IOException e){
					System.out.println("[ERREUR] Lors de la connexion de la socket channel");
					e.printStackTrace();
				}
				
				
					
						
	}

}
