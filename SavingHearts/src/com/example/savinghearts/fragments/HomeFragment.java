package com.example.savinghearts.fragments;
import com.example.savinghearts.*;
import com.example.savinghearts.activities.*;
import com.example.savinghearts.fragments.*;
import com.example.savinghearts.heartrate.*;
import com.example.savinghearts.helpers.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import com.example.savinghearts.R;
import com.example.savinghearts.helpers.CalculationsHelper;
import com.example.savinghearts.helpers.SettingsHelper;
import com.example.savinghearts.model.AgeData;
import com.example.savinghearts.sql.SavingHeartsDataSource;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener{

	public static final String ARG_SECTION_NUMBER = "section_number";
	public SavingHeartsDataSource database;
	public int age;
	public AgeData ageData;
	
	//If you need to manually reset the database and build it from scratch 
	//when the activity starts set this to true.
	private boolean DEV_resetDatabase = false;
	
	public HomeFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		//database
		database = SavingHeartsDataSource.getInstance(getActivity().getApplicationContext());
		if(DEV_resetDatabase)
		{
			database.resetDatabase();
			database.close();
			database.open();
		}
		ageData = new AgeData();
		ageData.setAge(20);
		if(database.getAgeDataCount() < 1){
			database.insertAgeData(ageData);
		}
		int monitorOn=0;
		
		monitorOn = getArguments().getInt("monitor"); 
		if(monitorOn == 1){
			ImageView img = (ImageView) view.findViewById(R.id.imageView1);
			img.setBackgroundResource(R.drawable.heart_animation);

			 // Get the background, which has been compiled to an AnimationDrawable object.
			 AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

			 // Start the animation (looped playback by default).
			 frameAnimation.start();
		}
		else{
			ImageView img = (ImageView) view.findViewById(R.id.imageView1);
			img.setImageResource(R.drawable.heart_shape);
		}
		
	
		return view;
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onStart() {
		super.onStart();
	//	database = SavingHeartsDataSource.getInstance(getActivity().getApplicationContext());
		// Target HR Card
		LinearLayout targetHRCard = (LinearLayout) this.getActivity().findViewById(R.id.target_hr_card);
		targetHRCard.setOnClickListener(this);
		targetHRCard.setVisibility(8);
		
		View targetHRCardDropshadow = (View) this.getActivity().findViewById(R.id.targerHRCardDropshadow);
		targetHRCardDropshadow.setVisibility(8);

		// Random Facts Card
		LinearLayout randomFactsCard = (LinearLayout) this.getActivity().findViewById(R.id.random_facts_card);
		randomFactsCard.setOnClickListener(this);
		randomFactsCard.setVisibility(8);
		/*
		//Healthy 85% MET Level
		LinearLayout metFact = (LinearLayout) this.getActivity().findViewById(R.id.met_fact);
		metFact.setVisibility(8);
		*/
		View randomFactsCardDropshadow = (View) this.getActivity().findViewById(R.id.factsCardDropshadow);
		randomFactsCardDropshadow.setVisibility(8);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// start card animation
		startAnimationPopOut(R.id.target_hr_card);
		startAnimationPopOut(R.id.targerHRCardDropshadow);
		startAnimationPopOut(R.id.random_facts_card);
		startAnimationPopOut(R.id.factsCardDropshadow);

		updateUIValues();
	}
	
	private void startAnimationPopOut(int id) {
		View myLayout = (View) getActivity().findViewById(id);
		myLayout.setVisibility(0);
		Animation animation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_up_from_right);

		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});

		myLayout.clearAnimation();
		myLayout.startAnimation(animation);

	}
	
	/**
	 * Loads saved data and fills in the UI elements
	 */
	private void updateUIValues() {

		int myAge = getMyAge();
		Typeface typeface = Typeface.createFromAsset(getActivity()
				.getAssets(), "font/roboto.ttf");
		
		// target heart rates
		TextView targetHR = (TextView) this.getActivity().findViewById(
				R.id.targertHeartRatesTextView);
		targetHR.setTypeface(typeface);
		//title Saving Hearts
		TextView titleSavingHearts = (TextView) this.getActivity().findViewById(
				R.id.TitleSavingHearts);
		titleSavingHearts.setTypeface(typeface);
		
		int bpm60 = com.example.savinghearts.helpers.CalculationsHelper.getTargetHeartRateFromAge(myAge,
				com.example.savinghearts.helpers.CalculationsHelper.TARGET_60_PERCENT);
		int bpm70 = CalculationsHelper.getTargetHeartRateFromAge(myAge,
				CalculationsHelper.TARGET_70_PERCENT);
		int bpm80 = CalculationsHelper.getTargetHeartRateFromAge(myAge,
				CalculationsHelper.TARGET_80_PERCENT);
		int bpm90 = CalculationsHelper.getTargetHeartRateFromAge(myAge,
				CalculationsHelper.TARGET_90_PERCENT);
		int bpm100 = CalculationsHelper.getTargetHeartRateFromAge(myAge,
				CalculationsHelper.TARGET_MAX);
		
		TextView BPM1 = (TextView) this.getActivity().findViewById(R.id.BPM1);		
		BPM1.setText(bpm60+ " - " +bpm70+ " BPM: 60% - 70% MHR -Fatburn");
		
		TextView BPM2 = (TextView) this.getActivity().findViewById(R.id.BPM2);
		BPM2.setText(bpm70+ " - " +bpm80+ " BPM: 70% - 80% MHR -Aerobic");
		
		TextView BPM3 = (TextView) this.getActivity().findViewById(R.id.BPM3);		
		BPM3.setText(bpm80+ " - " +bpm90+ " BPM: 80% - 90% MHR -Anaerobic");
		
		TextView BPM4 = (TextView) this.getActivity().findViewById(R.id.BPM4);	
		BPM4.setText(bpm90+ " - " +bpm100+ " BPM: 90% - 100% MHR -Maximal");
		
		//for the first box showing max HR and max Mets
		TextView targetMaxHR = (TextView) this.getActivity().findViewById(R.id.TargetMaxHR);	
		targetMaxHR.setText("Max Heart Rate :  " + bpm100);
		
		TextView targetMaxMets = (TextView) this.getActivity().findViewById(R.id.TargetMaxMets);	
		
		NumberFormat formatter = new DecimalFormat("#0.00");     
		targetMaxMets.setText("Max MET Level :  " + formatter.format(CalculationsHelper.getMaxMETLevel(myAge)));
		
		
		// random facts
		Random rand = new Random();
		Resources res = getResources();
		String[] randomFacts = res.getStringArray(R.array.random_facts_array);
		
		TextView randomFactsTextView = (TextView) this.getActivity().findViewById(R.id.randomFact);
		randomFactsTextView.setText(randomFacts[rand.nextInt(randomFacts.length)]);
	
		//met 85% healthy fact
		TextView targetMET85 = (TextView) this.getActivity().findViewById(R.id.MET85);	
		double met85result = 0.85 * CalculationsHelper.getMaxMETLevel(myAge);
		targetMET85.setText(formatter.format(met85result)+" METs");
		
		TextView MET100 = (TextView) this.getActivity().findViewById(R.id.MET100);	
		MET100.setText(formatter.format(CalculationsHelper.getMaxMETLevel(myAge))+" METs");

	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
			
		case R.id.target_hr_card: // Display info on target heart rates
			intent = new Intent(getActivity(), TargetHRInformationActivity.class);
			getActivity().startActivity(intent);
			break;
		 
		}
	}
	
	public int getMyAge(){
		age = database.getAgeFromDB(1).getAge();
		return age;
	}
}
