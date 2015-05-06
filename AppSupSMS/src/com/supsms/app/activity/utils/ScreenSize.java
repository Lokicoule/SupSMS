/*
 * Author : Lokicoule
 */
package com.supsms.app.activity.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ScreenSize {
	
	DisplayMetrics displaymetrics;
	
	public ScreenSize(Activity activity) {
		displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	}
	
	public int getScreenHeight()
	{
		return displaymetrics.heightPixels;
	}
	
	public int getScreenWidth()
	{
		return displaymetrics.widthPixels;
	}

}
