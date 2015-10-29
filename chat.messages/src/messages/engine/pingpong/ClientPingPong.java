package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import messages.engine.AcceptCallback;
import messages.engine.ConnectCallback;
import messages.engine.Server;

public class ClientPingPong {
	private int port ;
	private ConnectCallback cc;
	private SocketChannel socketChannel;
	
	public ClientPingPong(InetAddress hostAddress, int port, ConnectCallback callback) {
		this.port = port;
		this.cc = callback;
		// create a non-blocking socket channel
		try{
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(hostAddress, port));
		}catch(IOException e){
			System.out.println("[ERREUR] Lors de la connexion de la socket channel");
			e.printStackTrace();
		}
	}

	public SocketChannel getSocketChannel() {
		return this.socketChannel;
	}
	
	public ConnectCallback getConnectCallback() {
		return this.cc;
	}
	


}
