package messages.engine.pingpong;

import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.DeliverCallback;

public class ConnectCallBack implements ConnectCallback{

	 /**
	   * Callback to notify that a previously connected channel has been closed.
	   * 
	   * @param channel
	   */
	public void closed(Channel channel) {
		System.out.println("[ConnectCallBack] : " + channel.getRemoteAddress() + " close ");
		channel.close();
	}

	/**
	   * Callback to notify that a connection has succeeded.
	   * @param channel
	   */
	public void connected(Channel channel) {
		System.out.println("[ConnectCallBack] : " + channel.getRemoteAddress() + " connection succeeded ");
		// Envoie du message
		String message = "ping" ;
		channel.send(message.getBytes(), 0, message.getBytes().length);
		//DeliverCallback dc = new DeliverCallBack();
		//channel.setDeliverCallback(dc);
	}

}
