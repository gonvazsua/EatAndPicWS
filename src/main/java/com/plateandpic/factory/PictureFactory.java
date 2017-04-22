package com.plateandpic.factory;

public class PictureFactory {
	
	private static PictureFactory _instance = null;
	
	private PictureFactory(){}
	
	public static PictureFactory Instance(){
		
		if(_instance == null)
			_instance = new PictureFactory();
		
		return _instance;
	}

}
