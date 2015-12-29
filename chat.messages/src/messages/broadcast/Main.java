package messages.broadcast;

import java.net.InetAddress;

import messages.engine.Engine;



public class Main {

	public static void main(String[] args) throws Exception {
		
		Pong pong = new Pong(8080);
		
		Ping ping = new Ping(8080);
		Ping ping2 = new Ping(8080);

		String[] cmd = { "/bin/sh", "script.sh" };
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            System.out.println("hello");
            p.getOutputStream().close();
            p.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		 pong.start();
		 //ping.start();
		 //ping2.start();
		 
	}

}
