package com.example.savinghearts.activities;

import com.example.savinghearts.*;
import com.example.savinghearts.fragments.*;
import com.example.savinghearts.heartrate.*;
import com.example.savinghearts.helpers.*;

import com.example.savinghearts.helpers.SQLDatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class AllMetsPrintedActivity extends Activity implements OnItemClickListener {

	private ListView mMetsList;
	private ArrayAdapter<MetActivity> mListAdapter;
	private SQLDatabaseHelper mSqlDBHelper;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weeks_met_activities);
		
		mSqlDBHelper = new SQLDatabaseHelper(this);
		
		// list of Mets Activities
		mListAdapter = new ArrayAdapter<MetActivity>(this, android.R.layout.simple_list_item_1);
		mMetsList = (ListView)findViewById(R.id.weeksmets_listview);
		mMetsList.setAdapter(mListAdapter);
		mMetsList.setOnItemClickListener(this);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateMetActivitiesList();
	}

	/**
	 * Refreshes MET activities list
	 */
	private void updateMetActivitiesList() {
		mListAdapter.clear();
		for (MetActivity a: mSqlDBHelper.getAllMetActivities()) {
			mListAdapter.add(a);
		}
		mListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		switch (parent.getId()) {
		
		case R.id.weeksmets_listview:
			
			final MetActivity metActivity = mListAdapter.getItem(position);
			final String name = metActivity.getName();
			final int min = metActivity.getMinutes();
			
			// alert dialog
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(""+name);
			alert.setMessage("How many minutes did you "+name+"?:");
			
			// minutes input 
			final EditText input = new EditText(this);
			input.setText(""+min);
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
			alert.setView(input);
			
			// save button
			alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					int newval = Integer.valueOf(input.getText().toString());
					metActivity.setMinutes(newval);
					mSqlDBHelper.saveMetActivity(metActivity);
				}
			});

			// delete button
			alert.setNegativeButton("Delete Record", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					mSqlDBHelper.deleteMetActivity(metActivity);
					mListAdapter.remove(metActivity);
					mListAdapter.notifyDataSetChanged();
				}
			});

			alert.show();
			break;
		}
		
		
	}
	
}