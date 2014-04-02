package com.example.savinghearts.model;

public class ActivityData {
	private long id;
	private String activity_name;
	private long duration; //in seconds
	private int max_HR;
	private int min_HR;
	private int ave_HR;
	private double mets;
	private double calories;
	private long max_zones; //in seconds
	private long hard_zones; //in seconds
	private long moderate_zones; //in seconds
	private long light_zones; //in seconds
	private int monitor; //boolean 0 for false (manual), 1 for true (monitor)
	private String date; //format:  dd
	private String month; //format: MM
	private String year; //format:  yyyy
	private String timestamp; //format: yyyy-MM-dd
	
	public long getId(){
		return this.id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	public String getActivityName(){
		return this.activity_name;
	}
	
	public void setActivityName(String activity_name){
		this.activity_name = activity_name;
	}
	
	public long getMaxZones(){
		return this.max_zones;
	}
	
	public void setMaxZones(long max_zones){
		this.max_zones = max_zones;
	}
	
	public long getHardZones(){
		return this.hard_zones;
	}
	
	public void setHardZones(long hard_zones){
		this.hard_zones = hard_zones;
	}
	
	public long getModerateZones(){
		return this.moderate_zones;
	}
	
	public void setModerateZones(long moderate_zones){
		this.moderate_zones = moderate_zones;
	}
	
	public long getLightZones(){
		return this.light_zones;
	}
	
	public void setLightZones(long light_zones){
		this.light_zones = light_zones;
	}
	
	public int getMonitor(){
		return this.monitor;
	}
	
	public void setMonitor(int monitor){
		this.monitor = monitor;
	}
	
	public int getMaxHR(){
		return this.max_HR;
	}
	
	public void setMaxHR(int max_HR){
		this.max_HR = max_HR;
	}
	
	public int getMinHR(){
		return this.min_HR;
	}
	
	public void setMinHR(int min_HR){
		this.min_HR = min_HR;
	}
	
	public int getAveHR(){
		return this.ave_HR;
	}
	
	public void setAveHR(int ave_HR){
		this.ave_HR = ave_HR;
	}
	
	public double getMets(){
		return this.mets;
	}
	
	public void setMets(double mets){
		this.mets = mets;
	}
	
	public double getCalories(){
		return this.calories;
	}
	
	public void setCalories(double calories){
		this.calories = calories;
	}
	
	public long getDuration(){
		return this.duration;
	}
	
	public void setDuration(long duration){
		this.duration = duration;
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
	
	public String getTimestamp(){
		return this.timestamp;
	}
	
	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}

}
