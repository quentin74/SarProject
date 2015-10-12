package messages.engine.pingpong;

import java.net.InetSocketAddress;


import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.Engine;
import messages.engine.Server;
import messages.engine.pingpong.EnginePingPong;

public class Ping extends Thread{
	  InetSocketAddress socket_host;
	  AcceptCallback ac = new AcceptCallBack();
	  ConnectCallback cc = new ConnectCallBack();
	  Server s;
	  Channel ch;
	  Engine e;
	  int m_port;
	  
	  Ping(int port, Engine e, Channel ch) throws Exception {
		  this.m_port = port;
		  this.e = e;
		  this.ch = ch;
		  
		  this.socket_host = ch.getRemoteAddress();
		  e.connect(socket_host.getAddress(), port, cc);
		  ac.accepted(s, ch);

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
