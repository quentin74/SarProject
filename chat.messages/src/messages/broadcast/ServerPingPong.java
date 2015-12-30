package messages.broadcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import messages.engine.AcceptCallback;
import messages.engine.Server;

public class ServerPingPong extends Server{
	// The host:port combination to listen on
	private InetAddress hostAddress;
	private int port ;
	private AcceptCallBack ac = new AcceptCallBack();
	private ConnectCallBack cc ;
	private ServerSocketChannel serverSocketServer;
	private SocketChannel socketChannel = null;
	
	public ServerPingPong(int port, AcceptCallback callback) {
		try {
			this.hostAddress = InetAddress.getByName("127.0.0.42");
			this.port = port;		
			this.ac = (AcceptCallBack) callback;
			
			  // Create a new non-blocking server socket channel
			serverSocketServer = ServerSocketChannel.open();
			serverSocketServer.configureBlocking(false);
			// Bind the server socket to the specified address and port
			InetSocketAddress isa = new InetSocketAddress(hostAddress, port);
			serverSocketServer.socket().bind(isa);
		   	System.out.println("Initialisation "+serverSocketServer.socket());
		} catch (IOException e) {
			System.out.println("[ERREUR] Lors de accept connections on the given port " + port);
			e.printStackTrace();
		}		
	}

	public ServerSocketChannel getServerSocket() {
		return this.serverSocketServer;
	}
	
	public AcceptCallback getAcceptCallback() {
		return this.ac;
	}
	
	/**
	  * @return the port onto which connections are accepted.
	  */
	public int getPort() {			
		return this.port;
	}


	/**
	 * Close the server port, no longer accepting connections.
	 */
	public void close() {
		try {
			serverSocketServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}

}
