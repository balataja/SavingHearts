package com.example.savinghearts.activities;

import com.example.savinghearts.*;
import com.example.savinghearts.activities.*;
import com.example.savinghearts.fragments.*;
import com.example.savinghearts.heartrate.*;
import com.example.savinghearts.helpers.*;

import java.util.Date;
import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);		
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	/**
	 * Method called on starting AboutFragment to add links to AboutPage text
	 */
	@Override
	public void onStart() {
		super.onStart();
		
		TextView textview = (TextView)this.findViewById(R.id.txtvw_about_text);
		Linkify.addLinks(textview, Linkify.WEB_URLS);
		
	}
}
