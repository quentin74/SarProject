package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.sound.sampled.Port;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.DeliverCallback;
import messages.engine.Server;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
public class ChannelPingPong extends Channel {

	
	private SocketChannel clientChannel;
	private InetAddress serverAddress;
	
	public ChannelPingPong() throws IOException{
		clientChannel = SocketChannel.open();
		clientChannel.configureBlocking(false);
		
	}
		
		
	
	private static final InetSocketAddress InetSocketAddress = null;
	DeliverCallback callback;

	
	@Override
	public void setDeliverCallback(DeliverCallback callback) {
				
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
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
		Object selector;
		socketChannel.keyFor(selector).cancel();
		try{
			socketChannel.close();
		} catch (IOException e) {
			//nothing to do, the channel is already closed
		}

	}


	public void read() {
		// TODO Auto-generated method stub
		
	}


	public boolean write() {
		return false;
		// TODO Auto-generated method stub
		
	}

}
