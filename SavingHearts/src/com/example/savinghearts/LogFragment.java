package com.example.savinghearts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.androidplot.Plot;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

public class LogFragment extends Fragment{
	
	public static final String ARG_SECTION_NUMBER = "section_number";
	//private View view;
	private static final int NUM_PLOTS = 10;
    private static final int NUM_POINTS_PER_SERIES = 10;
    private static final int NUM_SERIES_PER_PLOT = 5;
    private ListView lv;
	
	public LogFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.listview_example, container, false);
        lv = (ListView) view.findViewById(R.id.listView1);
        lv.setAdapter(new MyViewAdapter(getActivity().getApplicationContext(), R.layout.listview_example_item, null));
        return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
    class MyViewAdapter extends ArrayAdapter<View> {
        public MyViewAdapter(Context context, int resId, List<View> views) {
            super(context, resId, views);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View v = convertView;
            if (v == null) {
                v = inf.inflate(R.layout.listview_example_item, parent, false);
            }

            Plot p = (XYPlot) v.findViewById(R.id.xyplot);
            Random generator = new Random();

            for (int k = 0; k < NUM_SERIES_PER_PLOT; k++) {
                ArrayList<Number> nums = new ArrayList<Number>();
                for (int j = 0; j < NUM_POINTS_PER_SERIES; j++) {
                    nums.add(generator.nextFloat());
                }

                double rl = Math.random();
                double gl = Math.random();
                double bl = Math.random();

                double rp = Math.random();
                double gp = Math.random();
                double bp = Math.random();

                XYSeries series = new SimpleXYSeries(nums, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "S" + k);
                p.addSeries(series, new LineAndPointFormatter(
                        Color.rgb(new Double(rl * 255).intValue(), new Double(gl * 255).intValue(), new Double(bl * 255).intValue()),
                        Color.rgb(new Double(rp * 255).intValue(), new Double(gp * 255).intValue(), new Double(bp * 255).intValue()),
                        null, null));
            }
            return v;
        }
    }

	@Override
	public void onStart() {
		super.onStart();
	}
}
