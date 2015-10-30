package messages.engine.pingpong;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.Engine;

public class Ping extends Thread{
	int m_port;  
	Engine e = new EnginePingPong();
	ConnectCallback cc = new ConnectCallBack();
	  
	Ping(int port) throws Exception {
		this.m_port = port;	
		InetSocketAddress inetSocketAddress= new InetSocketAddress(m_port);
		this.e = e;		
		e.connect(InetAddress.getLocalHost(), port, cc);
		System.out.println("Client port set to " + port + "with hostAdress" + InetAddress.getLocalHost());
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
