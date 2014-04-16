package com.example.savinghearts.fragments;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ListIterator;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.example.savinghearts.R;
import com.example.savinghearts.model.ActivityData;
import com.example.savinghearts.sql.SavingHeartsDataSource;

import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class LogFragment extends Fragment{
	public static final String ARG_SECTION_NUMBER = "section_number";
	
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
    
    View view, view2;
    
	SavingHeartsDataSource db;
    
	public LogFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ScrollView sv = new ScrollView(getActivity().getApplicationContext());
		sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		LinearLayout ll = new LinearLayout(getActivity().getApplicationContext());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		sv.addView(ll);
		db = SavingHeartsDataSource.getInstance(getActivity().getApplicationContext());
		List <ActivityData> activities = db.getAllActivities();
		//ListIterator <ActivityData> li = activities.listIterator();
		//while(li.hasNext())
		if(activities.size()>0)
		{
			ActivityData activityData = activities.remove(0);
			
			view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.log_workout_layout, null);
			LinearLayout workout = (LinearLayout) view.findViewById(R.id.workout_result);
			
			addWorkoutToView(activityData, workout);
		}
		if(activities.size() > 1)
		{
			ActivityData activityData2 = activities.remove(1);
			
			view2 = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.log_workout_layout, null);
			LinearLayout workout2 = (LinearLayout) view2.findViewById(R.id.workout_result);
			
			addWorkoutToView(activityData2, workout2);
		}
        ll.addView(view);
        ll.addView(view2);
		
		return sv;
	}
	
	private void addWorkoutToView(ActivityData activityData, LinearLayout workout)
	{
		/*
         * below
         * fat burn
         * aerobic
         * anaerobic
         * maximal
         */
		activityName = activityData.getActivityName();
		mets = activityData.getMets();
		aveHeartRate = activityData.getAveHR();
		calories = activityData.getCalories();
		minutes = activityData.getMinHR();
		maxHeartRate = activityData.getMaxHR();
		below = (int) activityData.getLightZones();
		fatburn = (int) activityData.getModerateZones();
		aerobic = (int) activityData.getHardZones();
		anaerobic = (int) activityData.getMaxZones();
		maximal = (int) activityData.getHardZones();
		total = below + fatburn + aerobic + anaerobic + maximal;
		
		TextView temp_time = (TextView)workout.findViewById(R.id.results_duration);
        TextView temp_maxHR= (TextView)workout.findViewById(R.id.results_maxHR);
        TextView temp_aveHR= (TextView)workout.findViewById(R.id.results_aveHR);
        TextView temp_mets= (TextView)workout.findViewById(R.id.results_mets);
        TextView temp_calories= (TextView)workout.findViewById(R.id.results_calories);
                
        //Reset the text display
        oneDigit = new DecimalFormat("#,##0.0");
        temp_time.setText(minutes + " min");
        temp_maxHR.setText(Integer.toString(maxHeartRate) + " bpm");
        temp_calories.setText(oneDigit.format(calories)+ " cal" );
        temp_aveHR.setText(Integer.toString(aveHeartRate)+ " bpm");
        temp_mets.setText(Double.toString(mets));
        
        belowPie = (PieChart) workout.findViewById(R.id.belowPieChart);
        fatburnPie = (PieChart) workout.findViewById(R.id.fatburnPieChart);
        aerobicPie = (PieChart) workout.findViewById(R.id.abovePieChart);
        anaerobicPie = (PieChart) workout.findViewById(R.id.hardPieChart);
        maximalPie = (PieChart) workout.findViewById(R.id.insanePieChart);
        
        percent = 35;//(below / total)*100;
        formatPieChart(belowPie, percent);
        percent = 10;//(fatburn / total)*100;
        formatPieChart(fatburnPie, percent);
        percent = 17;//(aerobic / total)*100;
        formatPieChart(aerobicPie, percent);
        percent = 42;//(anaerobic / total)*100;
        formatPieChart(anaerobicPie, percent);
        percent = 60;//(maximal / total)*100;
        formatPieChart(maximalPie, percent);
	}
    private void formatPieChart(PieChart pie, int percentage) {
        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);
        
        EmbossMaskFilter emf2 = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);
        
        SegmentFormatter sf1 = new SegmentFormatter();
        sf1.configure(getActivity().getApplicationContext(), R.xml.pie_segment_formatter);
        SegmentFormatter sf2 = new SegmentFormatter();
        sf2.configure(getActivity().getApplicationContext(), R.xml.pie_segment_formatter2);

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
}
