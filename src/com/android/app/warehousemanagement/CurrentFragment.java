package com.android.app.warehousemanagement;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.app.warehousemanagement.db.InnerDBExec;
import com.android.app.warehousemanagement.db.InnerDBTable;

public class CurrentFragment extends ListFragment
		                     implements TextWatcher, View.OnClickListener {
			
	private InnerDBExec db = null;
	private SimpleCursorAdapter mAdapter = null;
	private String sortBy = "";
	private String keyword = "";
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
           		db.currentSearch("", ""),
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
        
        Button currentTypeButton = (Button)getView().findViewById(R.id.currentTypeButton);
        currentTypeButton.setOnClickListener(this);
        Button currentEntryButton = (Button)getView().findViewById(R.id.currentEntryButton);
        currentEntryButton.setOnClickListener(this);
    }
    
	@Override
    public void onClick(View v){
    	int buttonId = v.getId();
    	String[] sortDetail = sortBy.split(" ");
    	String sortColumn = "";
    	
    	switch (buttonId){
    	case R.id.currentTypeButton:
    		sortColumn = InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE;
    		break;
    	case R.id.currentEntryButton:
    		sortColumn = InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME;
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
    	
    	mAdapter.swapCursor(db.currentSearch(keyword, sortBy));
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
		mAdapter.swapCursor(db.currentSearch(keyword, sortBy));
	}
	
    @Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
	}
}
