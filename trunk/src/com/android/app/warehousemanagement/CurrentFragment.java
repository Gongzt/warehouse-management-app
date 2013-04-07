package com.android.app.warehousemanagement;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.android.app.warehousemanagement.db.InnerDBExec;
import com.android.app.warehousemanagement.db.InnerDBTable;

public class CurrentFragment extends ListFragment
		                     implements View.OnClickListener, SearchView.OnQueryTextListener{
			
	private InnerDBExec db = null;
	private SimpleAdapter mAdapter = null;
	private InputMethodManager inputManager = null;
	private String sortBy = "";
	private String keyword = "";
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	private String[] fromColumns = new String[] {			
			InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE,
			InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME,
			InnerDBTable.Current.COLUMN_NAME_AMOUNT,
			InnerDBTable.Current.COLUMN_NAME_UNIT
	};
	private int[] toViews = new int[] {
			R.id.currentTypeImage,
			R.id.currentEntryTextView,
			R.id.currentAmountTextView,
			R.id.currentUnitTextView
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
		//Stage1: setup datebase and listview adpter
        db = new InnerDBExec(getActivity());
        
        mAdapter= new SimpleAdapter(getActivity(),
        		list,
          		R.layout.current_list_item,
           		fromColumns,
           		toViews);
        setListAdapter(mAdapter);
		
    	return inflater.inflate(R.layout.current_fragment, container, false);
    }
	
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState){
		//Stage2: check if some instances are saved, if so, recover the view with those instances
    	super.onViewCreated(view, savedInstanceState);
        
        if (!sortBy.equals("")){
        	Button currentButton = null;
        	String[] sortDetail = sortBy.split(" ");
        	if (sortDetail[0].equals(InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE))
        		currentButton = (Button)getView().findViewById(R.id.currentTypeButton);
        	else if (sortDetail[0].equals(InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME))
            	currentButton = (Button)getView().findViewById(R.id.currentEntryButton);
        	
        	if (sortDetail.length == 2){
        		currentButton.setBackgroundResource(R.drawable.column_button_green);
	    		currentButton.setTextColor(Color.rgb(102, 153, 0));
        	}
        	else {
        		currentButton.setBackgroundResource(R.drawable.column_button_orange);
    			currentButton.setTextColor(Color.rgb(255, 136, 0));
        	}
        		
        }
        
    }
	
	@Override
	public void onResume(){
		//Stage3: bind view with listener and update the listview data
		super.onResume();
		
		SearchView searchView = (SearchView) getView().findViewById(R.id.currentSearchBar);
        searchView.setOnQueryTextListener(this);
        searchView.setSelected(true);
    	
        Button currentTypeButton = (Button)getView().findViewById(R.id.currentTypeButton);
        currentTypeButton.setOnClickListener(this);
        Button currentEntryButton = (Button)getView().findViewById(R.id.currentEntryButton);
        currentEntryButton.setOnClickListener(this);
        
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
		
        updateCursor(db.currentSearch(keyword, sortBy));
    	mAdapter.notifyDataSetChanged();
	}
	
	//update the binding list with the data of the cursor
  	private void updateCursor(Cursor c){
  		list.clear();
  		
  		c.moveToFirst();
  		for(int i=0;i<c.getCount();i++)     
  		{     
  			HashMap<String, Object> map = new HashMap<String, Object>();
  			map.put(InnerDBTable.Current.COLUMN_NAME_ENTRY_ID, c.getString(0));
  			map.put(InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME, c.getString(1));  
  			map.put(InnerDBTable.Current.COLUMN_NAME_UNIT, c.getString(3));  
  			map.put(InnerDBTable.Current.COLUMN_NAME_AMOUNT, c.getString(4));  
  			if (c.getString(2).equals(getResources().getString(R.string.product))){
  				map.put(InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE, R.drawable.icon_product);
  			}
  			else{
  				map.put(InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE, R.drawable.icon_material);
  			}
  			
  			list.add(map);    
  			c.moveToNext();
  		}
  		
  	}

  	//listen when sortby button is clicked
	@Override
    public void onClick(View v){
    	int buttonId = v.getId();
    	String[] sortDetail = sortBy.split(" ");
    	String sortColumn = "";
    	Button currentTypeButton = (Button)getView().findViewById(R.id.currentTypeButton);
        Button currentEntryButton = (Button)getView().findViewById(R.id.currentEntryButton); 
        Button currentButton= null;
    	
    	switch (buttonId){
    	case R.id.currentTypeButton:
    		sortColumn = InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE;
    		currentEntryButton.setBackgroundResource(R.drawable.column_button_blue);
    		currentEntryButton.setTextColor(Color.rgb(0, 153, 204));  
    		currentButton = currentTypeButton;
    		break;
    	case R.id.currentEntryButton:
    		sortColumn = InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME;
    		currentTypeButton.setBackgroundResource(R.drawable.column_button_blue);
    		currentTypeButton.setTextColor(Color.rgb(0, 153, 204));
    		currentButton = currentEntryButton;
    		break;
    	}
    	
    	if (sortDetail[0].equals(sortColumn) ){
			if (sortDetail.length == 2) {
				sortBy = sortColumn;
				currentButton.setBackgroundResource(R.drawable.column_button_orange);
				currentButton.setTextColor(Color.rgb(255, 136, 0));
			}
			else{
				sortBy = "";
				currentButton.setBackgroundResource(R.drawable.column_button_blue);
				currentButton.setTextColor(Color.rgb(0, 153, 204));
			}
		}
		else {
			sortBy = sortColumn + " DESC";
			currentButton.setBackgroundResource(R.drawable.column_button_green);
			currentButton.setTextColor(Color.rgb(102, 153, 0));
		}
    	
    	updateCursor(db.currentSearch(keyword, sortBy));
    	mAdapter.notifyDataSetChanged();
    }
	
	//listen when the searchbar text is changed
	@Override
	public boolean onQueryTextChange(String str) {
    	
    	keyword = str;
    	updateCursor(db.currentSearch(keyword, sortBy));
    	mAdapter.notifyDataSetChanged();
		
		return false;
	}
	
	@Override
	public boolean onQueryTextSubmit(String str) {
    	
    	keyword = str;
    	updateCursor(db.currentSearch(keyword, sortBy));
    	mAdapter.notifyDataSetChanged();
		inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS); 
		
		return false;
	}

	//listen when the item of listview is clicked and go the information of the listview
    @SuppressWarnings("unchecked")
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		
		HashMap<String,Object> hashMap = (HashMap<String,Object>)listView.getItemAtPosition(position);
		
		Intent intent = new Intent(getActivity(), SingleCurrentActivity.class);
		intent.putExtra("name", hashMap.get(InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME).toString());
		if (Integer.parseInt(hashMap.get(InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE).toString()) == R.drawable.icon_material)
			intent.putExtra("type", getResources().getString(R.string.material));
		else
			intent.putExtra("type", getResources().getString(R.string.product));
		intent.putExtra("unit", hashMap.get(InnerDBTable.Current.COLUMN_NAME_UNIT).toString());
		startActivity(intent);
	}
}