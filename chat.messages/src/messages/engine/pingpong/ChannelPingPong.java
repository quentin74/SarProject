package messages.engine.pingpong;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import javax.sound.sampled.Port;

import messages.engine.Channel;
import messages.engine.DeliverCallback;
import messages.engine.Server;

public class ChannelPingPong extends Channel {
	DeliverCallback callback;
	
	@Override
	public void setDeliverCallback(DeliverCallback callback) {
		this.callback= callback;
			
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		
		InetAddress inet;
		return inet.getLocalHost().getHostAddress();
		
		
		
	}

	@Override
	public void send(byte[] bytes, int offset, int length) {
		
		 ByteBuffer buf;
		    buf = ByteBuffer.allocate(4+length);
		    buf.putInt(length);
		    buf.put(bytes, offset, length);
		    int count = buf.position();
		    buf.position(0);
		    if (count != m_ch.write(buf)) {
		      System.out.println("  ---> wrote " + count + " bytes.");
		      System.exit(-1);
		    }
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
