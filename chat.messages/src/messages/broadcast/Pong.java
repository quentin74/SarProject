package messages.broadcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;

import messages.engine.AcceptCallback;
import messages.engine.Engine;
import messages.engine.Server;
import messages.engine.pingpong.ServerPingPong;

public class Pong extends Thread {
	private int port;
	private EnginePingPong e = new EnginePingPong() ;
	private AcceptCallback ac = new AcceptCallBack();
	private messages.broadcast.ServerPingPong s;

	public Pong(int port) {
		try {
			this.port = port;
			
			this.s = e.listen(port, ac);
		} catch (IOException exception) {
			exception.printStackTrace();
			System.out.println("Server : port " + port + "is already used or can't be bound");
		}
	}
	
	public messages.broadcast.ServerPingPong getServer(){
		return s;
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
