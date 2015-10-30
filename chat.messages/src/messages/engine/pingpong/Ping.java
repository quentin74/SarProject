package messages.engine.pingpong;

<<<<<<< HEAD
import java.net.InetAddress;
=======
import java.net.InetSocketAddress;
>>>>>>> branch 'master' of https://github.com/quentin74/SarProject.git

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.Engine;

public class Ping extends Thread{
	int m_port;  
	Engine e = new EnginePingPong();
	ConnectCallback cc = new ConnectCallBack();
	  
<<<<<<< HEAD
	Ping(int port) throws Exception {
		this.m_port = port;	
=======
	Ping(int port, Engine e) throws Exception {
		this.m_port = port;
		InetSocketAddress inetSocketAddress= new InetSocketAddress(m_port);
		this.e = e;		
>>>>>>> branch 'master' of https://github.com/quentin74/SarProject.git
		//Ask this NioEngine to connect to the given port on the given host
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
