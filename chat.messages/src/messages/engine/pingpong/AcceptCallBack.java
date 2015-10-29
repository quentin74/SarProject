package messages.engine.pingpong;

import java.nio.channels.ClosedChannelException;

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.DeliverCallback;
import messages.engine.Server;

public class AcceptCallBack implements AcceptCallback {

	/**
	  * Callback to notify about an accepted connection.
	  * @param server
	  * @param channel
	  */
	public void accepted(Server server, Channel channel) {
		System.out.println("[AcceptCallBack] : Server " + server.toString() + "Succesfully connected to the port " + server.getPort() + " to " + channel.getRemoteAddress());
		//DeliverCallback dc = new DeliverCallBack();
		//channel.setDeliverCallback(dc);
		
		String message = "ping" ;
		channel.send(message.getBytes(), 0, message.getBytes().length);
	}

	/**
	  * Callback to notify that a previously accepted channel 
	  * has been closed.
	  * @param channel
	  */
	public void closed(Channel channel) {
		channel.close();
		System.out.println("[AcceptCallBack] : accepted channel " + channel.toString() + " closed");
		
	}

}
