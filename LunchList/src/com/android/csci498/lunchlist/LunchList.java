package com.android.csci498.lunchlist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.TabActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class LunchList extends TabActivity {
	
	List<Restaurant> model = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null;
	
	private EditText name = null;
	private EditText address = null;
	private RadioGroup types = null;
	private EditText notes = null;
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
        
        helper = new RestaurantHelper(this);
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
        
        ListView list = (ListView) findViewById(R.id.restaurants);
        adapter = new RestaurantAdapter();
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
		}
	};
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			current = model.get(position);
			
			name.setText(current.getName());
			address.setText(current.getAddress());
			
			if (current.getType().equals("sit_down")) {
				types.check(R.id.sit_down);
			} else if (current.getType().equals("take_out")) {
				types.check(R.id.take_out);
			} else {
				types.check(R.id.delivery);
			}
			notes.setText(current.getNotes());
			getTabHost().setCurrentTab(1);
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
    	
    	void populateFrom(Restaurant r) {
    		name.setText(r.getName());
    		address.setText(r.getAddress());
    		if (r.getType().equals("sit_down")) {
    			icon.setImageResource(R.drawable.ball_red);
    		} else if (r.getType().equals("take_out")) {
    			icon.setImageResource(R.drawable.ball_yellow);
    		} else {
    			icon.setImageResource(R.drawable.ball_green);
    		}
    	}
    }
    
    class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    	RestaurantAdapter() {
    		super(LunchList.this,
    				android.R.layout.simple_list_item_1,
    				model);
    	}
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View row = convertView;
    		RestaurantHolder holder = null;

    		if (row == null) {
    			LayoutInflater inflater = getLayoutInflater();
    			row = inflater.inflate(R.layout.row, null);

    			holder = new RestaurantHolder(row);
    			row.setTag(holder);    			  
    		} else {	
    			holder = (RestaurantHolder)row.getTag();
    		}

    		holder.populateFrom(model.get(position));
    		return row;
    	}
    }
}
