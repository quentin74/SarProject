package messages.engine.pingpong;

import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.DeliverCallback;

public class ConnectCallBack implements ConnectCallback{
	DeliverCallback bc ;
	@Override
	public void closed(Channel channel) {
		channel.close();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connected(Channel channel) {
		channel.setDeliverCallback(bc);
		// TODO Auto-generated method stub
		
	}

}
