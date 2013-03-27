package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChildAdapter extends BaseAdapter {

	private ArrayList<Child> items;
	private LayoutInflater inflater;
	
	public ChildAdapter(Activity activity, ArrayList<Child> items) {
		
		this.items = items;
		this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

    public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			v = inflater.inflate(R.layout.child_row, null);
		}
		
        TextView nameTextView = (TextView)v.findViewById(R.id.name);
        TextView countTextView = (TextView)v.findViewById(R.id.count);
        ImageView childImage = (ImageView)v.findViewById(R.id.child_image);
 
        Child c = items.get(position);
 
        nameTextView.setText(c.getName());
        countTextView.setText(c.getSequenceCount() + " sekvenser");
        childImage.setImageDrawable(c.getImage());
        return v;
    }

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Child getItem(int position) {
        return items.get(position);
    }
	
	@Override
	public long getItemId(int position) {
        return position;
    }
}
