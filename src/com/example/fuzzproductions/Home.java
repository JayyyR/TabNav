package com.example.fuzzproductions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
			ImageDownloader getImages = new ImageDownloader(activity);
			getImages.execute();
		}

	}

	/*private class to download images*/
	private class ImageDownloader extends AsyncTask<String, Void, String>{

		Activity activity;
		ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

		public ImageDownloader(Activity activity){
			this.activity = activity;
		};

		@Override
		protected void onPreExecute(){
			//progress dialog displays while connecting to the internet
			progressDialog= ProgressDialog.show(activity, "Downloading Images","Please Wait", true);
		}

		@Override
		protected String doInBackground(String... params) {

			//go through items
			for (FuzzItem item : data){
				//if we hit an image, download and store it
				if (item.isImage()){
					try {
						URL url = new URL(item.data);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setDoInput(true);
						connection.setConnectTimeout(3000);	//one of the images times out, so i set limit (it's this url: http://images-5.findicons.com/files/icons/1156/fugue/16/grid.png)
						connection.connect();
						InputStream input = connection.getInputStream();
						Bitmap bitmap = BitmapFactory.decodeStream(input);
						item.image = bitmap;
						//also a good amount of the image url's lead to file not found exceptions
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
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
