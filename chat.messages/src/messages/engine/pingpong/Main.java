package messages.engine.pingpong;

import java.net.InetAddress;

import messages.engine.Engine;



public class Main {

	public static void main(String[] args) throws Exception {
		Pong pong = new Pong(8080); 
		Ping ping = new Ping(8080);

		 pong.start();
		 ping.start();
	}

}
