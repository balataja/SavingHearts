package com.example.savinghearts.activities;
import com.example.savinghearts.*;
import com.example.savinghearts.R.drawable;
import com.example.savinghearts.R.id;
import com.example.savinghearts.R.layout;
import com.example.savinghearts.R.menu;
import com.example.savinghearts.R.string;
import com.example.savinghearts.R.style;
import com.example.savinghearts.activities.*;
import com.example.savinghearts.fragments.*;
import com.example.savinghearts.heartrate.*;
import com.example.savinghearts.helpers.*;

import java.util.Locale;

import com.example.savinghearts.heartrate.Activity_SearchUiHeartRateSampler;
import com.example.savinghearts.helpers.SettingsHelper;
import com.example.savinghearts.sql.SavingHeartsDataSource;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	final private int SPLASH_DURATION = 5000;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private Dialog mSplashScreenDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        //showSplashScreen();
		if (isInitalRun()) {
  		  setInitialRun(false);
  		  
  		  // user will be redirected to the settings activity in order to
  		  // save their weight and birthday
  		  Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
  		  startActivity(intent);
			
			}
	  	
        setContentView(R.layout.activity_main);
        
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}
	
	/**
	 * Method to display splash screen image when starting the app
	 */
	protected void showSplashScreen() {
		
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
	    	  
	    	  if (isInitalRun()) {
	    		  setInitialRun(false);
	    		  
	    		  // user will be redirected to the settings activity in order to
	    		  // save their weight and birthday
	    		  Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
	    		  startActivity(intent);
				
				} else {
					return;
				}
	    	  
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

	private boolean isInitalRun() {
		return SettingsHelper.isInitialRun(this);
	}
	
	private void setInitialRun (boolean initRun) {
		SettingsHelper.setInitialRun(this, initRun);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Method for Start Workout button on Home Fragment
	 */
	public void startButton (View view)
	{
		/*
		if(SearchMonitor_Base.hrPcc != null)
        {
			SearchMonitor_Base.hrPcc.releaseAccess();
			SearchMonitor_Base.hrPcc = null;
        }
       */
		Bundle args = new Bundle();
		Bundle fromMonitor = getIntent().getExtras();
		int monitor=0;
		if (fromMonitor != null) 
		   {
		     monitor = fromMonitor.getInt("monitor");
		   }
		args.putInt("monitor", monitor);
		System.out.println("trying to press start button..");
		Intent intent = new Intent(this, METSListActivity.class);
		intent.putExtras(args);
		startActivity(intent);
	}
	
	/**
	 * Method for Connect To Device button on Monitor Fragment
	 
	public void sendMessage (View view)
	{	/*
        Intent i = new Intent(this, Activity_SearchUiHeartRateSampler.class);
        startActivity(i);
        /*
		Intent intent = new Intent(this, Activity_Dashboard.class);
		startActivity(intent);
	}
*/
	/**
	 * Starts selected activity when user selects options menu item
	 * @return boolean returns true if valid menu option selected
	 */
	//@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch(item.getItemId()) {
		
		case R.id.action_settings: //user selects "Settings" from the menu
			Intent intent = new Intent(this, SettingsActivity.class);
			this.startActivity(intent);
			return true;
		case R.id.action_about: //user selects "About" from the menu
			Intent intent2 = new Intent(this, AboutActivity.class);
			this.startActivity(intent2);
			return true;
		default:
			return false;				
		}		
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			Bundle args = new Bundle();
			Bundle fromMonitor = getIntent().getExtras();
			int monitor=0;
			if (fromMonitor != null) 
			   {
			     monitor = fromMonitor.getInt("monitor");
			   }
			args.putInt("monitor", monitor);
			
			switch (position) {
			case 0:
				fragment = new HomeFragment();
				fragment.setArguments(args);
				args.putInt(HomeFragment.ARG_SECTION_NUMBER, position + 1);
				return fragment;
			case 1:
				fragment = new LogFragment();
				fragment.setArguments(args);
				args.putInt(LogFragment.ARG_SECTION_NUMBER, position + 1);
				return fragment;
			case 2:
				fragment = new MonitorFragment();
				fragment.setArguments(args);
				args.putInt(MonitorFragment.ARG_SECTION_NUMBER, position + 1);
				return fragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	

}
