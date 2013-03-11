package com.android.app.warehousemanagement.db;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class InnerDBExec{
	
	private InnerDB dbHelper;
	
	public InnerDBExec(Context context){
		dbHelper = new InnerDB(context);
	}
	
	//search the product(material) name containing the keyword
	//return order: entryname
	public String[] currentSearchName(String keyword){
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
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
			result[i] = c.getString(0);
			c.moveToNext();
		}

		readableDB.close();
		return result;
	}
	
	public String[] currentSelectByName(String name){
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE,
				InnerDBTable.Current.COLUMN_NAME_UNIT,
		};
		String selection = InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME + " = ?";
		String[] selectionArgs = {name};
		String groupBy = InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		Cursor c = readableDB.query(InnerDBTable.Current.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		
		String[] result = new String[2];
		c.moveToFirst();
		result[0] = c.getString(0);
		result[1] = c.getString(1);

		readableDB.close();
		return result;
	}
	
	//search the product(material) relative to the keyword
	//return order: entryid, entryname, type, amount
	public Cursor currentSearch(String keyword, String sortBy){
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				InnerDBTable.Current.COLUMN_NAME_ENTRY_ID,
				InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME,
				InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE,
				InnerDBTable.Current.COLUMN_NAME_UNIT,
				"SUM(" + InnerDBTable.Current.COLUMN_NAME_AMOUNT + ") AS " + InnerDBTable.Current.COLUMN_NAME_AMOUNT 
		};
		String selection = 
				InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME + " LIKE ? OR " +
				InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE + " LIKE ? OR " +
				InnerDBTable.Current.COLUMN_NAME_WAREHOUSE + " LIKE ?";
		String[] selectionArgs = {"%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%"};
		String groupBy = InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME + " , " + InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE;
		String having = null;
		String orderBy = sortBy;
		String limit = null;

		Cursor c = readableDB.query(InnerDBTable.Current.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();
		readableDB.close();
		return c;
	}
	
	//update the product(material) amount, if product(material) not exist, insert it
	public long currentUpdate (String name, String type, String warehouse, int change, String unit) {
		
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
			values.put(InnerDBTable.Current.COLUMN_NAME_UNIT, unit);
			
			writableDB.insert(InnerDBTable.Current.TABLE_NAME, "null", values);
		}
		else {
			ContentValues values = new ContentValues();
			values.put(InnerDBTable.Current.COLUMN_NAME_AMOUNT, amount+change);
			
			writableDB.update(InnerDBTable.Current.TABLE_NAME, values, selection, selectionArgs);
			
		}
		
		return id;
	}
	
	//search the record relative to the keyword
	//return order: recordid, entryname, type, warehouse, amount, inorout, status, remark, date
	public Cursor recordSearch(String keyword, String sortBy, String startDate, String endDate){
		
		if (startDate.equals("")){
			startDate = "190001010000";
		}
		if (endDate.equals("")){
			Calendar c = Calendar.getInstance();
			endDate = parseDate(c);
		}
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				InnerDBTable.Record.COLUMN_NAME_RECORD_ID,
				InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME,
				InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE,
				InnerDBTable.Record.COLUMN_NAME_WAREHOUSE,
				InnerDBTable.Record.COLUMN_NAME_AMOUNT,
				InnerDBTable.Record.COLUMN_NAME_UNIT,
				InnerDBTable.Record.COLUMN_NAME_INOROUT,
				InnerDBTable.Record.COLUMN_NAME_STATUS,
				InnerDBTable.Record.COLUMN_NAME_REMARK,
				"SUBSTR("+InnerDBTable.Record.COLUMN_NAME_DATE+",1,4) || '-' || " +
				"SUBSTR("+InnerDBTable.Record.COLUMN_NAME_DATE+",5,2) || '-' || " +
				"SUBSTR("+InnerDBTable.Record.COLUMN_NAME_DATE+",7,2) || ' ' || " +
				"SUBSTR("+InnerDBTable.Record.COLUMN_NAME_DATE+",9,2) || ':' || " +
				"SUBSTR("+InnerDBTable.Record.COLUMN_NAME_DATE+",11,2) AS " + InnerDBTable.Record.COLUMN_NAME_DATE 
		};
		String selection = "(" + InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME + " LIKE ? OR " +
				InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE + " LIKE ? OR " +
				InnerDBTable.Record.COLUMN_NAME_REMARK + " LIKE ? OR " +
				InnerDBTable.Record.COLUMN_NAME_STATUS + " LIKE ? OR " +
				InnerDBTable.Record.COLUMN_NAME_WAREHOUSE + " LIKE ? OR " +
				InnerDBTable.Record.COLUMN_NAME_INOROUT + " LIKE ? ) AND " + 
				InnerDBTable.Record.COLUMN_NAME_DATE + " > ? AND " +
				InnerDBTable.Record.COLUMN_NAME_DATE + " < ?";
		String[] selectionArgs = {"%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%", startDate, endDate};
		String groupBy = null;
		String having = null;
		String orderBy = sortBy;
		String limit = null;
		
		Cursor c = readableDB.query(InnerDBTable.Record.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();

		readableDB.close();
		return c;
	}
	
	//insert a new record into the Record table
	public void recordInsert(String entryName, String entryType, String warehouse, int amount, String unit, String inOrOut, String status, String remark, Calendar calendar){
		SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME, entryName);
		values.put(InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE, entryType);
		values.put(InnerDBTable.Record.COLUMN_NAME_WAREHOUSE, warehouse);
		values.put(InnerDBTable.Record.COLUMN_NAME_AMOUNT, amount);
		values.put(InnerDBTable.Record.COLUMN_NAME_UNIT, unit);
		values.put(InnerDBTable.Record.COLUMN_NAME_INOROUT, inOrOut);
		values.put(InnerDBTable.Record.COLUMN_NAME_STATUS, status);
		values.put(InnerDBTable.Record.COLUMN_NAME_REMARK, remark);
		values.put(InnerDBTable.Record.COLUMN_NAME_DATE, parseDate(calendar));
		
		writableDB.insert(InnerDBTable.Record.TABLE_NAME, "null", values);
		writableDB.close();
	}

	//update the record status and date
	public void recordUpdate(String id){

		SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
		
		String selection = InnerDBTable.Record.COLUMN_NAME_RECORD_ID + " = ?";
		String[] selectionArgs = {""+id};
		
		Calendar calendar = Calendar.getInstance();
		ContentValues values = new ContentValues();
		values.put(InnerDBTable.Record.COLUMN_NAME_STATUS, "ÉóÅú");
		values.put(InnerDBTable.Record.COLUMN_NAME_DATE, parseDate(calendar));
		
		writableDB.update(InnerDBTable.Record.TABLE_NAME, values, selection, selectionArgs);
		
		writableDB.close();
	}
	
	//delete a record
	public void recordDelete(String id){
		
		SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
		
		String selection = InnerDBTable.Record.COLUMN_NAME_RECORD_ID + " = ?";
		String[] selectionArgs = {""+id};
		
		writableDB.delete(InnerDBTable.Record.TABLE_NAME, selection, selectionArgs);
		
		writableDB.close();
	}
	
	public String[] warehouseSelectAll(){
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				InnerDBTable.Warehouse.COLUMN_NAME_WAREHOUSE_NAME
		};
		String selection = "";
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = "";
		String limit = null;
		
		Cursor c = readableDB.query(InnerDBTable.Warehouse.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();

		String[] result = new String[c.getCount()];
		for (int i = 0; i<c.getCount(); i++){
			result[i] = c.getString(0);
			c.moveToNext();
		}
		
		readableDB.close();
		return result;
	}
	
	private String parseDate(Calendar calendar){
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		String date = "" + year;
		if (month<10) date += "0" + month;
		else date += month;
		if (day<10) date += "0" + day;
		else date += day;
		if (hour<10) date += "0" + hour;
		else date += hour;
		if (minute<10) date += "0" + minute;
		else date += minute;
		
		return date;
	}
	
	public Calendar parseDate(String date){
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4,6));
		int day = Integer.parseInt(date.substring(6, 8));
		int hour = Integer.parseInt(date.substring(8, 10));
		int minute = Integer.parseInt(date.substring(10, 12));
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, hour, minute);
		
		return calendar;
	}

}
