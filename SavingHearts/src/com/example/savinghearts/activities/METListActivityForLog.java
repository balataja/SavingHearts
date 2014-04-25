package com.example.savinghearts.activities;
import com.example.savinghearts.*;
import com.example.savinghearts.activities.*;
import com.example.savinghearts.fragments.*;
import com.example.savinghearts.heartrate.*;
import com.example.savinghearts.helpers.*;

import com.example.savinghearts.heartrate.WorkoutScreen;
import com.example.savinghearts.helpers.METSCSVHelper;
import com.example.savinghearts.helpers.SQLDatabaseHelper;
import com.example.savinghearts.model.ActivityData;
import com.example.savinghearts.sql.SavingHeartsDataSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class METListActivityForLog extends Activity implements OnItemClickListener{

	SQLDatabaseHelper mSQLDbHelper;
	SavingHeartsDataSource db = SavingHeartsDataSource.getInstance(this);

	private ArrayAdapter<GeneralMetActivity> loadedMetsListAdapter;
	public static String name = null;
	public static double mets = 0.0;
	public static boolean yes = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSQLDbHelper = new SQLDatabaseHelper(this);
		setContentView(R.layout.activity_metlist);		
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Code to open up workout screen goes here
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//metActivity gives the names of the exercise selected
		final GeneralMetActivity metActivity = loadedMetsListAdapter.getItem(position);
		name = metActivity.getName();
		mets = metActivity.getMetsvalue();
		
		
		// alert dialog
				final AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("For How Long Did You Do It?");
				alert.setMessage("For how many minutes did you do: "+metActivity.getName());

				// minutes input
				final EditText input = new EditText(this);
				input.setHint("How many minutes?");
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				alert.setView(input);

				// save button
				alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();
						if (value.isEmpty()) {
							value = "0";
						}
						ActivityData activity = new ActivityData();
						activity.setActivityName(metActivity.getName());
						activity.setAveHR(0);
						activity.setCalories(CalculationsHelper.getCaloriesFromMetMinutes(db.getWeightFromDB(db.getWeightDataCount()).getWeight(), Integer.parseInt(value)));
						activity.setDuration(Integer.parseInt(value));
						activity.setHardZones(0);
						activity.setLightZones(0);
						activity.setMaxHR(0);
						activity.setMaxZones(0);
						activity.setMets(mets);
						activity.setMinHR(0);
						activity.setModerateZones(0);
						activity.setMonitor(0);
						db.insertActivity(activity);
						
						Intent i = new Intent(METListActivityForLog.this, MainActivity.class);
						startActivity(i);
					}
				});

				// cancel button
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								// Do nothing.
							}
						});

				alert.show();
				
	            
	}

	/**
	 * Builds METs list
	 */
	@Override
	public void onStart() {
		super.onStart();

		// loaded mets list
		loadedMetsListAdapter = new ArrayAdapter<GeneralMetActivity>(this, android.R.layout.simple_list_item_1);
		ListView loadedMetsListView = (ListView) this.findViewById(R.id.lstvw_metlistfragment_loadedmets);
		loadedMetsListView.setAdapter(loadedMetsListAdapter);
		loadedMetsListView.setOnItemClickListener(this);
	}

	/**
	 * Refreshes Available METs list
	 */
	@Override
	public void onResume() {
		super.onResume();

		updateAvailableMetsList();
	}

	private void updateAvailableMetsList() {

		loadedMetsListAdapter.clear();
		for (GeneralMetActivity activity : METSCSVHelper.getAllAvailableMetActivities(this)) {
			loadedMetsListAdapter.add(activity);
		}
		loadedMetsListAdapter.notifyDataSetChanged();
	}
}