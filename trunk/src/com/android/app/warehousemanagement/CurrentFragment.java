package com.android.app.warehousemanagement;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.android.app.warehousemanagement.db.InnerDBExec;
import com.android.app.warehousemanagement.db.InnerDBTable;

public class CurrentFragment extends ListFragment
		                     implements TextWatcher {
			
	private InnerDBExec db = null;
	private SimpleCursorAdapter mAdapter = null;
	private String[] fromColumns = new String[] {
			InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME,
			InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE,
			InnerDBTable.Current.COLUMN_NAME_AMOUNT
	};
	private int[] toViews = new int[] {
			R.id.currentEntryTextView,
			R.id.currentTypeTextView,
			R.id.currentAmountTextView
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
          		R.layout.current_list_item,
           		db.currentSelectAll(),
           		fromColumns,
           		toViews,
           		0);
        setListAdapter(mAdapter);
		
    	return inflater.inflate(R.layout.current_fragment, container, false);
    }
    
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState){
    	
    	super.onViewCreated(view, savedInstanceState);
    	
    	EditText editText = (EditText)getView().findViewById(R.id.currentSearchText);
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
		mAdapter.swapCursor(db.currentSearch(s.toString()));

	}
	
    @Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
	}
}
