package com.android.app.warehousemanagement.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.android.app.warehousemanagement.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class InnerDBExec{
	
	private InnerDB dbHelper;
	
	public InnerDBExec(Context context){
		dbHelper = new InnerDB(context);
	}
	
	/* Select all the entry information
	 * INPUT: N/A 
	 * OUTPUT: entry.Name, entry.Type entry.Unit
	 *		   IN ArrayList<HashMap<String, Object>> 
	 */ 
	public ArrayList<HashMap<String, Object>> entrySelectAll(){
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
			SqlDBTable.Entry.COLUMN_NAME_ID,
			SqlDBTable.Entry.COLUMN_NAME_NAME,
			SqlDBTable.Entry.COLUMN_NAME_TYPE,
			SqlDBTable.Entry.COLUMN_NAME_UNIT,
		};
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		Cursor c = readableDB.query(SqlDBTable.Entry.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		c.moveToFirst();
		for (int i = 0; i<c.getCount(); i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(SqlDBTable.Entry.COLUMN_NAME_ID, c.getInt(0));
			map.put(SqlDBTable.Entry.COLUMN_NAME_NAME, c.getString(1));
			map.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, R.drawable.icon_material);
			map.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, R.drawable.icon_product);
			map.put(SqlDBTable.Entry.COLUMN_NAME_UNIT, c.getString(3));
			
			arrayList.add(map);
			c.moveToNext();
		}

		readableDB.close();
		return arrayList;
	}
		
	/* Select all the current entry inside the warehouse with specific name and type
	 * INPUT: String name - entry name
	 * 		  int type - entry type
	 * OUTPUT: entry._id, current.Amount, warehouse.Warehouse_Name, entry.Unit
	 *         IN ArrayList<HashMap<String, Object>>
	 */
	public ArrayList<HashMap<String, Object>> currentSelectByNameAndType(String name, int type){

		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				SqlDBTable.Current.COLUMN_NAME_ENTRY_ID,
				SqlDBTable.Current.COLUMN_NAME_AMOUNT,
				SqlDBTable.Warehouse.COLUMN_NAME_NAME,
				SqlDBTable.Entry.COLUMN_NAME_UNIT
		};
		String table = "(" + SqlDBTable.Current.TABLE_NAME + " LEFT JOIN " + SqlDBTable.Entry.TABLE_NAME + " ON " +
						SqlDBTable.Current.TABLE_NAME + "." + SqlDBTable.Current.COLUMN_NAME_ENTRY_ID + "=" + 
						SqlDBTable.Entry.TABLE_NAME + "." + SqlDBTable.Entry.COLUMN_NAME_ID+ ")" +
						" LEFT JOIN " + SqlDBTable.Warehouse.TABLE_NAME + " ON " + 
						SqlDBTable.Current.TABLE_NAME + "." + SqlDBTable.Current.COLUMN_NAME_WAREHOUSE_ID + "=" + 
						SqlDBTable.Warehouse.TABLE_NAME + "." + SqlDBTable.Warehouse.COLUMN_NAME_ID;
		String selection = SqlDBTable.Entry.COLUMN_NAME_NAME + " = ? AND "+ SqlDBTable.Entry.COLUMN_NAME_TYPE + "= " + type;
		String[] selectionArgs = {name};
		String groupBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		Cursor c = readableDB.query(table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();
		
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		c.moveToFirst();
		for (int i = 0; i<c.getCount(); i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(SqlDBTable.Current.COLUMN_NAME_ENTRY_ID, c.getInt(0));
			map.put(SqlDBTable.Current.COLUMN_NAME_AMOUNT, c.getInt(1));
			map.put(SqlDBTable.Warehouse.COLUMN_NAME_NAME, c.getString(2));
			map.put(SqlDBTable.Entry.COLUMN_NAME_UNIT, c.getString(3));
			
			arrayList.add(map);
			c.moveToNext();
		}
		
		readableDB.close();
		return arrayList;
	}
	
	/* Search the entry in current warehouse with specific keyword sorted in the specific order
	 * INPUT: String keyword - entry name the may containing this keyword
	 * 	      String sortby - sort in specific order
	 * OUTPUT: current._id, entry.Name, entry.Type, entry.Unit, SUM(current.Amount)
	 *         IN ArrayList<HashMap<String, Object>>
	 */
	public ArrayList<HashMap<String, Object>> currentSearch(String keyword, String sortBy){
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();

		String[] projection = {
				SqlDBTable.Current.TABLE_NAME + "." + SqlDBTable.Current.COLUMN_NAME_ID,
				SqlDBTable.Entry.COLUMN_NAME_NAME,
				SqlDBTable.Entry.COLUMN_NAME_TYPE,
				SqlDBTable.Entry.COLUMN_NAME_UNIT,
				"SUM(" + SqlDBTable.Current.COLUMN_NAME_AMOUNT + ") AS " + SqlDBTable.Current.COLUMN_NAME_AMOUNT 
		};
		String table = SqlDBTable.Current.TABLE_NAME + " LEFT JOIN " + SqlDBTable.Entry.TABLE_NAME + " ON " +
				SqlDBTable.Current.TABLE_NAME + "." + SqlDBTable.Current.COLUMN_NAME_ENTRY_ID + "=" + 
				SqlDBTable.Entry.TABLE_NAME + "." + SqlDBTable.Entry.COLUMN_NAME_ID;
		String selection = SqlDBTable.Entry.COLUMN_NAME_NAME + " LIKE ?";
		String[] selectionArgs = {"%"+keyword+"%"};
		String groupBy = SqlDBTable.Current.COLUMN_NAME_ENTRY_ID;
		String having = null;
		String orderBy = sortBy;
		String limit = null;

		Cursor c = readableDB.query(table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();
		
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		c.moveToFirst();
		for (int i = 0; i<c.getCount(); i++){ 
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(SqlDBTable.Entry.COLUMN_NAME_ID, c.getInt(0));
			map.put(SqlDBTable.Entry.COLUMN_NAME_NAME, c.getString(1));
			if (c.getInt(2) == 0) 
				map.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, R.drawable.icon_material);
			else
				map.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, R.drawable.icon_product);
			map.put(SqlDBTable.Entry.COLUMN_NAME_UNIT, c.getString(3));
			map.put(SqlDBTable.Current.COLUMN_NAME_AMOUNT, c.getInt(4));
			
			arrayList.add(map);
			c.moveToNext();
		}
		
		readableDB.close();
		return arrayList;
	}
	
	/* Search record in specific keyword between an interval of date sorted in specific order
	 * INPUT: String keyword - search name, warehouse and remark that may containing keyword
	 *        String sortBy - sort the list of output in specific order
	 *        String startDate, endDate - the interval of the date
	 * OUTPUT:  record._id, entry.Name, entry.Type,  record.Amount, entry.Unit, record.InOrOut, record.Status, record.Date
	 *          IN ArrayList<HashMap<String, Object>>
	 */
	public ArrayList<HashMap<String, Object>> recordSearch(String keyword, String sortBy, String startDate, String endDate){
		if (startDate.equals("")){
			startDate = "190001010000";
		}
		if (endDate.equals("")){
			Calendar c = Calendar.getInstance();
			endDate = parseDate(c).substring(0,6);
		}
		endDate += "2359";
		
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				SqlDBTable.Record.TABLE_NAME + "." + SqlDBTable.Record.COLUMN_NAME_ID,
				SqlDBTable.Entry.COLUMN_NAME_NAME,
				SqlDBTable.Entry.COLUMN_NAME_TYPE,
				SqlDBTable.Record.COLUMN_NAME_AMOUNT,
				SqlDBTable.Entry.COLUMN_NAME_UNIT,
				SqlDBTable.Record.COLUMN_NAME_INOROUT,
				SqlDBTable.Record.COLUMN_NAME_STATUS,
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",1,4) || '-' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",5,2) || '-' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",7,2) || ' ' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",9,2) || ':' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",11,2) AS " + SqlDBTable.Record.COLUMN_NAME_DATE 
		};
		String table = "(" + SqlDBTable.Record.TABLE_NAME + " LEFT JOIN " + SqlDBTable.Entry.TABLE_NAME + " ON " +
				SqlDBTable.Record.TABLE_NAME + "." + SqlDBTable.Record.COLUMN_NAME_ENTRY_ID + "=" + 
				SqlDBTable.Entry.TABLE_NAME + "." + SqlDBTable.Entry.COLUMN_NAME_ID+ ")" +
				" LEFT JOIN " + SqlDBTable.Warehouse.TABLE_NAME + " ON " + 
				SqlDBTable.Record.TABLE_NAME + "." + SqlDBTable.Record.COLUMN_NAME_WAREHOUSE_ID + "=" + 
				SqlDBTable.Warehouse.TABLE_NAME + "." + SqlDBTable.Warehouse.COLUMN_NAME_ID;
		String selection = "(" + SqlDBTable.Entry.COLUMN_NAME_NAME + " LIKE ? OR " +
				SqlDBTable.Record.COLUMN_NAME_REMARK + " LIKE ? OR " +
				SqlDBTable.Warehouse.COLUMN_NAME_NAME + " LIKE ?) AND " + 
				SqlDBTable.Record.COLUMN_NAME_DATE + " > ? AND " +
				SqlDBTable.Record.COLUMN_NAME_DATE + " < ?";
		String[] selectionArgs = {"%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%", startDate, endDate};
		String groupBy = null;
		String having = null;
		String orderBy = null;
		if (sortBy.equals(""))
			orderBy = SqlDBTable.Record.COLUMN_NAME_DATE + " DESC";
		else
			orderBy = sortBy + " , " + SqlDBTable.Record.COLUMN_NAME_DATE + " DESC";
		String limit = null;
		
		Cursor c = readableDB.query(table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();

		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		c.moveToFirst();
		
		for (int i = 0; i<c.getCount(); i++){ 
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(SqlDBTable.Record.COLUMN_NAME_ID, c.getInt(0));
			map.put(SqlDBTable.Entry.COLUMN_NAME_NAME, c.getString(1));
			if (c.getInt(2) == 0)
				map.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, R.drawable.icon_material);
			else
				map.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, R.drawable.icon_product);
			map.put(SqlDBTable.Record.COLUMN_NAME_AMOUNT, c.getInt(3));
			map.put(SqlDBTable.Entry.COLUMN_NAME_UNIT, c.getString(4));
			if (c.getInt(5) == 0)
				map.put(SqlDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_instock);
			else
				map.put(SqlDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_outstock);
			if (c.getInt(6) == 0)
				map.put(SqlDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pending);
			else
				map.put(SqlDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pass);
			map.put(SqlDBTable.Record.COLUMN_NAME_DATE, c.getString(7));
			
			
			arrayList.add(map);
			c.moveToNext();
		}
		
		readableDB.close();
		return arrayList;
	}
	
	/* Select record with specific ID
	 * INPUT: int id - the id of the record
	 * OUTPUT: record._id, entry.Name, entry.Type, warehouse.Warehouse_Name, record.Amount, entry.Unit, record.InOrOut,
	 *         record.Status, record.Remark, record.Date
	 *         IN HashMap<String, Object>
	 */
	public HashMap<String, Object> recordSelectById (int id){

		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				SqlDBTable.Record.TABLE_NAME + "." + SqlDBTable.Record.COLUMN_NAME_ID,
				SqlDBTable.Entry.COLUMN_NAME_NAME,
				SqlDBTable.Entry.COLUMN_NAME_TYPE,
				SqlDBTable.Warehouse.COLUMN_NAME_NAME,
				SqlDBTable.Record.COLUMN_NAME_AMOUNT,
				SqlDBTable.Entry.COLUMN_NAME_UNIT,
				SqlDBTable.Record.COLUMN_NAME_INOROUT,
				SqlDBTable.Record.COLUMN_NAME_STATUS,
				SqlDBTable.Record.COLUMN_NAME_REMARK,
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",1,4) || '-' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",5,2) || '-' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",7,2) || ' ' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",9,2) || ':' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",11,2) AS " + SqlDBTable.Record.COLUMN_NAME_DATE 
		};
		String table = "(" + SqlDBTable.Record.TABLE_NAME + " LEFT JOIN " + SqlDBTable.Entry.TABLE_NAME + " ON " +
				SqlDBTable.Record.TABLE_NAME + "." + SqlDBTable.Record.COLUMN_NAME_ENTRY_ID + "=" + 
				SqlDBTable.Entry.TABLE_NAME + "." + SqlDBTable.Entry.COLUMN_NAME_ID+ ")" +
				" LEFT JOIN " + SqlDBTable.Warehouse.TABLE_NAME + " ON " + 
				SqlDBTable.Record.TABLE_NAME + "." + SqlDBTable.Record.COLUMN_NAME_WAREHOUSE_ID + "=" + 
				SqlDBTable.Warehouse.TABLE_NAME + "." + SqlDBTable.Warehouse.COLUMN_NAME_ID;
		String selection = SqlDBTable.Record.TABLE_NAME + "." + SqlDBTable.Record.COLUMN_NAME_ID + " = " + id;
		String[] selectionArgs = {};
		String groupBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		Cursor c = readableDB.query(table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		c.moveToFirst();
		
		if (c.getCount() > 0){ 
			
			map.put(SqlDBTable.Record.COLUMN_NAME_ID, c.getInt(0));
			map.put(SqlDBTable.Entry.COLUMN_NAME_NAME, c.getString(1));
			map.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, c.getInt(2));
			map.put(SqlDBTable.Warehouse.COLUMN_NAME_NAME, c.getString(3));
			map.put(SqlDBTable.Record.COLUMN_NAME_AMOUNT, c.getInt(4));
			map.put(SqlDBTable.Entry.COLUMN_NAME_UNIT, c.getString(5));
			map.put(SqlDBTable.Record.COLUMN_NAME_INOROUT, c.getInt(6));
			map.put(SqlDBTable.Record.COLUMN_NAME_STATUS, c.getInt(7));
			map.put(SqlDBTable.Record.COLUMN_NAME_REMARK, c.getString(8));
			map.put(SqlDBTable.Record.COLUMN_NAME_DATE, c.getString(9));

			c.moveToNext();
		}

		readableDB.close();
		return map;
	}
	
	/* select record in specific name and type
	 * INTPUT: String name - the specific name of the record
	 *         int type - the specific type of the record
	 *         String sortby - the listing order
	 * OUTPUT: record._id, warehouse.Warehouse_Name, record.Amount, entry.Unit, record.InOrOut, record.Status, record.Date
	 *         IN ArrayList<HashMap<String, Object>>
	 */
	public ArrayList<HashMap<String, Object>> recordSelectByNameAndType (String name, int type, String sortby){
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				SqlDBTable.Record.TABLE_NAME + "." + SqlDBTable.Record.COLUMN_NAME_ID,
				SqlDBTable.Warehouse.COLUMN_NAME_NAME,
				SqlDBTable.Record.COLUMN_NAME_AMOUNT,
				SqlDBTable.Entry.COLUMN_NAME_UNIT,
				SqlDBTable.Record.COLUMN_NAME_INOROUT,
				SqlDBTable.Record.COLUMN_NAME_STATUS,
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",1,4) || '-' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",5,2) || '-' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",7,2) || ' ' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",9,2) || ':' || " +
				"SUBSTR("+SqlDBTable.Record.COLUMN_NAME_DATE+",11,2) AS " + SqlDBTable.Record.COLUMN_NAME_DATE 
		};
		String table = "(" + SqlDBTable.Record.TABLE_NAME + " LEFT JOIN " + SqlDBTable.Entry.TABLE_NAME + " ON " +
				SqlDBTable.Record.TABLE_NAME + "." + SqlDBTable.Record.COLUMN_NAME_ENTRY_ID + "=" + 
				SqlDBTable.Entry.TABLE_NAME + "." + SqlDBTable.Entry.COLUMN_NAME_ID+ ")" +
				" LEFT JOIN " + SqlDBTable.Warehouse.TABLE_NAME + " ON " + 
				SqlDBTable.Record.TABLE_NAME + "." + SqlDBTable.Record.COLUMN_NAME_WAREHOUSE_ID + "=" + 
				SqlDBTable.Warehouse.TABLE_NAME + "." + SqlDBTable.Warehouse.COLUMN_NAME_ID;
		String selection = SqlDBTable.Entry.COLUMN_NAME_NAME + " = ? AND " +
				SqlDBTable.Entry.COLUMN_NAME_TYPE + " = " + type;
		String[] selectionArgs = {name};
		String groupBy = null;
		String having = null;
		String orderBy;
		if (sortby.equals(""))
			orderBy = SqlDBTable.Record.COLUMN_NAME_DATE + " DESC";
		else
			orderBy = sortby + " , " + SqlDBTable.Record.COLUMN_NAME_DATE + " DESC";
		String limit = null;
		
		Cursor c = readableDB.query(table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();
		
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		c.moveToFirst();
		
		for (int i = 0; i<c.getCount(); i++){ 
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(SqlDBTable.Record.COLUMN_NAME_ID, c.getInt(0));
			map.put(SqlDBTable.Warehouse.COLUMN_NAME_NAME, c.getString(1));
			map.put(SqlDBTable.Record.COLUMN_NAME_AMOUNT, c.getInt(2));
			map.put(SqlDBTable.Entry.COLUMN_NAME_UNIT, c.getString(3));
			if (c.getInt(4) == 0)
				map.put(SqlDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_instock);
			else
				map.put(SqlDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_outstock);
			if (c.getInt(5) == 0)
				map.put(SqlDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pending);
			else
				map.put(SqlDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pass);
			map.put(SqlDBTable.Record.COLUMN_NAME_DATE, c.getString(6));
			
			arrayList.add(map);
			c.moveToNext();
		}

		readableDB.close();
		return arrayList;
	}
	
	/* Insert a new record
	 * INPUT: String entryName - the name of the entry
	 *        int entryType - material or product
	 *        int warehouseId - the warehouse of the entry 
	 *        int amount - the amount of the entry
	 *        String unit - the unit of the entry
	 *        int inOrOut - instock or outstock
	 *        String remark - the remark of the entry
	 * OUTPUT: N/A
	 */
	public void recordInsert(String entryName, int entryType, int warehouseId, int amount, String unit, int inOrOut, String remark){
		
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				SqlDBTable.Entry.COLUMN_NAME_ID 
		};
		String table = SqlDBTable.Entry.TABLE_NAME;
		String selection = SqlDBTable.Entry.COLUMN_NAME_NAME + " = ? AND " +
				SqlDBTable.Entry.COLUMN_NAME_TYPE + " = ? ";
		String[] selectionArgs = {entryName, ""+entryType};
		String groupBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		Cursor c = readableDB.query(table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();
		
		SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
		
		String entryId;
		if (c.getCount() <= 0){
			ContentValues values = new ContentValues();
			values.put(SqlDBTable.Entry.COLUMN_NAME_NAME, entryName);
			values.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, entryType);
			values.put(SqlDBTable.Entry.COLUMN_NAME_UNIT, unit);
			
			writableDB.insert(SqlDBTable.Entry.TABLE_NAME, "null", values);
			
			c = readableDB.query(table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
			c.moveToFirst();
		}
		entryId = c.getString(0);
		
		ContentValues values = new ContentValues();
		values.put(SqlDBTable.Record.COLUMN_NAME_ENTRY_ID, entryId);
		values.put(SqlDBTable.Record.COLUMN_NAME_WAREHOUSE_ID, warehouseId);
		values.put(SqlDBTable.Record.COLUMN_NAME_AMOUNT, amount);
		values.put(SqlDBTable.Record.COLUMN_NAME_INOROUT, inOrOut);
		values.put(SqlDBTable.Record.COLUMN_NAME_STATUS, 0);
		values.put(SqlDBTable.Record.COLUMN_NAME_REMARK, remark);
		values.put(SqlDBTable.Record.COLUMN_NAME_DATE, parseDate(Calendar.getInstance()));
		
		writableDB.insert(SqlDBTable.Record.TABLE_NAME, "null", values);
		writableDB.close();
	}

	/* update the record status and date and update the current table as well
	 * INPUT: int id - the record that need to update
	 * OUTPUT: N/A
	 */
	public void recordUpdate(int id){
		
		//select record 
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				SqlDBTable.Record.COLUMN_NAME_ENTRY_ID,
				SqlDBTable.Record.COLUMN_NAME_WAREHOUSE_ID,
				SqlDBTable.Record.COLUMN_NAME_AMOUNT,
				SqlDBTable.Record.COLUMN_NAME_INOROUT,
				SqlDBTable.Record.COLUMN_NAME_STATUS
		};
		String selection = 
				SqlDBTable.Record.TABLE_NAME + "." + SqlDBTable.Record.COLUMN_NAME_ID + " = "+id;
		String[] selectionArgs = {};
		String groupBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;
		
		Cursor c = readableDB.query(SqlDBTable.Record.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();
		
		String entryId = c.getString(0);
		String warehouseId = c.getString(1);
		int change = c.getInt(2);
		int inorout = c.getInt(3);
		
		SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
		
		Calendar calendar = Calendar.getInstance();
		ContentValues values = new ContentValues();
		values.put(SqlDBTable.Record.COLUMN_NAME_STATUS, "1");
		values.put(SqlDBTable.Record.COLUMN_NAME_DATE, parseDate(calendar));
		
		writableDB.update(SqlDBTable.Record.TABLE_NAME, values, selection, selectionArgs);
		
		//select current
		projection = new String[] {
				SqlDBTable.Current.COLUMN_NAME_ID,
				SqlDBTable.Current.COLUMN_NAME_AMOUNT};
		selection = 
				SqlDBTable.Current.COLUMN_NAME_ENTRY_ID + " = ? AND " +
				SqlDBTable.Current.COLUMN_NAME_WAREHOUSE_ID + " = ?";
		selectionArgs = new String[] {entryId, warehouseId};
		groupBy = null;
		having = null;
		orderBy = null;
		limit = null;
		
		c = readableDB.query(SqlDBTable.Current.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		if (c.getCount() > 0) {
			c.moveToFirst();
			int amount = c.getInt(1);
			
			values = new ContentValues();
			if (inorout == 0) {
				values.put(SqlDBTable.Current.COLUMN_NAME_AMOUNT, amount+change);
			}
			else {
				values.put(SqlDBTable.Current.COLUMN_NAME_AMOUNT, amount-change);
			}
			
			writableDB.update(SqlDBTable.Current.TABLE_NAME, values, selection, selectionArgs);
		}
		else {
			values = new ContentValues();
			values.put(SqlDBTable.Current.COLUMN_NAME_ENTRY_ID, entryId);
			values.put(SqlDBTable.Current.COLUMN_NAME_WAREHOUSE_ID, warehouseId);
			values.put(SqlDBTable.Current.COLUMN_NAME_AMOUNT, change);
			
			writableDB.insert(SqlDBTable.Current.TABLE_NAME, "null", values);
		}
		readableDB.close();
		writableDB.close();
	}
	
	/* delete specific record with the id
	 * INPUT: int id - the id of the record
	 * OUTPUT: N/A
	 */
	//delete a record
	public void recordDelete(int id){
		
		SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
		
		String selection = SqlDBTable.Record.COLUMN_NAME_ID + " = " + id;
		String[] selectionArgs = {};
		
		writableDB.delete(SqlDBTable.Record.TABLE_NAME, selection, selectionArgs);
		
		writableDB.close();
	}
	
	/* return the current usable amount of the entry
	 * INPUT: int id - the id of the specific entry
	 *        int warehouseId - the id of the specific warehouse
	 * OUTPUT: int usableAmount - the usable amount of the entry in the warehouse
	 */
	public int entrySelectUsableAmount(int id, int warehouseId){

		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				SqlDBTable.Current.COLUMN_NAME_AMOUNT, 
		};
		String selection = 
				SqlDBTable.Current.COLUMN_NAME_ENTRY_ID + " = " + id + " AND " +
				SqlDBTable.Current.COLUMN_NAME_WAREHOUSE_ID + " = " + warehouseId;
		String[] selectionArgs = {};
		String groupBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;

		Cursor c = readableDB.query(SqlDBTable.Current.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();

		int total;
		if (c.getCount() == 0)
			return 0;
		else
			total = Integer.parseInt(c.getString(0));
		
		projection = new String[] {
				"SUM(" + SqlDBTable.Record.COLUMN_NAME_AMOUNT + ") AS " + SqlDBTable.Record.COLUMN_NAME_AMOUNT 
		};
		selection = 
				SqlDBTable.Record.COLUMN_NAME_ENTRY_ID + " = " + id + " AND " +
				SqlDBTable.Record.COLUMN_NAME_WAREHOUSE_ID + " = " + warehouseId + " AND " +
				SqlDBTable.Record.COLUMN_NAME_STATUS + " = 0 AND " + 
				SqlDBTable.Record.COLUMN_NAME_INOROUT + " = 1";
		selectionArgs = new String[] {};
		groupBy = 
				SqlDBTable.Record.COLUMN_NAME_ENTRY_ID + ", " +
				SqlDBTable.Record.COLUMN_NAME_WAREHOUSE_ID;
		having = null;
		orderBy = null;
		limit = null;
		
		c = readableDB.query(SqlDBTable.Record.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		c.moveToFirst();
		
		int used;
		if (c.getCount() == 0)
			used = 0;
		else
			used = Integer.parseInt(c.getString(0));
		
		readableDB.close();
		
		return total - used;
		
	}
	
	/* select all the warehouse
	 * INPUT: N/A
	 * OUTPUT: warehouse._id, warehouse.Warehouse_Name
	 *         IN ArrayList<HashMap<String, Object>>
	 */
	public ArrayList<HashMap<String, Object>> warehouseSelectAll(){
		SQLiteDatabase readableDB = dbHelper.getReadableDatabase();
		
		String[] projection = {
				SqlDBTable.Warehouse.COLUMN_NAME_ID,
				SqlDBTable.Warehouse.COLUMN_NAME_NAME
		};
		String selection = "";
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = "";
		String limit = null;
		
		Cursor c = readableDB.query(SqlDBTable.Warehouse.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
		
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		c.moveToFirst();
		
		for (int i = 0; i<c.getCount(); i++){ 
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(SqlDBTable.Warehouse.COLUMN_NAME_ID, c.getInt(0));
			map.put(SqlDBTable.Warehouse.COLUMN_NAME_NAME, c.getString(1));
			
			arrayList.add(map);
			c.moveToNext();
		}
		
		readableDB.close();
		return arrayList;
	}
	
	private String parseDate(Calendar calendar){
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
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
