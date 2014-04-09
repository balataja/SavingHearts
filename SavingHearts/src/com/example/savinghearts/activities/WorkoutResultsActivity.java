package com.example.savinghearts.activities;

import com.example.savinghearts.R;
import com.example.savinghearts.activities.*;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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
		System.out.println("starting workoutresults page");
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
		
		setContentView(R.layout.workout_results_screen);
		TextView temp_time = (TextView)findViewById(R.id.results_duration);
        TextView temp_maxHR= (TextView)findViewById(R.id.results_maxHR);
        TextView temp_aveHR= (TextView)findViewById(R.id.results_aveHR);
        TextView temp_mets= (TextView)findViewById(R.id.results_mets);
        TextView temp_calories= (TextView)findViewById(R.id.results_calories);
        

        //Reset the text display
        temp_time.setText(Long.toString(minutes));
        temp_maxHR.setText(Integer.toString(maxHeartRate));
        temp_calories.setText(Double.toString(calories));
        temp_aveHR.setText(Integer.toString(aveHeartRate));
        temp_mets.setText(Double.toString(mets));
	}
}
