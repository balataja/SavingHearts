package com.example.savinghearts.activities;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;

import com.example.savinghearts.R;
import com.example.savinghearts.model.ActivityData;
import com.example.savinghearts.sql.SavingHeartsDataSource;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class BarPlot extends Activity {
    private GraphicalView mChartView;
    SavingHeartsDataSource db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_plot);
        db = SavingHeartsDataSource.getInstance(this);
       insertActivity();
        
       int y[]={25,30,55,40};
        //the minimum heart rate
        //the maximum heart rate
        //in 7 days
        //Data 1
        ArrayList<Integer> yMaxHR = new ArrayList<Integer>();
        ArrayList<Integer> xDates = new ArrayList<Integer>();

        int dbSize = db.getActivityDataCount();
        List<ActivityData> activities = db.getAllActivitiesInPast7Days();
        int activitiesSize = activities.size();
        String check = Integer.toString(activitiesSize);
        String date = db.getActivity(1).getTimestamp();
        /*
         * compare each activities in a list
         * if they have the same timestamp then compare the max HR and insert it to y
         * if they have different timestamp go to the next list of acivities
         * also get the dates
         * 
         
        int j=0,h=0,k=0;
        int timestamp = Integer.parseInt(activities.get(0).getDate());     
        yMaxHR.add(activities.get(j).getMaxHR());
        xDates.add(Integer.parseInt(activities.get(j).getDate()));
        while(j != activities.size() ){
        	if(j+1 != activities.size()){
	        	if(timestamp==Integer.parseInt(activities.get(j+1).getDate())){
	        		if(yMaxHR.get(j) < activities.get(j+1).getMaxHR()){
	        			yMaxHR.set(k, activities.get(j+1).getMaxHR());
	        		}
	        	}
	        	else {
	        		timestamp = Integer.parseInt(activities.get(j+1).getDate());
	        		k++;
	        		yMaxHR.add(activities.get(j+1).getMaxHR());
	        		xDates.add(Integer.parseInt(activities.get(j+1).getDate()));
	        	} 	
        	}
        	j++;
        }
       */
        CategorySeries series = new CategorySeries("Max Heart Rate");
        for(int i=0; i < y.length; i++){
            series.add("Max Heart Rate"+(i+1),y[i]);
        }
        /*
        CategorySeries series = new CategorySeries("Max Heart Rate");
        for(int i=0; i < yMaxHR.size(); i++){
            series.add("Max Heart Rate"+(i+1),yMaxHR.get(i));
        }
        */
        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();  // collection of series under one object.,there could any
        dataSet.addSeries(series.toXYSeries());
        
        //customization of the chart
    
        XYSeriesRenderer renderer = new XYSeriesRenderer();     // one renderer for one series
        renderer.setColor(Color.RED);
        renderer.setDisplayChartValues(true);
        renderer.setChartValuesSpacing((float) 5.5d);
        renderer.setLineWidth((float) 10.5d);

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();   // collection multiple values for one renderer or series
        mRenderer.addSeriesRenderer(renderer);
      
        mRenderer.setChartTitle("Max Heart Rate");
        mRenderer.setXTitle("Date");
        mRenderer.setYTitle("Heart Rate");
        mRenderer.setZoomButtonsVisible(false);    mRenderer.setShowLegend(true);
        mRenderer.setShowGridX(true);      // this will show the grid in  graph
        mRenderer.setShowGridY(true);              
//        mRenderer.setAntialiasing(true);
        mRenderer.setBarSpacing(.5);   // adding spacing between the line or stacks
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.WHITE);
        mRenderer.setMarginsColor(Color.GRAY);
        mRenderer.setXAxisMin(0);
//        mRenderer.setYAxisMin(.5);
        mRenderer.setXAxisMax(5);
        mRenderer.setYAxisMax(30);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setChartTitleTextSize(50);
        mRenderer.setLabelsTextSize(30);
        mRenderer.setLegendTextSize(30);
//    
        /*
         * get date size
         * get put date
         
        int numOfDates = xDates.size();
        int num=1;
        mRenderer.setXLabels(0);
        while(num <= numOfDates){
        	mRenderer.addXTextLabel(num,Integer.toString(xDates.get(num-1)));
        }
       */
        mRenderer.setXLabels(0);
        mRenderer.addXTextLabel(1,check);
        mRenderer.addXTextLabel(2,date);
        mRenderer.addXTextLabel(3,Integer.toString(yMaxHR.size()));
        mRenderer.addXTextLabel(4,activities.get(1).getTimestamp());
       
        
        mRenderer.setLabelsTextSize(20);
        mRenderer.setPanEnabled(false, false);    // will fix the chart position
        mRenderer.setZoomRate(0.2f);
        mRenderer.setZoomEnabled(false, false);
        //
        
        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        mChartView = ChartFactory.getBarChartView(this, dataSet, mRenderer, Type.DEFAULT);
        layout.addView(mChartView, new LayoutParams
    (LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        /*
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.chart2);
        mChartView2 = ChartFactory.getBarChartView(this, dataSet, mRenderer, Type.DEFAULT);
        layout2.addView(mChartView2, new LayoutParams
    (LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    */

        
    }
    public void insertActivity(){
    
		db = SavingHeartsDataSource.getInstance(this);
		ActivityData activity = new ActivityData();
		activity.setActivityName("Skiing");
		activity.setAveHR(80);
		activity.setCalories(300);
		activity.setDate("14");
		activity.setDuration(60);
		activity.setHardZones(10);
		activity.setLightZones(20);
		activity.setMaxHR(120);
		activity.setMaxZones(20);
		activity.setMets(10.1);
		activity.setMinHR(80);
		activity.setModerateZones(20);
		activity.setMonitor(1);
		activity.setMonth("04");
		activity.setTimestamp("2014-04-14");
		activity.setYear("2014");
		
		ActivityData activity8 = new ActivityData();
		activity8.setActivityName("Skiing");
		activity8.setAveHR(80);
		activity8.setCalories(300);
		activity8.setDate("14");
		activity8.setDuration(60);
		activity8.setHardZones(10);
		activity8.setLightZones(20);
		activity8.setMaxHR(110);
		activity8.setMaxZones(20);
		activity8.setMets(10.1);
		activity8.setMinHR(80);
		activity8.setModerateZones(20);
		activity8.setMonitor(1);
		activity8.setMonth("04");
		activity8.setTimestamp("2014-04-14");
		activity8.setYear("2014");
		
		ActivityData activity9 = new ActivityData();
		activity9.setActivityName("Skiing");
		activity9.setAveHR(80);
		activity9.setCalories(300);
		activity9.setDate("14");
		activity9.setDuration(60);
		activity9.setHardZones(10);
		activity9.setLightZones(20);
		activity9.setMaxHR(78);
		activity9.setMaxZones(20);
		activity9.setMets(10.1);
		activity9.setMinHR(80);
		activity9.setModerateZones(20);
		activity9.setMonitor(1);
		activity9.setMonth("04");
		activity9.setTimestamp("2014-04-14");
		activity9.setYear("2014");
		
		ActivityData activity2 = new ActivityData();
		activity2.setActivityName("Jumping");
		activity2.setAveHR(90);
		activity2.setCalories(400);
		activity2.setDate("13");
		activity2.setDuration(60);
		activity2.setHardZones(20);
		activity2.setLightZones(30);
		activity2.setMaxHR(150);
		activity2.setMaxZones(30);
		activity2.setMets(10.1);
		activity2.setMinHR(80);
		activity2.setModerateZones(20);
		activity2.setMonitor(1);
		activity2.setMonth("04");
		activity2.setTimestamp("2014-04-13");
		activity2.setYear("2014");
		
		ActivityData activity3 = new ActivityData();
		activity3.setActivityName("Running");
		activity3.setAveHR(90);
		activity3.setCalories(400);
		activity3.setDate("12");
		activity3.setDuration(60);
		activity3.setHardZones(20);
		activity3.setLightZones(30);
		activity3.setMaxHR(80);
		activity3.setMaxZones(30);
		activity3.setMets(10.1);
		activity3.setMinHR(80);
		activity3.setModerateZones(20);
		activity3.setMonitor(1);
		activity3.setMonth("04");
		activity3.setTimestamp("2014-04-12");
		activity3.setYear("2014");
		
		ActivityData activity4 = new ActivityData();
		activity4.setActivityName("Running");
		activity4.setAveHR(90);
		activity4.setCalories(400);
		activity4.setDate("11");
		activity4.setDuration(60);
		activity4.setHardZones(20);
		activity4.setLightZones(30);
		activity4.setMaxHR(120);
		activity4.setMaxZones(30);
		activity4.setMets(10.1);
		activity4.setMinHR(80);
		activity4.setModerateZones(20);
		activity4.setMonitor(1);
		activity4.setMonth("04");
		activity4.setTimestamp("2014-04-11");
		activity4.setYear("2014");
		
		ActivityData activity5 = new ActivityData();
		activity5.setActivityName("Running");
		activity5.setAveHR(90);
		activity5.setCalories(400);
		activity5.setDate("10");
		activity5.setDuration(60);
		activity5.setHardZones(20);
		activity5.setLightZones(30);
		activity5.setMaxHR(109);
		activity5.setMaxZones(30);
		activity5.setMets(10.1);
		activity5.setMinHR(80);
		activity5.setModerateZones(20);
		activity5.setMonitor(1);
		activity5.setMonth("04");
		activity5.setTimestamp("2014-04-10");
		activity5.setYear("2014");
		
		ActivityData activity6 = new ActivityData();
		activity6.setActivityName("Running");
		activity6.setAveHR(90);
		activity6.setCalories(400);
		activity6.setDate("09");
		activity6.setDuration(60);
		activity6.setHardZones(20);
		activity6.setLightZones(30);
		activity6.setMaxHR(79);
		activity6.setMaxZones(30);
		activity6.setMets(10.1);
		activity6.setMinHR(80);
		activity6.setModerateZones(20);
		activity6.setMonitor(1);
		activity6.setMonth("04");
		activity6.setTimestamp("2014-04-09");
		activity6.setYear("2014");
		
		ActivityData activity7 = new ActivityData();
		activity7.setActivityName("Running");
		activity7.setAveHR(90);
		activity7.setCalories(400);
		activity7.setDate("08");
		activity7.setDuration(60);
		activity7.setHardZones(20);
		activity7.setLightZones(30);
		activity7.setMaxHR(125);
		activity7.setMaxZones(30);
		activity7.setMets(10.1);
		activity7.setMinHR(80);
		activity7.setModerateZones(20);
		activity7.setMonitor(1);
		activity7.setMonth("04");
		activity7.setTimestamp("2014-04-08");
		activity7.setYear("2014");
		
		ActivityData activity10 = new ActivityData();
		activity10.setActivityName("Running");
		activity10.setAveHR(90);
		activity10.setCalories(400);
		activity10.setDate("07");
		activity10.setDuration(60);
		activity10.setHardZones(20);
		activity10.setLightZones(30);
		activity10.setMaxHR(134);
		activity10.setMaxZones(30);
		activity10.setMets(10.1);
		activity10.setMinHR(80);
		activity10.setModerateZones(20);
		activity10.setMonitor(1);
		activity10.setMonth("04");
		activity10.setTimestamp("2014-04-07");
		activity10.setYear("2014");
		db.insertActivityTest(activity);
		db.insertActivityTest(activity8);
		db.insertActivityTest(activity9);
		db.insertActivityTest(activity2);
		db.insertActivityTest(activity3);
		db.insertActivityTest(activity4);
		db.insertActivityTest(activity5);
		db.insertActivityTest(activity6);
		db.insertActivityTest(activity7);
		db.insertActivityTest(activity10);
		
    }
    
}
