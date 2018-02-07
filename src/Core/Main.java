package Core;

public class Main {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 3456;
		
		Server s = new Server(host, port);
		
		System.out.println("Serveur initialisé.");
	}
}
