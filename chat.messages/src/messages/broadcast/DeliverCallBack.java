package messages.broadcast;

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
		String message = new String(bytes);
		String reponse;
		
		// Envoie réponse
		if (message.equals("ping")){
			System.out.println("[DeliverCallBack] : " + ( (ChannelPingPong) channel).getLocalAddress() + " deliver to " + channel.getRemoteAddress() + " message : " +  message );
			reponse = "pong" ;
		}
		else{
			System.out.println("[DeliverCallBack] : " + channel.getRemoteAddress()  + " deliver to " + ( (ChannelPingPong) channel).getLocalAddress()+ " message : " +  message );
			reponse = "ping" ;
		}
		
		channel.send(reponse.getBytes(), 0, reponse.getBytes().length);
		//System.out.println(((ChannelPingPong)channel).getLocalAddress()+ " send : "+ reponse);
	}

}
