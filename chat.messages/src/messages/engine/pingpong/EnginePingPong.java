package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import messages.engine.AcceptCallback;
import messages.engine.ConnectCallback;
import messages.engine.Engine;
import messages.engine.Server;

public class EnginePingPong extends Engine {
	public Selector m_selector;
	

	@Override
	public void mainloop() {
		
		SocketChannel socketChannel = null;
		 long delay = 0;
		    for (;;) {
		      m_selector.select(delay);
		      Iterator<?> selectedKeys = this.m_selector.selectedKeys().iterator();
		      if (selectedKeys.hasNext()) {
		        SelectionKey key = (SelectionKey) selectedKeys.next();
		        selectedKeys.remove();
		        if (!key.isValid()) {
		          System.err.println(">>> Ping:  ---> readable key=" + key);
		          System.exit(-1);
		        } else if (key.isAcceptable()) {
		        		//handleAccept
		        		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		        		try {
		        			socketChannel = serverSocketChannel.accept();
		        			socketChannel.configureBlocking(false);
		        		} catch (IOException e) {
		        			// as if there was no accept done
		        			return;
		        		}
		          System.err.println(">>> Ping:  ---> acceptable key=" + key);
		          System.exit(-1);
		        } else if (key.isReadable()) {
		        	//handleRead
		        	SocketChannel socketChannel = (SocketChannel) key.channel();
		          System.err.println(">>> Ping:  ---> readable key=" + key);
		          System.exit(-1);
		        } else if (key.isWritable()) {
		        	//handleWrite
		          System.err.println(">>> Ping:  ---> writable key=" + key);
		          System.exit(-1);
		        } else if (key.isConnectable()) {
		        	//handleConnect
		          System.out.println(">>> Ping:  ---> connectable key=" + key);
		          handleConnect(key);
		          return;
		        }
		      }
		    }
		  }
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public Server listen(int port, AcceptCallback callback) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void connect(InetAddress hostAddress, int port,
			ConnectCallback callback) throws UnknownHostException,
			SecurityException, IOException {
		// TODO Auto-generated method stub
		
	}

}
