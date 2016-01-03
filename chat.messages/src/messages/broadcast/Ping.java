package messages.broadcast;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.Engine;




/*
 *  Cette classe ping représente la partie client, elle peut etre lancé autant de fois qu'il y a de client
 *  nous commenceons par lancer son automate, avec son numéro de port qui est donné en paramétre dans le scripte ou dans les arguments
 *  . Nous avons ensuite la liste de port correspondant aux ports des autres clients.
 * 
 */
public class Ping {
	
	public static void main(String[] args) {
		try {
			EnginePingPong e = new EnginePingPong();
			int port = Integer.parseInt(args[0]);
			HashSet ports = new HashSet<>();

			e.connect(InetAddress.getByName("localhost"), port, new ConnectCallBack());
			e.mainloop();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}

}
