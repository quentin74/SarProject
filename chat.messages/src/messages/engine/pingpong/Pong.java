package messages.engine.pingpong;

import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

public class Pong extends Thread {
	InetAddress m_localhost;
	  Selector m_selector;
	  ServerSocketChannel m_sch;
	  SelectionKey m_skey;
	  SocketChannel m_ch;
	  SelectionKey m_key;
	  int m_port;
	  EnginePingPong e = new EnginePingPong();

	  public Pong(int port) throws Exception {
		  m_port = port;
		  m_localhost = InetAddress.getByName("localhost");
		  m_selector = SelectorProvider.provider().openSelector();
		  m_key.interestOps(SelectionKey.OP_ACCEPT);
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
