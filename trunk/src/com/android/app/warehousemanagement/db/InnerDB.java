package com.android.app.warehousemanagement.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InnerDB extends SQLiteOpenHelper{

	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "Warehouse.db";
	private static final String INTEGER_TYPE = " Integer";
	private static final String TEXT_TYPE = " VARCHAR";
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
			InnerDBTable.Record.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
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
		String insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('����100ml', '�ֿܲ�', 5, 'ԭ��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('����100ml', '��ɽ�ֿ�', 6, 'ԭ��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('����100ml', '���ݲֿ�', 2, 'ԭ��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('����200ml', '�ֿܲ�', 1, 'ԭ��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('����200ml', '���ݲֿ�', 1, 'ԭ��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('����200ml', '��ɽ�ֿ�', 1, 'ԭ��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('˫ֽͭ100*100', '�ֿܲ�', 2, 'ԭ��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('˫ֽͭ200*100', '�ֿܲ�', 3, 'ԭ��')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('˫ֽͭ100*100', '�ֿܲ�', 1, '��Ʒ')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('˫ֽͭ100*100', '��ɽ�ֿ�', 6, '��Ʒ')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('ST&SAT����', '�ֿܲ�', 6, '��Ʒ')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('ST&SAT����', '��ɽ�ֿ�', 3, '��Ʒ')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('ST&SAT����', '���ݲֿ�', 12, '��Ʒ')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('���׺���', '���ݲֿ�', 11, '��Ʒ')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('���׺���', '��ɽ�ֿ�', 5, '��Ʒ')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Current (entryname, warehouse, amount, type) values ('��������', '�ֿܲ�', 12, '��Ʒ')";
		db.execSQL(insertSql);
		
		//initiate record database
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date) values " +
				"('����100ml', '�ֿܲ�', 2, 'ԭ��', '���', '��ʢ����', '���', '201204051203')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date) values " +
				"('����100ml', '��ɽ�ֿ�', 1, 'ԭ��', '���', '��ʢ����', '���', '201203060900')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date) values " +
				"('����100ml', '�ֿܲ�', 1, 'ԭ��', '����', '����������', '���', '201205051600')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date) values " +
				"('����100ml', '���ݲֿ�', 2, 'ԭ��', '����', '����������', '���', '201205060900')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date) values " +
				"('ST&SAT����', '�ֿܲ�', 2, '��Ʒ', '���', '����������', '���', '201206061400')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date) values " +
				"('ST&SAT����', '��ɽ�ֿ�', 5, '��Ʒ', '���', '����������', '���', '201206061500')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date) values " +
				"('��������', '�ֿܲ�', 1, '��Ʒ', '����', '��������', '���', '201207081600')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date) values " +
				"('ST&SAT����', '�ֿܲ�', 7, '��Ʒ', '����', '����������', '����', '201207081023')";
		db.execSQL(insertSql);
		insertSql = "INSERT INTO Record (entryname, warehouse, amount, type, inorout, remark, status, date) values " +
				"('˫ֽͭ100*100', '���ݲֿ�', 2, 'ԭ��', '����', '��������', '����', '201204201502')";
		db.execSQL(insertSql);
		
	}
	
	@SuppressLint("Override")
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL(SQL_DELETE_CURRENT);
		db.execSQL(SQL_DELETE_RECORD);
		onCreate(db);
	}
	
	@SuppressLint("Override")
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db,oldVersion,newVersion);
	}
	
	
}
