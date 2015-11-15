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
		String message = new String(bytes);
		String reponse;
		System.out.println("[DeliverCallBack] : " + channel.getRemoteAddress() + " : " +  message );
		
		// Envoie réponse
		if (message.equals("ping")){
			reponse = "pong" ;
		}
		else{
			reponse = "ping" ;
		}
		
		channel.send(reponse.getBytes(), 0, reponse.getBytes().length);
		System.out.println("Message send : "+ reponse);
	}

}
