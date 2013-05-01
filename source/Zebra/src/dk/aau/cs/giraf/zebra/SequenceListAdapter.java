package dk.aau.cs.giraf.zebra;

import java.util.List;

import dk.aau.cs.giraf.zebra.models.Child;
import dk.aau.cs.giraf.zebra.models.Sequence;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Adapter for a List of Sequences typically associated with a {@link Child}.
 *
 */
public class SequenceListAdapter extends BaseAdapter {

	private List<Sequence> items;
	private Context context;
	
	public SequenceListAdapter(Context context, List<Sequence> items) {
		
		this.items = items;
		this.context = context;
	}

    public View getView(int position, View convertView, ViewGroup parent) {
		PictogramView v = (PictogramView) convertView;
		
		if (v == null) {
			v = new PictogramView(context, 16f);
		}
		
        Sequence s = items.get(position);
        
        v.setTitle(s.getTitle());
        // TODO: GET THE IMAGE ID
        //v.setImage(s.getImageId());

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
