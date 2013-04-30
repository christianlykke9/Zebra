package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SequenceAdapter extends BaseAdapter {
	
	private Sequence sequence;
	private Context context;
	
	private OnCreateViewListener onCreateViewListener;
	
	public SequenceAdapter(Context context, Sequence sequence) {
		this.context = context;
		this.sequence = sequence;
	}

	@Override
	public int getCount() {
		if (sequence == null)
			return 0;
		else
			return sequence.getPictograms().size();
	}

	@Override
	public Object getItem(int position) {
		if (sequence == null) throw new IllegalStateException("No Sequence has been set for this Adapter");
		
		if (position >= 0 && position < sequence.getPictograms().size())
			return sequence.getPictograms().get(position);
		else
			return null;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	};
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		PictogramView view;
		
		if (convertView == null) {
			view = new PictogramView(context, 24f);
		} else
			view = (PictogramView)convertView;
		
		// TODO: GET THE IMAGE ID
		//view.setImage(sequence.getImageId());
		
		if (onCreateViewListener != null)
			onCreateViewListener.onCreateView(position, view);
		
		return view;
	}
	
	public void setOnCreateViewListener(OnCreateViewListener onCreateViewListener) {
		this.onCreateViewListener = onCreateViewListener;
	}
	
	public OnCreateViewListener getOnCreateViewListener() {
		return this.onCreateViewListener;
	}
	
	public interface OnCreateViewListener {
		public void onCreateView(int position, View view);
	}
}
