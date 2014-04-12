package com.example.savinghearts.fragments;

import com.example.savinghearts.*;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MonitorFragment extends Fragment implements OnClickListener{
	
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	public MonitorFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	View view =	inflater.inflate(R.layout.fragment_monitor, container, false);
	Button searchBeat = (Button) view.findViewById(R.id.searchMonitor);
	searchBeat.setOnClickListener(this);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.searchMonitor:
			{
				 int monitor = this.getArguments().getInt("monitor");
				 Bundle bundle = new Bundle();
          		 bundle.putInt("isConnected", monitor);
				 Intent i = new Intent(getActivity(), SearchMonitor.class);
				 i.putExtras(bundle);
			     startActivity(i);
			}
		}
		
	}
}
