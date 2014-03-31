package com.example.savinghearts;

public class GeneralMetActivity {

	private String mName;
	private double mMetValue;
	
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
	public String getName() {
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
	public double getMetsvalue() {
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
