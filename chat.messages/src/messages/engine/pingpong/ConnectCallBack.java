package messages.engine.pingpong;

import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.DeliverCallback;

public class ConnectCallBack implements ConnectCallback{
<<<<<<< HEAD

	 /**
	   * Callback to notify that a previously connected channel has been closed.
	   * 
	   * @param channel
	   */
=======
	DeliverCallback bc ;
	@Override
>>>>>>> branch 'master' of https://github.com/quentin74/SarProject.git
	public void closed(Channel channel) {
<<<<<<< HEAD
		System.out.println("[ConnectCallBack] : " + channel.getRemoteAddress() + " close ");
=======
		channel.close();
		// TODO Auto-generated method stub
>>>>>>> branch 'master' of https://github.com/quentin74/SarProject.git
		
	}

	/**
	   * Callback to notify that a connection has succeeded.
	   * @param channel
	   */
	public void connected(Channel channel) {
<<<<<<< HEAD
		System.out.println("[ConnectCallBack] : " + channel.getRemoteAddress() + " connection succeeded ");
		//DeliverCallback dc = new DeliverCallBack();
		//channel.setDeliverCallback(dc);
=======
		channel.setDeliverCallback(bc);
		// TODO Auto-generated method stub
>>>>>>> branch 'master' of https://github.com/quentin74/SarProject.git
		
	}

}
