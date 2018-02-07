import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Runnable;

public class CommandListener implements Runnable{
	private boolean isServerStop;
	private BufferedReader br;
	private Server s;

	public CommandListener(Server s){
		this.s = s;
		isServerStop = false;
		br = new BufferedReader(new InputStreamReader(System.in));
	}

	public void run(){
		while (!isServerStop){
			String cmd = "";
			try{
				cmd = br.readLine();
			} catch (IOException e){
				e.printStackTrace();
			}

			if (cmd.equalsIgnoreCase("STOP")){
				this.isServerStop = true;
				System.out.println("Server stoped !");
				s.close();
				System.exit(0);
			}
		}
	}
}