package com.android.app.warehousemanagement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.warehousemanagement.db.InnerDBExec;

public class SettingFragment extends ListFragment{
	private String[] items = new String[0];
	private TextView test;
	
	private class RequestTask extends AsyncTask<String, String, String>{

	    @Override
	    protected String doInBackground(String... uri) {
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpResponse response;
	        String responseString = null;
	        try {
	            response = httpclient.execute(new HttpGet(uri[0]));
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                
	                responseString = EntityUtils.toString(response.getEntity(), "gb2312");
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                Log.e("Request Fail", "Error Code: " + statusLine.getReasonPhrase());
	            }
	        } catch (ClientProtocolException e) {
	        	Log.e("ClientProtocolException","Setting fragment http request");
	        } catch (IOException e) {
	        	Log.e("IOException","Setting fragment http request");
	        }
	        return responseString;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        
	        test.setText(result);
	        
	    }
	}
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		
		InnerDBExec db = new InnerDBExec(getActivity());
		/*Cursor c = db.recordSearch("", "", "", "");
		items = new String[c.getCount()];
		for (int i=0; i<c.getCount(); i++){
			items[i] = c.getString(1);
			c.moveToNext();
		}*/
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
       
        return inflater.inflate(R.layout.setting_fragment, container, false);
    }
    
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState){
    	super.onViewCreated(view, savedInstanceState);
    	
		String httpUrl = "http://192.168.0.18/haha.php";
        test = (TextView)getView().findViewById(R.id.SettingTextView);
        
        RequestTask req = new RequestTask();
        req.execute(httpUrl);
	}
	
    @Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		Toast.makeText(getActivity(), "You clicked " + items[position], Toast.LENGTH_SHORT).show();
	}
}
