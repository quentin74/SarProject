package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

import messages.engine.AcceptCallback;
import messages.engine.Engine;
import messages.engine.Server;

public class Pong extends Thread implements messages.engine.Server {
	int port;
	Engine e;
	AcceptCallback ac = new AcceptCallBack();
	Server s = new NioServer();
	
	public Pong(int port, Engine e) {
		this.port = port;
		this.e = e;
		//Ask for this NioEngine to accept connections on the given port
		try {
			this.s = e.listen(port, ac);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public void run() {
		    try {
		      e.mainloop();
		    } catch (Exception ex) {
		      System.err.println("Ping: threw an exception: " + ex.getMessage());
		      ex.printStackTrace(System.err);
		    }
	}
	
}
