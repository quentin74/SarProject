package messages.broadcast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class Broadcast {

	private HashMap<SocketChannel, ChannelPingPong> groupe;
	
	public static void inscription(int portServer, String name, InetAddress ip, int port){
		try {
			new Client(InetAddress.getByName("localhost"), portServer, ip, port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
 	//Socket(InetAddress address, int port)
//Creates a stream socket and connects it to the specified port number at the specified IP address.
	public static void main(String[] args) {
		String fichier ="chat.messages/src/tests/test1";
		int portServer = 0;
		Serveur s = null ;
		Client c[] = null;
		int nbClient = 0;
		int i = 0;
		
		
		//lecture du fichier texte de test	
		try{
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			
			while ((ligne=br.readLine())!=null){
				//System.out.println(ligne);
				//Initialisation du Groupe avec l'inscription de chaque membre IP + port
				// On suppose le Groupe statique, on ne considérera pas les pannes ni les insertions.
				
				if(ligne.contains("Server")){
					s = new Serveur(Integer.parseInt(ligne.split(" ")[1]));
					portServer = Integer.parseInt(ligne.split(" ")[1]);
				}else if (ligne.contains("NbClient")){
					nbClient = Integer.parseInt(ligne.split(" ")[1]);
					c = new Client[nbClient];				
				}else{
					c[i]= new Client(InetAddress.getByName("localhost"), portServer,InetAddress.getByName(ligne.split(" ")[1]),Integer.parseInt(ligne.split(" ")[2]));
					i++;
				}
			}
			
			s.start();
			for(int j=0; j<nbClient; j++){
				c[j].start();
			}
			
			br.close();
		}			
		catch (Exception e){
			System.out.println(e.toString());
		}		
	}
}
