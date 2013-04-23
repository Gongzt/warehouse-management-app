package com.android.app.warehousemanagement;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.android.app.warehousemanagement.db.OutterDBExec.OutterDBException;
import com.android.app.warehousemanagement.db.SqlDBInterface;
import com.android.app.warehousemanagement.db.SqlDBTable;

public class InStockFragment extends Fragment
							 implements AdapterView.OnItemClickListener, View.OnClickListener, OnClickListener{
	
	private SqlDBInterface db = null;
	private InputMethodManager inputManager = null;
	
	private AutoCompleteTextView instockEntryAutocom = null;
	private EditText instockAmountEdittext = null;
	private EditText instockUnitEdittext = null;
	private EditText instockRemarkEdittext = null;
	private Spinner instockTypeSpinner = null;
	private Spinner instockWarehouseSpinner = null;
	
	private class InsertTask extends AsyncTask<String, Integer, Integer>{

		@Override
		protected void onPreExecute(){
			db.showWaiting(true, true);
		}
		
	    @Override
	    protected Integer doInBackground(String... params) {
	    	
	    	int type;
			if (instockTypeSpinner.getSelectedItem().toString().equals("原料"))
				type = 0;
			else
				type = 1;
	    	
			try {
				db.recordInsert(instockEntryAutocom.getText().toString(),
		    					 type, instockUnitEdittext.getText().toString(),
		    					 0, Integer.parseInt(instockAmountEdittext.getText().toString()), 
		    					 getWarehouseId(), 
		    					 instockRemarkEdittext.getText().toString());
			}
			catch (OutterDBException e){
				db.showExceptionDialog(e);
				publishProgress(100);
				return 0;
			}
	    	publishProgress(100);
	    	
	    	return 1;
	    }
	    
	    @Override  
        protected void onProgressUpdate(Integer... progress) {  
	    	db.setWaiting(progress[0]);
            super.onProgressUpdate(progress);  
        }  

	    @Override
	    protected void onPostExecute(Integer result) {
	    	
	    	instockEntryAutocom.setText("");
			instockAmountEdittext.setText("");
			instockUnitEdittext.setText("");
			instockRemarkEdittext.setText("");
			instockTypeSpinner.setSelection(0);
			instockWarehouseSpinner.setSelection(0);
			
			db.hideWaiting();
	    	
			if (result == 1) {
		    	AlertDialog.Builder completeDialog = new Builder(getActivity());
				completeDialog.setMessage("物品入库成功");
				completeDialog.setTitle("入库完成提示");
				completeDialog.setPositiveButton("确认", new OnClickListener() { 
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				completeDialog.create().show();
			}
	    	
	        super.onPostExecute(result);
	    }
	}

	
	private class InitialTask extends AsyncTask<String, Integer, ArrayList<ArrayList<HashMap<String,Object>>>>{

		@Override
		protected void onPreExecute(){
			db.showWaiting(true, false);
		}
		
	    @Override
	    protected ArrayList<ArrayList<HashMap<String,Object>>> doInBackground(String... params) {
	    	ArrayList<ArrayList<HashMap<String,Object>>> result = new ArrayList<ArrayList<HashMap<String,Object>>>();
	    	ArrayList<HashMap<String,Object>> entryList = new ArrayList<HashMap<String,Object>>();
	    	ArrayList<HashMap<String,Object>> warehouseList = new ArrayList<HashMap<String,Object>>();
	    	
	    	try{
		    	entryList = db.entrySelectAll();
		    	publishProgress(50); 
		    	warehouseList = db.warehouseSelectAll();
	    	}
	    	catch (OutterDBException e){
	    		db.showExceptionDialog(e);
	    	}
	    	
	    	result.add(entryList);
	    	result.add(warehouseList);
	    	publishProgress(100);
	        return result;
	    }
	    
	    @Override  
        protected void onProgressUpdate(Integer... progress) {  
	    	db.setWaiting(progress[0]);
            super.onProgressUpdate(progress);  
        }  

	    @Override
	    protected void onPostExecute(ArrayList<ArrayList<HashMap<String,Object>>> result) {
	    	
	    	SimpleAdapter entryAdapter = new SimpleAdapter(getActivity(), 
	    			result.get(0), 
	    			R.layout.simple_list_item, 
	    			new String[]{SqlDBTable.Entry.COLUMN_NAME_NAME}, 
	    			new int[]{R.id.simpleListText});    
	    	instockEntryAutocom.setAdapter(entryAdapter);  
	    	instockEntryAutocom.setOnItemClickListener(InStockFragment.this);
	    	
	    	SimpleAdapter warehouseAdapter = new SimpleAdapter(getActivity(),
	        		result.get(1),
	          		R.layout.simple_list_item,
	           		new String[]{SqlDBTable.Warehouse.COLUMN_NAME_NAME},
	           		new int[] {R.id.simpleListText});
	    	instockWarehouseSpinner.setAdapter(warehouseAdapter);
	    	
	    	db.hideWaiting();
	        super.onPostExecute(result);
	    }
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(R.string.app_name);

		LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.instock_actionbar, null);

		actionBar.setCustomView(v);
		
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	db = new SqlDBInterface(getActivity());

    	return inflater.inflate(R.layout.instock_fragment, container, false);
    }
    
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
    	
    	instockEntryAutocom = (AutoCompleteTextView) getView().findViewById(R.id.instockEntryAutocom);
    	instockAmountEdittext = (EditText) getView().findViewById(R.id.instockAmountEdittext);
    	instockUnitEdittext = (EditText)getView().findViewById(R.id.instockUnitEdittext);
    	instockRemarkEdittext = (EditText)getView().findViewById(R.id.instockRemarkEdittext);
    	instockTypeSpinner = (Spinner)getView().findViewById(R.id.instockTypeSpinner);
    	instockWarehouseSpinner = (Spinner)getView().findViewById(R.id.instockWarehouseSpinner);
    	
    	InitialTask initialTask = new InitialTask();
    	initialTask.execute();
    	
    	ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(), 
    			android.R.layout.simple_spinner_item, 
    			new String[] {getResources().getString(R.string.material), getResources().getString(R.string.product)});
    	typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	instockTypeSpinner.setAdapter(typeAdapter);

    	ImageButton actionbarCleanButton = (ImageButton)getActivity().getActionBar().getCustomView().findViewById(R.id.actionbarCleanButton);
    	actionbarCleanButton.setOnClickListener(this);
    	ImageButton actionbarSaveButton = (ImageButton)getActivity().getActionBar().getCustomView().findViewById(R.id.actionbarSaveButton);
    	actionbarSaveButton.setOnClickListener(this);
    	
    	inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
    }
    
    @SuppressWarnings("unchecked")
	private String getWarehouseName (){
		Spinner inoroutWarehouseSpinner = (Spinner)getView().findViewById(R.id.instockWarehouseSpinner);
		HashMap<String,Object> map = (HashMap<String,Object>)inoroutWarehouseSpinner.getAdapter()
				.getItem(inoroutWarehouseSpinner.getSelectedItemPosition());
		return map.get(SqlDBTable.Warehouse.COLUMN_NAME_NAME).toString();
	}
	
	@SuppressWarnings("unchecked")
	private int getWarehouseId (){
		Spinner inoroutWarehouseSpinner = (Spinner)getView().findViewById(R.id.instockWarehouseSpinner);
		HashMap<String,Object> map = (HashMap<String,Object>)inoroutWarehouseSpinner.getAdapter()
				.getItem(inoroutWarehouseSpinner.getSelectedItemPosition());
		return Integer.parseInt(map.get(SqlDBTable.Warehouse.COLUMN_NAME_ID).toString());
	}

    
    @SuppressWarnings("unchecked")
	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    	HashMap<String,Object> hashMap = (HashMap<String,Object>)parent.getItemAtPosition(position);
    	instockEntryAutocom.setText(hashMap.get(SqlDBTable.Entry.COLUMN_NAME_NAME).toString());
    	
    	Spinner typeSpinner = (Spinner)getView().findViewById(R.id.instockTypeSpinner);
    	if (Integer.parseInt(hashMap.get(SqlDBTable.Entry.COLUMN_NAME_TYPE).toString()) == 0)
    		typeSpinner.setSelection(0);
    	else
    		typeSpinner.setSelection(1);
    	
    	EditText unitEditText = (EditText)getView().findViewById(R.id.instockUnitEdittext);
    	unitEditText.setText(hashMap.get(SqlDBTable.Entry.COLUMN_NAME_UNIT).toString());
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		AlertDialog.Builder AlertDialog = new Builder(getActivity());
		if (id == R.id.actionbarCleanButton){
			AlertDialog.setMessage("确认清除所有内容？");
			AlertDialog.setTitle("清空提示");
			AlertDialog.setPositiveButton("确认",new OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					instockEntryAutocom.setText("");
					instockAmountEdittext.setText("");
					instockUnitEdittext.setText("");
					instockRemarkEdittext.setText("");
					instockTypeSpinner.setSelection(0);
					instockWarehouseSpinner.setSelection(0);
					dialog.dismiss();
					inputManager.hideSoftInputFromWindow(
			                getActivity().getCurrentFocus().getWindowToken(),
			                InputMethodManager.HIDE_NOT_ALWAYS); 
				}
			});
			AlertDialog.setNegativeButton("取消",new OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			AlertDialog.create().show();
		}
		else{
			AlertDialog.setMessage("请确认入库信息：\n" +
									"名称： " + instockEntryAutocom.getText().toString() + "\n" +
									"类型： " + instockTypeSpinner.getSelectedItem().toString() + "\n" + 
									"数量： " + instockAmountEdittext.getText().toString() +
									instockUnitEdittext.getText().toString() + "\n" +
									"仓库： " + getWarehouseName());
			AlertDialog.setTitle("入库提示");
			AlertDialog.setPositiveButton("确认", new OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					inputManager.hideSoftInputFromWindow(
			                getActivity().getCurrentFocus().getWindowToken(),
			                InputMethodManager.HIDE_NOT_ALWAYS); 
					
//					int type;
//					if (instockTypeSpinner.getSelectedItem().toString().equals("原料"))
//						type = 0;
//					else
//						type = 1;
//					db.recordInsert(instockEntryAutocom.getText().toString(), 
//							type, 
//							getWarehouseId(), 
//							Integer.parseInt(instockAmountEdittext.getText().toString()), 
//							instockUnitEdittext.getText().toString(), 
//							0,
//							instockRemarkEdittext.getText().toString());
					InsertTask insertTask = new InsertTask();
					insertTask.execute();
					
					dialog.dismiss();
				}
			});
			AlertDialog.setNegativeButton("取消",new OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			AlertDialog.create().show();
		}
		
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		Log.e("haha", dialog.getClass().getName());
		
	}
}
