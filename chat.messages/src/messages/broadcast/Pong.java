package messages.broadcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;

import messages.engine.AcceptCallback;
import messages.engine.Engine;
import messages.engine.Server;


public class Pong {
	
	public static void main(String[] args) {
		try {
			EnginePingPong e = new EnginePingPong();
			int port = Integer.parseInt(args[0]);
			
			e.listen(port, new AcceptCallBack());
			e.mainloop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}