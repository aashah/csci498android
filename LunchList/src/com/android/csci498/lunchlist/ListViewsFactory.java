package com.android.csci498.lunchlist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class ListViewsFactory implements RemoteViewsFactory {
	
	private Context ctxt;
	private Cursor restaurants;
	private RestaurantHelper helper;
	
	public ListViewsFactory(Context ctxt, Intent intent) {
		this.ctxt = ctxt;
	}
	
	@Override
	public int getCount() {
		return restaurants.getCount();
	}

	@Override
	public long getItemId(int position) {
		restaurants.moveToPosition(position);
		return restaurants.getInt(0);
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews row = new RemoteViews(ctxt.getPackageName(), R.layout.widget_row);
		
		restaurants.moveToPosition(position);
		row.setTextViewText(android.R.id.text1, restaurants.getString(1));
		
		Intent i = new Intent();
		Bundle extras = new Bundle();
		extras.putString(LunchList.ID_EXTRA, String.valueOf(restaurants.getInt(0)));
		
		i.putExtras(extras);
		
		row.setOnClickFillInIntent(android.R.id.text1, i);
		
		return row;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
		helper = new RestaurantHelper(ctxt);
		restaurants = helper
				.getReadableDatabase()
				.rawQuery("SELECT _ID, name from restaurants", null);

	}

	@Override
	public void onDataSetChanged() {
		//do nothing
	}

	@Override
	public void onDestroy() {
		helper.close();
		restaurants.close();

	}

}
