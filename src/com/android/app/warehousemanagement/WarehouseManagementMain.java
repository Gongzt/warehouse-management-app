	package com.android.app.warehousemanagement;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class WarehouseManagementMain extends FragmentActivity {
	
	private TabHost tabHost;
	private TabManager tabManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		// customized title
		setContentView(R.layout.warehouse_management_main); // 注意顺序      
		
		setupTabHost(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    public void setupTabHost(int currentTabNum) {
	    tabHost = (TabHost)findViewById(android.R.id.tabhost);
	    tabHost.setup();
	    
	    tabManager = new TabManager(this, tabHost, android.R.id.tabcontent);
	    
	    tabHost.setCurrentTab(currentTabNum);
	    
	    TabView view = new TabView(this, R.drawable.tab_current, "库  存");
		tabManager.addTab(
				tabHost.newTabSpec("CurrentFragment").setIndicator(view),
				CurrentFragment.class, null);
		view = new TabView(this, R.drawable.tab_instock, "入  库");
		tabManager.addTab(
				tabHost.newTabSpec("InStockFragment").setIndicator(view),
				InStockFragment.class, null);
		view = new TabView(this, R.drawable.tab_record, "历  史");
		tabManager.addTab(
				tabHost.newTabSpec("RecordFragment").setIndicator(view),
				RecordFragment.class, null);
		view = new TabView(this, R.drawable.tab_setting, "设  置");
		tabManager.addTab(
				tabHost.newTabSpec("SettingFragment").setIndicator(view),
				SettingFragment.class, null);
    }
    
    private class TabView extends LinearLayout {    
    	private ImageView imageView ;  
    	private TextView textView;  
    	          
    	public TabView(Context c, int icon, String text) {  
    		super(c);  
    		setOrientation(VERTICAL);  
    		setGravity(Gravity.CENTER);  
    		             
    		imageView = new ImageView(c);  
    		imageView.setImageDrawable(this.getResources().getDrawable(icon));  
    		imageView.setBackgroundColor(Color.TRANSPARENT);  
    		addView(imageView);  
    		  
    		textView = new TextView(c);  
    		textView.setText(text);    
    		textView.setGravity(Gravity.CENTER);
    		textView.setTextColor(Color.rgb(255, 255, 255));
    		addView(textView);  
    		
    		setBackgroundResource(R.drawable.tab);
    	}  
    }

}
