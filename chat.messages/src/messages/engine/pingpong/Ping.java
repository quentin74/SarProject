package messages.engine.pingpong;

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.Engine;

public class Ping extends Thread{
	int m_port;  
	Engine e;
	Channel ch = new ChannelPingPong();
	ConnectCallback cc = new ConnectCallBack();
	  
	Ping(int port, Engine e) throws Exception {
		this.m_port = port;
		this.e = e;		
		//Ask this NioEngine to connect to the given port on the given host
		e.connect(ch.getRemoteAddress().getAddress(), port, cc);
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
