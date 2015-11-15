package messages.engine.pingpong;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import messages.engine.Channel;
import messages.engine.DeliverCallback;
public class ChannelPingPong extends Channel {

	private SocketChannel socketChannel;
	
	// List of ByteBuffer
	private ArrayList<ByteBuffer> listBuffer;
	// ByteBuffer for writing
	private ByteBuffer writeBuffer;
	private ByteBuffer writeLength;
	// ByteBuffers for reading
	private ByteBuffer readBuffer;
	private ByteBuffer readLength;
	
	private DeliverCallback dc = new DeliverCallBack();
	
	public enum State {
		WRITING_LENGTH,WRITING_MSG, WRITING_DONE, READING_LENGTH,READING_MSG,READING_DONE
	}
	
	private State readState = State.READING_DONE;
	private State writeState = State.WRITING_DONE;
	
		
	
	public ChannelPingPong(SocketChannel socketChannel) throws IOException{
		this.socketChannel = socketChannel;
		this.listBuffer = new ArrayList<ByteBuffer>();
		this.writeLength = ByteBuffer.allocate(4);
		this.readLength = ByteBuffer.allocate(4);
		this.writeBuffer = ByteBuffer.allocate(0);
		this.readBuffer = ByteBuffer.allocate(0);
	}

	/**
	   * Get the Inet socket address for the other side of this channel.
	   * @return
	   */
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
				// length sur 4 bytes
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
				int nb = socketChannel.write(writeLength);
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

	@Override
	public void setDeliverCallback(DeliverCallback callback) {
		this.dc = callback;	
	}

	public byte[] writeBuffer() {
		return writeBuffer.array();
	}

	public InetSocketAddress getLocalAddress() {
		InetSocketAddress inetSocketAdresse =null;		
		try {
			inetSocketAdresse = (InetSocketAddress) this.socketChannel.getLocalAddress();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return inetSocketAdresse;
	}
}
