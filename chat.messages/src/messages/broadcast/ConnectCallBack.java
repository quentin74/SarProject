package messages.broadcast;

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
		System.out.println("[ConnectCallBack] : Server " + channel.getRemoteAddress() + " connection succeeded to " + ((ChannelPingPong) channel).getLocalAddress() );
		// Envoie du message
		//String message = "hello" ;
		//channel.send(message.getBytes(), 0, message.getBytes().length);
	}

}
