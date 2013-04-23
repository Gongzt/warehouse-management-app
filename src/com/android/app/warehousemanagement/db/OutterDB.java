package com.android.app.warehousemanagement.db;

import android.provider.BaseColumns;

public abstract class OutterDB {
	
	public OutterDB(){
		
	}
	
	public static final String URL = "http://192.168.0.16/";
	
	public static abstract class EntrySelectAll implements BaseColumns{
		public static final String METHOD_NAME = "entrySelectAll.php";
	}
	
	public static abstract class CurrentSearch implements BaseColumns{
		public static final String METHOD_NAME = "currentSearch.php";
		public static final String ARGUMENT_NAME_KEYWORD = "keyword";
		public static final String ARGUMENT_NAME_SORTBY = "sortby";
	}
	
	public static abstract class CurrentSelectByNameAndType implements BaseColumns{
		public static final String METHOD_NAME = "currentSelectByNameAndType.php";
		public static final String ARGUMENT_NAME_NAME = "name";
		public static final String ARGUMENT_NAME_TYPE = "type";
	} 
	
	public static abstract class EntrySelectUsableAmount implements BaseColumns{
		public static final String METHOD_NAME = "entrySelectUsableAmount.php";
		public static final String ARGUMENT_NAME_ENTRYID = "entry_id";
		public static final String ARGUMENT_NAME_WAREHOUSEID = "warehouse_id";
	} 
	
	public static abstract class RecordDelete implements BaseColumns{
		public static final String METHOD_NAME = "recordDelete.php";
		public static final String ARGUMENT_NAME_RECORDID = "record_id";
	} 
	
	public static abstract class RecordInsert implements BaseColumns{
		public static final String METHOD_NAME = "recordInsert.php";
		public static final String ARGUMENT_NAME_NAME = "name";
		public static final String ARGUMENT_NAME_TYPE = "type";
		public static final String ARGUMENT_NAME_WAREHOUSEID = "warehouse_id";
		public static final String ARGUMENT_NAME_AMOUNT = "amount";
		public static final String ARGUMENT_NAME_UNIT = "unit";
		public static final String ARGUMENT_NAME_INOROUT = "inorout";
		public static final String ARGUMENT_NAME_REMARK = "remark";
	} 
	
	public static abstract class RecordSearch implements BaseColumns{
		public static final String METHOD_NAME = "recordSearch.php";
		public static final String ARGUMENT_NAME_KEYWORD = "keyword";
		public static final String ARGUMENT_NAME_SORTBY = "sortby";
		public static final String ARGUMENT_NAME_STARTDATE = "startdate";
		public static final String ARGUMENT_NAME_ENDDATE = "enddate";
	} 
	
	public static abstract class RecordSelectByID implements BaseColumns{
		public static final String METHOD_NAME = "recordSelectByID.php";
		public static final String ARGUMENT_NAME_RECORDID = "record_id";
	} 
	
	public static abstract class RecordSelectByNameAndType implements BaseColumns{
		public static final String METHOD_NAME = "recordSelectByNameAndType.php";
		public static final String ARGUMENT_NAME_NAME = "name";
		public static final String ARGUMENT_NAME_TYPE = "type";
		public static final String ARGUMENT_NAME_SORTBY = "sortby";
	} 
	
	public static abstract class RecordUpdate implements BaseColumns{
		public static final String METHOD_NAME = "recordUpdate.php";
		public static final String ARGUMENT_NAME_RECORDID = "record_id";
	} 
	
	public static abstract class WarehouseSelectAll implements BaseColumns{
		public static final String METHOD_NAME = "warehouseSelectAll.php";
	}
}
