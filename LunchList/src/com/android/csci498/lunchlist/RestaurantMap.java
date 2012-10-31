package com.android.csci498.lunchlist;

import android.os.Bundle;

public class RestaurantMap extends com.google.android.maps.MapActivity {
	
	public static final String EXTRA_LONGITUDE = "csci498.edu.EXRA_LONGITUDE";
	public static final String EXTRA_LATITUDE = "csci498.edu.EXRA_LATITUDE";
	public static final STRING EXTRA_NAME = "csci498.edu.EXTRA_NAME";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
