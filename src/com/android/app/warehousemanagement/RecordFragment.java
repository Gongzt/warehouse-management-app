package com.android.app.warehousemanagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.android.app.warehousemanagement.db.InnerDBExec;
import com.android.app.warehousemanagement.db.InnerDBTable;

public class RecordFragment extends ListFragment 
							implements View.OnClickListener, SearchView.OnQueryTextListener{
	
	private InnerDBExec db = null;
	private SimpleAdapter mAdapter = null;
	private String sortBy = InnerDBTable.Record.COLUMN_NAME_DATE + " DESC";
	private String keyword = "";
	private InputMethodManager inputManager = null;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	private String[] fromColumns = new String[] {
    		InnerDBTable.Record.COLUMN_NAME_DATE,
    		InnerDBTable.Record.COLUMN_NAME_INOROUT,
    		InnerDBTable.Record.COLUMN_NAME_AMOUNT,
    		InnerDBTable.Record.COLUMN_NAME_UNIT,
    		InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE,
    		InnerDBTable.Record.COLUMN_NAME_STATUS,
    		InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME
    };
	private int[] toViews = new int[] {
    		R.id.recordDateTextView,
    		R.id.recordInoroutImage,
    		R.id.recordAmountTextView,
    		R.id.recordUnitTextView,
    		R.id.recordTypeImage,
    		R.id.recordStatusImage,
    		R.id.recordEntryTextView
    };
	
	public static final class DatePickerFragment extends DialogFragment
												 implements DatePickerDialog.OnDateSetListener{		
		
		private static InnerDBExec db = null;
		private static SimpleAdapter mAdapter = null;
		private static String date = "";
		private static String keyword = "";
		private static String sortBy = "";
		private static ArrayList<HashMap<String, Object>> list = null;;
		
		public static DatePickerFragment newInstance(InnerDBExec _db, SimpleAdapter _mAdapter, ArrayList<HashMap<String, Object>> _list, String _keyword, String _sortBy, String _date, int year, int month, int day){
			DatePickerFragment dialog  = new DatePickerFragment();
			 
			db = _db;
			mAdapter = _mAdapter;
			list = _list;
			keyword = _keyword;
			sortBy = _sortBy;
			date = _date;
			Bundle bundle = new Bundle();
			bundle.putInt("year", year);
			bundle.putInt("month", month);
			bundle.putInt("day", day);
			bundle.putString("date", date);
	        dialog.setArguments(bundle);
			return dialog;
		    }
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState){
			Bundle bundle = getArguments();
            int year = bundle.getInt("year");
            int month = bundle.getInt("month");
            int day = bundle.getInt("day");
            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
            datePicker.setOnCancelListener(this);
            return datePicker;
		}
		
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day){
			String strYear;
			String strMonth;
			String strDay;
			strYear = "" + year;
			month += 1;
			if (month < 10) 
				strMonth = "0" + month;
			else
				strMonth = "" + month;
			if (day < 10) 
				strDay = "0" + day;
			else
				strDay = "" + day;
			
			Cursor c = null;
			if (getTag().equals(R.id.recordDateStartButton+"")){
				Button dateStartButton = (Button)getActivity().findViewById(R.id.recordDateStartButton);
				dateStartButton.setText(strYear+"-"+strMonth+"-"+strDay);
				c = db.recordSearch(keyword, sortBy, strYear+strMonth+strDay+"0000", date);
			}
			else{
				Button dateEndButton = (Button)getActivity().findViewById(R.id.recordDateEndButton);
				dateEndButton.setText(strYear+"-"+strMonth+"-"+strDay);
				c = db.recordSearch(keyword, sortBy, date, strYear+strMonth+strDay+"0000");
			}
			
			updateCursor(list, c);
			
			mAdapter.notifyDataSetChanged();
		}

	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayShowCustomEnabled(false);
    	
    	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        db = new InnerDBExec(getActivity());
        updateCursor(list, db.recordSearch("", sortBy, "", ""));
        mAdapter= new SimpleAdapter(getActivity(),
        		list,
        		R.layout.record_list_item,
        		fromColumns,
        		toViews);
        
        setListAdapter(mAdapter);
        
        return inflater.inflate(R.layout.record_fragment, container, false);
    }
    
    public final static void updateCursor(ArrayList<HashMap<String, Object>> list, Cursor c){
  		list.clear();
  		
  		c.moveToFirst();
  		for(int i=0;i<c.getCount();i++)     
  		{     
  			HashMap<String, Object> map = new HashMap<String, Object>();
  			map.put(InnerDBTable.Record.COLUMN_NAME_RECORD_ID, c.getString(0));
  			map.put(InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME, c.getString(1));  
  			if (c.getString(2).equals("产品")){
  				map.put(InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE, R.drawable.icon_product); 
  			}
  			else {
  				map.put(InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE, R.drawable.icon_material); 
  			}
  			map.put(InnerDBTable.Record.COLUMN_NAME_AMOUNT, c.getString(4));
  			map.put(InnerDBTable.Record.COLUMN_NAME_UNIT, c.getString(5));
  			if (c.getString(6).equals("入库")){
  				map.put(InnerDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_instock);
  			}
  			else{
  				map.put(InnerDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_outstock);
  			}
  			if (c.getString(7).equals("通过")){
  				map.put(InnerDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pass);
  			}
  			else{
  				map.put(InnerDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pending);
  			}
  			map.put(InnerDBTable.Record.COLUMN_NAME_DATE, c.getString(9));
  			
  			list.add(map);    
  			c.moveToNext();
  		}
  		
  	}
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
    	
    	super.onViewCreated(view, savedInstanceState);
    	
    	SearchView searchView = (SearchView) getView().findViewById(R.id.recordSearchBar);
        searchView.setOnQueryTextListener(this);
        searchView.setSelected(true);
        
        Button recordDateButton = (Button)getView().findViewById(R.id.recordDateButton);
        recordDateButton.setOnClickListener(this);
        Button recordTypeButton = (Button)getView().findViewById(R.id.recordTypeButton);
        recordTypeButton.setOnClickListener(this);
        Button recordEntryButton = (Button)getView().findViewById(R.id.recordEntryButton);
        recordEntryButton.setOnClickListener(this);
        Button recordInoroutButton = (Button)getView().findViewById(R.id.recordInoroutButton);
        recordInoroutButton.setOnClickListener(this);
        Button recordStatusButton = (Button)getView().findViewById(R.id.recordStatusButton);
        recordStatusButton.setOnClickListener(this);
        
        Button recordDateStartButton = (Button)getView().findViewById(R.id.recordDateStartButton);
        recordDateStartButton.setOnClickListener(this);
        Button recordDateEndButton = (Button)getView().findViewById(R.id.recordDateEndButton);
        recordDateEndButton.setOnClickListener(this);
        
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	
    	Log.e("fuck","update");
    	updateCursor(list, db.recordSearch(keyword, sortBy, getButtonDate(R.id.recordDateStartButton), getButtonDate(R.id.recordDateEndButton)));
    	mAdapter.notifyDataSetChanged();
    	Log.e("fuck","update complete");
    }
    
    @Override
    public void onClick(View v){
    	int buttonId = v.getId();
    	
    	if (buttonId == R.id.recordDateStartButton || buttonId == R.id.recordDateEndButton){
    		Button button = (Button) v;
    		String strDate = "";
    		String tag = "";
    		
    		switch (button.getId()){
    		case R.id.recordDateStartButton:
    			strDate = getButtonDate(R.id.recordDateEndButton);
    			tag = R.id.recordDateStartButton + "";
    			break;
    		case R.id.recordDateEndButton:
    			strDate = getButtonDate(R.id.recordDateStartButton);
    			tag = R.id.recordDateEndButton + "";
    			break;
    		}
    		
			String[] date = button.getText().toString().split("-");
			int year;
			int month;
			int day;
			if (date.length != 3){
				Calendar c = Calendar.getInstance();
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
			}
			else {
				year = Integer.parseInt(date[0]);
				month = Integer.parseInt(date[1]) - 1;
				day = Integer.parseInt(date[2]);
			}
			DatePickerFragment datePicker = DatePickerFragment.newInstance(db, mAdapter, list, keyword, sortBy, strDate, year, month, day);
			datePicker.show(getActivity().getSupportFragmentManager(), tag);
    	}
    	else {
	    	String[] sortDetail = sortBy.split(" ");
	    	String sortColumn = "";
	    	Button recordDateButton = (Button)getView().findViewById(R.id.recordDateButton);
	    	recordDateButton.setBackgroundResource(R.drawable.column_button_blue);
	    	recordDateButton.setGravity(Gravity.CENTER);
	    	recordDateButton.setTextColor(Color.rgb(0, 153, 204));  
	        Button recordTypeButton = (Button)getView().findViewById(R.id.recordTypeButton);
	        recordTypeButton.setBackgroundResource(R.drawable.column_button_blue);
	        recordTypeButton.setGravity(Gravity.CENTER);
	        recordTypeButton.setTextColor(Color.rgb(0, 153, 204));  
	        Button recordEntryButton = (Button)getView().findViewById(R.id.recordEntryButton);
	        recordEntryButton.setBackgroundResource(R.drawable.column_button_blue);
	        recordEntryButton.setGravity(Gravity.CENTER);
	        recordEntryButton.setTextColor(Color.rgb(0, 153, 204));  
	        Button recordInoroutButton = (Button)getView().findViewById(R.id.recordInoroutButton);
	        recordInoroutButton.setBackgroundResource(R.drawable.column_button_blue);
	        recordInoroutButton.setGravity(Gravity.CENTER);
	        recordInoroutButton.setTextColor(Color.rgb(0, 153, 204));  
	        Button recordStatusButton = (Button)getView().findViewById(R.id.recordStatusButton);
	        recordStatusButton.setBackgroundResource(R.drawable.column_button_blue);
	        recordStatusButton.setGravity(Gravity.CENTER);
	        recordStatusButton.setTextColor(Color.rgb(0, 153, 204));  
	        Button currentButton = recordDateButton;
	    	
	    	switch (buttonId){
	    	case R.id.recordDateButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_DATE;
	    		currentButton = recordDateButton;
	    		break;
	    	case R.id.recordTypeButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE;
	    		currentButton = recordTypeButton;
	    		break;
	    	case R.id.recordEntryButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME;
	    		currentButton = recordEntryButton;
	    		break;
	    	case R.id.recordInoroutButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_INOROUT;
	    		currentButton = recordInoroutButton;
	    		break;
	    	case R.id.recordStatusButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_STATUS;
	    		currentButton = recordStatusButton;
	    		break;	
	    	}
	    			
	    	if (sortDetail[0].equals(sortColumn) ){
	    		if (sortDetail[1].equals("DESC")){
	    			sortBy = sortColumn + " , " + InnerDBTable.Record.COLUMN_NAME_DATE + " DESC";
	    			currentButton.setBackgroundResource(R.drawable.column_button_orange);
	    			currentButton.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
	    			currentButton.setTextColor(Color.rgb(255, 136, 0));
	    			currentButton.setPadding(3, 0, 0, 0);
	    		}
	    	else 
	    			sortBy = InnerDBTable.Record.COLUMN_NAME_DATE + " DESC";
	    	}
	    	else {
	    		sortBy = sortColumn + " DESC" + " , " + InnerDBTable.Record.COLUMN_NAME_DATE + " DESC";
	    		currentButton.setBackgroundResource(R.drawable.column_button_green);
	    		currentButton.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
	    		currentButton.setPadding(3, 0, 0, 0);
	    		currentButton.setTextColor(Color.rgb(102, 153, 0));
	    	}
	    	
	    	updateCursor(list, db.recordSearch(keyword, sortBy, getButtonDate(R.id.recordDateStartButton), getButtonDate(R.id.recordDateEndButton)));
	    	mAdapter.notifyDataSetChanged();
    	}
    }
    
    private String getButtonDate(int buttonId){
    	Button button = (Button)getView().findViewById(buttonId);
		String[] date = button.getText().toString().split("-");
		
		if (date.length != 3)
			return "";
		else 
			return date[0]+date[1]+date[2];
    }
    
	@Override
	public boolean onQueryTextChange(String str) {
    	
    	keyword = str;
    	updateCursor(list, db.recordSearch(keyword, sortBy, getButtonDate(R.id.recordDateStartButton), getButtonDate(R.id.recordDateEndButton)));
    	mAdapter.notifyDataSetChanged();
		
		return false;
	}
	
	@Override
	public boolean onQueryTextSubmit(String str) {
    	
    	keyword = str;
    	updateCursor(list, db.recordSearch(keyword, sortBy, getButtonDate(R.id.recordDateStartButton), getButtonDate(R.id.recordDateEndButton)));
    	mAdapter.notifyDataSetChanged();
		inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS); 
		
		return false;
	}
}
