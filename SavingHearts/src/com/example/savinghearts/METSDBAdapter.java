package com.example.savinghearts;

import com.example.savinghearts.*;
import com.example.savinghearts.activities.*;
import com.example.savinghearts.fragments.*;
import com.example.savinghearts.heartrate.*;
import com.example.savinghearts.helpers.*;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class METSDBAdapter {
	
	private static final String DATABASE_NAME = "mets.db";
	private static final int DATABASE_VERSION = 2;
	
	private static final String DATABASE_TABLE_NAME = "Mets";
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_UUID = "MetActivityUUID";
	private static final String COLUMN_NAME = "Name";
	private static final String COLUMN_METSVALUE = "MetsValue";
	private static final String COLUMN_MINUTESDONE = "MinutesDone";
	private static final String COLUMN_DATESUBMITTEDASLONG = "DateSubmittedAsLong";
	private static final String[] COLUMNS = {
		COLUMN_ID, COLUMN_UUID, COLUMN_NAME, COLUMN_METSVALUE, COLUMN_MINUTESDONE, COLUMN_DATESUBMITTEDASLONG
	};
	
	private static final String DATABASE_CREATE_SCRIPT = "create table "+DATABASE_TABLE_NAME+" (\n" +
			COLUMN_ID+" integer primary key autoincrement,\n" +
			COLUMN_UUID+" Text not null,\n" +
			COLUMN_NAME+" Text not null,\n" +
			COLUMN_METSVALUE+ " Double not null,\n"+
			COLUMN_MINUTESDONE+" Integer not null,\n" +
			COLUMN_DATESUBMITTEDASLONG+" Long not null\n" +
			");";

	private Context mContext;
	private METSDBOpenHelper mDbHelper;
	private SQLiteDatabase mDatabase;
	
	
	public METSDBAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new METSDBOpenHelper(this.mContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static class METSDBOpenHelper extends SQLiteOpenHelper {

		public METSDBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		
		public METSDBOpenHelper(Context context)
		{
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE_SCRIPT); // creates new sqlite DB
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
			onCreate(db);
		}

	}

	/**
	 * Saves MetActivity to the DB
	 * @param activity MetActivity to be saved to the DB
	 * @param date Date associated with the DB record
	 */
	public void saveMetActivity(MetActivity activity, Date date) {
		
		MetActivity original = getMetActivityByUUID(activity.getUUID());
		
		if (original != null) {
			deleteMetActivityByUUID(activity.getUUID());
		}
		
		addMetActivity(activity, date);
	}

	/**
	 * Adds MetActivity to the DB
	 * @param activity MetActivity to be written to the DB
	 * @param date Date associated with the DB record
	 */
	public void addMetActivity(MetActivity activity, Date date) {
		mDatabase = mDbHelper.getWritableDatabase();
		ContentValues dbInputValues = new ContentValues();
		
		String uuid = activity.getUUID();
		String name = activity.getName();
		double metsvalue = activity.getMetsvalue();
		int minutes = activity.getMinutes();
		
		
		long datelong = date.getTime();
		
		dbInputValues.put(COLUMN_UUID, uuid);
		dbInputValues.put(COLUMN_NAME, name);
		dbInputValues.put(COLUMN_METSVALUE, metsvalue);
		dbInputValues.put(COLUMN_MINUTESDONE, minutes);
		dbInputValues.put(COLUMN_DATESUBMITTEDASLONG, datelong);
		
		
		mDatabase.insert(DATABASE_TABLE_NAME, null, dbInputValues);
		mDbHelper.close();
	};
	
	/**
	 * Uses a UUID to retrieve and remove activity from the DB
	 * @param activityUUID String UUID associated with the record to be deleted
	 */
	public void deleteMetActivityByUUID(String activityUUID) {
		mDatabase = mDbHelper.getWritableDatabase();
		mDatabase.delete(DATABASE_TABLE_NAME, COLUMN_UUID+"='"+activityUUID+"'", null);
	}
	
	/**
	 * Returns a list of all MET activities saved in the DB
	 * @return List<MetActivity>
	 */
	public List<MetActivity> getAllMetActivities() {
		mDatabase = mDbHelper.getReadableDatabase();
		List<MetActivity> activities = new LinkedList<MetActivity>();
		String orderBy = COLUMN_DATESUBMITTEDASLONG + " DESC";
		Cursor cursor = mDatabase.query(DATABASE_TABLE_NAME, COLUMNS, null, null, null, null, orderBy);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			MetActivity activity = cursorToMetActivity(cursor);
			activities.add(activity);
			cursor.moveToNext();
		}
		
		mDbHelper.close();
		return activities;	
	}
	
	/**
	 * Retrieves all database records within given time range
	 * @param start Date beginning date of time range
	 * @param end Date ending date of time range
	 * @return Set<MetActivity>
	 */
	public Set<MetActivity> getMetActivitiesByDateRange(Date start, Date end) {
		
		String tblname = DATABASE_TABLE_NAME;
		String datecol = COLUMN_DATESUBMITTEDASLONG;
		long starttime = start.getTime();
		long endtime = end.getTime();
		
		mDatabase = mDbHelper.getReadableDatabase();
		String query = "SELECT * FROM "+tblname+"\n" +
					   "WHERE "+datecol+" >='"+starttime+"'\n" +
					   "AND "+datecol+" <='"+endtime+"'";
		Cursor cursor = mDatabase.rawQuery(query, null);
		cursor.moveToFirst();
		
		Set<MetActivity> activities = new HashSet<MetActivity>();
		while(!cursor.isAfterLast()) {
			MetActivity a = cursorToMetActivity(cursor);
			activities.add(a);
			cursor.moveToNext();
		}
		mDbHelper.close();
		return activities;
		
	}
	
	private MetActivity getMetActivityByUUID(String validUUID) {
		
		String tblname = DATABASE_TABLE_NAME;
		String col = COLUMN_UUID;

		mDatabase = mDbHelper.getReadableDatabase();
		String query = "SELECT * FROM "+tblname+"\n" +
					   "WHERE "+col+" ='"+validUUID+"'";
		Cursor cursor = mDatabase.rawQuery(query, null);
		cursor.moveToFirst();
		
		if (cursor.getCount() > 0) {
			
			MetActivity activity = cursorToMetActivity(cursor);
			mDbHelper.close();
			
			return activity;
			
		} else {
			
			mDbHelper.close();
			
			return null;
		}
	}
	
	private MetActivity cursorToMetActivity(Cursor cursor) {
		
		int colUuid = cursor.getColumnIndex(COLUMN_UUID);
		int colName = cursor.getColumnIndex(COLUMN_NAME);
		int colMetsVal = cursor.getColumnIndex(COLUMN_METSVALUE);
		int colMins = cursor.getColumnIndex(COLUMN_MINUTESDONE);
		int colDateSubmitted = cursor.getColumnIndex(COLUMN_DATESUBMITTEDASLONG);
		
		String uuid = cursor.getString(colUuid);
		String name = cursor.getString(colName);
		double metsvalue = cursor.getDouble(colMetsVal);
		int minutes = cursor.getInt(colMins);
		Date date = new Date(cursor.getLong(colDateSubmitted));
		
		MetActivity m = new MetActivity(uuid, name, metsvalue, minutes);
		m.setDateSaved(date);
		
		return m;
	}

}
