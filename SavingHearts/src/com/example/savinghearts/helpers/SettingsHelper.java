package com.example.savinghearts.helpers;

import java.util.Calendar;
import java.util.Date;
import com.example.savinghearts.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsHelper {

	public final static String PREF_NAME = "womenshearthealth_preferences";
	
	/**
	 * Returns the age
	 * @param context
	 * @return 
	 */
	public static int getAge(Context context) {
		
		Date myBirthdate = getBirthdate(context);
		
		Calendar nowCalendar = Calendar.getInstance();
	    Calendar thenCalendar = Calendar.getInstance();
	    thenCalendar.setTime(myBirthdate);
	    int age = thenCalendar.get(Calendar.YEAR) - nowCalendar.get(Calendar.YEAR);
	    
	    if (nowCalendar.get(Calendar.MONTH) > thenCalendar.get(Calendar.MONTH) || 
	        (nowCalendar.get(Calendar.MONTH) == thenCalendar.get(Calendar.MONTH) && 
	        nowCalendar.get(Calendar.DATE) > thenCalendar.get(Calendar.DATE))) {
	        
	    	age--;
	    }
	    
	    if (age < 0) {
	    	return -1;
	    } else {
	    	return age;
	    }
	}
	
	/**
	 * Returns the weight
	 * @param context
	 * @return Returns the weight
	 */
	public static int getWeight(Context context) {
		
		SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0);
		
		String weightStr = context.getApplicationContext().getString(R.string.pref_weight_key);
		int weight = prefs.getInt(weightStr, 0);
		
		return weight;
	}
	
	/**
	 * Sets the weight
	 * @param context Should be 'this'.
	 * @param weight The new weight.
	 * @return Returns true if the new weight was saved.
	 */
	public static boolean setWeight(Context context, int weight) {
		
		Editor prefsEditor = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit();
		
		String weightKey = context.getApplicationContext().getString(R.string.pref_weight_key);
		prefsEditor.putInt(weightKey, weight);
		
		return prefsEditor.commit();
	}
	
	
	public static Date getBirthdate(Context context) {
		
		SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0);
	
		String birthdateKey = context.getApplicationContext().getString(R.string.pref_birthdate_key);
		long birthdate = prefs.getLong(birthdateKey, 0);
		
		return new Date(birthdate);
	}
	
	public static boolean setBirthdate(Context context, Date birthdate) {
		
		Editor prefsEditor = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit();
		
		String birthdateKey = context.getApplicationContext().getString(R.string.pref_birthdate_key); 
		prefsEditor.putLong(birthdateKey, birthdate.getTime());
		
		return prefsEditor.commit();		
	}
	
	/**
	 * Returns if this is the initial run
	 * @param context
	 * @return Returns value of InitialRun flag
	 */
	public static boolean isInitialRun(Context context) {
		
		SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0);
		
		String key = context.getApplicationContext().getString(R.string.pref_isinitialrun_key); 
		
		return prefs.getBoolean(key, true);
	}
	
	/**
	 * Sets the initialRun preference value
	 * @param context
	 * @param value 'true' or 'false'
	 * @return Returns the initialRun property
	 */
	public static boolean setInitialRun(Context context, boolean value) {
		
		Editor prefsEditor = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit();
		
		String key = context.getApplicationContext().getString(R.string.pref_isinitialrun_key); 
		prefsEditor.putBoolean(key, value);
		
		return prefsEditor.commit();
	}
	
}
