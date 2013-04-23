package com.android.app.warehousemanagement.db;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.app.warehousemanagement.R;
import com.android.app.warehousemanagement.SingleRecordActivity;
import com.android.app.warehousemanagement.db.OutterDBExec.OutterDBException;


public class SqlDBInterface {
	//0-INNER SERVER; 1-PRODUCTION SERVER
	private static int server = 1;
	
	private InnerDBExec innerDB = null;
	private OutterDBExec outterDB = null;
	private Context context;
	private Boolean isWaiting = true;
	private View waitView = null;
	
	public SqlDBInterface (Context _context){
		context = _context;
		innerDB = new InnerDBExec(context);
		outterDB = new OutterDBExec();
	}
	
	public Context getContext(){
		return context;
	}
	
	public void showExceptionDialog(OutterDBException e){
		AlertDialog.Builder completeDialog = new Builder(context);
		completeDialog.setMessage(e.getMessage());
		completeDialog.setTitle(e.getTitle());
		completeDialog.setPositiveButton("确认", new OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		completeDialog.create().show();
	}
	
	public void showWaiting(boolean isBlur, boolean isUpload) {
	    isWaiting = true;
	        WindowManager.LayoutParams lp = null;

	        if (isBlur) {
	            lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, PixelFormat.TRANSLUCENT);
	        }
	        else {
	            lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, PixelFormat.TRANSLUCENT);
	        }
	        
	        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	        if (waitView == null) {
	            LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            waitView = inflate.inflate(R.layout.waiting_dialog, null);
	        }
	        if (isUpload) {
	        	((TextView)waitView.findViewById(R.id.waitingDialogTypeText)).setText("数据 上传中");
	        }
	        else {
	        	((TextView)waitView.findViewById(R.id.waitingDialogTypeText)).setText("数据加载中");
	        }
	        ((TextView)waitView.findViewById(R.id.waitingDialogProgressText)).setText("(0%)");
	        ((SeekBar)waitView.findViewById(R.id.waitDialogSeekBar)).setProgress(0);
	        
	        mWindowManager.addView(waitView, lp);
	        
	    
	}
	
	public void setWaiting(int progress){
		if (waitView != null){
			((TextView)waitView.findViewById(R.id.waitingDialogProgressText)).setText("("+progress+"%)");
			((SeekBar)waitView.findViewById(R.id.waitDialogSeekBar)).setProgress(progress);
		}
	}
	
	public void hideWaiting() {
	    isWaiting = false;
	    try {
	        if (waitView != null) {
	            WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	            mWindowManager.removeView(waitView);
	            waitView = null;
	        }
	    }
	    catch (Throwable e) {
	        Log.e("waitView", "[showWaiting]", e);
	    }
	}
		
	public ArrayList<HashMap<String, Object>> warehouseSelectAll() throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = null;
		if (server == 0){
			
		}
		else if (server == 1){
			arrayList = outterDB.warehouseSelectAll();
		}
		
		return arrayList;
	}
	
	public ArrayList<HashMap<String, Object>> currentSearch(String keyword, String sortby) throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		
		if (server == 0){
			
		}
		else if (server == 1){
			arrayList = outterDB.currentSearch(keyword, sortby);
		}
		
		for (int i=0; i<arrayList.size(); i++){
        	HashMap<String,Object> map = arrayList.get(i);
        	if (Integer.parseInt(map.get(SqlDBTable.Entry.COLUMN_NAME_TYPE).toString()) == 0){
        		map.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, R.drawable.icon_material);
            }
            else {
            	map.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, R.drawable.icon_product);
            }
        }
		
		return arrayList;
	}
	
	public ArrayList<HashMap<String, Object>> recordSearch(String keyword, String sortby, String startdate, String enddate) throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		if (server == 0){
			
		}
		else if (server == 1){
			arrayList = outterDB.recordSearch(keyword, sortby, startdate, enddate);
		}
		for (int i=0; i<arrayList.size(); i++){
        	HashMap<String,Object> map = arrayList.get(i);
            	
            if (Integer.parseInt(map.get(SqlDBTable.Entry.COLUMN_NAME_TYPE).toString()) == 0){
            	map.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, R.drawable.icon_material);
            }
            else {
            	map.put(SqlDBTable.Entry.COLUMN_NAME_TYPE, R.drawable.icon_product);
            }
            	
            if (Integer.parseInt(map.get(SqlDBTable.Record.COLUMN_NAME_INOROUT).toString()) == 0){
            	map.put(SqlDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_instock);
            }
            else {
            	map.put(SqlDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_outstock);
            }
            	
            if (Integer.parseInt(map.get(SqlDBTable.Record.COLUMN_NAME_STATUS).toString()) == 0){
            	map.put(SqlDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pending);
            }
            else {
            	map.put(SqlDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pass);
            }
        }
		
		return arrayList;
	}
	
	public ArrayList<HashMap<String, Object>> currentSelectByNameAndType (String name, int type) throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		if (server == 0){
			
		}
		else if (server == 1){
			arrayList = outterDB.currentSelectByNameAndType(name, type);
		}
		
		return arrayList;
	}
	
	public ArrayList<HashMap<String, Object>> recordSelectByNameAndType (String name, int type, String sortby) throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		if (server == 0){
			
		}
		else if (server == 1){
			arrayList = outterDB.recordSelectByNameAndType(name, type, sortby);
		}
		
		for (int i=0; i<arrayList.size(); i++){
        	HashMap<String,Object> map = arrayList.get(i);
        	if (Integer.parseInt(map.get(SqlDBTable.Record.COLUMN_NAME_INOROUT).toString()) == 0){
        		map.put(SqlDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_instock);
        	}
        	else {
        		map.put(SqlDBTable.Record.COLUMN_NAME_INOROUT, R.drawable.icon_outstock);
        	}
        	
        	if (Integer.parseInt(map.get(SqlDBTable.Record.COLUMN_NAME_STATUS).toString()) == 0){
        		map.put(SqlDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pending);
        	}
        	else {
        		map.put(SqlDBTable.Record.COLUMN_NAME_STATUS, R.drawable.icon_pass);
        	}
        }
		
		return arrayList;
	}
	
	public HashMap<String, Object> recordSelectById (int id) throws OutterDBException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (server == 0){
			
		} 
		else if (server == 1){
			map = outterDB.recordSelectById(id);
		}
		
		return map;
	}
	
	public ArrayList<HashMap<String, Object>> entrySelectAll () throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		if (server == 0){
			
		}
		else if (server == 1){
			arrayList = outterDB.entrySelectAll();
		}
		
		return arrayList;
	}
	
	public void recordInsert (String name, int type, String unit, int inorout, int amount, int warehouseId, String remark) throws OutterDBException{
		if (server == 0){
			
		}
		else if (server == 1){
			outterDB.recordInsert(name, type, unit, inorout, amount, warehouseId, remark);
		}
	}
	
	public int entrySelectUsableAmount (int entryId, int warehouseId) throws OutterDBException{
		int usableAmount = 0;
		if (server == 0){
			
		}
		else if (server == 1){
			usableAmount = outterDB.entrySelectUsableAmount(entryId, warehouseId);
		}
		
		return usableAmount;
	}

	public void recordUpdate (int recordId) throws OutterDBException{
		if (server == 0){
			
		}
		else if (server == 1){
			outterDB.recordUpdate(recordId);
		}
	}
	
	public void recordDelete (int recordId) throws OutterDBException{
		if (server == 0){
			
		}
		else if (server == 1){
			outterDB.recordDelete(recordId);
		}
	}
	
}
