package com.example.blindassistane3;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.Random;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class ConnectServer3 extends AsyncTask<Void, Void, Void> {
	
	public Queue<Bitmap> frame;
	//local: 10.0.2.2 static: 192.168.1.10
	public String ServerIpAddress="10.1.99.42";
	public int ServerPort=4433;
	public byte[] byteArray;
	TextToSpeech tts;
	String msg;
	
	public ConnectServer3(Queue<Bitmap> f) {
		this.frame=f;
	}
	
	private void storeImage(Bitmap image) {
	    File pictureFile = getOutputMediaFile();
	    if (pictureFile == null) {
	        Log.i("Store",
	                "Error creating media file, check storage permissions: ");// e.getMessage());
	        return;
	    } 
	    try {
	        FileOutputStream fos = new FileOutputStream(pictureFile);
	        image.compress(Bitmap.CompressFormat.PNG, 50, fos);
	        Log.i("Save Image","Success");
	        fos.close();
	    } catch (FileNotFoundException e) {
	        Log.i("Store-catch", "File not found: " + e.getMessage());
	    } catch (IOException e) {
	        Log.i("Store-catch", "Error accessing file: " + e.getMessage());
	    }  
	}
	
	private  File getOutputMediaFile(){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this. 
	    File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
	            + "/Android/data/"	            
	            + "/Files"); 

	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            return null;
	        }
	    } 
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
	    File mediaFile;
	        String mImageName="MI_"+ timeStamp +".jpg";
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);  
	    return mediaFile;
	} 

	@Override
	protected Void doInBackground(Void... params) {
		
		Random rn = new Random();
		final int k = rn.nextInt(3);
		final String msgs[]={"A room with a table in centre there is moving space around the table","A living room with a door in front there is moving space in the front","A room with a chair and a table there is moving space in the front"};
		
		Log.i("DEbuging doInBackground ConnServer", "In Server");
		 try {
			 Log.i("DEbuging doInBackground ConnServer", "before connect");
			Socket s = new Socket(ServerIpAddress,ServerPort);
			Log.i("DEbuging doInBackground ConnServer", "connected");
			Bitmap img;
			int len;
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			while(!frame.isEmpty()) {
				
				img = frame.poll();
				storeImage(img);
				img.compress(Bitmap.CompressFormat.JPEG, 10, stream);
				byteArray = stream.toByteArray();				
				Log.i("DEbuging imgByteArraySize", "length= "+byteArray.length);
				
				
				
				OutputStream out = s.getOutputStream();
				DataOutputStream dataOut = new DataOutputStream(out);
				len=byteArray.length;
				dataOut.writeInt(len);
				if (len > 0)
					dataOut.write(byteArray);
				
				InputStream in = s.getInputStream();;
				DataInputStream dis = new DataInputStream(in);
				msg = dis.readLine();
				Log.i("Returned", msg);
				
				
				
			}
			
			
			tts = new TextToSpeech(WelcomeActivity.getContext(),new TextToSpeech.OnInitListener() {
		         @Override
		         public void onInit(int status) {
		            if(status != TextToSpeech.ERROR) {
		               tts.setLanguage(Locale.UK);     
		               
		               tts.speak(msgs[k], TextToSpeech.QUEUE_FLUSH, null);
		            }
		         }
		      });
			
			//TODO Testing the code
			//TODO TTS conversion of the returned message
	       
	        //close connection
	        s.close();
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return null;
		
		
	}

}
