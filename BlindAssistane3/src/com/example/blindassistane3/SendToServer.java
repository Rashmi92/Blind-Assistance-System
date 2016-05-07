package com.example.blindassistane3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.util.Log;

public class SendToServer  {

	public Queue<Bitmap> frame;
	public String ServerIpAddress="localhost";
	public int ServerPort=4444;
	public byte[] byteArray;
	
	public SendToServer(Queue<Bitmap> f) {		
		this.frame=f;
	}

	public void connectToServer() {
		Log.i("DEbuging SendToServer", "In Server");
		 try {
			 Log.i("DEbuging SendToServer", "before connect");
			Socket s = new Socket(ServerIpAddress,ServerPort);
			Log.i("DEbuging SendToServer", "connected");
			Bitmap img;
			int len;
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			while(!frame.isEmpty()) {
				img = frame.poll();
				img.compress(Bitmap.CompressFormat.JPEG, 10, stream);
				byteArray = stream.toByteArray();
				
				
				OutputStream out = s.getOutputStream();
				DataOutputStream dataOut = new DataOutputStream(out);
				len=byteArray.length;
				dataOut.writeInt(len);
				if (len > 0)
					dataOut.write(byteArray);
				
			}
			
			
	       
	        //close connection
	        s.close();
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	
	

}
