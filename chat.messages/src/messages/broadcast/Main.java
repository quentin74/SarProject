package messages.broadcast;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Properties;

import messages.engine.Engine;



public class Main {

	private static final String OPTIONFILENAME = "groupe.xml";
	/** the version of the protocole to be used */
	protected static String version;
	/** Le nombre de client dans le groupe*/
	protected static int nbClient;
	
	public static void main(String[] args) throws Exception {

		Pong pong = new Pong(8080);
		String file = OPTIONFILENAME;
		
		nbClient=1;
		pong.start();
		
		Ping p[] = new Ping[nbClient];
		for (int i=0 ; i<nbClient ; i++) {		
			HashSet h = new HashSet<>();
			h.add(4242);
			p[i]= new Ping(pong.getServer().getServerSocket().socket().getLocalPort(),pong.getServer().getServerSocket().socket().getInetAddress(),h);
		    p[i].start(); //Causes this thread to begin execution; the Java Virtual Machine calls the run method of this thread.
		}
		 
	}

}
