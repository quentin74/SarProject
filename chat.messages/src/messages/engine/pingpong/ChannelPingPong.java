package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.sound.sampled.Port;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.DeliverCallback;
import messages.engine.Server;

public class ChannelPingPong extends Channel {
	private ConnectCallBack cc;
	private DeliverCallback dc;
	
	private SocketChannel socketChannel;
	private ByteBuffer buffer;
	private Object getSocketChannel;
	
	public ChannelPingPong(SocketChannel socketChannel) {
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void setDeliverCallback(DeliverCallback callback) {
				
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		
			//return (InetSocketAddress) clientSocketChannel.getRemoteAddress();
		return null;
	}

	@Override
	public void send(byte[] bytes, int offset, int length) {
		
		 /*
		    buffer = ByteBuffer.allocate(4+length);
		    buffer.putInt(length);
		    buffer.put(bytes, offset, length);
		    int count = buffer.position();
		    buffer.position(0);
		    if (count != m_ch.write(buffer)) {
		      System.out.println("  ---> wrote " + count + " bytes.");
		      System.exit(-1);
		    }
		 */		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}


	public void read() {
		// TODO Auto-generated method stub
		
	}


	public boolean write() {
		return false;
		// TODO Auto-generated method stub
		
	}

}
