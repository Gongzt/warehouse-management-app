package com.android.app.warehousemanagement.db;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.app.warehousemanagement.db.InnerDBExec;

public class test extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		
//		testRecordSelectAll();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendMessage(View view){
//		EditText editText = (EditText) findViewById(R.id.edit_message);
//		TextView text= (TextView) findViewById(R.id.text_message);
		
//		test currentSearch
//		InnerDBExec db = new InnerDBExec(this);
//    	String[][] result = db.currentSearch(editText.getText().toString());
//    	
//    	String resultStr = "";
//    	for (int i=0; i<result.length; i++)
//    		resultStr += result[i][0] + " " + result[i][1] + " " + result[i][2] + "\n";
//    	text.setText(resultStr);
		
//		test currentSearchName
//		InnerDBExec db = new InnerDBExec(this);
//    	String[] result = db.currentSearchName(editText.getText().toString());
//    	
//    	String resultStr = "";
//    	for (int i=0; i<result.length; i++)
//    		resultStr += result[i] + "\n";
//    	text.setText(resultStr);
		
//		test currentUpdate
//		InnerDBExec db = new InnerDBExec(this);
//		db.currentUpdate(editText.getText().toString(), "鍘熸枡", "鎬讳粨搴�, -1);
//		testCurrentSelectAll();
		
//		test recordSelectAll and recordInsert
//		InnerDBExec db = new InnerDBExec(this);
//		Calendar calendar = Calendar.getInstance();
//		db.recordInsert(editText.getText().toString(), "产品", "总仓库", 5 , "入库", "待审", "顶你个肺", calendar);
//		testRecordSelectAll();
		
//		test recordSearch
//		InnerDBExec db = new InnerDBExec(this);
//    	String[][] result = db.recordSearch(editText.getText().toString());
//    	
//    	String resultStr = "";
//    	for (int i=0; i<result.length; i++) {
//    		for (int j=0; j<9; j++) 
//    			resultStr += result[i][j] + " ";
//    		resultStr += "\n";
//    	}
//    	text.setText(resultStr);
		
//		test recordUpdate
//		InnerDBExec db = new InnerDBExec(this);
//    	db.recordUpdate(editText.getText().toString());
//    	
//    	testRecordSelectAll();
		
//		InnerDBExec db = new InnerDBExec(this);
//    	db.recordDelete(editText.getText().toString());
//    	
//    	testRecordSelectAll();
	}
	
	public void testCurrentSelectAll(){
//		Test currentSelectAll method
//		InnerDBExec db = new InnerDBExec(this);
//		String[][] result = db.currentSelectAll(); 
//		
//		String resultStr = "";
//		for (int i = 0; i<result.length; i++){
//			resultStr += result[i][0] + " " + result [i][1] + " " + result[i][2] +"\n";
//		}
//		TextView text = (TextView)findViewById(R.id.text_message);
//		text.setText(resultStr);
	}
	
	public void testRecordSelectAll(){
//		Test currentSelectAll method
//		InnerDBExec db = new InnerDBExec(this);
//		String[][] result = db.recordSelectAll(); 
//		
//		String resultStr = "";
//		for (int i = 0; i<result.length; i++){
//			resultStr += result[i][0] + " " + result [i][1] + " " + result[i][2] + " " + result[i][3] + " " + result[i][4] + " " + result[i][5] + " " + result[i][6] + " " + result[i][7] + " " + result[i][8] +"\n";
//		}
//		TextView text = (TextView)findViewById(R.id.text_message);
//		text.setText(resultStr);
	}
	
	public void testRecordSelectByDate(){
//		Test currentSelectAll method
//		InnerDBExec db = new InnerDBExec(this);
//		String[][] result = db.recordSelectByDate("201205060000","201207050000"); 
//		
//		String resultStr = "";
//		for (int i = 0; i<result.length; i++){
//			resultStr += result[i][0] + " " + result [i][1] + " " + result[i][2] + " " + result[i][3] + " " + result[i][4] + " " + result[i][5] + " " + result[i][6] + " " + result[i][7] + " " + result[i][8] +"\n";
//		}
//		TextView text = (TextView)findViewById(R.id.text_message);
//		text.setText(resultStr);
	}

}

