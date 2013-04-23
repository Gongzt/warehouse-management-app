package com.android.app.warehousemanagement;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.app.warehousemanagement.db.OutterDBExec.OutterDBException;
import com.android.app.warehousemanagement.db.SqlDBInterface;
import com.android.app.warehousemanagement.db.SqlDBTable;

public class InoroutActivity extends Activity 
                             implements View.OnClickListener, AdapterView.OnItemSelectedListener {

	private int id;
	private String name;
	private int type;
	private String typeName;
	private int usableAmount;
	private int amount;
	private String remark;
	private String unit;
	private int warehouseId;
	private String warehouse;
	private int from;
	private SqlDBInterface db = null;
	
	private class InsertTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected void onPreExecute(){
			db.showWaiting(true, false);
		}
		
	    @Override
	    protected Integer doInBackground(Integer... params) {
	    	try {
	    		db.recordInsert(name, type, unit, params[0], amount, warehouseId, remark);
	    	}
	    	catch (OutterDBException e){
	    		db.showExceptionDialog(e);
	    		return -1;
	    	}
	    	publishProgress(100);  
	    	return params[0];
	    }
	    
	    @Override  
        protected void onProgressUpdate(Integer... progress) {  
	    	db.setWaiting(progress[0]);
            super.onProgressUpdate(progress);  
        }  

	    @Override
	    protected void onPostExecute(Integer result) {
	    	
	    	if (result != -1) {
		    	AlertDialog.Builder completeDialog = new Builder(InoroutActivity.this);
				
				completeDialog.setTitle("登记完成提示");
				completeDialog.setPositiveButton("确认", new OnClickListener() { 
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
				
		    	if (result == 0) {
		    		completeDialog.setMessage("物品入库登记成功");
			    }
		    	else if (result == 1){
					completeDialog.setMessage("物品出库登记成功");
		    	}
		    	
		    	completeDialog.create().show();
		    	
	    	}
	    	db.hideWaiting();
	        super.onPostExecute(result);
	    }
	}
	
	private class InitialTask extends AsyncTask<Integer, Integer, ArrayList<HashMap<String,Object>>>{

		@Override
		protected void onPreExecute(){
			db.showWaiting(true, false);
		}
		
	    @Override
	    protected ArrayList<HashMap<String,Object>> doInBackground(Integer... params) {
	    	ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String,Object>>();
	    	
	    	try {
	    		arrayList = db.warehouseSelectAll();
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
	    	Spinner inoroutWarehouseSpinner = (Spinner)findViewById(R.id.inoroutWarehouseSpinner);
			SimpleAdapter warehouseAdapter = new SimpleAdapter(db.getContext(),
	        		result,
	          		R.layout.simple_list_item,
	           		new String[]{SqlDBTable.Warehouse.COLUMN_NAME_NAME},
	           		new int[] {R.id.simpleListText});
			inoroutWarehouseSpinner.setAdapter(warehouseAdapter);
			
			db.hideWaiting();
	        super.onPostExecute(result);
	    }
	}
	
	private class UpdateUsableAmountTask extends AsyncTask<String, Integer, String>{

		@Override
		protected void onPreExecute(){
			db.showWaiting(true, false);
		}
		
	    @Override
	    protected String doInBackground(String... params) {
	    	try {
	    		usableAmount = db.entrySelectUsableAmount(id, getWarehouseId());
	    		
	    	}
	    	catch (OutterDBException e){
	    		usableAmount = 0;
	    		db.showExceptionDialog(e);
	    	}
	    	
	    	publishProgress(100);
	        return null;
	    }
	    
	    @Override  
        protected void onProgressUpdate(Integer... progress) {  
	    	db.setWaiting(progress[0]);
            super.onProgressUpdate(progress);  
        }  

	    @Override
	    protected void onPostExecute(String result) {
			((TextView)findViewById(R.id.inoroutUsableAmountTextView)).setText(usableAmount+unit);
	    	
			db.hideWaiting();
	        super.onPostExecute(result);
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inorout_activity);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		db = new SqlDBInterface(this);
		
		Intent intent = getIntent();
		
		id  = intent.getIntExtra("id", 0);
		name = intent.getStringExtra("name");
		type = intent.getIntExtra("type", 0);
		unit = intent.getStringExtra("unit");
		from = intent.getIntExtra("from", 0);
		
		InitialTask initialTask = new InitialTask();
		initialTask.execute();
		
		if (type == 0)
			typeName = "原料";
		else
			typeName = "产品";
		
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.inorout_actionbar, null);
		
		actionBar.setCustomView(v);
		
		((ImageButton)actionBar.getCustomView().findViewById(R.id.inoroutConfirmButton)).setOnClickListener(this);
		
		((TextView)findViewById(R.id.inoroutEntryTextView)).setText(name);
		if (type == 0)
			((ImageView)findViewById(R.id.inoroutTypeImage)).setBackgroundResource(R.drawable.icon_material);
		else
			((ImageView)findViewById(R.id.inoroutTypeImage)).setBackgroundResource(R.drawable.icon_product);
		((TextView)findViewById(R.id.inoroutUnitTextview)).setText(unit);
		
		Spinner inoroutWarehouseSpinner = (Spinner)findViewById(R.id.inoroutWarehouseSpinner);

		if (from == R.id.singleCurrentInstockButton){
			((TextView)actionBar.getCustomView().findViewById(R.id.inoroutTitleTextView)).setText("入库申请");
			((LinearLayout)findViewById(R.id.inoroutUsableAmountLayout)).setVisibility(View.GONE);
		}
		else {
			((TextView)actionBar.getCustomView().findViewById(R.id.inoroutTitleTextView)).setText("出库申请");
			inoroutWarehouseSpinner.setOnItemSelectedListener(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inorout, menu);
		return true;
	}

	@SuppressWarnings("unchecked")
	private String getWarehouseName (){
		Spinner inoroutWarehouseSpinner = (Spinner)findViewById(R.id.inoroutWarehouseSpinner);
		HashMap<String,Object> map = (HashMap<String,Object>)inoroutWarehouseSpinner.getAdapter()
				.getItem(inoroutWarehouseSpinner.getSelectedItemPosition());
		return map.get(SqlDBTable.Warehouse.COLUMN_NAME_NAME).toString();
	}
	
	@SuppressWarnings("unchecked")
	private int getWarehouseId (){
		Spinner inoroutWarehouseSpinner = (Spinner)findViewById(R.id.inoroutWarehouseSpinner);
		HashMap<String,Object> map = (HashMap<String,Object>)inoroutWarehouseSpinner.getAdapter()
				.getItem(inoroutWarehouseSpinner.getSelectedItemPosition());
		return Integer.parseInt(map.get(SqlDBTable.Warehouse.COLUMN_NAME_ID).toString());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{    
	   switch (item.getItemId()) 
	   {        
	      case android.R.id.home:            
	         finish();            
	         return true;        
	      default:            
	         return super.onOptionsItemSelected(item);    
	   }
	}
	
	@Override
	public void onClick(View v) {
		AlertDialog.Builder AlertDialog = new Builder(this);
		String amountStr = ((EditText)findViewById(R.id.inoroutAmountEdittext)).getText().toString();
		
		if (amountStr.equals("") || Integer.parseInt(amountStr) == 0) {
			AlertDialog.Builder errorDialog = new Builder(InoroutActivity.this);
			errorDialog.setMessage("请确认数量已经填写或者数量不为0");
			errorDialog.setTitle("错误提示");
			errorDialog.setPositiveButton("确认", new OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			errorDialog.create().show();
		}
		else {
			amount = Integer.parseInt(amountStr);
			remark = ((EditText)findViewById(R.id.inoroutRemarkEdittext)).getText().toString();
			warehouse = getWarehouseName();
			warehouseId = getWarehouseId();
			
			if (from == R.id.singleCurrentInstockButton) {
				
				AlertDialog.setMessage("确认要入库一下物品？\n" +
									   "名称： " + name + "(" + typeName + ")\n" +
									   "数量： " + amount + unit + "\n" +
									   "仓库： " + warehouse);
									   
				AlertDialog.setTitle("入库提示");
				AlertDialog.setPositiveButton("确认",new OnClickListener() { 
					@Override
					public void onClick(DialogInterface dialog, int which) {
						InsertTask insertTask = new InsertTask();
						insertTask.execute(0);
						
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
			else {
				if (amount > usableAmount) {
					AlertDialog.Builder errorDialog = new Builder(InoroutActivity.this);
					errorDialog.setMessage( "出库数量超出可用出库数量\n" + 
							"可用出库数量： " + usableAmount + unit +"\n" +
							"申请出库数量： " + amount + unit);
					errorDialog.setTitle("错误提示");
					errorDialog.setPositiveButton("确认", new OnClickListener() { 
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					errorDialog.create().show();
				}
				else {
					AlertDialog.setMessage("确认要出库一下物品？\n" +
							   "名称： " + name + "(" + typeName + ")\n" +
							   "数量： " + amount + unit + "\n" +
							   "仓库： " + warehouse);
							   
					AlertDialog.setTitle("出库提示");
					AlertDialog.setPositiveButton("确认",new OnClickListener() { 
						@Override
						public void onClick(DialogInterface dialog, int which) {
							InsertTask insertTask = new InsertTask();
							insertTask.execute(1);
							
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
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		UpdateUsableAmountTask updateUsableAmountTask = new UpdateUsableAmountTask();
		updateUsableAmountTask.execute();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}
