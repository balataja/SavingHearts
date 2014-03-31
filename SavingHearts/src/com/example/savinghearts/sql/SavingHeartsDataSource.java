package com.example.savinghearts.sql;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	
	//Database helper
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	
	/*
	//The fields in the month table
	private String[] monthColumns = { MySQLiteHelper.MONTH_COLUMN_ID, 
			MySQLiteHelper.MONTH_COLUMN_MONTH_CALENDAR};
	
	//the fields in the date table
	private String[] dateColumns = { MySQLiteHelper.DATE_COLUMN_ID, 
			MySQLiteHelper.DATE_COLUMN_MONTH_ID, 
			MySQLiteHelper.DATE_COLUMN_DATE_CALENDAR};
	*/
	//fields for the activity table
	private String[] activityColumns = {MySQLiteHelper.ACTIVITY_COLUMN_ID, 
			MySQLiteHelper.ACTIVITY_COLUMN_HEART_RATE,
			MySQLiteHelper.ACTIVITY_COLUMN_METS,
			MySQLiteHelper.ACTIVITY_COLUMN_DATE,
			MySQLiteHelper.ACTIVITY_COLUMN_MONTH,
			MySQLiteHelper.ACTIVITY_COLUMN_YEAR};
	
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
	 *
	 * methods for interacting with month table
	 *
	//insert monthData
	public void insertMonth(MonthData month){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.MONTH_COLUMN_MONTH_CALENDAR, month.getMonth());
		//insert it into the table and set the insert id
		long insertId = database.insert(MySQLiteHelper.TABLE_MONTH,  null,  values);
		month.setId(insertId);
	}
	//update monthData
	public void updateMonth(MonthData month){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.MONTH_COLUMN_MONTH_CALENDAR, month.getMonth());
		long id = month.getId();
		database.update(MySQLiteHelper.TABLE_MONTH, values,  MySQLiteHelper.MONTH_COLUMN_ID + " = " + id, null);
	}
	
	//get month data based on month's Id
	public MonthData getMonth(long Id){
		String where = MySQLiteHelper.MONTH_COLUMN_ID + " = " + Id;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_MONTH, monthColumns, where, null, null, null,null);
		MonthData foundMonth = new MonthData();
		if(cursor.getCount() == 0){
			foundMonth = null;
		} else {
			cursor.moveToFirst();
			foundMonth.setId(cursor.getLong(0));
			foundMonth.setMonth(cursor.getString(1));
		}
		return foundMonth;
	}
	
	//get month data based on month's calendar
	public MonthData getMonth(String month_calendar){
		String where = MySQLiteHelper.MONTH_COLUMN_MONTH_CALENDAR + " = '" + month_calendar + "'";
		Cursor cursor = database.query(MySQLiteHelper.TABLE_MONTH, monthColumns, where, null, null, null,null);
		MonthData foundMonth = new MonthData();
		if(cursor.getCount() == 0){
			foundMonth = null;
		} else {
			cursor.moveToFirst();
			foundMonth.setId(cursor.getLong(0));
			foundMonth.setMonth(cursor.getString(1));
		}
		return foundMonth;
	}
	
	//get all monthDatas
	public List<MonthData> getAllMonths(){
		List <MonthData> monthList = new ArrayList<MonthData>();
		//Select all query
		String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_MONTH;
		Cursor cursor = database.rawQuery(selectQuery, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				MonthData monthData = new MonthData();
				monthData.setId(cursor.getLong(0));
				monthData.setMonth(cursor.getString(1));
				monthList.add(monthData);
				
			}while(cursor.moveToNext());
		}
		cursor.close();
		return monthList;
	}
	
	/*
	 * methods for interacting with date table
	 
	//insert DateData
	public void insertDate(DateData date){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.DATE_COLUMN_MONTH_ID, date.getMonthId());
		values.put(MySQLiteHelper.DATE_COLUMN_DATE_CALENDAR, date.getDate());
		//insert it into the table and set the insert id
		long insertId = database.insert(MySQLiteHelper.TABLE_DATE,  null,  values);
		date.setId(insertId);
	}
	
	//updating DateData
	public void updateDate(DateData date){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.DATE_COLUMN_MONTH_ID, date.getMonthId());
		values.put(MySQLiteHelper.DATE_COLUMN_DATE_CALENDAR, date.getDate());
		long id = date.getId();
		database.update(MySQLiteHelper.TABLE_DATE, values,  MySQLiteHelper.DATE_COLUMN_ID + " = " + id, null);
	}
	
	//get date according to date's id
	public DateData getDate(long Id){
		String where = MySQLiteHelper.DATE_COLUMN_ID + " = " +Id;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_DATE, dateColumns, where, null, null, null,null);
		DateData foundDate = new DateData();
		if(cursor.getCount() == 0){
			foundDate = null;
		} else {
			cursor.moveToFirst();
			foundDate.setId(cursor.getLong(0));
			foundDate.setMonthId(cursor.getLong(1));
			foundDate.setDate(Integer.parseInt(cursor.getString(2)));
		}
		return foundDate;
	}
	
	//get date according to month data and date calendar
	public DateData getDate(int date, MonthData month){
		String where = MySQLiteHelper.DATE_COLUMN_DATE_CALENDAR + " = " + date +
				" AND " + MySQLiteHelper.DATE_COLUMN_MONTH_ID + " = " + month.getId();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_DATE, dateColumns, where, null, null, null,null);
		DateData foundDate = new DateData();
		if(cursor.getCount() == 0){
			foundDate = null;
		} else {
			cursor.moveToFirst();
			foundDate.setId(cursor.getLong(0));
			foundDate.setMonthId(cursor.getLong(1));
			foundDate.setDate(Integer.parseInt(cursor.getString(2)));
		}
		return foundDate;
	}
	
	//get All Dates according to month
	public List<DateData> getAllDates(MonthData month){
		List <DateData> dateList = new ArrayList<DateData>();
		//Select all query
		String where = MySQLiteHelper.DATE_COLUMN_MONTH_ID + " = " + month.getId();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_DATE, dateColumns, where, null, null, null, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				DateData dateData = new DateData();
				dateData.setId(cursor.getLong(0));
				dateData.setMonthId(cursor.getLong(1));
				dateData.setDate(Integer.parseInt(cursor.getString(2)));
				dateList.add(dateData);
			
			}while(cursor.moveToNext());
		}
		cursor.close();
		return dateList;
	}
	
	//get All DateDatas
	public List<DateData> getAllDates(){
		List <DateData> dateList = new ArrayList<DateData>();
		//Select all query
		String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_DATE;
		Cursor cursor = database.rawQuery(selectQuery, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				DateData dateData = new DateData();
				dateData.setId(cursor.getLong(0));
				dateData.setMonthId(cursor.getLong(1));
				dateData.setDate(Integer.parseInt(cursor.getString(2)));
				dateList.add(dateData);
				
			}while(cursor.moveToNext());
		}
		cursor.close();
		return dateList;
	}
	*/
	
	/*
	 * methods for interacting with activity table
	 */
	//insert activity
	public void insertActivity(ActivityData activity){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_HEART_RATE, activity.getHeartRate());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_METS, activity.getMets());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_DATE, getCurrentDate());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MONTH, getCurrentMonth());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_YEAR, getCurrentYear());
		//insert it into the table and set the insert id
		long insertId = database.insert(MySQLiteHelper.TABLE_ACTIVITY,  null,  values);
		activity.setId(insertId);
		activity.setDate(getCurrentDate());
		activity.setMonth(getCurrentMonth());
		activity.setYear(getCurrentYear());
	}
	
	//update activity
	public void updateActivity(ActivityData activity){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_HEART_RATE, activity.getHeartRate());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_METS, activity.getMets());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_DATE, activity.getDate());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_MONTH, activity.getMonth());
		values.put(MySQLiteHelper.ACTIVITY_COLUMN_YEAR, activity.getYear());
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
			foundActivity.setHeartRate(Integer.parseInt(cursor.getString(1)));
			foundActivity.setMets(cursor.getDouble(2));
			foundActivity.setDate(cursor.getString(3));
			foundActivity.setMonth(cursor.getString(4));
			foundActivity.setYear(cursor.getString(5));
			
		}
		return foundActivity;
	}
	
	//get activity's current date
	public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}
	
	//get activity's current month
	public String getCurrentMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM", Locale.getDefault());
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
	
	//get max heart rate in one date (dd)
	public int getMaxHeartRateInOneDate(String date){		
		List <Integer> heartRateList = getAllHeartRatesInOneDate(date);
		//compare each heart rate
		Integer max = 0;
		if(heartRateList.size() != 0){
			max = heartRateList.get(0);
			for(int i =1; i < heartRateList.size() ; i++){
				if(max < heartRateList.get(i)){
					max = heartRateList.get(i);
				}
			}
		}
		
		return max;
	}
	//get max heart rate in one month (MM-dd)
	public int getMaxHeartRateInOneMonth(String month){		
		List <Integer> heartRateList = getAllHeartRatesInOneMonth(month);
		//compare each heart rate
		Integer max = 0;
		if(heartRateList.size() != 0){
			max = heartRateList.get(0);
			for(int i =1; i < heartRateList.size() ; i++){
				if(max < heartRateList.get(i)){
					max = heartRateList.get(i);
				}
			}
		}
		
		return max;
	}
	//get max heart rate in one year (yyyy-MM-dd)
	public int getMaxHeartRateInOneYear(String year){		
		List <Integer> heartRateList = getAllHeartRatesInOneYear(year);
		//compare each heart rate
		Integer max = 0;
		if(heartRateList.size() != 0){
			max = heartRateList.get(0);
			for(int i =1; i < heartRateList.size() ; i++){
				if(max < heartRateList.get(i)){
					max = heartRateList.get(i);
				}
			}
		}
		
		
		return max;
	}
	
	//get max mets in one date (yyyy-MM-dd)
	public Double getMaxMetsInOneDate(String date){		
		List <Double> metsList = getAllMetsInOneDate(date);
		//compare each heart rate
		Double max = 0.0;
		if(metsList.size() != 0){
			max = metsList.get(0);
			for(int i =1; i < metsList.size() ; i++){
				if(max < metsList.get(i)){
					max = metsList.get(i);
				}
			}
		}
		
		return max;
	}
	//get max mets in one month (yyyy-MM)
	public Double getMaxMetsInOneMonth(String month){		
		List <Double> metsList = getAllMetsInOneMonth(month);
		//compare each heart rate
		Double max = 0.0;
		if(metsList.size() != 0){
			max = metsList.get(0);
			for(int i =1; i < metsList.size() ; i++){
				if(max < metsList.get(i)){
					max = metsList.get(i);
				}
			}
		}
		
		return max;
	}
	//get max mets in one year (yyyy)
	public Double getMaxMetsInOneYear(String year){		
		List <Double> metsList = getAllMetsInOneDate(year);
		//compare each heart rate
		Double max = 0.0;
		if(metsList.size() != 0){
			max = metsList.get(0);
			for(int i =1; i < metsList.size() ; i++){
				if(max < metsList.get(i)){
					max = metsList.get(i);
				}
			}
		}
		
		return max;
	}
	
	//get all heart rates in one date (yyyy-MM-dd)
	public List<Integer> getAllHeartRatesInOneDate(String date){
		
		List <ActivityData> activityList = getAllActivitiesInOneDate(date);
		List <Integer> heartRateList = new ArrayList<Integer>();
		while(activityList.size() != 0){
			heartRateList.add(activityList.remove(0).getHeartRate());
		}
		
		return heartRateList;
	}
	//get all heart rates in one month (yyyy-MM)
	public List<Integer> getAllHeartRatesInOneMonth(String month){
		
		List <ActivityData> activityList = getAllActivitiesInOneMonth(month);
		List <Integer> heartRateList = new ArrayList<Integer>();
		while(activityList.size() != 0){
			heartRateList.add(activityList.remove(0).getHeartRate());
		}
		
		return heartRateList;
	}
	//get all heart rates in one year (yyyy)
	public List<Integer> getAllHeartRatesInOneYear(String year){
		
		List <ActivityData> activityList = getAllActivitiesInOneMonth(year);
		List <Integer> heartRateList = new ArrayList<Integer>();
		while(activityList.size() != 0){
			heartRateList.add(activityList.remove(0).getHeartRate());
		}
		
		return heartRateList;
	}
	
	//get all mets in one date (yyyy-MM-dddd)
		public List<Double> getAllMetsInOneDate(String date){
			
			List <ActivityData> activityList = getAllActivitiesInOneDate(date);
			List <Double> heartRateList = new ArrayList<Double>();
			while(activityList.size() != 0){
				heartRateList.add(activityList.remove(0).getMets());
			}
			
			return heartRateList;
		}
		//get all mets in one month (yyyy-MM)
		public List<Double> getAllMetsInOneMonth(String month){
			
			List <ActivityData> activityList = getAllActivitiesInOneMonth(month);
			List <Double> heartRateList = new ArrayList<Double>();
			while(activityList.size() != 0){
				heartRateList.add(activityList.remove(0).getMets());
			}
			
			return heartRateList;
		}
		//get all mets in one year (yyyy)
		public List<Double> getAllMetsInOneYear(String year){
			
			List <ActivityData> activityList = getAllActivitiesInOneMonth(year);
			List <Double> heartRateList = new ArrayList<Double>();
			while(activityList.size() != 0){
				heartRateList.add(activityList.remove(0).getMets());
			}
			
			return heartRateList;
		}
	
	//get all activities according to activity's date
	//must be in the yyyy-MM-dd format
	public List<ActivityData> getAllActivitiesInOneDate(String date){
		List <ActivityData> activityList = new ArrayList<ActivityData>();
		//where clause
		String where = MySQLiteHelper.ACTIVITY_COLUMN_DATE + " = " + "'" + date + "'";
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ACTIVITY, activityColumns, where, null, null, null, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				ActivityData activityData = new ActivityData();
				activityData.setId(cursor.getLong(0));
				activityData.setHeartRate(Integer.parseInt(cursor.getString(1)));
				activityData.setMets(cursor.getDouble(2));
				activityData.setDate(cursor.getString(3));
				activityData.setMonth(cursor.getString(4));
				activityData.setYear(cursor.getString(5));
				activityList.add(activityData);
				
			}while(cursor.moveToNext());
		}
		cursor.close();
		return activityList;
	}
	
	//get all activities according to activity's month
	//must be in yyyy-MM format
	public List<ActivityData> getAllActivitiesInOneMonth(String month){
		List <ActivityData> activityList = new ArrayList<ActivityData>();
		//where clause
		String where = MySQLiteHelper.ACTIVITY_COLUMN_MONTH + " = " + "'" + month + "'";
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ACTIVITY, activityColumns, where, null, null, null, null);
		//looping through all rows and adding to list
		if(cursor.moveToFirst()){
			do{
				ActivityData activityData = new ActivityData();
				activityData.setId(cursor.getLong(0));
				activityData.setHeartRate(Integer.parseInt(cursor.getString(1)));
				activityData.setMets(cursor.getDouble(2));
				activityData.setDate(cursor.getString(3));
				activityData.setMonth(cursor.getString(4));
				activityData.setYear(cursor.getString(5));
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
				ActivityData activityData = new ActivityData();
				activityData.setId(cursor.getLong(0));
				activityData.setHeartRate(Integer.parseInt(cursor.getString(1)));
				activityData.setMets(cursor.getDouble(2));
				activityData.setDate(cursor.getString(3));
				activityData.setMonth(cursor.getString(4));
				activityData.setYear(cursor.getString(5));
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
				ActivityData activityData = new ActivityData();
				activityData.setId(cursor.getLong(0));
				activityData.setHeartRate(Integer.parseInt(cursor.getString(1)));
				activityData.setMets(cursor.getDouble(2));
				activityData.setDate(cursor.getString(3));
				activityData.setMonth(cursor.getString(4));
				activityData.setYear(cursor.getString(5));
				activityList.add(activityData);
				
			}while(cursor.moveToNext());
		}
		cursor.close();
		return activityList;
	}
	
	
}