package messages.broadcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;

import messages.engine.AcceptCallback;
import messages.engine.Engine;
import messages.engine.Server;


/* cette classse correspond au serveur, elle ne peut etre executé qu'une unique fois, elle doit etre executée avant les clients
 * on initialise ce  server avec son numéro de port qui est de base 8080
 * puis nous disons au server d'ecouter sur son port d'ecoue jusqu'a l'arrivée de client pour envoyer les messages.
 * 
 */
public class Pong {
	
	public static void main(String[] args) {
		try {
			EnginePingPong e = new EnginePingPong();
			int port = Integer.parseInt(args[0]);
			
			e.listen(port, new AcceptCallBack());
			e.mainloop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}