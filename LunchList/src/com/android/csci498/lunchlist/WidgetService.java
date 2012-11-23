package com.android.csci498.lunchlist;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

public class WidgetService extends IntentService {

	public WidgetService() {
		super("WidgetService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ComponentName me = new ComponentName(this, AppWidget.class);
		RemoteViews updateViews = new RemoteViews("com.android.csci498.lunchlist", R.layout.widget);
		RestaurantHelper helper = new RestaurantHelper(this);
		AppWidgetManager mgr = AppWidgetManager.getInstance(this);
		
		try {
			Cursor c = helper
					.getReadableDatabase()
					.rawQuery("SELECT COUNT(*) FROM restaurants", null);
			c.moveToFirst();
			int count = c.getInt(0);
			c.close();
			
			if (count > 0) {
				//calculate offset
				int offset = (int) (count * Math.random());
				String args[] = {String.valueOf(offset)};
				
				//get entry with above offset
				c = helper
						.getReadableDatabase()
						.rawQuery("SELECT _ID, name FROM restaurants LIMIT 1 OFFSET ?", args);
				c.moveToFirst();
				
				updateViews.setTextViewText(R.id.name, c.getString(1));
				
				Intent i = new Intent(this, DetailForm.class);
				i.putExtra(LunchList.ID_EXTRA, c.getString(0));
				
				PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
				
				updateViews.setOnClickPendingIntent(R.id.name, pi);
				c.close();
			} else {
				updateViews.setTextViewText(R.id.title, this.getString(R.string.empty));
				
			}
		} finally {
			
		}
	}

}
