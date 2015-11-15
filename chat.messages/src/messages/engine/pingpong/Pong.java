package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;

import messages.engine.AcceptCallback;
import messages.engine.Engine;
import messages.engine.Server;

public class Pong extends Thread {
	private int port;
	private Engine e = new EnginePingPong() ;
	private AcceptCallback ac = new AcceptCallBack();
	private Server s;

	public Pong(int port) {
		try {
			this.port = port;
			this.s = e.listen(port, ac);
			System.out.println("Server : port set to " + port + " with hostAdress : " + InetAddress.getLocalHost());
		} catch (IOException exception) {
			exception.printStackTrace();
			System.out.println("Server : port " + port + "is already used or can't be bound");
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
