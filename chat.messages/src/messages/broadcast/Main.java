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
		
		EnginePingPong e = new EnginePingPong();
		Pong pong = new Pong(e,8080);
		String file = OPTIONFILENAME;
		
		nbClient=2;
		Ping p[] = new Ping[nbClient];
		for (int i=0 ; i<nbClient ; i++) {		
			HashSet h = new HashSet<>();
			h.add(4242);
			p[i]= new Ping(e,pong.getServer().getServerSocket().socket().getLocalPort(),pong.getServer().getServerSocket().socket().getInetAddress(),h);
		}
		
		pong.start();
		for (int j=0 ; j<nbClient ; j++) {		
		    p[j].start(); //Causes this thread to begin execution; the Java Virtual Machine calls the run method of this thread.
		}
		 
	}

}
