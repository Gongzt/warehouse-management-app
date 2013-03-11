package com.android.app.warehousemanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.app.warehousemanagement.db.InnerDBExec;

public class InStockFragment extends Fragment
							 implements AdapterView.OnItemClickListener{
	
	private InnerDBExec db = null;
	private String[] entryName = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	db = new InnerDBExec(getActivity());

    	return inflater.inflate(R.layout.instock_fragment, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
    	ArrayAdapter<String> entryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, db.currentSearchName(""));  
    	AutoCompleteTextView entryAutoComplete = (AutoCompleteTextView) getView().findViewById(R.id.instockEntryAutocom);  
    	entryAutoComplete.setAdapter(entryAdapter);  
    	entryAutoComplete.setThreshold(1);
    	entryAutoComplete.setOnItemClickListener(this);
    	
    	Spinner typeSpinner = (Spinner)getView().findViewById(R.id.instockTypeSpinner);
    	ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[] {"原料","产品"});
    	typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	typeSpinner.setAdapter(typeAdapter);
    	
    	Spinner warehouseSpinner = (Spinner)getView().findViewById(R.id.instockWarehouseSpinner);
    	ArrayAdapter<String> warehouseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, db.warehouseSelectAll());
    	warehouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	warehouseSpinner.setAdapter(warehouseAdapter);

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
}
