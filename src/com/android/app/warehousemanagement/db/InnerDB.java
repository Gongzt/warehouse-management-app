package com.android.app.warehousemanagement.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InnerDB extends SQLiteOpenHelper{

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Warehouse.db";
	private static final String INTEGER_TYPE = " Integer";
	private static final String TEXT_TYPE = " VARCHAR";
	private static final String COMMA_SEP = ",";
	
	private static final String SQL_CREATE_CURRENT =    
			"CREATE TABLE " + SqlDBTable.Current.TABLE_NAME + " (" +
			SqlDBTable.Current.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
			SqlDBTable.Current.COLUMN_NAME_ENTRY_ID + INTEGER_TYPE + COMMA_SEP +
			SqlDBTable.Current.COLUMN_NAME_WAREHOUSE_ID + INTEGER_TYPE + COMMA_SEP +
			SqlDBTable.Current.COLUMN_NAME_AMOUNT + TEXT_TYPE +
			" )";
	
	private static final String SQL_CREATE_ENTRY =    
			"CREATE TABLE " + SqlDBTable.Entry.TABLE_NAME + " (" +
			SqlDBTable.Entry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
			SqlDBTable.Entry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
			SqlDBTable.Entry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
			SqlDBTable.Entry.COLUMN_NAME_UNIT + TEXT_TYPE +
			" )";
	
	private static final String SQL_CREATE_RECORD = 
			"CREATE TABLE " + SqlDBTable.Record.TABLE_NAME + " (" +
			SqlDBTable.Record.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
			SqlDBTable.Record.COLUMN_NAME_ENTRY_ID + INTEGER_TYPE + COMMA_SEP +
			SqlDBTable.Record.COLUMN_NAME_WAREHOUSE_ID + INTEGER_TYPE + COMMA_SEP +
			SqlDBTable.Record.COLUMN_NAME_INOROUT + INTEGER_TYPE + COMMA_SEP +
			SqlDBTable.Record.COLUMN_NAME_REMARK + TEXT_TYPE + COMMA_SEP +
			SqlDBTable.Record.COLUMN_NAME_AMOUNT + INTEGER_TYPE + COMMA_SEP +
			SqlDBTable.Record.COLUMN_NAME_STATUS + INTEGER_TYPE + COMMA_SEP +
			SqlDBTable.Record.COLUMN_NAME_DATE + TEXT_TYPE + 
			" )";
	
	private static final String SQL_CREATE_WAREHOUSE =    
			"CREATE TABLE " + SqlDBTable.Warehouse.TABLE_NAME + " (" +
			SqlDBTable.Warehouse.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
			SqlDBTable.Warehouse.COLUMN_NAME_NAME + TEXT_TYPE + 
			" )";
	
	private static final String SQL_DELETE_CURRENT =    
			"DROP TABLE IF EXISTS " + SqlDBTable.Current.TABLE_NAME;
	
	private static final String SQL_DELETE_ENTRY =    
			"DROP TABLE IF EXISTS " + SqlDBTable.Entry.TABLE_NAME;
	
	private static final String SQL_DELETE_RECORD =    
			"DROP TABLE IF EXISTS " + SqlDBTable.Record.TABLE_NAME;
	
	private static final String SQL_DELETE_WAREHOUSE =    
			"DROP TABLE IF EXISTS " + SqlDBTable.Warehouse.TABLE_NAME;
	
	public InnerDB(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(SQL_CREATE_CURRENT);
		db.execSQL(SQL_CREATE_RECORD);
		db.execSQL(SQL_CREATE_WAREHOUSE);
		db.execSQL(SQL_CREATE_ENTRY);
		
		String insertSql = "INSERT INTO warehouse (Warehouse_Name) values ('总仓库')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO warehouse (Warehouse_Name) values ('广州仓库')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO warehouse (Warehouse_Name) values ('佛山仓库')";
		db.execSQL(insertSql);
		
		
		// Initiate Current table
		/*String insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('油漆100ml', '总仓库', 5, '原料', '罐')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('油漆100ml', '佛山仓库', 6, '原料', '罐')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('油漆100ml', '广州仓库', 2, '原料', '罐')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('油漆200ml', '总仓库', 1, '原料', '罐')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('油漆200ml', '广州仓库', 1, '原料', '罐')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('油漆200ml', '佛山仓库', 1, '原料', '罐')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('双铜纸100*100', '总仓库', 2, '原料', '吨')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('双铜纸200*100', '总仓库', 3, '原料', '吨')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('双铜纸100*100', '总仓库', 1, '产品', '吨')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('双铜纸100*100', '佛山仓库', 6, '产品', '吨')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('ST&SAT盒子', '总仓库', 6, '产品', '个')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('ST&SAT盒子', '佛山仓库', 3, '产品', '个')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('ST&SAT盒子', '广州仓库', 12, '产品', '个')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('华伦盒子', '广州仓库', 11, '产品', '个')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('华伦盒子', '佛山仓库', 5, '产品', '个')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('哈哈盒子', '总仓库', 12, '产品', '个')";
		db.execSQL(insertSql);*/
		
		//initiate record database
		/*insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('油漆100ml', '总仓库', 2, '原料', '入库', '荣盛发货', '通过', '201204051203', '罐')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('油漆100ml', '佛山仓库', 1, '原料', '入库', '荣盛发货', '待审', '201203060900', '罐')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('油漆100ml', '总仓库', 1, '原料', '出库', '星期六订单', '通过', '201205051600', '罐')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('油漆100ml', '广州仓库', 2, '原料', '出库', '星期六订单', '通过', '201205060900', '罐')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('ST&SAT盒子', '总仓库', 2, '产品', '入库', '星期六订单', '通过', '201206061400', '个')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('ST&SAT盒子', '佛山仓库', 5, '产品', '入库', '星期六订单', '待审', '201206061500', '个')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('哈哈盒子', '总仓库', 1, '产品', '出库', '哈哈订单', '通过', '201207081600', '个')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('ST&SAT盒子', '总仓库', 6, '产品', '出库', '星期六订单', '待审', '201207081023', '个')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('双铜纸100*100', '总仓库', 2, '原料', '出库', '百丽发货', '待审', '201204201502', '吨')";
		db.execSQL(insertSql);*/
		
		//initiate record database
		/*		insertSql = "INSERT INTO Warehouse (warehousename) values ('总仓库')";
				db.execSQL(insertSql);
				insertSql = "INSERT INTO Warehouse (warehousename) values ('佛山仓库')";
				db.execSQL(insertSql);
				insertSql = "INSERT INTO Warehouse (warehousename) values ('广州仓库')";
				db.execSQL(insertSql);*/
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL(SQL_DELETE_CURRENT);
		db.execSQL(SQL_DELETE_RECORD);
		db.execSQL(SQL_DELETE_WAREHOUSE);
		db.execSQL(SQL_DELETE_ENTRY);
		onCreate(db);
	}
	
	@SuppressLint("Override")
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db,oldVersion,newVersion);
	}
	
	
}
