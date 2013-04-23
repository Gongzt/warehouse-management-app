package com.android.app.warehousemanagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.android.app.warehousemanagement.db.OutterDBExec.OutterDBException;
import com.android.app.warehousemanagement.db.SqlDBInterface;
import com.android.app.warehousemanagement.db.SqlDBTable;

public class RecordFragment extends ListFragment 
							implements View.OnClickListener, SearchView.OnQueryTextListener{
	
	private SqlDBInterface db = null;
	private SimpleAdapter mAdapter = null;
	private String sortBy = "";
	private String keyword = "";
	private String startDate = "";
	private String endDate = "";
	private InputMethodManager inputManager = null;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	private String[] fromColumns = new String[] {
    		SqlDBTable.Record.COLUMN_NAME_DATE,
    		SqlDBTable.Record.COLUMN_NAME_INOROUT,
    		SqlDBTable.Record.COLUMN_NAME_AMOUNT,
    		SqlDBTable.Entry.COLUMN_NAME_UNIT,
    		SqlDBTable.Entry.COLUMN_NAME_TYPE,
    		SqlDBTable.Record.COLUMN_NAME_STATUS,
    		SqlDBTable.Entry.COLUMN_NAME_NAME
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
		
		private static UpdateListingTask updateListingTask = null;
		
		public static DatePickerFragment newInstance(UpdateListingTask _updateListingTask, int year, int month, int day){
			DatePickerFragment dialog  = new DatePickerFragment();
			
			updateListingTask = _updateListingTask;
			Bundle bundle = new Bundle();
			bundle.putInt("year", year);
			bundle.putInt("month", month);
			bundle.putInt("day", day);
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
			
			if (getTag().equals(R.id.recordDateStartButton+"")){
				Button dateStartButton = (Button)getActivity().findViewById(R.id.recordDateStartButton);
				dateStartButton.setText(strYear+"-"+strMonth+"-"+strDay);
			}
			else{
				Button dateEndButton = (Button)getActivity().findViewById(R.id.recordDateEndButton);
				dateEndButton.setText(strYear+"-"+strMonth+"-"+strDay);
			}
			
			updateListingTask.execute();
		}

	};
	
	private class UpdateListingTask extends AsyncTask<String, Integer, ArrayList<HashMap<String,Object>>>{

		@Override
		protected void onPreExecute(){
			db.showWaiting(true, false);
		}
		
	    @Override
	    protected ArrayList<HashMap<String,Object>> doInBackground(String... params) {
	    	ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String,Object>>();
	    	try{
		    	arrayList = db.recordSearch(keyword, sortBy, getButtonDate(R.id.recordDateStartButton), getButtonDate(R.id.recordDateEndButton));
		    	publishProgress(100);
	    	}
	    	catch (OutterDBException e){
	    		db.showExceptionDialog(e);
	    	}
	        return arrayList;
	    }
	    
	    @Override  
        protected void onProgressUpdate(Integer... progress) {  
	    	db.setWaiting(progress[0]);
            super.onProgressUpdate(progress);  
        }  

	    @Override
	    protected void onPostExecute(ArrayList<HashMap<String,Object>> result) {
	    	list.clear();
	    	list.addAll(result);
	    	mAdapter.notifyDataSetChanged();
	    	
	    	db.hideWaiting();
	        super.onPostExecute(result);
	    }
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayShowCustomEnabled(false);

    	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	//Stage1: set up database and adapter
    	db = new SqlDBInterface(getActivity());
        
        mAdapter= new SimpleAdapter(getActivity(),
        		list,
        		R.layout.record_list_item,
        		fromColumns,
        		toViews);
        
        setListAdapter(mAdapter);
        
        return inflater.inflate(R.layout.record_fragment, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
    	//Stage2: check if there are some saved instances, if so, recover the view status using those instances
    	super.onViewCreated(view, savedInstanceState);
        
        if (!startDate.equals(""))
    		((Button)getView().findViewById(R.id.recordDateStartButton)).setText(startDate);
    	if (!endDate.equals(""))
    		((Button)getView().findViewById(R.id.recordDateEndButton)).setText(endDate);
        if (!sortBy.equals("")){
        	Button currentButton = null;
        	String[] sortDetail = sortBy.split(" ");
        	if (sortDetail[0].equals(SqlDBTable.Record.COLUMN_NAME_DATE))
        		currentButton = (Button)getView().findViewById(R.id.recordDateButton);
        	else if (sortDetail[0].equals(SqlDBTable.Record.COLUMN_NAME_INOROUT))
            	currentButton = (Button)getView().findViewById(R.id.recordInoroutButton);
        	else if (sortDetail[0].equals(SqlDBTable.Entry.COLUMN_NAME_TYPE))
            	currentButton = (Button)getView().findViewById(R.id.recordTypeButton);
        	else if (sortDetail[0].equals(SqlDBTable.Record.COLUMN_NAME_STATUS))
            	currentButton = (Button)getView().findViewById(R.id.recordStatusButton);
        	else if (sortDetail[0].equals(SqlDBTable.Entry.COLUMN_NAME_NAME))
            	currentButton = (Button)getView().findViewById(R.id.recordEntryButton);
        	
        	if (sortDetail.length == 2){
        		currentButton.setBackgroundResource(R.drawable.column_button_green);
	    		currentButton.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
	    		currentButton.setPadding(3, 0, 0, 0);
	    		currentButton.setTextColor(Color.rgb(102, 153, 0));
        	}
        	else {
        		currentButton.setBackgroundResource(R.drawable.column_button_orange);
    			currentButton.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
    			currentButton.setTextColor(Color.rgb(255, 136, 0));
    			currentButton.setPadding(3, 0, 0, 0);
        	}
        }
        
    }
    
    
    @Override
    public void onResume(){
    	//Stage3: bind listener with the views
    	super.onResume();
    	
    	UpdateListingTask updateListingTask = new UpdateListingTask();
        updateListingTask.execute(keyword, sortBy, getButtonDate(R.id.recordDateStartButton), getButtonDate(R.id.recordDateEndButton));
    	
    	SearchView searchView = (SearchView) getView().findViewById(R.id.recordSearchBar);
        searchView.setOnQueryTextListener(this);
        
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
    public void onStop() {
    	//Stage stop: save instances
    	super.onStop();
    	
    	((SearchView)getView().findViewById(R.id.recordSearchBar)).setOnQueryTextListener(null);
    	startDate = ((Button)getView().findViewById(R.id.recordDateStartButton)).getText().toString();
    	endDate = ((Button)getView().findViewById(R.id.recordDateEndButton)).getText().toString();
    }
    
    @Override
    public void onClick(View v){
    	int buttonId = v.getId();
    	
    	if (buttonId == R.id.recordDateStartButton || buttonId == R.id.recordDateEndButton){
    		Button button = (Button) v;
    		String tag = "";
    		
    		switch (button.getId()){
    		case R.id.recordDateStartButton:
    			tag = R.id.recordDateStartButton + "";
    			break;
    		case R.id.recordDateEndButton:
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
			DatePickerFragment datePicker = DatePickerFragment.newInstance(new UpdateListingTask(), year, month, day);
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
	    		sortColumn = SqlDBTable.Record.COLUMN_NAME_DATE;
	    		currentButton = recordDateButton;
	    		break;
	    	case R.id.recordTypeButton:
	    		sortColumn = SqlDBTable.Entry.COLUMN_NAME_TYPE;
	    		currentButton = recordTypeButton;
	    		break;
	    	case R.id.recordEntryButton:
	    		sortColumn = SqlDBTable.Entry.COLUMN_NAME_NAME;
	    		currentButton = recordEntryButton;
	    		break;
	    	case R.id.recordInoroutButton:
	    		sortColumn = SqlDBTable.Record.COLUMN_NAME_INOROUT;
	    		currentButton = recordInoroutButton;
	    		break;
	    	case R.id.recordStatusButton:
	    		sortColumn = SqlDBTable.Record.COLUMN_NAME_STATUS;
	    		currentButton = recordStatusButton;
	    		break;	
	    	}
	    			
	    	if (sortDetail[0].equals(sortColumn) ){
	    		if (sortDetail.length ==2){
	    			sortBy = sortColumn;
	    			currentButton.setBackgroundResource(R.drawable.column_button_orange);
	    			currentButton.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
	    			currentButton.setTextColor(Color.rgb(255, 136, 0));
	    			currentButton.setPadding(3, 0, 0, 0);
	    		}
	    	else 
	    			sortBy = "";
	    	}
	    	else {
	    		sortBy = sortColumn + " DESC";
	    		currentButton.setBackgroundResource(R.drawable.column_button_green);
	    		currentButton.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
	    		currentButton.setPadding(3, 0, 0, 0);
	    		currentButton.setTextColor(Color.rgb(102, 153, 0));
	    	}
	    	
	    	UpdateListingTask updateListingTask = new UpdateListingTask();
	        updateListingTask.execute(keyword, sortBy, getButtonDate(R.id.recordDateStartButton), getButtonDate(R.id.recordDateEndButton));
	    	
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
		if (str == null || str.equals("")){
			UpdateListingTask updateListingTask = new UpdateListingTask();
	        updateListingTask.execute(keyword, sortBy, getButtonDate(R.id.recordDateStartButton), getButtonDate(R.id.recordDateEndButton));
		}
    	
		return false;
	}
	
	@Override
	public boolean onQueryTextSubmit(String str) {
		keyword = str;
		UpdateListingTask updateListingTask = new UpdateListingTask();
        updateListingTask.execute(keyword, sortBy, getButtonDate(R.id.recordDateStartButton), getButtonDate(R.id.recordDateEndButton));
		inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS); 
		
		return false;
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		
		HashMap<String,Object> hashMap = (HashMap<String,Object>)listView.getItemAtPosition(position);
		
		Intent intent = new Intent(getActivity(), SingleRecordActivity.class);
		intent.putExtra("id", Integer.parseInt(hashMap.get(SqlDBTable.Record.COLUMN_NAME_ID).toString()));
		startActivity(intent);
	}
}
