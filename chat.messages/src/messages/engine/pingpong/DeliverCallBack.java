package messages.engine.pingpong;

import messages.engine.Channel;
import messages.engine.DeliverCallback;

public class DeliverCallBack implements DeliverCallback {

	/**
	  * Callback to notify that a message has been received.
	  * The message is whole, all bytes have been accumulated.
	  * @param channel
	  * @param bytes
	  */
	public void deliver(Channel channel, byte[] bytes) {
		System.out.println("Message received from : " + channel.getRemoteAddress() + " : " + bytes.toString());		
	}

}
