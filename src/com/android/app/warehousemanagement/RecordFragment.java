package com.android.app.warehousemanagement;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.app.warehousemanagement.db.InnerDBExec;
import com.android.app.warehousemanagement.db.InnerDBTable;

public class RecordFragment extends ListFragment 
							implements TextWatcher, View.OnClickListener{
	
	private InnerDBExec db = null;
	private SimpleCursorAdapter mAdapter = null;
	private String sortBy = "";
	private String keyword = "";
	private String[] fromColumns = new String[] {
    		InnerDBTable.Record.COLUMN_NAME_DATE,
    		InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE,
    		InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME,
    		InnerDBTable.Record.COLUMN_NAME_INOROUT,
    		InnerDBTable.Record.COLUMN_NAME_AMOUNT,
    		InnerDBTable.Record.COLUMN_NAME_STATUS
    };
	private int[] toViews = new int[] {
    		R.id.recordDateTextView,
    		R.id.recordTypeTextView,
    		R.id.recordEntryTextView,
    		R.id.recordInoroutTextView,
    		R.id.recordAmountTextView,
    		R.id.recordStatusTextView
    };
	
	public static final class DatePickerFragment extends DialogFragment
												 implements DatePickerDialog.OnDateSetListener{		
		
		private static InnerDBExec db = null;
		private static SimpleCursorAdapter mAdapter = null;
		private static String date = "";
		private static String keyword = "";
		private static String sortBy = "";
		
		public static DatePickerFragment newInstance(InnerDBExec _db, SimpleCursorAdapter _mAdapter, String _keyword, String _sortBy, String _date, int year, int month, int day){
			DatePickerFragment dialog  = new DatePickerFragment();
			 
			db = _db;
			mAdapter = _mAdapter;
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
            return new DatePickerDialog(getActivity(), this, year, month, day);
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
				mAdapter.swapCursor(db.recordSearch(keyword, sortBy, strYear+strMonth+strDay+"0000", date));
			}
			else{
				Button dateEndButton = (Button)getActivity().findViewById(R.id.recordDateEndButton);
				dateEndButton.setText(strYear+"-"+strMonth+"-"+strDay);
				mAdapter.swapCursor(db.recordSearch(keyword, sortBy, date, strYear+strMonth+strDay+"0000"));
			}
		}

	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        db = new InnerDBExec(getActivity());
        
        mAdapter= new SimpleCursorAdapter(getActivity(),
        		R.layout.record_list_item,
        		db.recordSearch("", "", "", ""),
        		fromColumns,
        		toViews,
        		0);
        setListAdapter(mAdapter);
        
        return inflater.inflate(R.layout.record_fragment, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
    	
    	super.onViewCreated(view, savedInstanceState);
    	
    	EditText editText = (EditText)getView().findViewById(R.id.recordSearchText);
        editText.addTextChangedListener(this);
        
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
			DatePickerFragment datePicker = DatePickerFragment.newInstance(db, mAdapter, keyword, sortBy, strDate, year, month, day);
			datePicker.show(getActivity().getSupportFragmentManager(), tag);
    	}
    	else {
	    	String[] sortDetail = sortBy.split(" ");
	    	String sortColumn = "";
	    	
	    	switch (buttonId){
	    	case R.id.recordDateButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_DATE;
	    		break;
	    	case R.id.recordTypeButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE;
	    		break;
	    	case R.id.recordEntryButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME;
	    		break;
	    	case R.id.recordInoroutButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_INOROUT;
	    		break;
	    	case R.id.recordStatusButton:
	    		sortColumn = InnerDBTable.Record.COLUMN_NAME_STATUS;
	    		break;	
	    	}
	    			
	    	if (sortDetail[0].equals(sortColumn) ){
	    		if (sortDetail.length == 2)
	    			sortBy = sortColumn;
	    		else
	    			sortBy = "";
	    	}
	    	else {
	    		sortBy = sortColumn + " DESC";
	    	}
	    	
	    	mAdapter.swapCursor(db.recordSearch(keyword, sortBy, getButtonDate(R.id.recordDateStartButton), getButtonDate(R.id.recordDateEndButton)));
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
    public void afterTextChanged(Editable s) {

	}
    
    @Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

    @Override
	public void onTextChanged(CharSequence s, int start, int before,
			int count) {
    	keyword = s.toString();
		mAdapter.swapCursor(db.recordSearch(keyword, sortBy, getButtonDate(R.id.recordDateStartButton), getButtonDate(R.id.recordDateEndButton)));

	}

}
