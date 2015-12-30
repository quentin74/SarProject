package messages.broadcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
	//private HashMap<SelectionKey, ChannelPingPong> listeServerChannel;
	//private ServerPingPong server = null;
	
	private DeliverCallback dc = new DeliverCallBack();
	
	public EnginePingPong() {
		//this.listeServerChannel = new HashMap<SelectionKey, ChannelPingPong>();
		
		try {
			// Create a new selector
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
		    		Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
		    		
		    		while (selectedKeys.hasNext()) {
		    			SelectionKey key = (SelectionKey) selectedKeys.next();
		    			selectedKeys.remove();
		    		    if (!key.isValid()) { 
		    		    	System.out.println("[ERREUR] Key invalide");
		    		    	System.exit(-1);
		    		    } // Handle the event
		    		    else if (key.isAcceptable()){
		    		    	// a connection was accepted by a ServerSocketChannel.
		    		    	handleAccept(key);
		    		    } else if (key.isConnectable()){
		    		    	// a connection was established with a remote server.
		    		    	handleConnect(key);
		    		    } else if (key.isReadable()){
		    		    	// a channel is ready for reading
		    		    	handleRead(key);
		    		    } else if (key.isWritable()) {
		    		    	// a channel is ready for writing
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

	    // For an accept to be pending the channel must be a server socket channel.
		ServerSocketChannel serverSocketChannel =  (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = null;
		ChannelPingPong ch = null;
		SelectionKey m_key = null;
		
		try {
			System.out.println("handleAccept");
			
			// Accept the connection and make it non-blocking
		    socketChannel = serverSocketChannel.accept();
		    socketChannel.configureBlocking(false);
		    
		    //Channel created between server and client 
			ch = new ChannelPingPong(socketChannel);
		    
		    // Register the new SocketChannel with our Selector, indicating
		    // we'd like to be notified when there's data waiting to be read
		    m_key = socketChannel.register(this.selector, SelectionKey.OP_READ,ch);
		   
			//AcceptCallback : Callback to notify about an accepted connection
			AcceptCallback ac =  ((ServerPingPong)key.attachment()).getAcceptCallback();
			ac.accepted(((ServerPingPong)key.attachment()),ch);	
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Coté client
	private void handleConnect(SelectionKey key) {
		SocketChannel socketChannel =  (SocketChannel) key.channel();
		
		try {
			System.out.println("handleConnect");
			// Change interest
			socketChannel.register(selector, SelectionKey.OP_WRITE);			
			//ConnectCallback : Callback to notify about an connection channel has succeeded
			ConnectCallback cc = ((ConnectCallback) key.attachment());
			cc.connected(((ChannelPingPong) key.attachment()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void handleRead(SelectionKey key) {
		
		// Change interest
		SocketChannel socketChannel = (SocketChannel) key.channel();
		((ChannelPingPong) key.attachment()).read();
		dc.deliver(((ChannelPingPong) key.attachment()), ((ChannelPingPong) key.attachment()).writeBuffer());
		//dc.deliver(listeServerChannel.get(key),listeServerChannel.get(key).writeBuffer());
		try {
			socketChannel.register(selector, SelectionKey.OP_WRITE);
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleWrite(SelectionKey key) {
		boolean end =  ((ChannelPingPong) key.attachment()).write();// listeServerChannel.get(key).write();
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
		System.out.println("listen");
		SelectionKey m_key = null;
		// Creation du Server "socket bind au port"
		ServerPingPong server = new ServerPingPong(port, callback);
	
		// Register the server socket channel, indicating an interest in 
	    // accepting new connections
		m_key = server.getServerSocket().register(selector, SelectionKey.OP_ACCEPT,server);
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
					System.out.println("connect");
					socketChannel = SocketChannel.open();
					// You can set a SocketChannel into non-blocking mode. When you do so, you can call connect(), read() and write() in asynchronous mode. 
					socketChannel.configureBlocking(false);
					// connect socketChannel to a remote SocketAddress
					socketChannel.connect(new InetSocketAddress(hostAddress, port));
										
					while(! socketChannel.finishConnect() ){
					    //wait, or do something else...
					}// Passe dans le handleAccept
					
					System.out.println("Initialisation Client "+socketChannel.socket().getLocalAddress()+":"+socketChannel.socket().getLocalPort()+" accept connection to "+ hostAddress+":"+port);
					m_key = socketChannel.register(selector, SelectionKey.OP_CONNECT,callback);
				}catch(IOException e){
					System.out.println("[ERREUR] Lors de la connexion de la socket channel");
					e.printStackTrace();
				}
				
				
					
						
	}

}
