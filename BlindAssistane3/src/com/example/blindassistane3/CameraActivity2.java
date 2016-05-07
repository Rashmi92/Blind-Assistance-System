package com.example.blindassistane3;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraActivity2 extends Activity {
	
	private Camera mCamera;
    private SurfaceView mPreview;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;    
    private Handler handler;    
    public MediaMetadataRetriever mediaMetadataRetriever;
    private String opFile;
    public Queue<Bitmap> frame;
    public int time=10000;
    public Runnable r;
    public CameraPreview cameraPreview;
    TextToSpeech tts;
    private static Context mContext;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_activity2);
		Log.i("DEbuging cam act", "In Oncreate");
		mediaMetadataRetriever = new MediaMetadataRetriever();
		handler = new Handler();
		frame= new LinkedList<Bitmap>();
		
		// Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.        
        mPreview = new CameraPreview(this, mCamera);
        //mPreview.getHolder().addCallback(CameraPreview);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        /*if (prepareVideoRecorder()) {
            // Camera is available and unlocked, MediaRecorder is prepared,
            // now you can start recording                        	
            mMediaRecorder.start();
            
            // TODO: inform the user that recording has started           
            
            
        } else {
            // prepare didn't work, release the camera
            releaseMediaRecorder();
            // inform user
        }*/
        mContext=getApplicationContext();
        tts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
	         @Override
	         public void onInit(int status) {
	            if(status != TextToSpeech.ERROR) {
	               tts.setLanguage(Locale.UK);
	                	               
	               tts.speak("Click Anywhere to start recording", TextToSpeech.QUEUE_FLUSH, null);
	            }
	         }
	      });
        preview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
					// stop recording and release camera
				
                if (isRecording) {
                	Log.i("DEbuging cam act", "In isRecording");
                    // stop recording and release camera
                    mMediaRecorder.stop();  // stop the recording
                    tts= new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
              	         @Override
              	         public void onInit(int status) {
              	            if(status != TextToSpeech.ERROR) {
              	               tts.setLanguage(Locale.UK);
              	                	               
              	               tts.speak("Recording stopped", TextToSpeech.QUEUE_FLUSH, null);
              	            }
              	         }
              	      });

                    releaseMediaRecorder(); // release the MediaRecorder object
                    mCamera.lock();         // take camera access back from MediaRecorder

                    // TODO inform the user that recording has stopped                    
                    isRecording = false;
                    releaseCamera();
                    mediaMetadataRetriever.setDataSource(opFile);        
                    r = new Runnable() {            	
            			
            			@Override
            			public void run() {
            				Log.i("DEbuging cam act", "In runable");
            				//To call this thread periodically: handler.postDelayed(this, 2000);
            				//getFrmAtTime has microsec as parameter n postDelayed has millisec                				
            				//Bitmap tFrame[];
            				
            				
            				String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            				frame.add(mediaMetadataRetriever.getFrameAtTime(time));
            				/*int videoDuration = Integer.parseInt(duration);
            				System.out.println(videoDuration);
            				while(time<videoDuration) {
            					frame.add(mediaMetadataRetriever.getFrameAtTime(time));               					
                				time=time+1000;
            				}*/
            				/*SendToServer sts = new SendToServer(frame);
            				sts.connectToServer();*/
            				ConnectServer connectServer = new ConnectServer(frame);
            				connectServer.execute();
            				
            			}

						
            		};
					r.run();
                    
                } else {
                    // initialize video camera
                    if (prepareVideoRecorder()) {
                        // Camera is available and unlocked, MediaRecorder is prepared,
                        // now you can start recording                        	
                        mMediaRecorder.start();                            
                        //TODO  inform the user that recording has started                        
                        isRecording = true;
                        tts= new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
               	         @Override
               	         public void onInit(int status) {
               	            if(status != TextToSpeech.ERROR) {
               	               tts.setLanguage(Locale.UK);
               	                	               
               	               tts.speak("Started. Click Anywhere to stop recording", TextToSpeech.QUEUE_FLUSH, null);
               	            }
               	         }
               	      });
                        
                    } else {
                        // prepare didn't work, release the camera
                        releaseMediaRecorder();
                        // inform user
                    }
                }
			}
		});
	}

	public static void setCameraDisplayOrientation(Activity activity,
	         int cameraId, android.hardware.Camera camera) {
	     android.hardware.Camera.CameraInfo info =
	             new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay()
	             .getRotation();
	     int degrees = 0;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }

	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     camera.setDisplayOrientation(result);
	 }

		
	@SuppressWarnings("deprecation")
	public static Camera getCameraInstance() {
		
		Camera c = null;
	    try {
	    	
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	    	e.printStackTrace();
	        // Camera is not available (in use or does not exist)
	    }
	    return c;
	}
	
	private boolean prepareVideoRecorder(){

	    if(mCamera==null)
	    	mCamera = getCameraInstance();
	    
	    mMediaRecorder = new MediaRecorder();

	    // Step 1: Unlock and set camera to MediaRecorder
	    mCamera.unlock();
	    mMediaRecorder.setCamera(mCamera);

	    // Step 2: Set sources
	    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

	    // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
	    mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

	    // Step 4: Set output file
	    opFile = getOutputMediaFile(MEDIA_TYPE_VIDEO).toString();
	    mMediaRecorder.setOutputFile(opFile);
	    
	    
	    // Step 5: Set the preview output
	    mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
	    //mCamera.setDisplayOrientation(90);

	    // Step 6: Prepare configured MediaRecorder
	    try {
	        mMediaRecorder.prepare();
	    } catch (IllegalStateException e) {
	        e.printStackTrace();
	        releaseMediaRecorder();
	        return false;
	    } catch (IOException e) {
	    	e.printStackTrace();
	        releaseMediaRecorder();
	        return false;
	    }
	    return true;
	}

	
	// Saving
	
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
		
		
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	
	// Relesing
	@Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }
	
	private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera_activity2, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
