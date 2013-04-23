package com.android.app.warehousemanagement.db;

import android.provider.BaseColumns;

public abstract class SqlDBTable {
	
	private SqlDBTable(){
	}
	
	public static abstract class Current implements BaseColumns{
		public static final String TABLE_NAME = "current";
		public static final String COLUMN_NAME_ID = "_id";
		public static final String COLUMN_NAME_ENTRY_ID = "Entry_ID";
		public static final String COLUMN_NAME_WAREHOUSE_ID = "Warehouse_ID";
		public static final String COLUMN_NAME_AMOUNT = "Amount";
	}
	
	public static abstract class Entry implements BaseColumns{
		public static final String TABLE_NAME = "entry";
		public static final String COLUMN_NAME_ID = "_id";
		public static final String COLUMN_NAME_NAME = "Name";
		public static final String COLUMN_NAME_TYPE = "Type";
		public static final String COLUMN_NAME_UNIT = "Unit";
	}
	
	public static abstract class Record implements BaseColumns{
		public static final String TABLE_NAME = "record";
		public static final String COLUMN_NAME_ID = "_id";
		public static final String COLUMN_NAME_ENTRY_ID = "Entry_ID";
		public static final String COLUMN_NAME_WAREHOUSE_ID = "Warehouse_ID";
		public static final String COLUMN_NAME_AMOUNT = "Amount";
		public static final String COLUMN_NAME_INOROUT = "InOrOut";
		public static final String COLUMN_NAME_REMARK = "Remark";
		public static final String COLUMN_NAME_DATE = "Date";
		public static final String COLUMN_NAME_STATUS = "Status";
		
	}
	
	public static abstract class Warehouse implements BaseColumns{
		public static final String TABLE_NAME = "warehouse";
		public static final String COLUMN_NAME_ID = "_id";
		public static final String COLUMN_NAME_NAME = "Warehouse_Name";
	}
	

}
