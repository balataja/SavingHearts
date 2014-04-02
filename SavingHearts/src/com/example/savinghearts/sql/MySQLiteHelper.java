package com.example.savinghearts.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{
	
	//activity columns
	public static final String TABLE_ACTIVITY = "activity";
	public static final String ACTIVITY_COLUMN_ID = "id";
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
	
	//Statement to create activity table
	public static final String ACTIVITY_CREATE = "CREATE TABLE " 
			+ TABLE_ACTIVITY + " (" 
			+ ACTIVITY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "		
			+ ACTIVITY_COLUMN_ACTIVITY_NAME + " TEXT NOT NULL, "
			+ ACTIVITY_COLUMN_DURATION + " INTEGER NOT NULL, "
			+ ACTIVITY_COLUMN_MAX_HEART_RATE + " INTEGER NOT NULL, "
			+ ACTIVITY_COLUMN_MIN_HEART_RATE + " INTEGER NOT NULL, "
			+ ACTIVITY_COLUMN_AVE_HEART_RATE + " INTEGER NOT NULL, "	
			+ ACTIVITY_COLUMN_METS + " REAL NOT NULL,"
			+ ACTIVITY_COLUMN_CALORIES + " REAL NOT NULL,"
			+ ACTIVITY_COLUMN_MAX_ZONES + " INTEGER NOT NULL,"
			+ ACTIVITY_COLUMN_HARD_ZONES + " INTEGER NOT NULL,"
			+ ACTIVITY_COLUMN_MODERATE_ZONES + " INTEGER NOT NULL,"
			+ ACTIVITY_COLUMN_LIGHT_ZONES + " INTEGER NOT NULL,"
			+ ACTIVITY_COLUMN_MONITOR + " INTEGER NOT NULL,"
			+ ACTIVITY_COLUMN_DATE + " TEXT NOT NULL,"
			+ ACTIVITY_COLUMN_MONTH + " TEXT NOT NULL," 
			+ ACTIVITY_COLUMN_YEAR + " TEXT NOT NULL," 
			+ ACTIVITY_COLUMN_TIMESTAMP + " TEXT NOT NULL)";
	
	
	public static final String DATABASE_NAME = "SavingHearts.db";
	public static final int DATABASE_VERSION = 1;
	
	//SQLiteOpenHelper helps to create or open a database
	public MySQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	//the first time database is created
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("SQLSetup", "Creating databases");
		db.execSQL(ACTIVITY_CREATE);
		
	}
	
	//used to drop tables, add tables
	//droping the tables to delete the data
	//create onCreate to create new empty tables
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("SQLUpgrade", "Upgrading databases");
		String DROP_ACTIVITY= "DROP TABLE IF EXISTS " + TABLE_ACTIVITY;
		db.execSQL(DROP_ACTIVITY);
		
		onCreate(db);
		
	}
}
