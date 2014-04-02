package com.example.savinghearts.sql;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.savinghearts.sql.MySQLiteHelper;
import com.example.savinghearts.model.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SavingHeartsDataSource {
	/*
	 * 4/1/2014 11:55PM
	 * Note to James: I am finish with this class
	 */
	//Database helper
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	
	//fields for the activity table
	private String[] activityColumns = {MySQLiteHelper.ACTIVITY_COLUMN_ID, 
			MySQLiteHelper.ACTIVITY_COLUMN_ACTIVITY_NAME,
			MySQLiteHelper.ACTIVITY_COLUMN_DURATION,
			MySQLiteHelper.ACTIVITY_COLUMN_MAX_HEART_RATE,
			MySQLiteHelper.ACTIVITY_COLUMN_MIN_HEART_RATE,
			MySQLiteHelper.ACTIVITY_COLUMN_AVE_HEART_RATE,
			MySQLiteHelper.ACTIVITY_COLUMN_METS,
			MySQLiteHelper.ACTIVITY_COLUMN_CALORIES,
			MySQLiteHelper.ACTIVITY_COLUMN_MAX_ZONES,
			MySQLiteHelper.ACTIVITY_COLUMN_HARD_ZONES,
			MySQLiteHelper.ACTIVITY_COLUMN_MODERATE_ZONES,
			MySQLiteHelper.ACTIVITY_COLUMN_LIGHT_ZONES,
			MySQLiteHelper.ACTIVITY_COLUMN_MONITOR,
			MySQLiteHelper.ACTIVITY_COLUMN_DATE,
			MySQLiteHelper.ACTIVITY_COLUMN_MONTH,
			MySQLiteHelper.ACTIVITY_COLUMN_YEAR,
			MySQLiteHelper.ACTIVITY_COLUMN_TIMESTAMP};
	
	//make it a singleton
	private static SavingHeartsDataSource instance;
	private SavingHeartsDataSource(Context context)
	{
		dbHelper = new MySQLiteHelper(context, MySQLiteHelper.DATABASE_NAME, null, MySQLiteHelper.DATABASE_VERSION);
	}
		
	public static SavingHeartsDataSource getInstance(Context context)
	{
		if(instance == null)
		{
			instance = new SavingHeartsDataSource(context);
			instance.open();
		}
		return instance;
	}
	
	public void open() throws SQLException {
		Log.i("SQLSetup", "Calling getWritableDatabase");
		database = dbHelper.getWritableDatabase();
		Log.i("SQLSetup", "After getWritableDatabase");
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void resetDatabase(){
		dbHelper.onUpgrade(database, 0, 0);
	}
	
	/*
	 * methods for interacting with activity table
	 */
	//insert activity
	public void insertActivity(ActivityData activity){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_ACTIVITY_NAME, activity.getActivityName());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_DURATION, activity.getDuration());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MAX_HEART_RATE, activity.getMaxHR());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MIN_HEART_RATE, activity.getMinHR());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_AVE_HEART_RATE, activity.getAveHR());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_METS, activity.getMets());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_CALORIES, activity.getCalories());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MAX_ZONES, activity.getMaxZones());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_HARD_ZONES, activity.getHardZones());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MODERATE_ZONES, activity.getModerateZones());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_LIGHT_ZONES, activity.getLightZones());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MONITOR, activity.getMonitor());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_DATE, getCurrentDate());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MONTH, getCurrentMonth());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_YEAR, getCurrentYear());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_TIMESTAMP, getCurrentTimestamp());
		//insert it into the table and set the insert id
		long insertId = database.insert(MySQLiteHelper.TABLE_ACTIVITY,  null,  values);
		activity.setId(insertId);
		activity.setDate(getCurrentDate());
		activity.setMonth(getCurrentMonth());
		activity.setYear(getCurrentYear());
		activity.setYear(getCurrentTimestamp());
	}
	
	//update activity
	public void updateActivity(ActivityData activity){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_ACTIVITY_NAME, activity.getActivityName());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_DURATION, activity.getDuration());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MAX_HEART_RATE, activity.getMaxHR());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MIN_HEART_RATE, activity.getMinHR());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_AVE_HEART_RATE, activity.getAveHR());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_METS, activity.getMets());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_CALORIES, activity.getCalories());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MAX_ZONES, activity.getMaxZones());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_HARD_ZONES, activity.getHardZones());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MODERATE_ZONES, activity.getModerateZones());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_LIGHT_ZONES, activity.getLightZones());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MONITOR, activity.getMonitor());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_DATE, activity.getDate());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MONTH, activity.getMonth());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_YEAR, activity.getYear());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_TIMESTAMP, activity.getTimestamp());
		long id = activity.getId();
		database.update(MySQLiteHelper.TABLE_ACTIVITY, values,  MySQLiteHelper.ACTIVITY_COLUMN_ID + " = " + id, null);
	}
	
	//get activity according to activity's id
	public ActivityData getActivity(long Id){
		String where = MySQLiteHelper.ACTIVITY_COLUMN_ID + " = " + Id;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ACTIVITY, activityColumns, where, null, null, null,null);
		ActivityData foundActivity = new ActivityData();
		if(cursor.getCount() == 0){
			foundActivity = null;
		} else {
			cursor.moveToFirst();
			foundActivity.setId(cursor.getLong(0));
			foundActivity.setActivityName(cursor.getString(1));
			foundActivity.setDuration(cursor.getLong(2));
			foundActivity.setMaxHR(Integer.parseInt(cursor.getString(3)));
			foundActivity.setMinHR(Integer.parseInt(cursor.getString(4)));
			foundActivity.setAveHR(Integer.parseInt(cursor.getString(5)));
			foundActivity.setMets(cursor.getDouble(6));
			foundActivity.setCalories(cursor.getDouble(7));
			foundActivity.setMaxZones(Integer.parseInt(cursor.getString(8)));
			foundActivity.setHardZones(Integer.parseInt(cursor.getString(9)));
			foundActivity.setModerateZones(Integer.parseInt(cursor.getString(10)));
			foundActivity.setLightZones(Integer.parseInt(cursor.getString(11)));
			foundActivity.setMonitor(Integer.parseInt(cursor.getString(12)));
			foundActivity.setDate(cursor.getString(13));
			foundActivity.setMonth(cursor.getString(14));
			foundActivity.setYear(cursor.getString(15));
			foundActivity.setTimestamp(cursor.getString(16));
			
			
		}
		return foundActivity;
	}
	
	//get activity's current date
	public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}
	
	//get activity's current month
	public String getCurrentMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}
	
	//get activity's current year
	public String getCurrentYear() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}
	
	//get activity's current year
	public String getCurrentTimestamp() {
		Calendar timestamp = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());	
	    return dateFormat.format(timestamp.getTime());
	}
	//get

	//get all max heart rates in one date (dd), month (MM), year (yyyy)
	public List<Integer> getAllMaxHRInOneDate(String date, String month, String year){
		
		List <ActivityData> activityList = getAllActivitiesInOneDate(date, month, year);
		List <Integer> heartRateList = new ArrayList<Integer>();
		while(activityList.size() != 0){
			heartRateList.add(activityList.remove(0).getMaxHR());
		}
		
		return heartRateList;
	}
	//get all  max heart rates in one month (MM), year (yyyy)
	public List<Integer> getAllMaxHRInOneMonth(String month, String year){
		
		List <ActivityData> activityList = getAllActivitiesInOneMonth(month, year);
		List <Integer> heartRateList = new ArrayList<Integer>();
		while(activityList.size() != 0){
			heartRateList.add(activityList.remove(0).getMaxHR());
		}
		
		return heartRateList;
	}
	//get all max heart rates in one year (yyyy)
	public List<Integer> getAllMaxHRInOneYear(String year){
		
		List <ActivityData> activityList = getAllActivitiesInOneYear(year);
		List <Integer> heartRateList = new ArrayList<Integer>();
		while(activityList.size() != 0){
			heartRateList.add(activityList.remove(0).getMaxHR());
		}
		
		return heartRateList;
	}
	
	//get all mets in one date (dd), month (MM), year (yyyy)
		public List<Double> getAllMetsInOneDate(String date, String month, String year){
			
			List <ActivityData> activityList = getAllActivitiesInOneDate(date, month, year);
			List <Double> heartRateList = new ArrayList<Double>();
			while(activityList.size() != 0){
				heartRateList.add(activityList.remove(0).getMets());
			}
			
			return heartRateList;
		}
		//get all mets in one month (MM), year (yyyy)
		public List<Double> getAllMetsInOneMonth(String month, String year){
			
			List <ActivityData> activityList = getAllActivitiesInOneMonth(month, year);
			List <Double> heartRateList = new ArrayList<Double>();
			while(activityList.size() != 0){
				heartRateList.add(activityList.remove(0).getMets());
			}
			
			return heartRateList;
		}
		
		//get all mets in one year (yyyy)
		public List<Double> getAllMetsInOneYear(String year){
			
			List <ActivityData> activityList = getAllActivitiesInOneYear(year);
			List <Double> heartRateList = new ArrayList<Double>();
			while(activityList.size() != 0){
				heartRateList.add(activityList.remove(0).getMets());
			}
			
			return heartRateList;
		}
		
	//get all activities based on the activity type or name
	public List<ActivityData> getAllActivities(String activityName){
		List <ActivityData> activityList = new ArrayList<ActivityData>();
		//where clause
		String where = MySQLiteHelper.ACTIVITY_COLUMN_ACTIVITY_NAME + " = " + "'" + activityName + "'";
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ACTIVITY, activityColumns, where, null, null, null, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				ActivityData activityData = getActivity(Integer.parseInt(cursor.getString(0)));
				activityList.add(activityData);
							
			}while(cursor.moveToNext());
		}
		cursor.close();
		return activityList;
	}
	
	//get all activities in the past 7 days
	public List<ActivityData> getAllActivitiesInPast7Days(){
		List <ActivityData> activityList = new ArrayList<ActivityData>();
		//get current timestamp
		String current = getCurrentTimestamp();
		//get timestamp 7 days ago
		Calendar theStart = Calendar.getInstance();
		theStart.add(Calendar.DAY_OF_MONTH, -7);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String start = dateFormat.format(theStart.getTime());

		Cursor cursor = database.rawQuery("SELECT * FROM activity WHERE timestamp BETWEEN "+start+" AND "+current, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				ActivityData activityData = getActivity(Integer.parseInt(cursor.getString(0)));
				activityList.add(activityData);
				
			}while(cursor.moveToNext());
		}
		cursor.close();
		return activityList;
	}
	
	//get all activities in the past 30 days
	public List<ActivityData> getAllActivitiesInPast30Days(){
		List <ActivityData> activityList = new ArrayList<ActivityData>();
		//get current timestamp
		String current = getCurrentTimestamp();
		//get timestamp 7 days ago
		Calendar theStart = Calendar.getInstance();
		theStart.add(Calendar.DAY_OF_MONTH, -30);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String start = dateFormat.format(theStart.getTime());

		Cursor cursor = database.rawQuery("SELECT * FROM activity WHERE timestamp BETWEEN "+start+" AND "+current, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				ActivityData activityData = getActivity(Integer.parseInt(cursor.getString(0)));
				activityList.add(activityData);
					
			}while(cursor.moveToNext());
		}
		cursor.close();
		return activityList;
	}
		
	//get all activities according to activity's date
	//must be in the dd, MM, and yyyy format
	public List<ActivityData> getAllActivitiesInOneDate(String date, String month, String year){
		List <ActivityData> activityList = new ArrayList<ActivityData>();
		//where clause
		String where = MySQLiteHelper.ACTIVITY_COLUMN_DATE + " = " + "'" + date + "'" + " AND "
				+ MySQLiteHelper.ACTIVITY_COLUMN_MONTH + " = '" + month + "'" + " AND " 
				+ MySQLiteHelper.ACTIVITY_COLUMN_YEAR + " = '" + year + "'";
				
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ACTIVITY, activityColumns, where, null, null, null, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				ActivityData activityData = getActivity(Integer.parseInt(cursor.getString(0)));
				activityList.add(activityData);
				
			}while(cursor.moveToNext());
		}
		cursor.close();
		return activityList;
	}
	
	//get all activities according to activity's month
	//must be in yyyy and MM format
	public List<ActivityData> getAllActivitiesInOneMonth(String month, String year){
		List <ActivityData> activityList = new ArrayList<ActivityData>();
		//where clause
		String where = MySQLiteHelper.ACTIVITY_COLUMN_MONTH + " = " + "'" + month + "'" + " AND "
						+ MySQLiteHelper.ACTIVITY_COLUMN_YEAR + " = '" + year + "'";
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ACTIVITY, activityColumns, where, null, null, null, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				ActivityData activityData = getActivity(Integer.parseInt(cursor.getString(0)));
				activityList.add(activityData);
					
			}while(cursor.moveToNext());
		}
		cursor.close();
		return activityList;
	}
	//get all activities according to activity's year
	//must be in yyyy format
	public List<ActivityData> getAllActivitiesInOneYear(String year){
		List <ActivityData> activityList = new ArrayList<ActivityData>();
		//where clause
		String where = MySQLiteHelper.ACTIVITY_COLUMN_MONTH + " = " + "'" + year + "'";
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ACTIVITY, activityColumns, where, null, null, null, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				ActivityData activityData = getActivity(Integer.parseInt(cursor.getString(0)));
				activityList.add(activityData);
						
			}while(cursor.moveToNext());
		}
		cursor.close();
		return activityList;
	}
	


	//get all activities
	public List<ActivityData> getAllActivities(){
		List <ActivityData> activityList = new ArrayList<ActivityData>();
		//Select all query
		String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_ACTIVITY;
		Cursor cursor = database.rawQuery(selectQuery, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				ActivityData activityData = getActivity(Integer.parseInt(cursor.getString(0)));
				activityList.add(activityData);
				
			}while(cursor.moveToNext());
		}
		cursor.close();
		return activityList;
	}
	
	
}