package messages.broadcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import messages.engine.AcceptCallback;
import messages.engine.Server;

public class ServerPingPong extends Server{
	private int port ;
	private AcceptCallBack ac = new AcceptCallBack();
	private ServerSocketChannel serverSocketServer;
	
	public ServerPingPong(int port, AcceptCallback callback) {
		this.port = port;
		this.ac = (AcceptCallBack) callback;
		try {
			serverSocketServer = ServerSocketChannel.open();
			serverSocketServer.configureBlocking(false);
			// bind serverSocket with a specific "port"
			serverSocketServer.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
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
