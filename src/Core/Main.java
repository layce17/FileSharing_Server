import java.lang.Thread;

public class Main {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 3456;
		
		Server s = new Server(host, port);
		Thread cmd_listener = new Thread(new CommandListener(s));
		cmd_listener.start();
		
		System.out.println("Serveur initialisé.");
		s.open();
	}
}
