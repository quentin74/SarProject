package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import javax.sound.sampled.Port;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.DeliverCallback;
import messages.engine.Server;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
public class ChannelPingPong extends Channel {

	private SocketChannel socketChannel;
	private DeliverCallback dc;
	
	// List of ByteBuffer
	private ArrayList<ByteBuffer> listBuffer;
	// ByteBuffer for writing
	private ByteBuffer writeBuffer;
	private ByteBuffer writeLength;
	// ByteBuffers for reading
	private ByteBuffer readBuffer;
	private ByteBuffer readLength;
	
	public enum State {
		WRITING_LENGTH,WRITING_MSG, WRITING_DONE, READING_LENGTH,READING_MSG,READING_DONE
	}
	private State readState = State.READING_DONE;
	private State writeState = State.WRITING_DONE;
	
	
	
	
	public ChannelPingPong(SocketChannel socketChannel) throws IOException{
		this.socketChannel = socketChannel;
		this.listBuffer = new ArrayList<ByteBuffer>();	
	}
		

	public void setDeliverCallback(DeliverCallback callback) {
		this.dc = callback;	
	}

	public InetSocketAddress getRemoteAddress() {
		InetSocketAddress inetSocketAdresse =null;		
		try {
			inetSocketAdresse = (InetSocketAddress) this.socketChannel.getRemoteAddress();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return inetSocketAdresse;
	}

	/**
	   * Sending the given byte array, a copy is made into internal buffers,
	   * so the array can be reused after sending it.
	   * @param bytes
	   * @param offset
	   * @param length
	   */
	public void send(byte[] bytes, int offset, int length) {
		// VERIFICATION
		if(bytes.length <= length && offset < bytes.length){
			ByteBuffer buffer = ByteBuffer.allocate(length);
			while(length != 0){
				buffer.put(bytes[offset]);
				offset++;
				length--;
			}
			// Store in a ByteBuffer
			listBuffer.add(buffer);
		}else{
			System.out.println("[ERREUR] lors du sockage buffer");
			System.exit(-1);
		}		
	}

	@Override
	public void close() {
		try {
			socketChannel.close();
			System.out.println("Channel closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void read() {
		int nb = 0;

		if(readState == State.READING_DONE){
			readBuffer = null;
			readLength.clear();
			readState = State.READING_LENGTH;
		}

		if(readState == State.READING_LENGTH){
			try{
				nb = socketChannel.read(readLength);
			}catch(IOException e){
				this.close();
			}

			if(nb == -1){
				this.close();
			}


			if(readLength.remaining() == 0){
				// return to the position 0 to read at the correct offset
				readLength.position(0);
				int length = readLength.getInt();
				readBuffer = ByteBuffer.allocate(length);
				readState = State.READING_MSG;
			}
		}

		if(readState == State.READING_MSG){

			try{
				nb = socketChannel.read(readBuffer);
			}catch(IOException e){
				this.close();
			}

			if(nb == -1){
				this.close();
			}

			if(readBuffer.remaining() == 0){
				dc.deliver(this, readBuffer.duplicate().array());

				readState = State.READING_DONE;
			}
		}
		
	}


	public boolean write() {
		if(writeState == State.WRITING_DONE){
			if(listBuffer.size() > 0){
				writeBuffer = listBuffer.get(0);
				listBuffer.remove(0);
				// Clear all buffer for writing
				writeBuffer.clear();
				writeLength.clear();
				writeLength.putInt(writeBuffer.capacity());
				writeLength.position(0);
				writeState = State.WRITING_LENGTH;
			}
		}


		if(writeState == State.WRITING_LENGTH){

			try{
				socketChannel.write(writeLength);
			}catch(IOException e){
				this.close(); 
				System.out.println("Error during writing length");
				e.printStackTrace();
				System.exit(-1);
			}

			if(writeLength.remaining() == 0){
				writeState = State.WRITING_MSG;
			}

		}

		if(writeState == State.WRITING_MSG){

			if(writeBuffer.remaining() > 0){

				try{
					socketChannel.write(writeBuffer);
				}catch(IOException e){
					this.close(); 
					System.out.println("Error during writing message");
					e.printStackTrace();
					System.exit(-1);
				}
			}

			if(writeBuffer.remaining() == 0){
				writeState = State.WRITING_DONE;
			}
		}


		return (listBuffer.size() == 0);
		
	}

}
