package com.android.app.warehousemanagement.db;

import android.provider.BaseColumns;

public abstract class InnerDBTable {
	
	private InnerDBTable(){
	}
	
	public static abstract class Current implements BaseColumns{
		public static final String TABLE_NAME = "Current";
		public static final String COLUMN_NAME_ENTRY_ID = "_id";
		public static final String COLUMN_NAME_ENTRY_NAME = "entryname";
		public static final String COLUMN_NAME_WAREHOUSE = "warehouse";
		public static final String COLUMN_NAME_AMOUNT = "amount";
		public static final String COLUMN_NAME_ENTRY_TYPE = "type";
		public static final String COLUMN_NAME_UNIT = "unit";
		
	}
	
	public static abstract class Record implements BaseColumns{
		public static final String TABLE_NAME = "Record";
		public static final String COLUMN_NAME_RECORD_ID = "_id";
		public static final String COLUMN_NAME_DATE = "date";
		public static final String COLUMN_NAME_ENTRY_NAME = "entryname";
		public static final String COLUMN_NAME_ENTRY_TYPE = "type";
		public static final String COLUMN_NAME_WAREHOUSE = "warehouse";
		public static final String COLUMN_NAME_AMOUNT = "amount";
		public static final String COLUMN_NAME_INOROUT = "inorout";
		public static final String COLUMN_NAME_REMARK = "remark";
		public static final String COLUMN_NAME_STATUS = "status";
		public static final String COLUMN_NAME_UNIT = "unit";
	}
	
	public static abstract class Warehouse implements BaseColumns{
		public static final String TABLE_NAME = "Warehouse";
		public static final String COLUMN_NAME_WAREHOUSE_ID = "_id";
		public static final String COLUMN_NAME_WAREHOUSE_NAME = "warehousename";
	}
	

}
