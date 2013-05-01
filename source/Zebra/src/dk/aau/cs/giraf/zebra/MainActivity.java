package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import dk.aau.cs.giraf.zebra.models.Child;
import dk.aau.cs.giraf.zebra.models.Sequence;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {

	private List<Child> children = ZebraApplication.getChildren();
	private List<Sequence> sequences = new ArrayList<Sequence>();
	
	private GridView sequenceGrid;
	private boolean isInEditMode = false;
	
	private SequenceListAdapter sequenceAdapter;
	
	Child selectedChild;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);

		final ChildAdapter childAdapter = new ChildAdapter(this, children);
		
		ListView childList = (ListView)findViewById(R.id.child_list);
		childList.setAdapter(childAdapter);

		sequenceAdapter = new SequenceListAdapter(this, sequences);
		
		sequenceGrid = (GridView)findViewById(R.id.sequence_grid);
		sequenceGrid.setAdapter(sequenceAdapter);
		
		if (children.size() == 0) {
			Toast.makeText(this, getResources().getString(R.string.no_children), Toast.LENGTH_LONG);
		}
		else {
			selectedChild = children.get(0);
			refreshSelectedChild();
		}
		
		//Load Child
		childList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				selectedChild = childAdapter.getItem(arg2);
				refreshSelectedChild();
				
				final GridView sequenceGridView = ((GridView)findViewById(R.id.sequence_grid));
				
				//TODO: This is ridicilous
				sequenceGridView.smoothScrollToPositionFromTop(0, 0, 0);
				
			}
		});
		
		//Load Sequence
		sequenceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				((PictogramView)arg1).liftUp();
				
				Sequence sequence = sequenceAdapter.getItem(arg2);
				enterSequence(sequence, false);
			}		
		});
		
		
		// Starts a clean sequence activity - ready to add pictograms.
		((ImageButton)findViewById(R.id.add_button)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Sequence sequence = new Sequence();
				sequence.setTitle(getResources().getString(R.string.sequence_default_title));
				sequence.setSequenceId(selectedChild.getNextSequenceId());
				selectedChild.getSequences().add(sequence);
				
				enterSequence(sequence, true);
			}
		});
		
		
		// Edit mode switcher button
		ToggleButton button = (ToggleButton) findViewById(R.id.edit_mode_toggle);
		
		button.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToggleButton button = (ToggleButton)v;
				isInEditMode = button.isChecked();

				for (int i = 0; i < sequenceGrid.getChildCount(); i++) {
					View view = sequenceGrid.getChildAt(i);
					
					if (view instanceof PictogramView) {
						((PictogramView)view).setEditModeEnabled(isInEditMode);
					}
				}
			}
		});
	}
	
	public void refreshSelectedChild() {
		((TextView)findViewById(R.id.child_name)).setText(selectedChild.getName());
		
		sequences.clear();
		sequences.addAll(selectedChild.getSequences());
		sequenceAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onResume() {
		
		refreshSelectedChild();
		
		// Remove highlighting from all images
		for (int i = 0; i < sequenceGrid.getChildCount(); i++)
		{
			View view = sequenceGrid.getChildAt(i);
			
			if (view instanceof PictogramView)
			{
				((PictogramView)view).placeDown();
			}
		}
		
		super.onResume();
	}	

	private void enterSequence(Sequence sequence, boolean isNew) {
		Intent intent = new Intent(getApplication(), SequenceActivity.class);
		intent.putExtra("profileId", selectedChild.getProfileId());
		intent.putExtra("sequenceId", sequence.getSequenceId());
		intent.putExtra("editMode", isInEditMode);
		intent.putExtra("new", isNew);

		startActivity(intent);
	}
}