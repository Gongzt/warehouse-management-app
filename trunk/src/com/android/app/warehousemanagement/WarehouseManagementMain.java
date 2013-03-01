package com.android.app.warehousemanagement;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;

public class WarehouseManagementMain extends FragmentActivity {
	
	private TabHost tabHost;
	private TabManager tabManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		// customized title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); // 注意顺序   
		setContentView(R.layout.activity_main); // 注意顺序   
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.customized_title);  // 注意顺序   
        
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
	    
		tabManager.addTab(
				tabHost.newTabSpec("Fragment1").setIndicator("库存现状"),
				CurrentFragment.class, null);
		tabManager.addTab(
				tabHost.newTabSpec("Fragment2").setIndicator("入库"),
				InStockFragment.class, null);
		tabManager.addTab(
				tabHost.newTabSpec("Fragment3").setIndicator("历史"),
				RecordFragment.class, null);
		tabManager.addTab(
				tabHost.newTabSpec("Fragment4").setIndicator("设置"),
				SettingFragment.class, null);
    }

}
