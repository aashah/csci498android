package com.android.csci498.lunchlist;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


public class AppWidget extends AppWidgetProvider {
	public void onUpdate(Context ctxt, AppWidgetManager mgr, int[] appWidgetIds) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			onHCUpdate(ctxt, mgr, appWidgetIds);
		} else {
			ctxt.startService(new Intent(ctxt, WidgetService.class));
		}
	}
}
