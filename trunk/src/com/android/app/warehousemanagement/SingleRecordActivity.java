package com.android.app.warehousemanagement;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.app.warehousemanagement.db.OutterDBExec.OutterDBException;
import com.android.app.warehousemanagement.db.SqlDBInterface;
import com.android.app.warehousemanagement.db.SqlDBTable;

public class SingleRecordActivity extends Activity 
                                  implements View.OnClickListener{
	
	private SqlDBInterface db = null;
	private int id;
	private String name;
	private int type;
	private String warehouse;
	private int amount;
	private String unit;
	private int inorout;
	private int status;
	private String remark;
	private String date;
	
	private class InitialTask extends AsyncTask<String, Integer, HashMap<String,Object>>{

		@Override
		protected void onPreExecute(){
			db.showWaiting(true, false);
		}
		
	    @Override
	    protected HashMap<String,Object> doInBackground(String... params) {
	    	HashMap<String,Object> map = new HashMap<String,Object>();
	    	try{
	    		map = db.recordSelectById(id);
	    	}
	    	catch (OutterDBException e){
	    		db.showExceptionDialog(e);
	    		return null;
	    	}
	    	publishProgress(100); 
	        return map;
	    }
	    
	    @Override  
        protected void onProgressUpdate(Integer... progress) {  
	    	db.setWaiting(progress[0]);
            super.onProgressUpdate(progress);  
        }  

	    @Override
	    protected void onPostExecute(HashMap<String,Object> result) {
	    	if (result != null) {
		    	name = result.get(SqlDBTable.Entry.COLUMN_NAME_NAME).toString();
				type = Integer.parseInt(result.get(SqlDBTable.Entry.COLUMN_NAME_TYPE).toString());
				warehouse = result.get(SqlDBTable.Warehouse.COLUMN_NAME_NAME).toString();
				amount = Integer.parseInt(result.get(SqlDBTable.Record.COLUMN_NAME_AMOUNT).toString());
				unit = result.get(SqlDBTable.Entry.COLUMN_NAME_UNIT).toString();
				status = Integer.parseInt(result.get(SqlDBTable.Record.COLUMN_NAME_STATUS).toString());
				remark = result.get(SqlDBTable.Record.COLUMN_NAME_REMARK).toString();
				date = result.get(SqlDBTable.Record.COLUMN_NAME_DATE).toString();
				
				ActionBar actionBar = getActionBar();
				
				if (status == 1){
					actionBar.setTitle(name);
				}
				else {
					actionBar.setDisplayShowCustomEnabled(true);
					
					LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View v = inflator.inflate(R.layout.single_record_actionbar, null);
					
					actionBar.setCustomView(v);
					
					((TextView)actionBar.getCustomView().findViewById(R.id.singleRecordTitle)).setText(name);
					((ImageView)actionBar.getCustomView().findViewById(R.id.singleRecordPassButton)).setOnClickListener(SingleRecordActivity.this);
					((ImageView)actionBar.getCustomView().findViewById(R.id.singleRecordDeleteButton)).setOnClickListener(SingleRecordActivity.this);
				}
				
				if (type == 0)
					((ImageView)findViewById(R.id.singleRecordTypeImage)).setBackgroundResource(R.drawable.icon_material);
				else
					((ImageView)findViewById(R.id.singleRecordTypeImage)).setBackgroundResource(R.drawable.icon_product);
				
				((TextView)findViewById(R.id.singleRecordEntryTextView)).setText(name);
				((TextView)findViewById(R.id.singleRecordWarehouseTextView)).setText(warehouse);
				
				if (inorout == 0)
					((ImageView)findViewById(R.id.singleRecordInoroutImage)).setBackgroundResource(R.drawable.icon_instock);
				else
					((ImageView)findViewById(R.id.singleRecordInoroutImage)).setBackgroundResource(R.drawable.icon_outstock);
				
				((TextView)findViewById(R.id.singleRecordDateTextView)).setText(date);
	
				if (status == 1)
					((ImageView)findViewById(R.id.singleRecordStatusImage)).setBackgroundResource(R.drawable.icon_pass);
				else
					((ImageView)findViewById(R.id.singleRecordStatusImage)).setBackgroundResource(R.drawable.icon_pending);
				
				((TextView)findViewById(R.id.singleRecordAmountTextView)).setText(amount+unit);
				((TextView)findViewById(R.id.singleRecordRemarkTextView)).setText("       "+remark);
		    	
	    	}
			db.hideWaiting();
	        super.onPostExecute(result);
	    }
	}
	
	private class DeleteTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected void onPreExecute(){
			db.showWaiting(true, false);
		}
		
	    @Override
	    protected Integer doInBackground(Integer... params) {
	    	try {
	    		db.recordDelete(id);
	    	}
	    	catch (OutterDBException e){
	    		db.showExceptionDialog(e);
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
	    	if (result == 1){
		    	AlertDialog.Builder completeDialog = new Builder(SingleRecordActivity.this);
				completeDialog.setMessage("物品出入库记录已删除！");
				completeDialog.setTitle("成功删除提示");
				completeDialog.setPositiveButton("确认", new OnClickListener() { 
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
				completeDialog.create().show();
		    	
	    	}
			db.hideWaiting();
	        super.onPostExecute(result);
	    }
	}
	
	private class PassTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected void onPreExecute(){
			db.showWaiting(true, false);
		}
		
	    @Override
	    protected Integer doInBackground(Integer... params) {
	    	try {
	    		db.recordUpdate(id);
	    	}
	    	catch (OutterDBException e){
	    		db.showExceptionDialog(e);
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
	    	if (result == 1){
		    	AlertDialog.Builder completeDialog = new Builder(SingleRecordActivity.this);
				completeDialog.setMessage("物品出入库记录已通过！");
				completeDialog.setTitle("成功通过提示");
				completeDialog.setPositiveButton("确认", new OnClickListener() { 
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
				completeDialog.create().show();
		    	
	    	}
			db.hideWaiting();
	        super.onPostExecute(result);
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_record_activity);
		
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		db = new SqlDBInterface(this);
		
		Intent intent = getIntent();
		
		id = intent.getIntExtra("id", 0);
		
		InitialTask initialTask = new InitialTask();
		initialTask.execute();
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_record, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		AlertDialog.Builder AlertDialog = new Builder(this);
		if (viewId == R.id.singleRecordDeleteButton){
			AlertDialog.setMessage("确认要删除该出入库记录？（一旦删除，将需要专员另行添加）");
			AlertDialog.setTitle("删除提示");
			AlertDialog.setPositiveButton("确认",new OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					DeleteTask deleteTask = new DeleteTask();
					deleteTask.execute();
					
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
		else{
			AlertDialog.setMessage("确认要通过该出入库记录？（一旦通过，库存将会相应更新）");
			AlertDialog.setTitle("通过提示");
			AlertDialog.setPositiveButton("确认",new OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					PassTask passTask = new PassTask();
					passTask.execute();
					
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
