package com.android.app.warehousemanagement;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.android.app.warehousemanagement.db.InnerDBExec;

public class SingleRecordActivity extends Activity {

	private InnerDBExec db = null;
	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_record_activity);
		
		db = new InnerDBExec(this);
		
		Intent intent = getIntent();
		Log.e("fuck",id+"");
		id = intent.getStringExtra("id");
		Cursor c = db.recordSelectById(id);
		((TextView)findViewById(R.id.haha)).setText(c.getString(1)+c.getString(2)+c.getString(3)+c.getString(4)+c.getString(5)+c.getString(6));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_record, menu);
		return true;
	}

}
