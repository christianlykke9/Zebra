package dk.aau.cs.giraf.zebra;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SequenceAdapter extends BaseAdapter {

	private List<Sequence> items;
	private LayoutInflater inflater;
	
	public SequenceAdapter(Activity activity, List<Sequence> items) {
		
		this.items = items;
		this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

    public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			v = inflater.inflate(R.layout.sequence_item, null);
		}
		
        TextView titleTextView = (TextView)v.findViewById(R.id.title);
        ImageView image = (ImageView)v.findViewById(R.id.image);
 
        Sequence s = items.get(position);
 
        titleTextView.setText(s.getName());
        image.setImageDrawable(s.getImage());
        return v;
    }

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Sequence getItem(int position) {
        return items.get(position);
    }
	
	@Override
	public long getItemId(int position) {
        return position;
    }

}
