package com.android.app.warehousemanagement.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class InnerDBExec{
	
	private InnerDB dbHelper;
	
	public InnerDBExec(Context context){
		dbHelper = new InnerDB(context);
	}
	
	public ArrayList<HashMap<String, Object>> currentSelectAll(){
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				InnerDBTable.Current.COLUMN_NAME_ENTRY_ID,
				InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME,
				InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE,
				"SUM(" + InnerDBTable.Current.COLUMN_NAME_AMOUNT + ")" 
		};
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME + " , " + InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		Cursor c = readableDB.query(InnerDBTable.Current.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		c.moveToFirst();
		for (int i = 0; i<c.getCount(); i++){
			HashMap<String, Object> temp = new HashMap<String, Object>();
			temp.put(InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME, c.getString(1));
			temp.put(InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE, c.getString(2));
			temp.put(InnerDBTable.Current.COLUMN_NAME_AMOUNT, c.getInt(3));
			result.add(temp);
			c.moveToNext();
		}

		readableDB.close();
		return result;
	}
	
	public String[] currentSearchName(String keyword){
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				InnerDBTable.Current.COLUMN_NAME_ENTRY_ID,
				InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME, 
		};
		String selection = InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME + " LIKE ?";
		String[] selectionArgs = {"%"+keyword+"%"};
		String groupBy = InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		Cursor c = readableDB.query(InnerDBTable.Current.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		
		String[] result = new String[c.getCount()];
		c.moveToFirst();
		for (int i = 0; i<c.getCount(); i++){
			result[i] = c.getString(1);
			c.moveToNext();
		}

		readableDB.close();
		return result;
	}
	
	public ArrayList<HashMap<String, Object>> currentSearch(String keyword){
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				InnerDBTable.Current.COLUMN_NAME_ENTRY_ID,
				InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME,
				InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE,
				"SUM(" + InnerDBTable.Current.COLUMN_NAME_AMOUNT + ")" 
		};
		String selection = 
				InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME + " LIKE ? OR " +
				InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE + " LIKE ? OR " +
				InnerDBTable.Current.COLUMN_NAME_WAREHOUSE + " LIKE ?";
		String[] selectionArgs = {"%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%"};
		String groupBy = InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME + " , " + InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		Cursor c = readableDB.query(InnerDBTable.Current.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		c.moveToFirst();
		for (int i = 0; i<c.getCount(); i++){
			HashMap<String, Object> temp = new HashMap<String, Object>();
			temp.put(InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME, c.getString(1));
			temp.put(InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE, c.getString(2));
			temp.put(InnerDBTable.Current.COLUMN_NAME_AMOUNT, c.getInt(3));
			result.add(temp);
			c.moveToNext();
		}

		readableDB.close();
		return result;
	}
	
	public long currentUpdate (String name, String type, String warehouse, int change) {
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				InnerDBTable.Current.COLUMN_NAME_ENTRY_ID,
				InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME,
				InnerDBTable.Current.COLUMN_NAME_AMOUNT
		};
		String selection = 
				InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME + " = ? AND " +
				InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE + " = ? AND " +
				InnerDBTable.Current.COLUMN_NAME_WAREHOUSE + " = ?";
		String[] selectionArgs = {name, type, warehouse};
		String groupBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		Cursor c = readableDB.query(InnerDBTable.Current.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		long id = c.getCount();
		int amount = 0;
		if (id>0) {
			c.moveToFirst();
			id = c.getLong(0);
			amount = c.getInt(2);
		}
		else {
			id = -1;
		}
		readableDB.close();
		
		SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
		
		
		if (id == -1){
			ContentValues values = new ContentValues();
			values.put(InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME, name);
			values.put(InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE, type);
			values.put(InnerDBTable.Current.COLUMN_NAME_WAREHOUSE, warehouse);
			values.put(InnerDBTable.Current.COLUMN_NAME_AMOUNT, amount+change);
			
			writableDB.insert(InnerDBTable.Current.TABLE_NAME, "null", values);
		}
		else {
			ContentValues values = new ContentValues();
			values.put(InnerDBTable.Current.COLUMN_NAME_AMOUNT, amount+change);
			
			writableDB.update(InnerDBTable.Current.TABLE_NAME, values, selection, selectionArgs);
			
		}
		
		return id;
	}
	
	public String[][] RecordSelectAll(){
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				InnerDBTable.Record.COLUMN_NAME_RECORD_ID,
				InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME,
				InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE,
				InnerDBTable.Record.COLUMN_NAME_WAREHOUSE,
				InnerDBTable.Record.COLUMN_NAME_AMOUNT,
				InnerDBTable.Record.COLUMN_NAME_INOROUT,
				InnerDBTable.Record.COLUMN_NAME_STATUS,
				InnerDBTable.Record.COLUMN_NAME_REMARK,
				InnerDBTable.Record.COLUMN_NAME_DATE, 
		};
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		Cursor c = readableDB.query(InnerDBTable.Record.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		
		String[][] result = new String[c.getCount()][8];
		c.moveToFirst();
		for (int i = 0; i<c.getCount(); i++){
			result[i][0] = c.getString(1);
			result[i][1] = c.getString(2);
			result[i][2] = c.getString(3);
			result[i][3] = c.getString(4);
			result[i][4] = c.getString(5);
			result[i][5] = c.getString(6);
			result[i][6] = c.getString(7);
			result[i][7] = c.getString(8);
			c.moveToNext();
		}

		readableDB.close();
		return result;
	}
	
	public void insertEntry(String entryName, String entryType, String warehouse, int amount, String inOrOut, String status, String remark, Date date){
		SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME, entryName);
		values.put(InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE, entryType);
		values.put(InnerDBTable.Record.COLUMN_NAME_WAREHOUSE, warehouse);
		values.put(InnerDBTable.Record.COLUMN_NAME_AMOUNT, amount);
		values.put(InnerDBTable.Record.COLUMN_NAME_INOROUT, inOrOut);
		values.put(InnerDBTable.Record.COLUMN_NAME_STATUS, status);
		values.put(InnerDBTable.Record.COLUMN_NAME_REMARK, remark);
		values.put(InnerDBTable.Record.COLUMN_NAME_DATE, date.toString());
		
		writableDB.insert(InnerDBTable.Record.TABLE_NAME, "null", values);
		writableDB.close();
	}
	
	public String[] readEntry(){
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				InnerDBTable.Current.COLUMN_NAME_ENTRY_ID,
				InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME,
				InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE,
				InnerDBTable.Current.COLUMN_NAME_AMOUNT
		};
		
		Cursor c = readableDB.query(InnerDBTable.Current.TABLE_NAME, projection, null, null, null, null, null);
		
		String[] result = new String[c.getCount()];
		c.moveToFirst();
		int i = 0;
		while (!c.isLast()){
			result[i] = c.getString(1);
			c.moveToNext();
			i++;
		}
		result[i] = c.getString(1);
		
		
		readableDB.close();
		
		return result;
	}

}
