package com.example.fuzzproductions;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ArrayListFragment  extends ListFragment {
	int mNum;

	//new instance of our list fragment, pass num as the argument to indicate what page we're on
	static ArrayListFragment newInstance(int num) {
		ArrayListFragment f = new ArrayListFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}

	//grab num which is our page number
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments() != null ? getArguments().getInt("num") : 1;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//String[] temp = {"test", "hi","go"};
		//create new custom adapter
		ListAdapter customAdapter;
		Log.v("num", "mNum is: " + mNum);
		//all the data
		if(mNum == 0){
			customAdapter = new ListAdapter(getActivity(), R.layout.list_item, Home.data);
		}
		//just text
		else if (mNum == 1){
			//create new list with just text items
			ArrayList<FuzzItem> textItems = new ArrayList<FuzzItem>();
			for (FuzzItem item: Home.data){
				if (item.isText())
					textItems.add(item);
			}
			customAdapter = new ListAdapter(getActivity(), R.layout.list_item, textItems);
		}
		//just images
		else{
			//create new list with just image items
			ArrayList<FuzzItem> imageItems = new ArrayList<FuzzItem>();
			for (FuzzItem item: Home.data){
				if (item.isImage())
					imageItems.add(item);
			}
			customAdapter = new ListAdapter(getActivity(), R.layout.list_item, imageItems);
		}

		setListAdapter(customAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i("FragmentList", "Item clicked: " + id);
		
		Intent myIntent = new Intent(getActivity(), WebPageActivity.class);
		getActivity().startActivity(myIntent);
	}
}