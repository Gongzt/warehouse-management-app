package com.android.app.warehousemanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.app.warehousemanagement.db.InnerDBExec;

public class SettingFragment extends ListFragment{
	private String[] items;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	items = new String[9];
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		
		InnerDBExec db = new InnerDBExec(getActivity());
		Cursor c = db.recordSelectAll();
		items = new String[9];
		for (int i=0; i<c.getCount(); i++){
			items[i] = c.getString(i);
			c.moveToNext();
		}
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
    	return inflater.inflate(R.layout.current_fragment, container, false);
    }
    
    @Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		Toast.makeText(getActivity(), "You clicked " + items[position], Toast.LENGTH_SHORT).show();
	}
}
