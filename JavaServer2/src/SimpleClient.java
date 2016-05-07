import java.net.Socket;


public class SimpleClient {
	
	public static String ServerIpAddress="192.168.1.10";
	public static int ServerPort=4444;
	
	public static void main(String args[]) throws Exception {
		System.out.println("Client");
		Socket s = new Socket(ServerIpAddress,ServerPort);
		System.out.println("Client Connected");
		
	}

}
