import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

import  org.apache.commons.codec.binary.*;

public class SimpleServer2 {
	
	public static String[] readResult() {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("/home/rashmi/CVProject/Notes/Result/vis.json"));
			String str = br.readLine();
			String temp[] = str.split(",");
			String res[]=new String[temp.length];
			
			for(int i=0;i<temp.length;i=i+2) {
				res[i]=temp[i].split(":")[1];
				res[i] = res[i].substring(1,res[i].length()-1);
				System.out.println(res[i]);
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
			
			//InetAddress addr1 = InetAddress.getByName("10.42.0.94");
			//ServerSocket ss = new ServerSocket(4444,50,addr1);
			ServerSocket ss = new ServerSocket(4433);
			//If BindException thrown then go to terminal and type lsof -i:4444 and kill that process with kill <pid>
			byte[] buffer;
			while(true) {
				/*System.out.println("Connecting...");	
				System.out.println(InetAddress.getLocalHost().getHostAddress());
				Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
                for (; n.hasMoreElements();)
                {                		
                        NetworkInterface e = n.nextElement();
                        System.out.println("Interface: " + e.getName());
                        Enumeration<InetAddress> a = e.getInetAddresses();
                        for (; a.hasMoreElements();)
                        {
                                InetAddress addr = a.nextElement();
                                System.out.println(" " + addr.getHostAddress());
                        }
                        
                }*/
                System.out.println("Accept connection");
				Socket s=ss.accept();
				System.out.println("Connected ");
				
				/*File f1 = new File("/home/rashmi/CVProject/Notes/Sample/img1.jpg");
				FileInputStream fis = new FileInputStream(f1);
				buffer = new byte[fis.available()];
				fis.read(buffer);			
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.flush();
				oos.writeObject(buffer);*/
				
				/*BufferedReader bin = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String imgString=bin.readLine();*/
				
				/*DataInputStream dis = new DataInputStream(s.getInputStream());
				String imgString=dis.readLine();
				
				
				byte[] imageByteArray = Base64.decodeBase64(imgString);
				System.out.println("byte length="+imageByteArray.length);
				System.out.println("String length="+imgString.length());
				
				FileOutputStream imageOutFile = new FileOutputStream("/home/rashmi/CVProject/Notes/Sample/img1.png");
				imageOutFile.write(imageByteArray);
				imageOutFile.close();*/
				
				
				
				DataInputStream dis = new DataInputStream(s.getInputStream());				
				int len=dis.readInt();
				System.out.println(len);
				buffer = new byte[len];
				if(len>0) {
					dis.read(buffer, 0, len);
					//dis.read(buffer);
				}
				
				
				String imgString = new String(buffer);
				
				byte[] imageByteArray = Base64.decodeBase64(imgString);
				
				System.out.println("buffer length="+buffer.length);
				System.out.println("String length="+imgString.length());
				System.out.println("byte length="+imageByteArray.length);
				
				
				FileOutputStream fos = new FileOutputStream("/home/rashmi/CVProject/Notes/Sample/img1.jpg");
				fos.write(imageByteArray);
				fos.close();
				
								
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				ProcessBuilder pb = new ProcessBuilder("/home/rashmi/CVProject/neuraltalk2-master/run.sh");
				pb.start();
				String result[]=readResult();
				for(int i=0;i<result.length;i++) {
					dos.writeBytes(result[i]);
				}	
				System.out.println("msg written");
				
				s.close();
				//ss.close();
				
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
			
		}
		
	}

}
