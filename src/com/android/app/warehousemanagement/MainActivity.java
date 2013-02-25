package com.android.app.warehousemanagement;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setupTabHost(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    public void setupTabHost(int currentTabNum) {
	    TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
	    tabHost.setup();
	    
	    TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1").setContent(R.id.tab1).setIndicator("库存现况");
	    TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2").setContent(R.id.tab2).setIndicator("入库");
	    TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3").setContent(R.id.tab3).setIndicator("历史记录");
	    TabHost.TabSpec tab4 = tabHost.newTabSpec("tab4").setContent(R.id.tab4).setIndicator("设置");
	    
	    tabHost.addTab(tab1);
	    tabHost.addTab(tab2);
	    tabHost.addTab(tab3);
	    tabHost.addTab(tab4);
	    
	    tabHost.setCurrentTab(currentTabNum);
	    tabHost.setOnTabChangedListener(new TabHostOnTabChangedListener());
    }

}

class TabHostOnTabChangedListener implements OnTabChangeListener {
	
	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
	}
}
