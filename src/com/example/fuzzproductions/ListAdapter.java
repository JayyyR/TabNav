package com.example.fuzzproductions;

import java.util.List;

import android.content.ClipData.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<FuzzItem> {
	

	public ListAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	}

	public ListAdapter(Context context, int resource, List<FuzzItem> items) {
	    super(context, resource, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    View v = convertView;

	    if (v == null) {

	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.list_item, null);

	    }

	    FuzzItem p = getItem(position);

	    if (p != null) {

	        TextView id = (TextView) v.findViewById(R.id.idTextView);
	        TextView text = (TextView) v.findViewById(R.id.textTextView);
	        TextView image = (TextView) v.findViewById(R.id.imageImageView);

	        if (id != null) {
	        	id.setText(p.id);
	        }
	        if (text != null && p.type.equals("text")) {
	        	text.setVisibility(View.VISIBLE);
	        	image.setVisibility(View.GONE);
	            text.setText(p.data);
	        }
	        else if (image != null) {
	        	image.setVisibility(View.VISIBLE);
	        	text.setVisibility(View.GONE);
	            image.setText(p.data);
	        }
	    }

	    return v;

	}
}
