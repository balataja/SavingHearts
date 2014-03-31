package com.example.savinghearts.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{
	/*
	//month columns
	public static final String TABLE_MONTH = "month";
	public static final String MONTH_COLUMN_ID = "id";
	public static final String MONTH_COLUMN_MONTH_CALENDAR= "month_calendar";
	
	//Statement to create month table
	public static final String MONTH_CREATE = "CREATE TABLE " 
			+ TABLE_MONTH + "(" 
			+ MONTH_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ MONTH_COLUMN_MONTH_CALENDAR + " TEXT NOT NULL)";
	
	
	//date columns
	public static final String TABLE_DATE = "date";
	public static final String DATE_COLUMN_ID = "id";
	public static final String DATE_COLUMN_MONTH_ID = "month_id";
	public static final String DATE_COLUMN_DATE_CALENDAR = "date_calendar";
	
	//Statement to create date table
	public static final String DATE_CREATE = "CREATE TABLE " 
			+ TABLE_DATE + "(" 
			+ DATE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DATE_COLUMN_MONTH_ID + " INTEGER NOT NULL, " 
			+ DATE_COLUMN_DATE_CALENDAR + " INTEGER NOT NULL," 
			+ " FOREIGN KEY" + " ( " + DATE_COLUMN_MONTH_ID + " ) " + "REFERENCES " + TABLE_MONTH + " ( " + MONTH_COLUMN_ID + " )"
			+ ")";
					
	*/
	
	//activity columns
	public static final String TABLE_ACTIVITY = "activity";
	public static final String ACTIVITY_COLUMN_ID = "id";
	//public static final String ACTIVITY_COLUMN_DATE_ID = "date_id";
	public static final String ACTIVITY_COLUMN_HEART_RATE = "max_HR";
	public static final String ACTIVITY_COLUMN_METS = "max_METS";
	public static final String ACTIVITY_COLUMN_DATE = "date";
	public static final String ACTIVITY_COLUMN_MONTH = "month";
	public static final String ACTIVITY_COLUMN_YEAR = "year";
	
	//Statement to create activity table
	public static final String ACTIVITY_CREATE = "CREATE TABLE " 
			+ TABLE_ACTIVITY + "(" 
			+ ACTIVITY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "		
			+ ACTIVITY_COLUMN_HEART_RATE + " INTEGER NOT NULL, "
			+ ACTIVITY_COLUMN_METS + " REAL NOT NULL,"
			+ ACTIVITY_COLUMN_DATE + " TEXT NOT NULL,"
			+ ACTIVITY_COLUMN_MONTH + " TEXT NOT NULL," 
			+ ACTIVITY_COLUMN_YEAR + " TEXT NOT NULL)";
	
	
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
		//executes query as long as the query isn't a select or if the query doesn't return any data
		//db.execSQL(MONTH_CREATE);
		//db.execSQL(DATE_CREATE);
		db.execSQL(ACTIVITY_CREATE);
		
	}
	
	//used to drop tables, add tables
	//droping the tables to delete the data
	//create onCreate to create new empty tables
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("SQLUpgrade", "Upgrading databases");
		//String DROP_MONTH= "DROP TABLE IF EXISTS " + TABLE_MONTH;
		//String DROP_DATE= "DROP TABLE IF EXISTS " + TABLE_DATE;
		String DROP_ACTIVITY= "DROP TABLE IF EXISTS " + TABLE_ACTIVITY;
		
		//db.execSQL(DROP_MONTH);
		//db.execSQL(DROP_DATE);
		db.execSQL(DROP_ACTIVITY);
		
		onCreate(db);
		
	}
}
