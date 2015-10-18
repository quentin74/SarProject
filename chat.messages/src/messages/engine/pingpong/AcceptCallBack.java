package messages.engine.pingpong;

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.Server;

public class AcceptCallBack implements AcceptCallback {

	@Override
	public void accepted(Server server, Channel channel) {
		
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closed(Channel channel) {
		channel.close();
		// TODO Auto-generated method stub
		
	}

}
