package com.penguen.sharebugg.application;

public class clsEntity {
	public int ID = 0;
	public String Name = "";
	public String DateCreated = "";
	
	public clsEntity(int ID, String Name) {
		this.ID=ID;
		this.Name=Name;
	}

	public clsEntity() {
		this.ID=0;
		this.Name="";
	}
	
	@Override 
	 public String toString() {
	  return this.Name;   
	 }	
}

