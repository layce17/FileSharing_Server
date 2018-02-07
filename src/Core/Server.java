package Core;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private int port = 3456;
	private String host = "127.0.0.1";
	private ServerSocket server = null;
	private boolean isRunning = true;
	
	public Server() {
		try {
			server = new ServerSocket(port, 100, InetAddress.getByName(host));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Server(String host, int port) {
		this.host = host;
		this.port = port;
		
		try {
			server = new ServerSocket(port, 100, InetAddress.getByName(host));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void open() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (isRunning) {
					try {
						Socket client = server.accept();
						
						System.out.println("Client : "+ client.getInetAddress().getHostAddress()+ " connecté");
						Thread t = new Thread(new ClientProcesseur(client));
						t.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		t.start();
	}
}
