/*
This software is subject to the license described in the License.txt file 
included with this software distribution. You may not use this file except in compliance 
with this license.

Copyright (c) Dynastream Innovations Inc. 2013
All rights reserved.
 */

package com.example.savinghearts.heartrate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc.IHeartRateDataReceiver;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IDeviceStateChangeReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IPluginAccessResultReceiver;
import com.example.savinghearts.R;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.example.savinghearts.activities.METSListActivity;
import com.example.savinghearts.heartrate.Activity_HeartRateDisplayBase;

import java.math.BigDecimal;
import java.util.EnumSet;

/**
 * Base class to connects to Heart Rate Plugin and display all the event data.
 */
public abstract class Activity_HeartRateDisplayBase extends Activity implements SensorEventListener
{
    protected abstract void requestAccessToPcc();
    
    //Sensor Variables
    AntPlusHeartRatePcc hrPcc = null;
    TextView tv_status;
    TextView tv_computedHeartRate;
    private static final int HISTORY_SIZE = 300;            // number of points to plot in history
    private SensorManager sensorMgr = null;
    private Sensor orSensor = null;
 
    //Plot Variables
    private XYPlot aprHistoryPlot = null;
    private SimpleXYSeries azimuthHistorySeries = null;
    private int HeartRatePoint = 50;

	//Timer variables
	private Button startButton;
	private Button pauseButton;
	private TextView timerValue;	
	private long startTime = 0L;	
	private Handler customHandler = new Handler();	
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	
	//Database variables
	private int maxHeartRate = 0;
	private String activityName = null;
	private double mets = 0.0;
	private int aveHeartRate;
	private double calories;
	private int maxZones = 0;
	private int hardZones = 0;
	private int moderateZones = 0;
	private int lightZones = 0;
	private int totalUpdates = 0;
	private double pounds = 0.0;
	private double kilos = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        handleReset();
        activityName = METSListActivity.name;
        mets = METSListActivity.mets;
        System.out.println(activityName + "........." + mets);
    }

    /**
     * Resets the PCC connection to request access again and clears any existing display data.
     */    
    protected void handleReset()
    {
        //Release the old access if it exists
        if(hrPcc != null)
        {
            hrPcc.releaseAccess();
            hrPcc = null;
        }

        requestAccessToPcc();
    }

    protected void showDataDisplay(String status)
    {
        setContentView(R.layout.activity_heart_rate_test);

        tv_status = (TextView)findViewById(R.id.textView_Status);
        tv_computedHeartRate = (TextView)findViewById(R.id.textView_ComputedHeartRate);
        //Reset the text display
        tv_status.setText(status);

        tv_computedHeartRate.setText("---");
        // setup the APR History plot:
        aprHistoryPlot = (XYPlot) findViewById(R.id.aprHistoryPlot);
        azimuthHistorySeries = new SimpleXYSeries("Heart Rate");
        azimuthHistorySeries.useImplicitXVals();
        aprHistoryPlot.setRangeBoundaries(50, 150, BoundaryMode.FIXED);
        aprHistoryPlot.setDomainBoundaries(0, 300, BoundaryMode.FIXED);
        aprHistoryPlot.addSeries(azimuthHistorySeries, new LineAndPointFormatter(Color.rgb(200, 100, 100), Color.RED, null, null));
        aprHistoryPlot.setDomainStepValue(5);
        aprHistoryPlot.setTicksPerRangeLabel(3);
        aprHistoryPlot.setRangeLabel("Heart Rate (BPM)");
 
        // register for orientation sensor events:
        sensorMgr = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        for (Sensor sensor : sensorMgr.getSensorList(Sensor.TYPE_ORIENTATION)) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
                orSensor = sensor;
            }
        }
 
        // if we can't access the orientation sensor then exit:
        if (orSensor == null) {
            System.out.println("Failed to attach to orSensor.");
            cleanup();
        }
 
        sensorMgr.registerListener(this, orSensor, SensorManager.SENSOR_DELAY_UI);
 
		timerValue = (TextView) findViewById(R.id.timerValue);

		startButton = (Button) findViewById(R.id.startButton);
		
		startButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				startTime = SystemClock.uptimeMillis();
				customHandler.postDelayed(updateTimerThread, 0);

			}
		});

		pauseButton = (Button) findViewById(R.id.pauseButton);
		
		pauseButton.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View view) {
			
				timeSwapBuff += timeInMilliseconds;
				customHandler.removeCallbacks(updateTimerThread);

			}
		});
    }
    
    private void cleanup() {
        // unregister with the orientation sensor before exiting:
        sensorMgr.unregisterListener(this);
        finish();
    }
 
    // Called whenever a new orSensor reading is taken.
    @Override
    public synchronized void onSensorChanged(SensorEvent sensorEvent) {
 
        // get rid the oldest sample in history:
        if (azimuthHistorySeries.size() > HISTORY_SIZE) {
            azimuthHistorySeries.removeFirst();
        }
         // add the latest history sample:
        
//adding data point to graph...........................................................................................        
        azimuthHistorySeries.addLast(null, HeartRatePoint);
        // redraw the Plots:
        aprHistoryPlot.redraw();
        
        //Workout variable updates
        if(HeartRatePoint > maxHeartRate)
        {
        	maxHeartRate = HeartRatePoint;
        }
        totalUpdates++;
        aveHeartRate = aveHeartRate*(totalUpdates-1)/totalUpdates + HeartRatePoint/totalUpdates;
        
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

    /**
     * Switches the active view to the data display and subscribes to all the data events
     */
    public void subscribeToHrEvents()
    {
        hrPcc.subscribeHeartRateDataEvent(new IHeartRateDataReceiver()
        {
            @Override
            public void onNewHeartRateData(final long estTimestamp, final EnumSet<EventFlag> eventFlags,
                final int computedHeartRate, final long heartBeatCounter)
            {
                runOnUiThread(new Runnable()
                {                                            
                    @Override
                    public void run()
                    {

                        tv_computedHeartRate.setText(String.valueOf(computedHeartRate));
                        //tv_heartBeatCounter.setText(String.valueOf(heartBeatCounter));
                        HeartRatePoint = Integer.parseInt(String.valueOf(computedHeartRate));
                    }
                });
            }
        });
    }

    protected IPluginAccessResultReceiver<AntPlusHeartRatePcc> base_IPluginAccessResultReceiver =
        new IPluginAccessResultReceiver<AntPlusHeartRatePcc>()
        {
        //Handle the result, connecting to events on success or reporting failure to user.
        @Override
        public void onResultReceived(AntPlusHeartRatePcc result, RequestAccessResult resultCode,
            DeviceState initialDeviceState)
        {
            showDataDisplay("Connecting..."); 
            switch(resultCode)
            {
                case SUCCESS:
                    hrPcc = result;
                    tv_status.setText(result.getDeviceName() + ": " + initialDeviceState);
                    subscribeToHrEvents();
                    break;
                case CHANNEL_NOT_AVAILABLE:
                    Toast.makeText(Activity_HeartRateDisplayBase.this, "Channel Not Available", Toast.LENGTH_SHORT).show();
                    tv_status.setText("Error. Do Menu->Reset.");
                    break;
                case OTHER_FAILURE:
                    Toast.makeText(Activity_HeartRateDisplayBase.this, "RequestAccess failed. See logcat for details.", Toast.LENGTH_SHORT).show();
                    tv_status.setText("Error. Do Menu->Reset.");
                    break;
                case DEPENDENCY_NOT_INSTALLED:
                    tv_status.setText("Error. Do Menu->Reset.");
                    AlertDialog.Builder adlgBldr = new AlertDialog.Builder(Activity_HeartRateDisplayBase.this);
                    adlgBldr.setTitle("Missing Dependency");
                    adlgBldr.setMessage("The required service\n\"" + AntPlusHeartRatePcc.getMissingDependencyName() + "\"\n was not found. You need to install the ANT+ Plugins service or you may need to update your existing version if you already have it. Do you want to launch the Play Store to get it?");
                    adlgBldr.setCancelable(true);
                    adlgBldr.setPositiveButton("Go to Store", new OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent startStore = null;
                            startStore = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=" + AntPlusHeartRatePcc.getMissingDependencyPackageName()));
                            startStore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            Activity_HeartRateDisplayBase.this.startActivity(startStore);
                        }
                    });
                    adlgBldr.setNegativeButton("Cancel", new OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog waitDialog = adlgBldr.create();
                    waitDialog.show();
                    break;
                case USER_CANCELLED:
                    tv_status.setText("Cancelled. Do Menu->Reset.");
                    break;
                case UNRECOGNIZED:
                    //TODO This flag indicates that an unrecognized value was sent by the service, an upgrade of your PCC may be required to handle this new value.
                    Toast.makeText(Activity_HeartRateDisplayBase.this, "Failed: UNRECOGNIZED. Upgrade Required?", Toast.LENGTH_SHORT).show();
                    tv_status.setText("Error. Do Menu->Reset.");
                    break;
                default:
                    Toast.makeText(Activity_HeartRateDisplayBase.this, "Unrecognized result: " + resultCode, Toast.LENGTH_SHORT).show();
                    tv_status.setText("Error. Do Menu->Reset.");
                    break;
            } 
        }
        };

        //Receives state changes and shows it on the status display line
        protected  IDeviceStateChangeReceiver base_IDeviceStateChangeReceiver = 
            new IDeviceStateChangeReceiver()
        {                    
            @Override
            public void onDeviceStateChange(final DeviceState newDeviceState)
            {
                runOnUiThread(new Runnable()
                {                                            
                    @Override
                    public void run()
                    {
                        tv_status.setText(hrPcc.getDeviceName() + ": " + newDeviceState);
                        if(newDeviceState == DeviceState.DEAD)
                            hrPcc = null;
                    }
                });


            }
        };

        @Override
        protected void onDestroy()
        {
            if(hrPcc != null)
            {
                hrPcc.releaseAccess();
                hrPcc = null;
            }
            super.onDestroy();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.activity_heart_rate, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            switch(item.getItemId())
            {
                case R.id.menu_reset:
                    handleReset();
                    tv_status.setText("Resetting...");
                    return true;
                default:
                    return super.onOptionsItemSelected(item);                
            }
        }
        
    	private Runnable updateTimerThread = new Runnable() {

    		public void run() {
    			
    			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
    			
    			updatedTime = timeSwapBuff + timeInMilliseconds;

    			int secs = (int) (updatedTime / 1000);
    			int mins = secs / 60;
    			secs = secs % 60;
    			int milliseconds = (int) (updatedTime % 1000);
    			timerValue.setText("" + mins + ":"
    					+ String.format("%02d", secs) + ":"
    					+ String.format("%03d", milliseconds));
    			customHandler.postDelayed(this, 0);
    		}

    	};
}
