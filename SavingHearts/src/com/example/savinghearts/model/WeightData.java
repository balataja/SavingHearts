package com.example.savinghearts.model;

public class WeightData {

	private long id;
	private int weight;
	
	public WeightData(){
		this.weight = 150;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return this.id;
	}
	
	public void setWeight(int weight){
		this.weight = weight;
	}
	
	public int getWeight(){
		return this.weight;
	}

}
