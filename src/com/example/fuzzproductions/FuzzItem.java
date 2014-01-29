package com.example.fuzzproductions;

import com.google.gson.annotations.SerializedName;

public class FuzzItem {
	
	@SerializedName("id")
	public String id;
	
	@SerializedName("type")
	public String type;
	
	@SerializedName("data")
	public String data;
	
	public boolean isImage(){
		if (type.equals("image"))
			return true;
		return false;
	}
	
	public boolean isText(){
		if (type.equals("text"))
			return true;
		return false;
	}

}
