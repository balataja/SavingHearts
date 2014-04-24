package com.example.savinghearts.fragments;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.example.savinghearts.R;
import com.example.savinghearts.model.ActivityData;
import com.example.savinghearts.sql.SavingHeartsDataSource;

import android.content.Intent;
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
import android.widget.RelativeLayout;
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
    
	private int year;
	private int month;
	private int day;
	private TextView tvDisplayDate;
    
    View view1, view2;
    
	SavingHeartsDataSource db;
    
	public LogFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_log, container, false);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.charrtholder);
		System.out.println("about to get data base");
		db = SavingHeartsDataSource.getInstance(this.getActivity());
		
	    final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		List <ActivityData> activities = db.getAllActivitiesInOneDate(String.valueOf(day), String.valueOf(month), String.valueOf(year));
		
		if(activities.size()>0)
		{
			ActivityData activityData = activities.remove(0);
			
			view1 = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.log_workout_layout, null);
			RelativeLayout workout = (RelativeLayout) view1.findViewById(R.id.results_layout);
			
			addWorkoutToView(activityData, workout);
		}
		if(activities.size() > 1)
		{
			ActivityData activityData2 = activities.remove(1);
			
			view2 = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.log_workout_layout, null);
			RelativeLayout workout2 = (RelativeLayout) view2.findViewById(R.id.results_layout);
			
			addWorkoutToView(activityData2, workout2);
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			layoutParams.setMargins(0, 0, 0, 20);
		
		if(activities.size() > 0)
		{
			ll.addView(view1, layoutParams);
			System.out.println("adding first layout");
		}
		if(activities.size() > 1)
			ll.addView(view2, layoutParams);
		
		Button button= (Button) view.findViewById(R.id.addManually);
	    button.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            //Intent i = new Intent(getActivity(), AddActivityManually.class);
	            //getActivity().startActivity(i);
	        }
	    });
	    
	    tvDisplayDate = (TextView) view.findViewById(R.id.date);
		
		tvDisplayDate.setText(new StringBuilder()
		// Month is 0 based, just add 1
		.append(month + 1).append("-").append(day).append("-")
		.append(year).append(" "));
		Button left = (Button) view.findViewById(R.id.left);
	    left.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            if(day == 1)
	            {
	            	day = 30;
	            	month--;
	            	if (month < 1)
	            	{
	            		month = 12;
	            		year--;
	            	}
	            } else {
		        	day--;
	            }
	            tvDisplayDate.setText(new StringBuilder()
	    		// Month is 0 based, just add 1
	    		.append(month + 1).append("-").append(day).append("-")
	    		.append(year).append(" "));
	            refresh();
	        }
	    });
	    Button right = (Button) view.findViewById(R.id.right);
	    right.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            if(day < c.get(Calendar.DAY_OF_MONTH))
	            {	
	            	if(day == 30)
	            	{
	            		day = 1;
	            		month++;
	            		if(month > 12)
	            		{
	            			month = 1;
	            			year++;
	            		}
	            	} else {
	            		day++;
	            	}
	            }
	            tvDisplayDate.setText(new StringBuilder()
	    		// Month is 0 based, just add 1
	    		.append(month + 1).append("-").append(day).append("-")
	    		.append(year).append(" "));
	            refresh();
	        }
	    });
        
		return view;
	}
	
	protected void refresh() {
		View view = getView();
		ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.charrtholder);
		viewGroup.removeAllViews();
		
		LinearLayout ll = (LinearLayout) viewGroup;
		String strDay = null, strMonth = null;
		if(day < 10)
		{
			strDay = String.valueOf("0"+day);
		} else
			strDay = String.valueOf(day);
		if(month < 10)
		{
			strMonth = String.valueOf("0"+(month+1));
		} else
			strMonth = String.valueOf(month+1);
		System.out.println(strDay+ "  "+strMonth+"  "+year);

		List <ActivityData> activities = db.getAllActivitiesInOneDate(strDay, strMonth, String.valueOf(year));
		System.out.println("activities: "+activities.size());
		if(activities.size()>0)
		{
			ActivityData activityData = activities.remove(0);
			
			view1 = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.log_workout_layout, null);
			RelativeLayout workout = (RelativeLayout) view1.findViewById(R.id.results_layout);
			
			addWorkoutToView(activityData, workout);
		}
		if(activities.size() > 1)
		{
			ActivityData activityData2 = activities.remove(1);
			
			view2 = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.log_workout_layout, null);
			RelativeLayout workout2 = (RelativeLayout) view2.findViewById(R.id.results_layout);
			
			addWorkoutToView(activityData2, workout2);
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			layoutParams.setMargins(0, 0, 0, 20);
		
		if(activities.size() > 0)
		{
			ll.addView(view1, layoutParams);
			System.out.println("adding first layout");
		}
		if(activities.size() > 1)
			ll.addView(view2, layoutParams);
	}

	private void addWorkoutToView(ActivityData activityData, RelativeLayout workout2)
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
		System.out.println(below +" "+ fatburn +" "+ aerobic +" "+ anaerobic +" "+ maximal);
		
		TextView temp_time = (TextView)workout2.findViewById(R.id.results_duration);
        TextView temp_maxHR= (TextView)workout2.findViewById(R.id.results_maxHR);
        TextView temp_aveHR= (TextView)workout2.findViewById(R.id.results_aveHR);
        TextView temp_mets= (TextView)workout2.findViewById(R.id.results_mets);
        TextView temp_calories= (TextView)workout2.findViewById(R.id.results_calories);
        TextView activity_name = (TextView)workout2.findViewById(R.id.typeOfActivity);
                
        //Reset the text display
        oneDigit = new DecimalFormat("#,##0.0");
        temp_time.setText(minutes + " min");
        temp_maxHR.setText(Integer.toString(maxHeartRate) + " bpm");
        temp_calories.setText(oneDigit.format(calories)+ " cal" );
        temp_aveHR.setText(Integer.toString(aveHeartRate)+ " bpm");
        temp_mets.setText(Double.toString(mets));
        activity_name.setText(activityName);
        
        belowPie = (PieChart) workout2.findViewById(R.id.belowPieChart);
        fatburnPie = (PieChart) workout2.findViewById(R.id.fatburnPieChart);
        aerobicPie = (PieChart) workout2.findViewById(R.id.abovePieChart);
        anaerobicPie = (PieChart) workout2.findViewById(R.id.hardPieChart);
        maximalPie = (PieChart) workout2.findViewById(R.id.insanePieChart);
        
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