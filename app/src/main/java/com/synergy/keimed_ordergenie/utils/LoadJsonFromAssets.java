package com.synergy.keimed_ordergenie.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by prakash on 19/07/16.
 */
public class LoadJsonFromAssets {

	private static String sfileName;
	private static Context context ;
	private String json;
	public LoadJsonFromAssets(String sFileName,Context context) {
		this.context = context ;
		this.sfileName = sFileName ;
		loadjsonfile(sfileName,context);
	}

	public String loadjsonfile(String s, Context context){
		//String json = null;
		try {
			InputStream is = context.getAssets().open(sfileName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return "";
		}
		return json;

	}
	public String getJson()
	{
		return json ;
	}
}
