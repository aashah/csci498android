package com.android.csci498.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HelpPage extends Activity {
	private WebView browser;
	
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.help);
		
		browser = (WebView) findViewById(R.id.webkit);
		browser.loadUrl("file:///android_asset/helper.html");
		
	}
}
