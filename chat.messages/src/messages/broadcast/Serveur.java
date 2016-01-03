package messages.broadcast;

import java.io.IOException;
import java.net.InetAddress;

import messages.engine.Engine;
import messages.engine.Server;

public class Serveur extends Thread {
	private EnginePingPong e = new EnginePingPong() ;
	private Server s;

	public Serveur(int port) {
		try {
			this.s = e.listen(port,new AcceptCallBack());
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
