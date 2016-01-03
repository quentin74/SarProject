package messages.broadcast;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import messages.engine.Engine;

public class Client extends Thread{
	EnginePingPong e = new EnginePingPong();
	
	Client(InetAddress ip, int portServer, InetAddress ip2, int port) throws Exception {	
		//Ask this NioEngine to connect to the given port on the given host
		e.connectBroadcast(ip, portServer, new ConnectCallBack(), ip2, port);
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
