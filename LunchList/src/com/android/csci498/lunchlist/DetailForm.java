package com.android.csci498.lunchlist;

import android.app.Activity;
import android.database.Cursor;
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
	
	String restaurantId = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
        retrieveFormHandlers();
        registerEventHandlers();
        processIntentData();
	}
	
    @Override
    public void onSaveInstanceState(Bundle state) {
    	super.onSaveInstanceState(state);
    	
    	state.putString("name", name.getText().toString());
    	state.putString("address", address.getText().toString());
    	state.putString("notes", notes.getText().toString());
    	state.putInt("type", types.getCheckedRadioButtonId());
    	
    }
    
    @Override
    public void onRestoreInstanceState(Bundle state) {
    	name.setText(state.getString("name"));
    	address.setText(state.getString("address"));
    	notes.setText(state.getString("notes"));
    	types.check(state.getInt("type"));
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
	
	public void processIntentData() {
		restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
		if (restaurantId != null) {
			loadFromId();
		}
	}
	
	public void loadFromId() {
		Cursor c = helper.getById(restaurantId);
		
		c.moveToFirst();
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		
		if (helper.getType(c).equals("@string/type_sit_down")) {
			types.check(R.id.sit_down);
		} else if (helper.getType(c).equals("@string/type_take_out")) {
			types.check(R.id.take_out);
		} else {
			types.check(R.id.delivery);
		}
		
		c.close();
		
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
			
			if (restaurantId != null) {
				helper.update(restaurantId, 
						name.getText().toString(), 
						address.getText().toString(), 
						type, 
						notes.getText().toString());
			} else {
				helper.insert(name.getText().toString(), 
						address.getText().toString(), 
						type, 
						notes.getText().toString());
			}
			finish();
		}
	};
}
