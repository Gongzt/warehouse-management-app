package com.android.app.warehousemanagement;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.android.app.warehousemanagement.db.InnerDBExec;

public class InStockFragment extends Fragment
							 implements AdapterView.OnItemClickListener, View.OnClickListener, OnClickListener{
	
	private InnerDBExec db = null;
	private InputMethodManager inputManager = null;
	
	private AutoCompleteTextView instockEntryAutocom = null;
	private EditText instockAmountEdittext = null;
	private EditText instockUnitEdittext = null;
	private EditText instockRemarkEdittext = null;
	private Spinner instockTypeSpinner = null;
	private Spinner instockWarehouseSpinner = null;
	
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
    	
    	db = new InnerDBExec(getActivity());

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
    	
    	ArrayAdapter<String> entryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, db.currentSearchName(""));    
    	instockEntryAutocom.setAdapter(entryAdapter);  
    	instockEntryAutocom.setThreshold(1);
    	instockEntryAutocom.setOnItemClickListener(this);
    	
    	ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(), 
    			android.R.layout.simple_spinner_item, 
    			new String[] {getResources().getString(R.string.material), getResources().getString(R.string.product)});
    	typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	instockTypeSpinner.setAdapter(typeAdapter);
    	
    	ArrayAdapter<String> warehouseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, db.warehouseSelectAll());
    	warehouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	instockWarehouseSpinner.setAdapter(warehouseAdapter);

    	ImageButton actionbarCleanButton = (ImageButton)getActivity().getActionBar().getCustomView().findViewById(R.id.actionbarCleanButton);
    	actionbarCleanButton.setOnClickListener(this);
    	ImageButton actionbarSaveButton = (ImageButton)getActivity().getActionBar().getCustomView().findViewById(R.id.actionbarSaveButton);
    	actionbarSaveButton.setOnClickListener(this);
    	
    	inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
    }

    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    	String entryName = ((TextView) view).getText().toString();
    	String[] info = db.currentSelectByName(entryName);
    	
    	Spinner typeSpinner = (Spinner)getView().findViewById(R.id.instockTypeSpinner);
    	if (info[0].equals("原料"))
    		typeSpinner.setSelection(0);
    	else
    		typeSpinner.setSelection(1);
    	
    	EditText unitEditText = (EditText)getView().findViewById(R.id.instockUnitEdittext);
    	unitEditText.setText(info[1]);
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
			
			AlertDialog.setMessage("确认入库所填信息？");
			AlertDialog.setTitle("入库提示");
			AlertDialog.setPositiveButton("确认", new OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					inputManager.hideSoftInputFromWindow(
			                getActivity().getCurrentFocus().getWindowToken(),
			                InputMethodManager.HIDE_NOT_ALWAYS); 
					db.recordInsert(instockEntryAutocom.getText().toString(), 
							instockTypeSpinner.getSelectedItem().toString(), 
							instockWarehouseSpinner.getSelectedItem().toString(), 
							Integer.parseInt(instockAmountEdittext.getText().toString()), 
							instockUnitEdittext.getText().toString(), 
							"入库",
							"待审",
							instockRemarkEdittext.getText().toString(),
							Calendar.getInstance());
					
					instockEntryAutocom.setText("");
					instockAmountEdittext.setText("");
					instockUnitEdittext.setText("");
					instockRemarkEdittext.setText("");
					instockTypeSpinner.setSelection(0);
					instockWarehouseSpinner.setSelection(0);
					dialog.dismiss();
					
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
