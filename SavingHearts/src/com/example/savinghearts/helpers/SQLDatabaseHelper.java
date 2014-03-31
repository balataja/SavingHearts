package com.example.savinghearts.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.example.savinghearts.METSDBAdapter;
import com.example.savinghearts.MetActivity;

import android.annotation.SuppressLint;
import android.app.Activity;

public class SQLDatabaseHelper {

	
	private Activity mActivity;
	private METSDBAdapter mDbAdapter;
	
	/**
	 * Initializes SQLDatabaseHelper
	 * @param activity
	 */
	public SQLDatabaseHelper (Activity activity) {
		this.mActivity = activity;
        this.mDbAdapter = new METSDBAdapter(activity);

        initMETSDB();
	}
	
	/**
	 * Saves a MetActivity object to the database
	 * @param activity MetActivity to be saved to the DB
	 */
	public void saveMetActivity(MetActivity activity) {
    	mDbAdapter.saveMetActivity(activity, new Date());
    }
	
	/**
	 * Removes the MetActivity object with the same UUID from the DB
	 * @param activity MetActivity to be removed from the DB
	 */
	public void deleteMetActivity(MetActivity activity) {
		String uuid = activity.getUUID();
		mDbAdapter.deleteMetActivityByUUID(uuid);
	}
	
	/**
	 * Returns a list of all MET activities saved in the DB
	 * @return List<MetActivity>
	 */
	public List<MetActivity> getAllMetActivities() {
		return mDbAdapter.getAllMetActivities();
	}
	
	/**
	 * Returns a list of all MET activities in the DB on the specified date
	 * @param day 
	 * @return Set<MetActivity>
	 */
	public Set<MetActivity> getMetActivitiesForDay(Date day) {
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(day);
		startCal.set(Calendar.HOUR, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		Date startTime = startCal.getTime();
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(day);
		endCal.set(Calendar.HOUR, 11);
		endCal.set(Calendar.MINUTE, 59);
		endCal.set(Calendar.SECOND, 59);
		Date endTime = endCal.getTime();
		
		return mDbAdapter.getMetActivitiesByDateRange(startTime, endTime);
	}
	
	@SuppressLint("SdCardPath")
	private void initMETSDB() {
		
		try {

            String destPath = "/data/data/" + this.mActivity.getPackageName() +
                "/databases";
            File f = new File(destPath);
            System.out.println(destPath);
            if (!f.exists()) {
            	System.out.println("copying db...................................");
            	f.mkdirs();
                f.createNewFile();
            	
            	//Copy db from assets folder to the databases folder
                CopyDB(mActivity.getBaseContext().getAssets().open("METS"),
                    new FileOutputStream(destPath + "/METS"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
    private void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
    	
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
            System.out.println(buffer);
        }
        inputStream.close();
        outputStream.close();
        
    }

	

}
