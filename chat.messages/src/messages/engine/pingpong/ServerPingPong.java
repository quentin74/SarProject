package messages.engine.pingpong;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import messages.engine.Server;

public class ServerPingPong  extends Server{
	int port ;
	ServerSocketChannel serverSocketChannel;
	
	@Override
	public int getPort() {			
		return this.port;
	}

	@Override
	public void close() {
		
		
		
	}

}
