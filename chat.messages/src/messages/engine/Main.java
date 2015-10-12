package messages.engine;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.rmi.server.ServerRef;
import java.util.Iterator;

public class Main   {
	static int port;
	public SocketChannel m_ch;
	Engine e = new Engine() {
	
	

	@Override
	public void mainloop() {
		
		 
		   
		
	}
	
	@Override
	public Server listen(int port, AcceptCallback callback) throws IOException {
		
		
		return null;
	}
	
	@Override
	public void connect(InetAddress hostAddress, int port,
			ConnectCallback callback) throws UnknownHostException,
			SecurityException, IOException {
		// create a non-blocking socket channel
	    m_ch = SocketChannel.open();
	    m_ch.configureBlocking(false);
	    m_ch.socket().setTcpNoDelay(true);

	    // be notified when the connection to the server will be accepted
	    //m_key = m_ch.register(m_selector, SelectionKey.OP_CONNECT);

	    // request to connect to the server
	    m_ch.connect(new InetSocketAddress(hostAddress, port));
		
		
	}
};
	
Server s = new Server() {
	
	@Override
	public int getPort() {
		
		return port;
	}
	
	@Override
	public void close() {
		ch.close();
		
	}
};

messages.engine.Channel ch = new messages.engine.Channel() {
	
	@Override
	public void setDeliverCallback(DeliverCallback callback) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void send(byte[] bytes, int offset, int length) {
		ByteBuffer buf;
	    buf = ByteBuffer.allocate(4+length);
	    buf.putInt(length);
	    buf.put(bytes, offset, length);
	    int count = buf.position();
	    buf.position(0);
	    try {
			if (count != m_ch.write(buf)) {
			  System.out.println("  ---> wrote " + count + " bytes.");
			  System.exit(-1);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	@Override
	public InetSocketAddress getRemoteAddress() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void close() {
		ch.close();
	
		
	}
};
public static void main(String[] args) {
	
	port =123;
	
	
	
	

}


}
