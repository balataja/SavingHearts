package com.example.savinghearts.activities;

import android.app.Activity;
import android.os.Bundle;

public class WorkoutResultsActivity extends Activity{

	private int maxHeartRate = 0;
	private String activityName = null;
	private double mets = 0.0;
	private int aveHeartRate;
	private double calories;
	private int maxZones = 0;
	private int hardZones = 0;
	private int moderateZones = 0;
	private int lightZones = 0;
	private long minutes = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
        Bundle b = getIntent().getExtras();
		activityName = b.getString("activity");
		mets = b.getDouble("mets");
		aveHeartRate = (int) b.getDouble("aveHR");
		calories = b.getDouble("calories");
		minutes = b.getLong("minutes");
		maxHeartRate = b.getInt("maxHR");
		lightZones = (int) b.getDouble("lightZone");
		hardZones = (int) b.getDouble("hardZone");
		maxZones = (int) b.getDouble("maxZone");
		moderateZones = (int) b.getDouble("moderateZone");
        
	}
}
