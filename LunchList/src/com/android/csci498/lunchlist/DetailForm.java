package com.android.csci498.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends Activity {
	
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;
	
	RestaurantHelper helper = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
        retrieveFormHandlers();
        registerEventHandlers();
	}
	
	public void retrieveFormHandlers() {
		
		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.addr);
		notes = (EditText) findViewById(R.id.notes);
		types = (RadioGroup) findViewById(R.id.types);
	}
	public void registerEventHandlers() {
		helper = new RestaurantHelper(this);
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(onSave);
	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String type = null;
			
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					type = "@string/type_take_out";
					break;
				case R.id.take_out:
					type = "@string/type_sit_down";
					break;
				case R.id.delivery:
					type = "@string/type_delivery";
					break;
			}
		}
	};
}
