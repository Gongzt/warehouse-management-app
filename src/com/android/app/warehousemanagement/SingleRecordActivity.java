package com.android.app.warehousemanagement;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.app.warehousemanagement.db.InnerDBExec;

public class SingleRecordActivity extends Activity 
                                  implements View.OnClickListener{

	private InnerDBExec db = null;
	private String id;
	private String name;
	private String type;
	private String warehouse;
	private int change;
	private String unit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_record_activity);
		
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		db = new InnerDBExec(this);
		
		Intent intent = getIntent();
		
		id = intent.getStringExtra("id");
		Cursor c = db.recordSelectById(id);	
		
		name = c.getString(1);
		type = c.getString(2);
		warehouse = c.getString(3);
		if (c.getString(6).equals(getResources().getString(R.string.instock)))
			change = Integer.parseInt(c.getString(4));
		else
			change = -1*Integer.parseInt(c.getString(4));
		unit = c.getString(5);
		
		ActionBar actionBar = getActionBar();
		
		if (c.getString(7).equals(getResources().getString(R.string.passed))){
			actionBar.setTitle(name);
		}
		else {
			actionBar.setDisplayShowCustomEnabled(true);
			
			LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(R.layout.single_record_actionbar, null);
			
			actionBar.setCustomView(v);
			
			((TextView)actionBar.getCustomView().findViewById(R.id.singleRecordTitle)).setText(c.getString(1));
			((ImageView)actionBar.getCustomView().findViewById(R.id.singleRecordPassButton)).setOnClickListener(this);
			((ImageView)actionBar.getCustomView().findViewById(R.id.singleRecordDeleteButton)).setOnClickListener(this);
		}
		
		if (c.getString(2).equals(getResources().getString(R.string.material)))
			((ImageView)findViewById(R.id.singleRecordTypeImage)).setBackgroundResource(R.drawable.icon_material);
		else
			((ImageView)findViewById(R.id.singleRecordTypeImage)).setBackgroundResource(R.drawable.icon_product);
		
		((TextView)findViewById(R.id.singleRecordEntryTextView)).setText(c.getString(1));
		((TextView)findViewById(R.id.singleRecordWarehouseTextView)).setText(c.getString(3));
		
		if (c.getString(6).equals(getResources().getString(R.string.instock)))
			((ImageView)findViewById(R.id.singleRecordInoroutImage)).setBackgroundResource(R.drawable.icon_instock);
		else
			((ImageView)findViewById(R.id.singleRecordInoroutImage)).setBackgroundResource(R.drawable.icon_outstock);
		
		((TextView)findViewById(R.id.singleRecordDateTextView)).setText(c.getString(9));

		if (c.getString(7).equals(getResources().getString(R.string.passed)))
			((ImageView)findViewById(R.id.singleRecordStatusImage)).setBackgroundResource(R.drawable.icon_pass);
		else
			((ImageView)findViewById(R.id.singleRecordStatusImage)).setBackgroundResource(R.drawable.icon_pending);
		
		((TextView)findViewById(R.id.singleRecordAmountTextView)).setText(c.getString(4)+c.getString(5));
		((TextView)findViewById(R.id.singleRecordRemarkTextView)).setText("       "+c.getString(8));
		
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
					db.recordDelete(id);
					finish();
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
					db.currentUpdate(name, type, warehouse, change, unit);
					db.recordUpdate(id);
					dialog.dismiss();
					
					finish();
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
