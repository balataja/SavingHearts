package com.example.savinghearts.heartrate;

import android.os.Bundle;

import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;

public class WorkoutScreen extends Activity_HeartRateDisplayBase{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        showDataDisplay("Connecting...");
        super.onCreate(savedInstanceState);
	}
    
	@Override
	protected void requestAccessToPcc() {
        AntPlusHeartRatePcc.requestAccess(this, this, base_IPluginAccessResultReceiver, base_IDeviceStateChangeReceiver);
	}
}
