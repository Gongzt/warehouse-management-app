package com.android.app.warehousemanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.app.warehousemanagement.db.InnerDBExec;
import com.android.app.warehousemanagement.db.InnerDBTable;

public class RecordFragment extends ListFragment 
							implements TextWatcher{
	
	private InnerDBExec db = null;
	private SimpleCursorAdapter mAdapter = null;
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
        		db.recordSelectAll(),
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
		mAdapter.swapCursor(db.recordSearch(s.toString()));

	}

}
