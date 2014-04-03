package com.example.savinghearts.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{
	
	//activity columns
	public static final String TABLE_ACTIVITY = "activity";
	public static final String ACTIVITY_COLUMN_ID = "_id";
	public static final String ACTIVITY_COLUMN_ACTIVITY_NAME = "activity_name";
	public static final String ACTIVITY_COLUMN_DURATION = "duration";
	public static final String ACTIVITY_COLUMN_MAX_HEART_RATE = "max_HR";
	public static final String ACTIVITY_COLUMN_MIN_HEART_RATE = "min_HR";
	public static final String ACTIVITY_COLUMN_AVE_HEART_RATE = "ave_HR";
	public static final String ACTIVITY_COLUMN_METS = "mets";
	public static final String ACTIVITY_COLUMN_CALORIES = "calories";
	public static final String ACTIVITY_COLUMN_MAX_ZONES = "max_zones";
	public static final String ACTIVITY_COLUMN_HARD_ZONES = "hard_zones";
	public static final String ACTIVITY_COLUMN_MODERATE_ZONES = "moderate_zones";
	public static final String ACTIVITY_COLUMN_LIGHT_ZONES = "light_zones";
	public static final String ACTIVITY_COLUMN_MONITOR = "monitor";
	public static final String ACTIVITY_COLUMN_DATE = "date";
	public static final String ACTIVITY_COLUMN_MONTH = "month";
	public static final String ACTIVITY_COLUMN_YEAR = "year";
	public static final String ACTIVITY_COLUMN_TIMESTAMP = "timestamp";
	
	//birth of date columns
	public static final String TABLE_AGE = "agedata";
	public static final String AGE_COLUMN_ID = "_id";
	public static final String AGE_COLUMN_AGE = "age";
	
	//Statement to create activity table
	public static final String ACTIVITY_CREATE = "create table " 
			+ TABLE_ACTIVITY + " (" 
			+ ACTIVITY_COLUMN_ID + " integer primary key autoincrement, "		
			+ ACTIVITY_COLUMN_ACTIVITY_NAME + " text not null, "
			+ ACTIVITY_COLUMN_DURATION + " integer not null, "
			+ ACTIVITY_COLUMN_MAX_HEART_RATE + " integer not null, "
			+ ACTIVITY_COLUMN_MIN_HEART_RATE + " integer not null, "
			+ ACTIVITY_COLUMN_AVE_HEART_RATE + " integer not null, "	
			+ ACTIVITY_COLUMN_METS + " real not null,"
			+ ACTIVITY_COLUMN_CALORIES + " real not null,"
			+ ACTIVITY_COLUMN_MAX_ZONES + " integer not null,"
			+ ACTIVITY_COLUMN_HARD_ZONES + " integer not null,"
			+ ACTIVITY_COLUMN_MODERATE_ZONES + " integer not null,"
			+ ACTIVITY_COLUMN_LIGHT_ZONES + " integer not null,"
			+ ACTIVITY_COLUMN_MONITOR + " integer not null,"
			+ ACTIVITY_COLUMN_DATE + " text not null,"
			+ ACTIVITY_COLUMN_MONTH + " text not null," 
			+ ACTIVITY_COLUMN_YEAR + " text not null," 
			+ ACTIVITY_COLUMN_TIMESTAMP + " text not null);";
	
	//Statement to create birthofdate table
		public static final String AGE_CREATE = "create table " 
				+ TABLE_AGE + " (" 
				+ AGE_COLUMN_ID + " integer primary key autoincrement,"
				+ AGE_COLUMN_AGE + " integer not null" 
				+");";
	
	
	public static final String DATABASE_NAME = "SavingHearts.db";
	public static final int DATABASE_VERSION = 3;
	
	//SQLiteOpenHelper helps to create or open a database
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	//the first time database is created
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("SQLSetup", "Creating databases");
		db.execSQL(ACTIVITY_CREATE);
		db.execSQL(AGE_CREATE);
		
	}
	
	//used to drop tables, add tables
	//dropping the tables to delete the data
	//create onCreate to create new empty tables
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("SQLUpgrade", "Upgrading databases");
		String DROP_ACTIVITY= "DROP TABLE IF EXISTS " + TABLE_ACTIVITY;
		String DROP_BIRTHDATE= "DROP TABLE IF EXISTS " + TABLE_AGE;
		db.execSQL(DROP_ACTIVITY);
		db.execSQL(DROP_BIRTHDATE);
		
		onCreate(db);
		
	}
}
