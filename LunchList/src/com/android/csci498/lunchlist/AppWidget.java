package com.android.csci498.lunchlist;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;


public class AppWidget extends AppWidgetProvider {
	public void onUpdate(Context ctxt, AppWidgetManager mgr, int[] appWidgetIds) {
		//ctxt.startService(new Intent(ctxt, WidgetService.class));
	}
}
