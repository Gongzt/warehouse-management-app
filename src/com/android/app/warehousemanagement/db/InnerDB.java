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
		
		String insertSql = "INSERT INTO warehouse (Warehouse_Name) values ('�ֿܲ�')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO warehouse (Warehouse_Name) values ('���ݲֿ�')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO warehouse (Warehouse_Name) values ('��ɽ�ֿ�')";
		db.execSQL(insertSql);
		
		
		// Initiate Current table
		/*String insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('����100ml', '�ֿܲ�', 5, 'ԭ��', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('����100ml', '��ɽ�ֿ�', 6, 'ԭ��', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('����100ml', '���ݲֿ�', 2, 'ԭ��', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('����200ml', '�ֿܲ�', 1, 'ԭ��', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('����200ml', '���ݲֿ�', 1, 'ԭ��', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('����200ml', '��ɽ�ֿ�', 1, 'ԭ��', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('˫ֽͭ100*100', '�ֿܲ�', 2, 'ԭ��', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('˫ֽͭ200*100', '�ֿܲ�', 3, 'ԭ��', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('˫ֽͭ100*100', '�ֿܲ�', 1, '��Ʒ', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('˫ֽͭ100*100', '��ɽ�ֿ�', 6, '��Ʒ', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('ST&SAT����', '�ֿܲ�', 6, '��Ʒ', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('ST&SAT����', '��ɽ�ֿ�', 3, '��Ʒ', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('ST&SAT����', '���ݲֿ�', 12, '��Ʒ', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('���׺���', '���ݲֿ�', 11, '��Ʒ', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('���׺���', '��ɽ�ֿ�', 5, '��Ʒ', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type, unit) values ('��������', '�ֿܲ�', 12, '��Ʒ', '��')";
		db.execSQL(insertSql);*/
		
		//initiate record database
		/*insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('����100ml', '�ֿܲ�', 2, 'ԭ��', '���', '��ʢ����', 'ͨ��', '201204051203', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('����100ml', '��ɽ�ֿ�', 1, 'ԭ��', '���', '��ʢ����', '����', '201203060900', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('����100ml', '�ֿܲ�', 1, 'ԭ��', '����', '����������', 'ͨ��', '201205051600', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('����100ml', '���ݲֿ�', 2, 'ԭ��', '����', '����������', 'ͨ��', '201205060900', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('ST&SAT����', '�ֿܲ�', 2, '��Ʒ', '���', '����������', 'ͨ��', '201206061400', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('ST&SAT����', '��ɽ�ֿ�', 5, '��Ʒ', '���', '����������', '����', '201206061500', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('��������', '�ֿܲ�', 1, '��Ʒ', '����', '��������', 'ͨ��', '201207081600', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('ST&SAT����', '�ֿܲ�', 6, '��Ʒ', '����', '����������', '����', '201207081023', '��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date, unit) values " +
				"('˫ֽͭ100*100', '�ֿܲ�', 2, 'ԭ��', '����', '��������', '����', '201204201502', '��')";
		db.execSQL(insertSql);*/
		
		//initiate record database
		/*		insertSql = "INSERT INTO Warehouse (warehousename) values ('�ֿܲ�')";
				db.execSQL(insertSql);
				insertSql = "INSERT INTO Warehouse (warehousename) values ('��ɽ�ֿ�')";
				db.execSQL(insertSql);
				insertSql = "INSERT INTO Warehouse (warehousename) values ('���ݲֿ�')";
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
