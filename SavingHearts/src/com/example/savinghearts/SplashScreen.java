package com.example.savinghearts;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class SplashScreen extends Activity{
	
	final private int SPLASH_DURATION = 5000;
	private Dialog mSplashScreenDialog;
	
	public SplashScreen() {}
	
	/**
	 * Method to display splash screen image when starting the app
	 */
	public void showSplashScreen() {
		
		ImageView splashscreen = new ImageView(this);
		splashscreen.setOnTouchListener(new OnTouchListener()
	    {
	
	        @Override
	        public boolean onTouch(View v, MotionEvent event)
	        {
	        	dismissSplashScreen(); 
	            return false;
	        }
	        
	   });
		
		Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.splashscreen);
		splashscreen.setImageBitmap(image);
		
		mSplashScreenDialog = new Dialog(this, R.style.SplashScreen);
		mSplashScreenDialog.setTitle(R.string.app_name);
		mSplashScreenDialog.setContentView(splashscreen);
		mSplashScreenDialog.setCancelable(false);
		mSplashScreenDialog.show();
		
		final Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	      @Override
	      public void run() {
	
	    	  dismissSplashScreen();
	    	  
	    	  return;
	    	  
	      }
	    }, SPLASH_DURATION);
		
	}
	
	/**
	 * Method used to hide splash screen image
	 */
	protected void dismissSplashScreen() {
		if (mSplashScreenDialog != null) {
			mSplashScreenDialog.dismiss();
			mSplashScreenDialog = null;
		}
	}
}