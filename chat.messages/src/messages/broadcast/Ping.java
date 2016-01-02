package messages.broadcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.Engine;

public class Ping {
	
	public static void main(String[] args) {
		try {
			EnginePingPong e = new EnginePingPong();
			int port = Integer.parseInt(args[0]);
			HashSet ports = new HashSet<>();

			e.connect(InetAddress.getByName("localhost"), port, new ConnectCallBack());
			e.mainloop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
