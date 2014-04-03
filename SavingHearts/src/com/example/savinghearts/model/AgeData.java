package com.example.savinghearts.model;

public class AgeData {

	private long id;
	private int age;
	
	public AgeData(){
		this.age = 20;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return this.id;
	}
	
	public void setAge(int age){
		this.age = age;
	}
	
	public int getAge(){
		return this.age;
	}

}
