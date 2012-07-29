package com.penguen.sharebugg.application;

import android.graphics.Bitmap;

public class clsUser  extends clsEntity{
	public String FirstName;
	public String LastName;
	public String UserName;
	public String Password;
	public String ProfileImageURL;
	public Bitmap ProfileImage;
	
	public clsUser(int ID, String FirstName, String LastName) {
		this.ID = ID;
		this.FirstName = FirstName;
		this.LastName = LastName;
		this.Name = this.FirstName + " " + this.LastName;
		this.UserName = this.FirstName + " " + this.LastName.charAt(0) + ".";
	}

	public clsUser() {
		// TODO Auto-generated constructor stub
	}
}
