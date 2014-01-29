package com.example.fuzzproductions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.ViewGroup;

public class Home extends FragmentActivity {

	//define variables for pager and adapter
	MyPagerAdapter mAdapter;
	ViewPager mPager;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		actionBar = getActionBar();

		//create new adapter from custom class
		mAdapter = new MyPagerAdapter(getSupportFragmentManager());

		//grab pager and set adapter appropriately
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		
		// Specify that tabs should be displayed in the action bar.
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    // Create a tab listener that is called when the user changes tabs.
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(Tab tab,
					android.app.FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}

			//when tab is selected go to the correct page
			@Override
			public void onTabSelected(Tab tab,
					android.app.FragmentTransaction ft) {
				mPager.setCurrentItem(tab.getPosition());
				
			}

			@Override
			public void onTabUnselected(Tab tab,
					android.app.FragmentTransaction ft) {
				
			}
	    };

	    // Add 3 tabs, specifying the tab's text and TabListener
	    for (int i = 0; i < 3; i++) {
	    	Tab tab = actionBar.newTab()
	    			.setTabListener(tabListener);
	    	if (i == 0)
	    		tab.setText("All");
	    	else if (i==1)
	    		tab.setText("Text");
	    	else
	    		tab.setText("Images");
	        actionBar.addTab(tab);
	    }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {
	    public MyPagerAdapter(FragmentManager fm) {
	        super(fm);
	    }


	    @Override
	    public Fragment getItem(int position) {
	        return ArrayListFragment.newInstance(position);
	    }
	    



		@Override
		public void finishUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			super.finishUpdate(container);
			
			//change tabs
			actionBar.selectTab(actionBar.getTabAt(mPager.getCurrentItem()));;
		}


		@Override
		public int getCount() {
			return 3;
		}
	}

}
