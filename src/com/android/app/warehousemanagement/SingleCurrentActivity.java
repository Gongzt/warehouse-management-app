package com.android.app.warehousemanagement;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.app.warehousemanagement.db.InnerDBExec;
import com.android.app.warehousemanagement.db.InnerDBTable;

public class SingleCurrentActivity extends ListActivity 
								   implements View.OnClickListener{

	private InnerDBExec db = null;
	private String name;
	private String type;
	private String unit;
	private String groupData;
	private ArrayList<String[]> childData = new ArrayList<String[]>();
	
	private String sortBy = "";
	private SimpleAdapter mAdapter = null;
	private ExpandableAdapter eAdapter = null;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	private String[] fromColumns = new String[] {
			InnerDBTable.Record.COLUMN_NAME_DATE,
			InnerDBTable.Record.COLUMN_NAME_WAREHOUSE,
			InnerDBTable.Record.COLUMN_NAME_INOROUT,
			InnerDBTable.Record.COLUMN_NAME_AMOUNT,
			InnerDBTable.Record.COLUMN_NAME_UNIT,
			InnerDBTable.Record.COLUMN_NAME_STATUS
			
	};
	private int[] toViews = new int[] {
			R.id.singleCurrentListingDateTextView,
			R.id.singleCurrentListingWarehouseTextView,
			R.id.singleCurrentListingInoroutImage,
			R.id.singleCurrentListingAmountTextView,
			R.id.singleCurrentListingUnitTextView,
			R.id.singleCurrentListingStatusImage
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_current_activity);
		
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		db = new InnerDBExec(this);
		
		
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		type = intent.getStringExtra("type");
		unit = intent.getStringExtra("unit");
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.single_current_actionbar, null);

		actionBar.setCustomView(v);
		
		((TextView)actionBar.getCustomView().findViewById(R.id.singleCurrentTitleTextView)).setText(name);
		((ImageButton)actionBar.getCustomView().findViewById(R.id.singleCurrentInstockButton)).setOnClickListener(this);
        ((ImageButton)actionBar.getCustomView().findViewById(R.id.singleCurrentOutstockButton)).setOnClickListener(this);
		
		eAdapter = new ExpandableAdapter();
		ExpandableListView expandableListView = (ExpandableListView)findViewById(R.id.singleCurrentAmountExpandable);
		expandableListView.setAdapter(eAdapter);
        
        mAdapter= new SimpleAdapter(this,
        		list,
          		R.layout.single_current_record_listing,
           		fromColumns,
           		toViews);
        setListAdapter(mAdapter);
        
        ((Button)findViewById(R.id.singleCurrentDateButton)).setOnClickListener(this);
        ((Button)findViewById(R.id.singleCurrentWarehouseButton)).setOnClickListener(this);
        ((Button)findViewById(R.id.singleCurrentInoroutButton)).setOnClickListener(this);
        ((Button)findViewById(R.id.singleCurrentStatusButton)).setOnClickListener(this);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		updateCursor(db.recordSelectByNameAndType(name, type, ""));
		mAdapter.notifyDataSetChanged();
		
		String[] result= db.currentSelectTotal(name, type);
		groupData = "现有数量：  " + result[0] + result[1];
		
		Cursor c = db.currentSelectByNameAndType(name, type);
		childData.clear();
		for (int i=0; i<c.getCount(); i++){
			childData.add(new String[] {c.getString(2), c.getString(0)+c.getString(1)});
			c.moveToNext();
		}
		eAdapter.notifyDataSetChanged();
		
	}
	
	private void updateCursor(Cursor c){
		list.clear();
  		
  		c.moveToFirst();
  		for(int i=0;i<c.getCount();i++)     
  		{    
			
  			HashMap<String, Object> map = new HashMap<String, Object>();
  			map.put(InnerDBTable.Record.COLUMN_NAME_RECORD_ID, c.getString(0));
  			map.put(InnerDBTable.Record.COLUMN_NAME_WAREHOUSE, c.getString(1));
  			map.put(InnerDBTable.Record.COLUMN_NAME_AMOUNT, c.getString(2));
  			map.put(InnerDBTable.Record.COLUMN_NAME_UNIT, c.getString(3));
  			
  			if (c.getString(4).equals(getResources().getString(R.string.instock)))
  				map.put(InnerDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_instock);
  			else
  				map.put(InnerDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_outstock);
  			
  			if (c.getString(5).equals(getResources().getString(R.string.passed)))
  				map.put(InnerDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pass);
  			else
  				map.put(InnerDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pending);
  			
  			map.put(InnerDBTable.Record.COLUMN_NAME_DATE, c.getString(6));
  			
  			list.add(map);    
  			c.moveToNext();
  		}
	}

	private class ExpandableAdapter extends BaseExpandableListAdapter {    
        @Override  
        public Object getChild(int groupPosition, int childPosition) {  
            return childData.get(childPosition);  
        }  
  
        @Override  
        public long getChildId(int groupPosition, int childPosition) {  
            return 0;  
        }  
  
        @Override  
        public View getChildView(int groupPosition, int childPosition,  
                boolean isLastChild, View convertView, ViewGroup parent) {  
        	String[] data = childData.get(childPosition);
        	LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View v = inflator.inflate(R.layout.single_current_expandable_listing, null);
            TextView warehouseTextview = (TextView) v.findViewById(R.id.singleCurrentExpandableWarehouseTextView);
            warehouseTextview.setText(data[0]);
            TextView amountTextview = (TextView) v.findViewById(R.id.singleCurrentExpandableAmountTextView);
            amountTextview.setText(data[1]);
            return v;    
        }  
  
        @Override  
        public int getChildrenCount(int groupPosition) {  
            return childData.size();  
        }  
  
        @Override  
        public Object getGroup(int groupPosition) {  
            return groupData;  
        }  
  
        @Override  
        public int getGroupCount() {  
            return 1;  
        }  
  
        @Override  
        public long getGroupId(int groupPosition) {  
            return 0;  
        }  
  
        @Override  
        public View getGroupView(int groupPosition, boolean isExpanded,  
                View convertView, ViewGroup parent) {  
        	LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View v = inflator.inflate(R.layout.single_current_expandable, null);
            ImageView image = (ImageView) v.findViewById(R.id.singleCurrentExpandableImage);
            if (type.equals(getResources().getString(R.string.material)))
            	image.setBackgroundResource(R.drawable.icon_material);
            else
            	image.setBackgroundResource(R.drawable.icon_product);
            TextView text = (TextView) v.findViewById(R.id.singleCurrentExpandableTextView);
            text.setText(groupData);
            return v;  
        }  
  
        @Override  
        public boolean hasStableIds() {  
            return false;  
        }  
  
        @Override  
        public boolean isChildSelectable(int groupPosition, int childPosition) {  
            return false;  
        }    
    }    
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_current, menu);
		return true;
	}	

	@Override
	public void onClick(View v) {
		int buttonId = v.getId();
		
		if (buttonId == R.id.singleCurrentOutstockButton || buttonId == R.id.singleCurrentInstockButton){
			Intent intent = new Intent(this, InoroutActivity.class);
			
			intent.putExtra("name", name);
			intent.putExtra("type", type);
			intent.putExtra("unit", unit);
			
			if (buttonId == R.id.singleCurrentInstockButton)
				intent.putExtra("from", R.id.singleCurrentInstockButton);
			else
				intent.putExtra("from", R.id.singleCurrentOutstockButton);
			startActivity(intent);
		}
		else {
	    	String[] sortDetail = sortBy.split(" ");
	    	String sortColumn = "";
	    	Button singleCurrentDateButton = (Button)findViewById(R.id.singleCurrentDateButton);
	        Button singleCurrentWarehouseButton = (Button)findViewById(R.id.singleCurrentWarehouseButton);
	        Button singleCurrentInoroutButton = (Button)findViewById(R.id.singleCurrentInoroutButton);
	        Button singleCurrentStatusButton = (Button)findViewById(R.id.singleCurrentStatusButton);
	    	
	        singleCurrentDateButton.setBackgroundResource(R.drawable.column_button_blue);
	        singleCurrentDateButton.setTextColor(Color.rgb(0, 153, 204));
	        singleCurrentDateButton.setGravity(Gravity.CENTER);
	        singleCurrentWarehouseButton.setBackgroundResource(R.drawable.column_button_blue);
	        singleCurrentWarehouseButton.setTextColor(Color.rgb(0, 153, 204));
	        singleCurrentWarehouseButton.setGravity(Gravity.CENTER);
	        singleCurrentInoroutButton.setBackgroundResource(R.drawable.column_button_blue);
	        singleCurrentInoroutButton.setTextColor(Color.rgb(0, 153, 204));
	        singleCurrentInoroutButton.setGravity(Gravity.CENTER);
	        singleCurrentStatusButton.setBackgroundResource(R.drawable.column_button_blue);
	        singleCurrentStatusButton.setTextColor(Color.rgb(0, 153, 204));
	        singleCurrentStatusButton.setGravity(Gravity.CENTER);
	        
	        Button currentButton = singleCurrentDateButton;
	    	switch (buttonId){
	    	case R.id.singleCurrentDateButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_DATE;
	    		currentButton = singleCurrentDateButton;
	    		break;
	    	case R.id.singleCurrentWarehouseButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_WAREHOUSE;
	    		currentButton = singleCurrentWarehouseButton;
	    		break;
	    	case R.id.singleCurrentInoroutButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_INOROUT;
	    		currentButton = singleCurrentInoroutButton;
	    		break;
	    	case R.id.singleCurrentStatusButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_STATUS;
	    		currentButton = singleCurrentStatusButton;
	    		break;
	    	}
	    	
	    	if (sortDetail[0].equals(sortColumn) ){
				if (sortDetail.length == 2) {
					sortBy = sortColumn;
					currentButton.setBackgroundResource(R.drawable.column_button_orange);
					currentButton.setTextColor(Color.rgb(255, 136, 0));
					currentButton.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
					currentButton.setPadding(3, 0, 0, 0);
				}
				else{
					sortBy = "";
				}
			}
			else {
				sortBy = sortColumn + " DESC";
				currentButton.setBackgroundResource(R.drawable.column_button_green);
				currentButton.setTextColor(Color.rgb(102, 153, 0));
				currentButton.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
				currentButton.setPadding(3, 0, 0, 0);
			}
	    	
	    	updateCursor(db.recordSelectByNameAndType(name, type, sortBy));
	    	mAdapter.notifyDataSetChanged();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		
		HashMap<String,Object> hashMap = (HashMap<String,Object>)listView.getItemAtPosition(position);
		
		Intent intent = new Intent(SingleCurrentActivity.this, SingleRecordActivity.class);
		intent.putExtra("id", (String)hashMap.get(InnerDBTable.Record.COLUMN_NAME_RECORD_ID));		
		startActivity(intent);
	}

}
