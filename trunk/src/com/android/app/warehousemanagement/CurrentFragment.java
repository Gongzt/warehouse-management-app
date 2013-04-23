package com.android.app.warehousemanagement;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.android.app.warehousemanagement.db.OutterDBExec.OutterDBException;
import com.android.app.warehousemanagement.db.SqlDBInterface;
import com.android.app.warehousemanagement.db.SqlDBTable;

public class CurrentFragment extends ListFragment
		                     implements View.OnClickListener, SearchView.OnQueryTextListener{
			
	private SimpleAdapter mAdapter = null;
	private String sortby = "";
	private String keyword = "";
	private SqlDBInterface db = null;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	private String[] fromColumns = new String[] {
			SqlDBTable.Entry.COLUMN_NAME_NAME,
			SqlDBTable.Entry.COLUMN_NAME_TYPE,
			SqlDBTable.Entry.COLUMN_NAME_UNIT,
			SqlDBTable.Current.COLUMN_NAME_AMOUNT,
	};
	private int[] toViews = new int[] {
			R.id.currentEntryTextView,
			R.id.currentTypeImage,
			R.id.currentUnitTextView,
			R.id.currentAmountTextView
	};
	
	private class UpdateListingTask extends AsyncTask<String, Integer, ArrayList<HashMap<String,Object>>>{

		@Override
		protected void onPreExecute(){
			db.showWaiting(true, false);
		}
		
	    @Override
	    protected ArrayList<HashMap<String,Object>> doInBackground(String... params) {
	    	ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String,Object>>();
	    	try {
	    		arrayList = db.currentSearch(keyword, sortby);
	    	}
	    	catch (OutterDBException e){
	    		db.showExceptionDialog(e);
	    	}
	    	
	    	publishProgress(100); 
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
		//Stage1: setup datebase and listview adpter
		db = new SqlDBInterface(getActivity());
        
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
        
        if (!sortby.equals("")){
        	Button currentButton = null;
        	String[] sortDetail = sortby.split(" ");
        	if (sortDetail[0].equals(SqlDBTable.Entry.COLUMN_NAME_TYPE))
        		currentButton = (Button)getView().findViewById(R.id.currentTypeButton);
        	else if (sortDetail[0].equals(SqlDBTable.Entry.COLUMN_NAME_NAME))
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
		
		UpdateListingTask updateListingTask = new UpdateListingTask();
        updateListingTask.execute();
		
		SearchView searchView = (SearchView) getView().findViewById(R.id.currentSearchBar);
        searchView.setOnQueryTextListener(this);
        searchView.setSelected(true);
    	
        Button currentTypeButton = (Button)getView().findViewById(R.id.currentTypeButton);
        currentTypeButton.setOnClickListener(this);
        Button currentEntryButton = (Button)getView().findViewById(R.id.currentEntryButton);
        currentEntryButton.setOnClickListener(this);
	}
	
  	//listen when sortby button is clicked
	@Override
    public void onClick(View v){
    	int buttonId = v.getId();
	    	String[] sortDetail = sortby.split(" ");
	    	String sortColumn = "";
	    	Button currentTypeButton = (Button)getView().findViewById(R.id.currentTypeButton);
	        Button currentEntryButton = (Button)getView().findViewById(R.id.currentEntryButton); 
	        Button currentButton= null;
	    	
	    	switch (buttonId){
	    	case R.id.currentTypeButton:
	    		sortColumn = SqlDBTable.Entry.COLUMN_NAME_TYPE;
	    		currentEntryButton.setBackgroundResource(R.drawable.column_button_blue);
	    		currentEntryButton.setTextColor(Color.rgb(0, 153, 204));  
	    		currentButton = currentTypeButton;
	    		break;
	    	case R.id.currentEntryButton:
	    		sortColumn = SqlDBTable.Entry.COLUMN_NAME_NAME;
	    		currentTypeButton.setBackgroundResource(R.drawable.column_button_blue);
	    		currentTypeButton.setTextColor(Color.rgb(0, 153, 204));
	    		currentButton = currentEntryButton;
	    		break;
	    	}
	    	
	    	if (sortDetail[0].equals(sortColumn) ){
				if (sortDetail.length == 2) {
					sortby = sortColumn;
					currentButton.setBackgroundResource(R.drawable.column_button_orange);
					currentButton.setTextColor(Color.rgb(255, 136, 0));
				}
				else{
					sortby = "";
					currentButton.setBackgroundResource(R.drawable.column_button_blue);
					currentButton.setTextColor(Color.rgb(0, 153, 204));
				}
			}
			else {
				sortby = sortColumn + " DESC";
				currentButton.setBackgroundResource(R.drawable.column_button_green);
				currentButton.setTextColor(Color.rgb(102, 153, 0));
			}
    	
    	UpdateListingTask updateListingTask = new UpdateListingTask();
    	updateListingTask.execute();
    }
	
	//listen when the searchbar text is changed
	@Override
	public boolean onQueryTextChange(String str) {
    	
    	keyword = str;
    	if (str == null || str.equals("")){
    		UpdateListingTask updateListingTask = new UpdateListingTask();
        	updateListingTask.execute();
    	}
		
		return false;
	}
	
	@Override
	public boolean onQueryTextSubmit(String str) {
    	
    	keyword = str;
    	UpdateListingTask updateListingTask = new UpdateListingTask();
    	updateListingTask.execute();
		
		return false;
	}

	//listen when the item of listview is clicked and go the information of the listview
    @SuppressWarnings("unchecked")
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		
		HashMap<String,Object> hashMap = (HashMap<String,Object>)listView.getItemAtPosition(position);
		
		Intent intent = new Intent(getActivity(), SingleCurrentActivity.class);
		intent.putExtra("name", hashMap.get(SqlDBTable.Entry.COLUMN_NAME_NAME).toString());
		if (Integer.parseInt(hashMap.get(SqlDBTable.Entry.COLUMN_NAME_TYPE).toString()) == R.drawable.icon_material)
			intent.putExtra("type", 0);
		else
			intent.putExtra("type", 1);
		intent.putExtra("unit", hashMap.get(SqlDBTable.Entry.COLUMN_NAME_UNIT).toString());
		startActivity(intent);
	}
}
