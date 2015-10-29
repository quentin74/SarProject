package messages.engine.pingpong;

import java.net.InetAddress;

import messages.engine.Engine;



public class Main {

	public static void main(String[] args) throws Exception {
		 Ping ping = new Ping(1234);
		 Pong pong = new Pong(1234);
		 
		 pong.start();
		 ping.start();
	}

}
