package Core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientProcesseur implements Runnable {
	private Socket sock;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;
	
	public ClientProcesseur(Socket s) {
		sock = s;
	}
	
	@Override
	public void run() {
		boolean close_con = false;
		
		while (!sock.isClosed()) {
			try {
				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedInputStream(sock.getInputStream());
				
				String response = read();
				InetSocketAddress remote = (InetSocketAddress) sock.getRemoteSocketAddress();
			
				String debug = "Client : "+ remote.getAddress().getHostAddress() +" - Commande : "+ response;
				System.out.println("\n" + debug);
				
				String toSend = "";
				
				switch (response.toUpperCase()) {
				case "BONJOUR":
					toSend = "Bonjour à toi !";
					break;
				case "QUIT":
					toSend = "Communication terminée";
					break;
				default:
					toSend = "Commande inconnue";
					break;
				}
				
				writer.write(toSend);
				writer.flush();
				
				if (close_con) {
					System.out.println("COMMANDE QUIT DETECTEE ! ");
					writer = null;
					reader = null;
					sock.close();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String read() throws IOException{
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);
		
		return response;
	}
}
