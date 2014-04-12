package com.example.savinghearts.activities;

import java.text.DecimalFormat;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.example.savinghearts.R;
import com.example.savinghearts.activities.*;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
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
	private DecimalFormat oneDigit;
	
    private PieChart belowPie;
    private Segment s1, s2;
    private Paint clear, red;

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
        oneDigit = new DecimalFormat("#,##0.0");
        temp_time.setText(oneDigit.format(calories) + " min");
        temp_maxHR.setText(Integer.toString(maxHeartRate) + " bpm");
        temp_calories.setText(Double.toString(calories)+ " cal" );
        temp_aveHR.setText(Integer.toString(aveHeartRate)+ " bpm");
        temp_mets.setText(Double.toString(mets));
        
        //Reset the text display
        oneDigit = new DecimalFormat("#,##0.0");
        temp_time.setText(oneDigit.format(calories) + " min");
        temp_maxHR.setText(Integer.toString(maxHeartRate) + " bpm");
        temp_calories.setText(Double.toString(calories)+ " cal" );
        temp_aveHR.setText(Integer.toString(aveHeartRate)+ " bpm");
        temp_mets.setText(Double.toString(mets));
        
        belowPie = (PieChart) findViewById(R.id.belowPieChart);
        
        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);
        
        SegmentFormatter sf1 = new SegmentFormatter();
        sf1.configure(getApplicationContext(), R.xml.pie_segment_formatter);

        sf1.getFillPaint().setMaskFilter(emf);
        System.out.println(sf1.getOuterEdgePaint());
        
        red = new Paint();
        red.setColor(0);
        clear = new Paint();
        clear.setColor(0);
        sf1.setOuterEdgePaint(clear);
        sf1.setInnerEdgePaint(clear);

        SegmentFormatter sf2 = new SegmentFormatter();
        sf2.setFillPaint(clear);
        sf2.setOuterEdgePaint(clear);
        sf2.setInnerEdgePaint(clear);
        
        s1 = new Segment("s1", 10);
        s2 = new Segment("s2", 1);
        belowPie.addSeries(s1, sf1);
        belowPie.addSeries(s2, sf2);
        
        belowPie.getBorderPaint().setColor(Color.TRANSPARENT);
        belowPie.getBackgroundPaint().setColor(Color.TRANSPARENT);
        belowPie.redraw();
	}
}