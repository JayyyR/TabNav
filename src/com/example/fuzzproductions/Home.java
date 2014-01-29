package com.example.fuzzproductions;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;

public class Home extends FragmentActivity {

	public static final int TAB_COUNT = 3;
	//define variables for pager and adapter
	MyPagerAdapter mAdapter;
	ViewPager mPager;
	ActionBar actionBar;
	boolean firstLoad = true;
	public static ArrayList<FuzzItem> data = new ArrayList<FuzzItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		//grab json data
		JSONReader dataGrabber = new JSONReader("http://dev.fuzzproductions.com/MobileTest/test.json", this);
		dataGrabber.execute();

	}

	public void setUp(){

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

	//class to create adapter for our viewpager
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
			if (!firstLoad)			// on first load, you will get an index out of bounds error if you try to select a tab that's not there
				actionBar.selectTab(actionBar.getTabAt(mPager.getCurrentItem()));
			else{
				firstLoad=false;
			}
		}


		@Override
		public int getCount() {
			return TAB_COUNT;
		}
	}

	/*private class to grab json array from url*/
	private class JSONReader extends AsyncTask<String, Void, String>{

		String url;
		Activity activity;
		java.lang.reflect.Type arrayListType = new TypeToken<ArrayList<FuzzItem>>(){}.getType();
		Gson gson = new Gson();
		ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

		public JSONReader(String url, Activity activity){
			this.url = url;
			this.activity = activity;
		};

		@Override
		protected void onPreExecute(){
			//progress dialog displays while connecting to the internet
			progressDialog= ProgressDialog.show(activity, "Grabbing Data","Please Wait", true);
		}

		@Override
		protected String doInBackground(String... params) {

			HttpClient httpClient = new DefaultHttpClient();
			try {
				//grab json data
				HttpResponse response = httpClient.execute(new HttpGet(url));
				HttpEntity entity = response.getEntity();
				Reader reader = new InputStreamReader(entity.getContent());
				//set our arraylist with fuzzitem objects using gson
				data = gson.fromJson(reader, arrayListType);
			} catch (Exception e) {
				Log.e("error", "error grabbing json");
			}
			return null;
		}


		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			for ( FuzzItem item : data){
				Log.v("item", "item id is: " + item.id + " item type is: " + item.type + " item data is: " + item.data);
			}
			progressDialog.dismiss();
			setUp();
		}

	}
}
