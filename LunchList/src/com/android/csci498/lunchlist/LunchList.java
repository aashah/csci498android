package com.android.csci498.lunchlist;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class LunchList extends TabActivity {
	
	Cursor model;
	RestaurantAdapter adapter = null;
	
	private EditText name = null;
	private EditText address = null;
	private EditText notes = null;
	private RadioGroup types = null;
	private Restaurant current = null;
	
	private RestaurantHelper helper = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.i("LunchList", "Starting up application");
        
        retrieveFormHandlers();
        registerEventHandlers();
        setupTabLayout();
        
       
    }
    
    private void retrieveFormHandlers() {
        this.name = (EditText) findViewById(R.id.name);
        this.address = (EditText) findViewById(R.id.addr);
        this.types = (RadioGroup) findViewById(R.id.types);
        this.notes = (EditText) findViewById(R.id.notes);
    }
    
    private void registerEventHandlers() {
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        helper = new RestaurantHelper(this);        
        model = helper.getAll();
        startManagingCursor(model);
        adapter = new RestaurantAdapter(model);
        
        ListView list = (ListView) findViewById(R.id.restaurants);
        list.setAdapter(adapter);
        list.setOnItemClickListener(onListClick);
    }
    
    private void setupTabLayout() {
        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List", getResources()
                                   .getDrawable(R.drawable.list));
        getTabHost().addTab(spec);
        spec = getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources()
                                     .getDrawable(R.drawable.restaurant));
        getTabHost().addTab(spec);
        getTabHost().setCurrentTab(0);
    }
    
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();	
	}
    
	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}
	
    private View.OnClickListener onSave = new View.OnClickListener() {
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
			
			helper.insert(name.getText().toString(), 
						address.getText().toString(), 
						type, 
						notes.getText().toString());
			model.requery();
		}
	};
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			
			Intent i = new Intent(LunchList.this, DetailForm.class);
			startActivity(i);
			
			/* stub for dealing with later
			model.moveToPosition(position);
			
			name.setText(helper.getName(model));
			address.setText(helper.getAddress(model));
			notes.setText(helper.getNotes(model));
			
			if (helper.getType(model).equals("sit_down")) {
				types.check(R.id.sit_down);
			} else if (helper.getType(model).equals("take_out")) {
				types.check(R.id.take_out);
			} else {
				types.check(R.id.delivery);
			}
			
			getTabHost().setCurrentTab(1);
			*/
		}
		
	};
 
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	return(super.onCreateOptionsMenu(menu));
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return (super.onOptionsItemSelected(item));
    }
    
    static class RestaurantHolder {
    	private TextView name = null;
    	private TextView address = null;
    	private ImageView icon = null;
    	
    	RestaurantHolder(View row) {
    		name = (TextView) row.findViewById(R.id.title);
    		address = (TextView) row.findViewById(R.id.address);
    		icon = (ImageView) row.findViewById(R.id.icon);
    	}
    	
    	void populateFrom(Cursor c, RestaurantHelper helper) {
    		
    		name.setText(helper.getName(c));
    		address.setText(helper.getAddress(c));
    		
    		if (helper.getType(c).equals("@string/type_sit_down")) {
    			icon.setImageResource(R.drawable.ball_red);
    		} else if (helper.getType(c).equals("@string/type_take_out")) {
    			icon.setImageResource(R.drawable.ball_yellow);
    		} else {
    			icon.setImageResource(R.drawable.ball_green);
    		}
    	}
    }
    class RestaurantAdapter extends CursorAdapter {
    	public RestaurantAdapter(Cursor c) {
			super(LunchList.this, c);
		}

		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder) row.getTag();
			holder.populateFrom(c, helper);
			
		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);
			row.setTag(holder);
			return row;
		}
    }
}
