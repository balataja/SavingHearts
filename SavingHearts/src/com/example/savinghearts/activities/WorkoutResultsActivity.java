package com.example.savinghearts.activities;

import java.text.DecimalFormat;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.example.savinghearts.R;
import com.example.savinghearts.activities.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WorkoutResultsActivity extends Activity implements OnClickListener{

	private int maxHeartRate = 0;
	private String activityName = null;
	private double mets = 0.0;
	private int aveHeartRate;
	private double calories;
	private int below = 0;
	private int fatburn = 0;
	private int aerobic = 0;
	private int anaerobic = 0;
	private int maximal = 0;
	private long minutes = 0;
	private DecimalFormat oneDigit;
	private int percent = 0;
	private int total;
	
    private PieChart belowPie, fatburnPie, aerobicPie, anaerobicPie, maximalPie;
    private Segment s1, s2;
    private Paint clear, red;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
   
        /*
         * below
         * fat burn
         * aerobic
         * anaerobic
         * maximal
         */
		System.out.println("starting workoutresults page");
        Bundle b = getIntent().getExtras();
		activityName = b.getString("activity");
		mets = b.getDouble("mets");
		aveHeartRate = (int) b.getDouble("aveHR");
		calories = b.getDouble("calories");
		minutes = b.getLong("minutes");
		maxHeartRate = b.getInt("maxHR");
		below = (int) b.getDouble("lightZone");
		fatburn = (int) b.getDouble("hardZone");
		aerobic = (int) b.getDouble("maxZone");
		anaerobic = (int) b.getDouble("moderateZone");
		maximal = (int) b.getDouble("maximal");
		total = below + fatburn + aerobic + anaerobic + maximal;

		setContentView(R.layout.workout_results_screen);
		
		TextView temp_time = (TextView)findViewById(R.id.results_duration);
        TextView temp_maxHR= (TextView)findViewById(R.id.results_maxHR);
        TextView temp_aveHR= (TextView)findViewById(R.id.results_aveHR);
        TextView temp_mets= (TextView)findViewById(R.id.results_mets);
        TextView temp_calories= (TextView)findViewById(R.id.results_calories);
        
        //set up button
        Button goToLogBtn = (Button) findViewById(R.id.btn_go_to_log);
        goToLogBtn.setOnClickListener(this);
        

        //Reset the text display
        oneDigit = new DecimalFormat("#,##0.0");
        temp_time.setText(minutes + " min");
        temp_maxHR.setText(Integer.toString(maxHeartRate) + " bpm");
        temp_calories.setText(oneDigit.format(calories)+ " cal" );
        temp_aveHR.setText(Integer.toString(aveHeartRate)+ " bpm");
        temp_mets.setText(Double.toString(mets));
        
        belowPie = (PieChart) findViewById(R.id.belowPieChart);
        fatburnPie = (PieChart) findViewById(R.id.fatburnPieChart);
        aerobicPie = (PieChart) findViewById(R.id.abovePieChart);
        anaerobicPie = (PieChart) findViewById(R.id.hardPieChart);
        maximalPie = (PieChart) findViewById(R.id.insanePieChart);
        
        if(total == 0)
        	total = 1;
        percent = (below / total)*100;
        formatPieChart(belowPie, percent);
        percent = (fatburn / total)*100;
        formatPieChart(fatburnPie, percent);
        percent = (aerobic / total)*100;
        formatPieChart(aerobicPie, percent);
        percent = (anaerobic / total)*100;
        formatPieChart(anaerobicPie, percent);
        percent = (maximal / total)*100;
        formatPieChart(maximalPie, percent);
	}
    
    private void formatPieChart(PieChart pie, int percentage) {
        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);
        
        EmbossMaskFilter emf2 = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);
        
        SegmentFormatter sf1 = new SegmentFormatter();
        sf1.configure(getApplicationContext(), R.xml.pie_segment_formatter);
        SegmentFormatter sf2 = new SegmentFormatter();
        sf2.configure(getApplicationContext(), R.xml.pie_segment_formatter2);

        sf1.getFillPaint().setMaskFilter(emf);
        sf2.getFillPaint().setMaskFilter(emf2);
        
        red = new Paint();
        red.setColor(0);
        clear = new Paint();
        clear.setColor(0);
        sf1.setOuterEdgePaint(clear);
        sf1.setInnerEdgePaint(clear);


        sf2.setOuterEdgePaint(clear);
        sf2.setInnerEdgePaint(clear);
        
        s1 = new Segment("", percentage);
        s2 = new Segment("", 100-percentage);
        pie.addSeries(s1, sf1);
        pie.addSeries(s2, sf2);
        
        pie.getBorderPaint().setColor(Color.TRANSPARENT);
        pie.getBackgroundPaint().setColor(Color.TRANSPARENT);
        pie.setTitle("");
        pie.redraw();
	}

	public void goToLog(){
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    }

	@Override
	public void onClick(View arg0) {
		goToLog();
		
	}
}