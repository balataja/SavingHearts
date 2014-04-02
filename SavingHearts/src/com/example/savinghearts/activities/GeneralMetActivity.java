package com.example.savinghearts.activities;
import com.example.savinghearts.*;
import com.example.savinghearts.activities.*;
import com.example.savinghearts.fragments.*;
import com.example.savinghearts.heartrate.*;
import com.example.savinghearts.helpers.*;

public class GeneralMetActivity {

	private static String mName;
	private static double mMetValue;

	/**
	 * Instatiates new GeneralMetActivity
	 * @param name String name of the activity
	 * @param metsvalue double METs value of the activity
	 */
	public GeneralMetActivity(String name, double metsvalue) {
		this.mName = name;
		this.mMetValue = metsvalue;
	}

	/**
	 * @return the name
	 */
	public static String getName() {
		return mName;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.mName = name;
	}

	/**
	 * @return the metsvalue
	 */
	public static double getMetsvalue() {
		return mMetValue;
	}

	/**
	 * @param metsvalue the metsvalue to set
	 */
	public void setMetsvalue(double metsvalue) {
		this.mMetValue = metsvalue;
	}

	@Override
	public String toString() {
		return mName + " " + mMetValue;
	}



}