package com.android.app.warehousemanagement.db;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class OutterDBExec {
	
	HttpClient httpclient;
	
	public OutterDBExec(){
		httpclient = new DefaultHttpClient();
	}
	
	public class OutterDBException extends Exception{
		private static final long serialVersionUID = 1L;
		String title;
		String message;
		
		public OutterDBException(String _title, String _message){
			title = _title;
			message = _message;
		}
		
		public String getTitle(){
			return title;
		}
		
		public String getMessage(){
			return message;
		}
	}
		
	@SuppressWarnings("unchecked")
	private ArrayList<HashMap<String, Object>> jsonConvArray(JSONArray array) throws JSONException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i<array.length(); i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			JSONObject object = array.getJSONObject(i);
			
			Iterator<String> keys = (Iterator<String>) object.keys();  
			while (keys.hasNext()){
				String key = keys.next();
				map.put(key, object.get(key));
			}
			
			arrayList.add(map);
		}
		
		return arrayList;
	}
	
	public ArrayList<HashMap<String, Object>> warehouseSelectAll() throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = null;
		
	    HttpResponse response;
	    HttpPost request = new HttpPost(OutterDB.URL + OutterDB.WarehouseSelectAll.METHOD_NAME);
	        
	    try {
	    	response = httpclient.execute(request);
	        StatusLine statusLine = response.getStatusLine();
	            
	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        	String responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	            arrayList = jsonConvArray(new JSONArray(responseString));
	        } 
	        else{
	            response.getEntity().getContent().close();
	            Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	        }
	    } catch (JSONException e){
	    	throw new OutterDBException("程序错误提示", "warehouseSelectAll:JSONException");
	    } catch (ClientProtocolException e) {
	    	throw new OutterDBException("程序错误提示", "warehouseSelectAll:ClientProtocolException");
	    } catch (IOException e) {
	    	throw new OutterDBException("程序错误提示", "warehouseSelectAll:IOException");
	    }
		
		return arrayList;
	}

	public ArrayList<HashMap<String, Object>> currentSearch(String keyword, String sortby) throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();

	    HttpResponse response;
	    HttpPost request = new HttpPost(OutterDB.URL + OutterDB.CurrentSearch.METHOD_NAME);

	    try {
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
	    	nameValuePairs.add(new BasicNameValuePair(OutterDB.CurrentSearch.ARGUMENT_NAME_KEYWORD, keyword));
	 	    nameValuePairs.add(new BasicNameValuePair(OutterDB.CurrentSearch.ARGUMENT_NAME_SORTBY, sortby));  
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "gb2312"));
	        response = httpclient.execute(request);
	        StatusLine statusLine = response.getStatusLine();
	            
	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        	String responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	            arrayList = jsonConvArray(new JSONArray(responseString));
	        } 
	        else{
	        	response.getEntity().getContent().close();
	            Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	        }
	    } catch (UnsupportedEncodingException e1) {
	    	throw new OutterDBException("程序错误提示", "currentSearch:UnsupportedEncodingException");
	    } catch (JSONException e){
	    	throw new OutterDBException("程序错误提示", "currentSearch:JSONException");
	    } catch (ClientProtocolException e) {
	    	throw new OutterDBException("程序错误提示", "currentSearch:ClientProtocolException");
	    } catch (IOException e) {
	    	throw new OutterDBException("程序错误提示", "currentSearch:IOException");
	    }
		
		return arrayList;
	}

	public ArrayList<HashMap<String, Object>> recordSearch(String keyword, String sortby, String startdate, String enddate) throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		
		HttpResponse response;
	    HttpPost request = new HttpPost(OutterDB.URL + OutterDB.RecordSearch.METHOD_NAME);

	    try {
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordSearch.ARGUMENT_NAME_KEYWORD, keyword));
	 	    nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordSearch.ARGUMENT_NAME_SORTBY, sortby));
	 	    nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordSearch.ARGUMENT_NAME_STARTDATE, startdate));
	 	    nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordSearch.ARGUMENT_NAME_ENDDATE, enddate));
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "gb2312"));
	        response = httpclient.execute(request);
	        StatusLine statusLine = response.getStatusLine();
	            
	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        	String responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	            arrayList = jsonConvArray(new JSONArray(responseString));
	        } 
	        else{
	        	response.getEntity().getContent().close();
	            Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	        }
	    } catch (UnsupportedEncodingException e1) {
	    	throw new OutterDBException("程序错误提示", "recordSearch:UnsupportedEncodingException");
	    } catch (JSONException e){
	    	throw new OutterDBException("程序错误提示", "recordSearch:JSONException");
	    } catch (ClientProtocolException e) {
	    	throw new OutterDBException("程序错误提示", "recordSearch:ClientProtocolException");
	    } catch (IOException e) {
	    	throw new OutterDBException("程序错误提示", "recordSearch:IOException");
	    }
		
		return arrayList;
	}

	public ArrayList<HashMap<String, Object>> currentSelectByNameAndType (String name, int type) throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		
	    HttpResponse response;
	    HttpPost request = new HttpPost(OutterDB.URL + OutterDB.CurrentSelectByNameAndType.METHOD_NAME);

	    try {
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
	    	nameValuePairs.add(new BasicNameValuePair(OutterDB.CurrentSelectByNameAndType.ARGUMENT_NAME_NAME, name));
	 	    nameValuePairs.add(new BasicNameValuePair(OutterDB.CurrentSelectByNameAndType.ARGUMENT_NAME_TYPE, type+""));

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "gb2312"));
	        response = httpclient.execute(request);
	        StatusLine statusLine = response.getStatusLine();
	            
	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	            String responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	            arrayList = jsonConvArray(new JSONArray(responseString));
	        } 
	        else{
	            response.getEntity().getContent().close();
	            Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	        }
	    } catch (UnsupportedEncodingException e1) {
	    	throw new OutterDBException("程序错误提示", "currentSelectByNameAndType:UnsupportedEncodingException");
	    } catch (JSONException e){
	    	throw new OutterDBException("程序错误提示", "currentSelectByNameAndType:JSONException");
	    } catch (ClientProtocolException e) {
	    	throw new OutterDBException("程序错误提示", "currentSelectByNameAndType:ClientProtocolException");
	    } catch (IOException e) {
	    	throw new OutterDBException("程序错误提示", "currentSelectByNameAndType:IOException");
	    }
		
		return arrayList;
	}

	public ArrayList<HashMap<String, Object>> recordSelectByNameAndType (String name, int type, String sortby) throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
	    
		HttpResponse response;
	    HttpPost request = new HttpPost(OutterDB.URL + OutterDB.RecordSelectByNameAndType.METHOD_NAME);

	    try {
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordSelectByNameAndType.ARGUMENT_NAME_NAME, name));
	 	    nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordSelectByNameAndType.ARGUMENT_NAME_TYPE, type+""));
	 	    nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordSelectByNameAndType.ARGUMENT_NAME_SORTBY, sortby));

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "gb2312"));
	        response = httpclient.execute(request);
	        StatusLine statusLine = response.getStatusLine();
	            
	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        	String responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	            arrayList = jsonConvArray(new JSONArray(responseString));
	        } 
	        else{
	        	response.getEntity().getContent().close();
	            Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	        }
	    } catch (UnsupportedEncodingException e1) {
	    	throw new OutterDBException("程序错误提示", "recordSelectByNameAndType:UnsupportedEncodingException");
	    } catch (JSONException e){
	    	throw new OutterDBException("程序错误提示", "recordSelectByNameAndType:JSONException");
	    } catch (ClientProtocolException e) {
	    	throw new OutterDBException("程序错误提示", "recordSelectByNameAndType:ClientProtocolException");
	    } catch (IOException e) {
	    	throw new OutterDBException("程序错误提示", "recordSelectByNameAndType:IOException");
	    }
		
		return arrayList;
	}

	public HashMap<String, Object> recordSelectById (int id) throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();

	    HttpResponse response;
	    HttpPost request = new HttpPost(OutterDB.URL + OutterDB.RecordSelectByID.METHOD_NAME);

	    try {
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
	    	nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordSelectByID.ARGUMENT_NAME_RECORDID, id+""));

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "gb2312"));
	        response = httpclient.execute(request);
	        StatusLine statusLine = response.getStatusLine();
	            
	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        	String responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	            arrayList = jsonConvArray(new JSONArray(responseString));
	        } 
	        else{
	        	response.getEntity().getContent().close();
	            Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	        }
	    } catch (UnsupportedEncodingException e1) {
	    	throw new OutterDBException("程序错误提示", "recordSelectById:UnsupportedEncodingException");
	    } catch (JSONException e){
	    	throw new OutterDBException("程序错误提示", "recordSelectById:JSONException");
	    } catch (ClientProtocolException e) {
	    	throw new OutterDBException("程序错误提示", "recordSelectById:ClientProtocolException");
	    } catch (IOException e) {
	    	throw new OutterDBException("程序错误提示", "recordSelectById:IOException");
	    }
		
		return arrayList.get(0);
	}

	public ArrayList<HashMap<String, Object>> entrySelectAll () throws OutterDBException{
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();

	    HttpResponse response;
	    HttpPost request = new HttpPost(OutterDB.URL + OutterDB.EntrySelectAll.METHOD_NAME);

	    try {
	    	response = httpclient.execute(request);
	        StatusLine statusLine = response.getStatusLine();
	            
	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        	String responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	        	arrayList = jsonConvArray(new JSONArray(responseString));
	        } 
	        else{
	        	response.getEntity().getContent().close();
	            Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	        }
	    } catch (UnsupportedEncodingException e1) {
	    	throw new OutterDBException("程序错误提示", "entrySelectAll:UnsupportedEncodingException");
	    } catch (JSONException e){
	    	throw new OutterDBException("程序错误提示", "entrySelectAll:JSONException");
	    } catch (ClientProtocolException e) {
	    	throw new OutterDBException("程序错误提示", "entrySelectAll:ClientProtocolException");
	    } catch (IOException e) {
	    	throw new OutterDBException("程序错误提示", "entrySelectAll:IOException");
	    }
		
		return arrayList;
	}

	public void recordInsert (String name, int type, String unit, int inorout, int amount, int warehouseId, String remark) throws OutterDBException{
		
	    HttpResponse response;
	    HttpPost request = new HttpPost(OutterDB.URL + OutterDB.RecordInsert.METHOD_NAME);

	    try {
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordInsert.ARGUMENT_NAME_NAME, name));
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordInsert.ARGUMENT_NAME_TYPE, type+""));
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordInsert.ARGUMENT_NAME_WAREHOUSEID, warehouseId+""));
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordInsert.ARGUMENT_NAME_AMOUNT, amount+""));
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordInsert.ARGUMENT_NAME_UNIT, unit));
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordInsert.ARGUMENT_NAME_INOROUT, inorout+""));
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordInsert.ARGUMENT_NAME_REMARK, remark));

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "gb2312"));
	        	
	        response = httpclient.execute(request);
	        StatusLine statusLine = response.getStatusLine();
	            
	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        	String responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	        } 
	        else{
	        	response.getEntity().getContent().close();
	            Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	        }
	    } catch (UnsupportedEncodingException e1) {
	    	throw new OutterDBException("程序错误提示", "recordInsert:UnsupportedEncodingException");
	    } catch (ClientProtocolException e) {
	    	throw new OutterDBException("程序错误提示", "recordInsert:ClientProtocolException");
	    } catch (IOException e) {
	    	throw new OutterDBException("程序错误提示", "recordInsert:IOException");
	    }
	}

	public int entrySelectUsableAmount (int entryId, int warehouseId) throws OutterDBException{
		int usableAmount = 0;
	        
		HttpResponse response;
	    HttpPost request = new HttpPost(OutterDB.URL + OutterDB.EntrySelectUsableAmount.METHOD_NAME);

	    try {
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.EntrySelectUsableAmount.ARGUMENT_NAME_ENTRYID, entryId+""));
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.EntrySelectUsableAmount.ARGUMENT_NAME_WAREHOUSEID, warehouseId+""));

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "gb2312"));
	        	
	        response = httpclient.execute(request);
	        StatusLine statusLine = response.getStatusLine();
	            
	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        	String responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	            ArrayList<HashMap<String, Object>> arrayList = jsonConvArray(new JSONArray(responseString));
	            usableAmount = Integer.parseInt(arrayList.get(0).get(SqlDBTable.Current.COLUMN_NAME_AMOUNT).toString());
	        } 
	        else{
	        	response.getEntity().getContent().close();
	            Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	        }
	    } catch (UnsupportedEncodingException e1) {
	    	throw new OutterDBException("程序错误提示", "entrySelectUsableAmount:UnsupportedEncodingException");
	    } catch (JSONException e){
	    	throw new OutterDBException("程序错误提示", "entrySelectUsableAmount:JSONException");
	    } catch (ClientProtocolException e) {
	    	throw new OutterDBException("程序错误提示", "entrySelectUsableAmount:ClientProtocolException");
	    } catch (IOException e) {
	    	throw new OutterDBException("程序错误提示", "entrySelectUsableAmount:IOException");
	    }
		
		return usableAmount;
	}

	public void recordUpdate (int recordId) throws OutterDBException{
		
	    HttpResponse response;
	    HttpPost request = new HttpPost(OutterDB.URL + OutterDB.RecordUpdate.METHOD_NAME);

	    try {
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordUpdate.ARGUMENT_NAME_RECORDID, recordId+""));

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "gb2312"));
	        	
	        response = httpclient.execute(request);
	        StatusLine statusLine = response.getStatusLine();
	            
	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        	String responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	        } 
	        else{
	        	response.getEntity().getContent().close();
	            Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	        }
	    } catch (UnsupportedEncodingException e1) {
	    	throw new OutterDBException("程序错误提示", "recordUpdate:UnsupportedEncodingException");
	    } catch (ClientProtocolException e) {
	    	throw new OutterDBException("程序错误提示", "recordUpdate:ClientProtocolException");
	    } catch (IOException e) {
	    	throw new OutterDBException("程序错误提示", "recordUpdate:IOException");
	    }
	}
	
	public void recordDelete (int recordId) throws OutterDBException{
		HttpResponse response;
	    HttpPost request = new HttpPost(OutterDB.URL + OutterDB.RecordDelete.METHOD_NAME);

	    try {
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        nameValuePairs.add(new BasicNameValuePair(OutterDB.RecordDelete.ARGUMENT_NAME_RECORDID, recordId+""));

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "gb2312"));
	        	
	        response = httpclient.execute(request);
	        StatusLine statusLine = response.getStatusLine();
	            
	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        	String responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	        } 
	        else{
	        	response.getEntity().getContent().close();
	            Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	        }
	    } catch (UnsupportedEncodingException e1) {
	    	throw new OutterDBException("程序错误提示", "recordDelete:UnsupportedEncodingException");
	    } catch (ClientProtocolException e) {
	    	throw new OutterDBException("程序错误提示", "recordDelete:ClientProtocolException");
	    } catch (IOException e) {
	    	throw new OutterDBException("程序错误提示", "recordDelete:IOException");
	    }
	}
}
