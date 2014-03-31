package com.example.savinghearts.model;

public class ActivityData {
	private long id;
	//private long date_id;
	private int heart_rate;
	private double mets;
	private String date; //format:  yyyy-MM-dd
	private String month; //format: yyyy-MM
	private String year; //format:  yyyy
	
	public long getId(){
		return this.id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	/*
	public long getDateId(){
		return this.date_id;
	}
	
	public void setDateId(long date_id){
		this.date_id = date_id;
	}
	*/
	public int getHeartRate(){
		return this.heart_rate;
	}
	
	public void setHeartRate(int heart_rate){
		this.heart_rate = heart_rate;
	}
	
	public double getMets(){
		return this.mets;
	}
	
	public void setMets(double mets){
		this.mets = mets;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public String getMonth(){
		return this.month;
	}
	
	public void setMonth(String month){
		this.month = month;
	}
	
	public String getYear(){
		return this.year;
	}
	
	public void setYear(String year){
		this.year = year;
	}

}
