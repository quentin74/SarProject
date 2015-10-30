package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

import messages.engine.AcceptCallback;
import messages.engine.Engine;
import messages.engine.Server;

<<<<<<< HEAD
public class Pong extends Thread {
	Engine e = new EnginePingPong() ;
=======
public class Pong extends Thread{
	int port;
	Engine e;
>>>>>>> branch 'master' of https://github.com/quentin74/SarProject.git
	AcceptCallback ac = new AcceptCallBack();
<<<<<<< HEAD
	Server s;
=======
	
	Server s = new ServerPingPong();
>>>>>>> branch 'master' of https://github.com/quentin74/SarProject.git
	
<<<<<<< HEAD
	public Pong(int port) {
=======
	public Pong(int port, Engine e) {
		InetSocketAddress isa=new InetSocketAddress("localhost",8080);
		this.port = port;
		this.e = e;
>>>>>>> branch 'master' of https://github.com/quentin74/SarProject.git
		//Ask for this NioEngine to accept connections on the given port
		try {
			this.s = e.listen(port, ac);
			System.out.println("Server : " + s.toString() + " port set to " + port);
		} catch (IOException exception) {
			exception.printStackTrace();
			System.out.println("port " + port + "is already used or can't be bound");
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
