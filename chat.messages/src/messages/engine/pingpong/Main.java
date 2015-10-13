package messages.engine.pingpong;

import messages.engine.Engine;



public class Main {

	public static void main(String[] args) throws Exception {
		 Engine e = new EnginePingPong(); 
		 
		 Ping ping = new Ping(1234,e);
		 Pong pong = new Pong(1234,e);
		 
		 ping.start();
		 pong.start();
	}

}
