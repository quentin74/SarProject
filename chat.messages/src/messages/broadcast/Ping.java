package messages.broadcast;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.Engine;

public class Ping extends Thread{
	private EnginePingPong e ;
	private int port;
	private ConnectCallback cc = new ConnectCallBack();
	private HashSet ports = new HashSet<>();
	  
	Ping(EnginePingPong e, int port,InetAddress ip, HashSet ports) throws Exception {
		this.port = port;
		this.ports = ports;
		this.e = e;
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
