package com.android.csci498.lunchlist;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedActivity extends ListActivity {

	public static final String FEED_URL = "apt.tutorial.FEED_URL";
	private InstanceState state;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		state = (InstanceState) getLastNonConfigurationInstance();
		
		if (state == null) {
			state = new InstanceState();
			state.task = new FeedTask(this);
			state.task.execute(getIntent().getStringExtra(FEED_URL));
		} else {
			if (state.task != null) {
				state.task.attach(this);
			}
			
			if (state.feed != null) {
				setFeed(state.feed);
			}
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		if (state.task != null) {
			state.task.detach();
		}
		return state;
	}
	
	private void setFeed(RSSFeed feed) {
		state.feed = feed;
		setListAdapter(new FeedAdapter(feed));
	}
	
	private void goBlooey(Throwable t) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Exception").setMessage(t.toString()).setPositiveButton("OK", null).show();
	}
	
	private static class InstanceState {
		public RSSFeed feed = null;
		public FeedTask task = null;
	}
	
	private static class FeedTask extends AsyncTask<String, Void, RSSFeed> {
		private RSSReader reader = new RSSReader();		
		private Exception e;
		private FeedActivity activity;
		
		public FeedTask(FeedActivity activity) {
			attach(activity);
		}

		void attach(FeedActivity activity) {
			this.activity = activity;
		}
		
		void detach() {
			this.activity = null;
		}
		
		@Override
		public RSSFeed doInBackground(String... urls) {
			RSSFeed result = null;
			try {
				result = reader.load(urls[0]);
			} catch (Exception e) {
				this.e=e;
			}
			return result;
		}
		
		@Override
		public void onPostExecute(RSSFeed feed) {
			if (e == null) {
				activity.setFeed(feed);
			} else {
				//exceptions
				activity.goBlooey(e);
			}
		}
	}

	private class FeedAdapter extends BaseAdapter {

		private RSSFeed feed;
		
		public FeedAdapter(RSSFeed feed) {
			super();
			this.feed = feed;
		}
		
		@Override
		public int getCount() {
			return feed.getItems().size();
		}

		@Override
		public Object getItem(int position) {
			return feed.getItems().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			}
			RSSItem item = (RSSItem) getItem(position);
			((TextView) row).setText(item.getTitle());
			return null;
		}

	}
}
