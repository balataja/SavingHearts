package com.example.savinghearts;

import java.util.Calendar;
import java.util.Date;



public class MetActivity extends GeneralMetActivity {

	private String mUUID;
	private int mMinutes;
	private Date mDateSaved;
	
	/**
	 * Instantiates a new MetActivity
	 * @param activity GeneralMetActivity 
	 * @param minutes int number of minutes the activity was performed
	 */
	public MetActivity(GeneralMetActivity activity, int minutes) {
		super(activity.getName(), activity.getMetsvalue());
		
		mMinutes = 0;
		setMinutes(minutes);
		mUUID = java.util.UUID.randomUUID().toString();
	}
	
	/**
	 * Instantiates a new MetActivity
	 * @param uuid String a UUID for the MetActivity
	 * @param name String name describing the activity
	 * @param metsvalue double METs value of the activity
	 * @param minutes
	 */
	public MetActivity(String uuid, String name, double metsvalue, int minutes) {
		super(name, metsvalue);
		
		mMinutes = 0;
		setMinutes(minutes);
		this.mUUID = uuid;
	}

	/**
	 * @return int number of minutes the activity was performed
	 */
	public int getMinutes() {
		return mMinutes;
	}
	
	/**
	 * Sets the number of minutes the activity was performed
	 * @param minutes int
	 */
	public void setMinutes(int minutes) {
		if (minutes > 0) {
			this.mMinutes = minutes;
		}
	}
	
	/**
	 * @return double MET minutes the activity was performed
	 */
	public double getMetMinutes() {
		double activityMetCount = getMetsvalue();
		int minutes = getMinutes();
		return activityMetCount * minutes;
	}
	
	/**
	 * @return double MET hours the activity was performed
	 */
	public double getMetHours() {
		double metmins = getMetMinutes();
		return metmins / 60.0;
	}
	
	@Override
	public String toString() {
		String name = getName();
		String min = ""+getMinutes()%60;
		if(min.length() == 1)
			min = "0"+min;
		String time = getMinutes()/60+":"+min;
		double mets_double = Math.floor(getMetMinutes() * 100) / 100;
		String metsvalue = mets_double + " MET-mins";
		String display = "";
		
		if (mDateSaved != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(mDateSaved);
			int month = Integer.valueOf(c.get(Calendar.MONTH))+1;
			int day = Integer.valueOf(c.get(Calendar.DAY_OF_MONTH));
			String date = "("+month+"/"+day+")";
			
			display = date + " - ";
		}
		display += name+"\n\t "+time+", "+metsvalue;
		return display;
	}

	/**
	 * @return Date date that the activity was saved
	 */
	public Date getDateSaved() {
		return mDateSaved;
	}

	/**
	 * @param dateSaved Date updates date the activity was saved
	 */
	public void setDateSaved(Date dateSaved) {
		this.mDateSaved = dateSaved;
	}

	/**
	 * Sets a UUID for the activity
	 * @param uuid String a UUID for the MetActivity
	 */
	public void setUUID(String uuid) {
		try {
			mUUID = java.util.UUID.fromString(uuid).toString();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * @return String the UUID for the MetActivity
	 */
	public String getUUID() {
		return this.mUUID;
	}
}
