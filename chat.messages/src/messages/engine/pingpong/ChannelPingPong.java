package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.sound.sampled.Port;

import messages.engine.Channel;
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
		this.callback= callback;
			
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return null;
		
		

		
		
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
		Object selector;
		socketChannel.keyFor(selector).cancel();
		try{
			socketChannel.close();
		} catch (IOException e) {
			//nothing to do, the channel is already closed
		}

	}

}
