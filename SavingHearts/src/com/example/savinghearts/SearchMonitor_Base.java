/*
This software is subject to the license described in the License.txt file 
included with this software distribution. You may not use this file except in compliance 
with this license.

Copyright (c) Dynastream Innovations Inc. 2013
All rights reserved.
 */

package com.example.savinghearts;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc.IHeartRateDataReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc.IHeartRateDataTimestampReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc.IPage4AddtDataReceiver;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IDeviceStateChangeReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IPluginAccessResultReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusLegacyCommonPcc.ICumulativeOperatingTimeReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusLegacyCommonPcc.IManufacturerAndSerialReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusLegacyCommonPcc.IVersionAndModelReceiver;
import com.example.savinghearts.activities.MainActivity;
import com.example.savinghearts.heartrate.WorkoutScreen;

import java.math.BigDecimal;
import java.util.EnumSet;

/**
 * Base class to connects to Heart Rate Plugin and display all the event data.
 */
public abstract class SearchMonitor_Base extends Activity
{
    protected abstract void requestAccessToPcc();

    public static AntPlusHeartRatePcc hrPcc = null;

    TextView tv_status;

    TextView tv_estTimestamp;

    TextView tv_computedHeartRate;
    TextView tv_heartBeatCounter;
    TextView tv_timestampOfLastEvent;

    TextView tv_manufacturerSpecificByte;
    TextView tv_previousToLastHeartBeatEventTimeStamp;

    TextView tv_cumulativeOperatingTime;

    TextView tv_manufacturerID;
    TextView tv_serialNumber;

    TextView tv_hardwareVersion;
    TextView tv_softwareVersion;
    TextView tv_modelNumber;
    Bundle bundle;
    Intent intent;
    

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        handleReset();
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
    	intent = new Intent(this, MainActivity.class);
	   startActivity(intent);
	  
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
                	bundle = new Bundle();
             		bundle.putInt("monitor", 1);
             		intent.putExtras(bundle);  
             		startActivity(intent);
                        
                    break;
                case CHANNEL_NOT_AVAILABLE:
                    Toast.makeText(SearchMonitor_Base.this, "Channel Not Available", Toast.LENGTH_SHORT).show();               
                    break;
                case OTHER_FAILURE:
                    Toast.makeText(SearchMonitor_Base.this, "RequestAccess failed. See logcat for details.", Toast.LENGTH_SHORT).show();
                    tv_status.setText("Error. Do Menu->Reset.");
                    break;
                case DEPENDENCY_NOT_INSTALLED:
                  
                    AlertDialog.Builder adlgBldr = new AlertDialog.Builder(SearchMonitor_Base.this);
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

                            SearchMonitor_Base.this.startActivity(startStore);
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
                	Bundle fromMonitor = getIntent().getExtras();
        			int monitor=0;
        			if (fromMonitor != null) 
        			   {
        			     monitor = fromMonitor.getInt("isConnected");
        			   }
        			bundle = new Bundle();
             		bundle.putInt("monitor", monitor);
             		intent.putExtras(bundle);
             		startActivity(intent);
                   
                    break;
                case UNRECOGNIZED:
                    //TODO This flag indicates that an unrecognized value was sent by the service, an upgrade of your PCC may be required to handle this new value.
                    Toast.makeText(SearchMonitor_Base.this, "Failed: UNRECOGNIZED. Upgrade Required?", Toast.LENGTH_SHORT).show();
                  
                    break;
                default:
                    Toast.makeText(SearchMonitor_Base.this, "Unrecognized result: " + resultCode, Toast.LENGTH_SHORT).show();
                    
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
                       
                        if(newDeviceState == DeviceState.DEAD)
                        {  hrPcc = null;
                        	bundle = new Bundle();
                        	bundle.putInt("monitor", 0);
                        	intent.putExtras(bundle);
                        	startActivity(intent);
                        }
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
}
