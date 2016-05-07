import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class SimpleServer3 {
	
	public static String[] readResult() {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("/home/rashmi/CVProject/Notes/Result/vis.json"));
			String str = br.readLine();
			String temp[] = str.split(",");
			String res[]=new String[temp.length/2];
			int j=0;
			for(int i=0;i<temp.length;i=i+2) {
				res[j]=temp[i].split(":")[1];
				res[j] = res[j].substring(1,res[j].length()-1);
				System.out.println(res[j]);
				j++;
			}
			
			return res;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String args[]) {
		
		try {
			ServerSocket ss = new ServerSocket(4445);
			
			while(true) {
				System.out.println("Waiting for client");
				Socket s = ss.accept();
				System.out.println("Connected");
				
				DataInputStream dis = new DataInputStream(s.getInputStream());
				FileOutputStream fos = new FileOutputStream("/home/rashmi/CVProject/Notes/Sample/img3.jpg");
				
				int length,len;
				len=dis.readInt();
				System.out.println(len);
				byte[] buffer = new byte[1024];				
				while (len>0  ) {
					//(length = dis.read(buffer, 0, buffer.length))> -1
					length = dis.read(buffer, 0, buffer.length);
					System.out.println("length="+length);
					fos.write(buffer, 0, length);				
					len -=length;
				}
				System.out.println("File written");
				fos.close();
				System.out.println("File closed");
				
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				/*ProcessBuilder pb = new ProcessBuilder("/home/rashmi/CVProject/neuraltalk2-master/run.sh");
				Process p =pb.start();
				p.waitFor();*/
				String result[]=readResult();
				System.out.println(result.length);
				
				//dos.writeBytes(result[0]);
				//dos.writeUTF(result[0]);
				/*for(int i=0;i<result.length;i++) {
					dos.writeBytes(result[i]);
				}*/	
				System.out.println("msg written");				
				s.close();
				
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
