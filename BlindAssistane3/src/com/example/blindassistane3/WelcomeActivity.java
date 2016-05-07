package com.example.blindassistane3;


import java.util.Locale;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class WelcomeActivity extends Activity {
	
	TextToSpeech tts;
	String txt;
	private static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		mContext=getApplicationContext();
		tts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
	         @Override
	         public void onInit(int status) {
	            if(status != TextToSpeech.ERROR) {
	               tts.setLanguage(Locale.UK);
	               txt = "Welcome to Blind Assistance System. Click anywhere to Start Application";	               
	               tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
	            }
	         }
	      });
		
		rLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startCameraActivity(v);
				
			}
		});
	}
	
	public static Context getContext() {
        return mContext;
    }
	
	public void startCameraActivity(View view) {
		Intent intent = new Intent(this, CameraActivity2.class);
		startActivity(intent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
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
