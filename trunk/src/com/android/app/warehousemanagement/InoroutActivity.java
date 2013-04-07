package com.android.app.warehousemanagement;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.app.warehousemanagement.db.InnerDBExec;

public class InoroutActivity extends Activity 
                             implements View.OnClickListener, AdapterView.OnItemSelectedListener {

	private String name;
	private String type;
	private String amount;
	private int usableAmount;
	private String unit;
	private String warehouse;
	private String remark;
	private int from;
	private InnerDBExec db = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inorout_activity);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		db = new InnerDBExec(this);
		
		Intent intent = getIntent();
		
		name = intent.getStringExtra("name");
		type = intent.getStringExtra("type");
		unit = intent.getStringExtra("unit");
		from = intent.getIntExtra("from", 0);
		
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.inorout_actionbar, null);
		
		actionBar.setCustomView(v);
		
		((ImageButton)actionBar.getCustomView().findViewById(R.id.inoroutConfirmButton)).setOnClickListener(this);
		
		((TextView)findViewById(R.id.inoroutEntryTextView)).setText(name);
		if (type.equals(getResources().getString(R.string.material)))
			((ImageView)findViewById(R.id.inoroutTypeImage)).setBackgroundResource(R.drawable.icon_material);
		else
			((ImageView)findViewById(R.id.inoroutTypeImage)).setBackgroundResource(R.drawable.icon_product);
		((TextView)findViewById(R.id.inoroutUnitTextview)).setText(unit);
		
		Spinner inoroutWarehouseSpinner = (Spinner)findViewById(R.id.inoroutWarehouseSpinner);
		 
		ArrayAdapter<String> warehouseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, db.warehouseSelectAll());
		warehouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		inoroutWarehouseSpinner.setAdapter(warehouseAdapter);

		if (from == R.id.singleCurrentInstockButton){
			((TextView)actionBar.getCustomView().findViewById(R.id.inoroutTitleTextView)).setText("入库申请");
			((LinearLayout)findViewById(R.id.inoroutUsableAmountLayout)).setVisibility(View.GONE);
		}
		else {
			((TextView)actionBar.getCustomView().findViewById(R.id.inoroutTitleTextView)).setText("出库申请");
			inoroutWarehouseSpinner.setOnItemSelectedListener(this);
			updateUsableAmount(inoroutWarehouseSpinner.getSelectedItem().toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inorout, menu);
		return true;
	}

	private void updateUsableAmount(String warehouse){
		usableAmount = db.entrySelectUsableAmount(name, type, warehouse);
		((TextView)findViewById(R.id.inoroutUsableAmountTextView)).setText(usableAmount+unit);
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
		amount = ((EditText)findViewById(R.id.inoroutAmountEdittext)).getText().toString();
		warehouse = ((Spinner)findViewById(R.id.inoroutWarehouseSpinner)).getSelectedItem().toString();
		remark = ((EditText)findViewById(R.id.inoroutRemarkEdittext)).getText().toString();
		
		if (amount.equals("") || Integer.parseInt(amount) == 0) {
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
			if (from == R.id.singleCurrentInstockButton) {
				AlertDialog.setMessage("确认要入库一下物品？\n" +
									   "名称： " + name + "(" + type + ")\n" +
									   "数量： " + amount + unit + "\n" +
									   "仓库： " + warehouse);
									   
				AlertDialog.setTitle("入库提示");
				AlertDialog.setPositiveButton("确认",new OnClickListener() { 
					@Override
					public void onClick(DialogInterface dialog, int which) {
						db.recordInsert(name, 
										type, 
										warehouse, 
										Integer.parseInt(amount), 
										unit, 
										"入库", "待审", 
										remark, 
										Calendar.getInstance());
						dialog.dismiss();
						
						AlertDialog.Builder completeDialog = new Builder(InoroutActivity.this);
						completeDialog.setMessage("物品入库登记成功");
						completeDialog.setTitle("登记完成提示");
						completeDialog.setPositiveButton("确认", new OnClickListener() { 
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								finish();
							}
						});
						completeDialog.create().show();
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
				if (Integer.parseInt(amount) > this.usableAmount) {
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
							   "名称： " + name + "(" + type + ")\n" +
							   "数量： " + amount + unit + "\n" +
							   "仓库： " + warehouse);
							   
					AlertDialog.setTitle("出库提示");
					AlertDialog.setPositiveButton("确认",new OnClickListener() { 
						@Override
						public void onClick(DialogInterface dialog, int which) {
							db.recordInsert(name, 
											type, 
											warehouse, 
											Integer.parseInt(amount), 
											unit, 
											"出库", "待审", 
											remark, 
											Calendar.getInstance());
							dialog.dismiss();
							
							AlertDialog.Builder completeDialog = new Builder(InoroutActivity.this);
							completeDialog.setMessage("物品出库登记成功");
							completeDialog.setTitle("登记完成提示");
							completeDialog.setPositiveButton("确认", new OnClickListener() { 
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									finish();
								}
							});
							completeDialog.create().show();
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
		String warehouse = ((Spinner)findViewById(R.id.inoroutWarehouseSpinner)).getSelectedItem().toString();
		updateUsableAmount(warehouse);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}
