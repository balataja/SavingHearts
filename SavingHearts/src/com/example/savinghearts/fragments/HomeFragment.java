package com.example.savinghearts.fragments;
import com.example.savinghearts.*;
import com.example.savinghearts.activities.*;
import com.example.savinghearts.fragments.*;
import com.example.savinghearts.heartrate.*;
import com.example.savinghearts.helpers.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import com.example.savinghearts.R;
import com.example.savinghearts.helpers.CalculationsHelper;
import com.example.savinghearts.helpers.SettingsHelper;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener{

	public static final String ARG_SECTION_NUMBER = "section_number";
	
	public HomeFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		
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

		int myAge = com.example.savinghearts.helpers.SettingsHelper.getAge(this.getActivity());
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
		targetMaxMets.setText("Max MET Level :  " + CalculationsHelper.getMaxMETLevel(myAge));
		
		
		// random facts
		Random rand = new Random();
		Resources res = getResources();
		String[] randomFacts = res.getStringArray(R.array.random_facts_array);
		
		TextView randomFactsTextView = (TextView) this.getActivity().findViewById(R.id.randomFact);
		randomFactsTextView.setText(randomFacts[rand.nextInt(randomFacts.length)]);

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
		Bundle bundle = this.getArguments();
		String birthDate = bundle.getString("birthDate");
				
		Calendar timestamp = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());	
		String currentDate = dateFormat.format(timestamp.getTime());
		
		String[] birthDateAr = birthDate.split("/");
		String monthOfBirth = birthDateAr[0];
		String dayOfBirth = birthDateAr[1];
		String yearBirth = birthDateAr[2];
	
		return 0;
	}
}
