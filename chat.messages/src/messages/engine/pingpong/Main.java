package messages.engine.pingpong;

import messages.engine.Engine;



public class Main {

	public static void main(String[] args) throws Exception {
		 Engine e = new EnginePingPong(); 
		 Channel ch = new ChannelPingPong();
		 
		 Ping ping = new Ping(e);
		 Pong pong = new Pong(1234,e);
		 pong.start();
		 ping.start();

	}

}
