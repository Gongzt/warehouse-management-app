package com.android.app.warehousemanagement.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InnerDB extends SQLiteOpenHelper{

	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "Warehouse.db";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String TEXT_TYPE = " VARCHAR";
	private static final String DATE_TYPE = " DATE";
	private static final String COMMA_SEP = ",";
	
	private static final String SQL_CREATE_CURRENT =    
			"CREATE TABLE " + InnerDBTable.Current.TABLE_NAME + " (" +
			InnerDBTable.Current.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
			InnerDBTable.Current.COLUMN_NAME_ENTRY_NAME + TEXT_TYPE + COMMA_SEP +
			InnerDBTable.Current.COLUMN_NAME_ENTRY_TYPE + TEXT_TYPE + COMMA_SEP +
			InnerDBTable.Current.COLUMN_NAME_WAREHOUSE + TEXT_TYPE + COMMA_SEP +
			InnerDBTable.Current.COLUMN_NAME_AMOUNT + INTEGER_TYPE +
			" )";
	
	private static final String SQL_CREATE_RECORD = 
			"CREATE TABLE " + InnerDBTable.Record.TABLE_NAME + " (" +
			InnerDBTable.Record.COLUMN_NAME_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
			InnerDBTable.Record.COLUMN_NAME_ENTRY_NAME + TEXT_TYPE + COMMA_SEP +
			InnerDBTable.Record.COLUMN_NAME_ENTRY_TYPE + TEXT_TYPE + COMMA_SEP +
			InnerDBTable.Record.COLUMN_NAME_WAREHOUSE + TEXT_TYPE + COMMA_SEP +
			InnerDBTable.Record.COLUMN_NAME_INOROUT + TEXT_TYPE + COMMA_SEP +
			InnerDBTable.Record.COLUMN_NAME_REMARK + TEXT_TYPE + COMMA_SEP +
			InnerDBTable.Record.COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP +
			InnerDBTable.Record.COLUMN_NAME_DATE + DATE_TYPE + COMMA_SEP +
			InnerDBTable.Current.COLUMN_NAME_AMOUNT + INTEGER_TYPE +
			" )";
	
	private static final String SQL_DELETE_CURRENT =    
			"DROP TABLE IF EXISTS " + InnerDBTable.Current.TABLE_NAME;
	
	private static final String SQL_DELETE_RECORD =    
			"DROP TABLE IF EXISTS " + InnerDBTable.Record.TABLE_NAME;
	
	public InnerDB(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(SQL_CREATE_CURRENT);
		db.execSQL(SQL_CREATE_RECORD);
		
		// Initiate Current table
		String insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('油漆100ml', '总仓库', 5, '原料')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('油漆100ml', '佛山仓库', 6, '原料')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('油漆100ml', '广州仓库', 2, '原料')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('油漆200ml', '总仓库', 1, '原料')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('油漆200ml', '广州仓库', 1, '原料')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('油漆200ml', '佛山仓库', 1, '原料')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('双铜纸100*100', '总仓库', 2, '原料')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('双铜纸200*100', '总仓库', 3, '原料')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('双铜纸100*100', '总仓库', 1, '产品')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('双铜纸100*100', '佛山仓库', 6, '产品')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('ST&SAT盒子', '总仓库', 6, '产品')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('ST&SAT盒子', '佛山仓库', 3, '产品')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('ST&SAT盒子', '广州仓库', 12, '产品')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('华伦盒子', '广州仓库', 11, '产品')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('华伦盒子', '佛山仓库', 5, '产品')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('哈哈盒子', '总仓库', 12, '产品')";
		db.execSQL(insertSql);
		
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL(SQL_DELETE_CURRENT);
		db.execSQL(SQL_DELETE_RECORD);
		onCreate(db);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db,oldVersion,newVersion);
	}
	
	
}
