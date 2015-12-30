package messages.broadcast;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.Engine;

public class Ping extends Thread{
	private int port;  
	private Engine e = new EnginePingPong();
	private ConnectCallback cc = new ConnectCallBack();
	private HashSet ports = new HashSet<>();
	  
	Ping(int port,InetAddress ip, HashSet ports) throws Exception {
		this.port = port;
		this.ports = ports;
		//Ask this NioEngine to connect to the given port on the given host
		e.connect(ip, port, cc);
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
