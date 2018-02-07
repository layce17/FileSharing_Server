import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ObjectOutputStream;

import javax.swing.tree.TreeModel;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.Socket;

public class ClientProcesseur implements Runnable {
	private Socket sock;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;
	private ObjectOutputStream obj_writer = null; 
	
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
				obj_writer = new ObjectOutputStream(sock.getOutputStream());
				
				String response = read();
				InetSocketAddress remote = (InetSocketAddress) sock.getRemoteSocketAddress();
			
				String debug = "Client : "+ remote.getAddress().getHostAddress() +" - Commande : "+ response;
				System.out.println("\n" + debug);
				
				String toSend = "";
				
				switch (response.toUpperCase()) {
				case "BONJOUR":
					toSend = "Bonjour à toi !";
					break;
				case "TREE_FILES":
					TreeModel ft = this.getTreeFiles();
					obj_writer.writeObject(ft);
					System.out.println("Tree envoyé");
					break;
				case "QUIT":
					toSend = "Connexion arrêtée";
					close_con = true;
					break;
				default:
					toSend = "Commande inconnue";
					break;
				}
				
				if (response != "TREE_FILES"){
					writer.write(toSend);
					writer.flush();

					if (close_con){
						writer = null;
						obj_writer = null;
						reader = null;
						sock.close();
						System.out.println("Client : "+ remote.getAddress().getHostAddress()+" - Déconnecté !");
						break;
					}
				}
			} catch (SocketException e) {
				System.err.println("CONNEXION INTERROMPUE !");
				break;
			} catch (IOException e){
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

	private TreeModel getTreeFiles(){
		return new FileTree("D:/").getModel();
	}
}
