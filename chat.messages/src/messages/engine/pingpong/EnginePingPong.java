package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.Engine;
import messages.engine.Server;

public class EnginePingPong extends Engine {
	public Selector m_selector;
	public SelectionKey m_key;
	public Channel channel = new ChannelPingPong();
	public Server server = new ServerPingPong();

	@Override
	public void mainloop() {
		    for (;;) {
		    	try {
		    		// Wait for an event one of the registered channels
		    		m_selector.select();
		    		
		    		// Some events have been received
		    		Iterator selectedKeys = m_selector.selectedKeys().iterator();
		    		while (selectedKeys.hasNext()) {
		    			SelectionKey key = (SelectionKey) selectedKeys.next();
		    		    selectedKeys.remove();
		    		    if (!key.isValid()) { 
		    		    	System.err.println(">>> Ping:  ---> readable key=" + key);
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
		    		    }
		    		}
		    	} catch (IOException e) { return; }	    	  
	}
}

private void handleAccept(SelectionKey key) {
	ServerSocketChannel serverChannel =  (ServerSocketChannel) key.channel();
	// Accept the connection and make it non-blocking
    SocketChannel socketChannel = null;
	try {
		socketChannel = serverChannel.accept();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    // Register the new SocketChannel with our Selector, indicating
    // we'd like to be notified when there's data waiting to be read
    try {
		socketChannel.register(m_selector, SelectionKey.OP_READ);
	} catch (ClosedChannelException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
}

private void handleConnect(SelectionKey key) {
	SocketChannel socketChannel =  (SocketChannel) key.channel();
    // finish the connection
    try {
		socketChannel.finishConnect();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    // register the read interest on the selector"
    try {
		socketChannel.register(m_selector, SelectionKey.OP_READ);
	} catch (ClosedChannelException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}		
}

private void handleRead(SelectionKey key) {
	
		
	}

private void handleWrite(SelectionKey key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Server listen(int port, AcceptCallback callback) throws IOException {
		m_selector = SelectorProvider.provider().openSelector();
		
		// Create a new non-blocking server socket channel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		// bind serverSocket with to a specific port
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(new InetSocketAddress(port));
						
		// Be notified when connection requests arrive 
		serverSocketChannel.register(m_selector, SelectionKey.OP_ACCEPT);
		
		//AcceptCallback : Callback to notify about an accepted connection
		//callback.accepted(server,channel); 
		//callback.accepted(, serverSocket.getChannel());
		
		return server;
	}


	@Override
	public void connect(InetAddress hostAddress, int port, ConnectCallback callback) throws UnknownHostException, SecurityException, IOException {
		m_selector = SelectorProvider.provider().openSelector();
		// create a non-blocking socket channel
		SocketChannel clientSocketChannel = SocketChannel.open();
		clientSocketChannel.configureBlocking(false);
		clientSocketChannel.socket().setTcpNoDelay(true);
	    // be notified when the connection to the server will be accepted
	    m_key = clientSocketChannel.register(m_selector, SelectionKey.OP_CONNECT);

	    // request to connect to the server
	    clientSocketChannel.connect(new InetSocketAddress(hostAddress, port));
	    //callback ???
	}

}
