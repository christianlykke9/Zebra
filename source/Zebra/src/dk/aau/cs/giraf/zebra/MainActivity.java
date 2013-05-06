package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.List;

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
import dk.aau.cs.giraf.zebra.serialization.SequenceFileStore;

public class MainActivity extends Activity {

	private List<Child> children = ZebraApplication.getChildren();
	private List<Sequence> sequences = new ArrayList<Sequence>();
	
	private ChildAdapter childAdapter;
	
	private GridView sequenceGrid;
	private boolean isInEditMode = false;
	
	private SequenceListAdapter sequenceAdapter;
	
	Child selectedChild;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		childAdapter = new ChildAdapter(this, children);
		
		ListView childList = (ListView)findViewById(R.id.child_list);
		childList.setAdapter(childAdapter);

		sequenceAdapter = new SequenceListAdapter(this, sequences);
		
		sequenceGrid = (GridView)findViewById(R.id.sequence_grid);
		sequenceGrid.setAdapter(sequenceAdapter);
		
		loadSequences();
		
		if (children.size() == 0) {
			Toast toast = Toast.makeText(this, getResources().getString(R.string.no_children), Toast.LENGTH_LONG);
			toast.show();
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
		
			
		// Creates clean sequence and starts the sequence activity - ready to add pictograms.
		final ImageButton createButton = (ImageButton)findViewById(R.id.add_button);
		createButton.setVisibility(isInEditMode ? View.VISIBLE : View.GONE);
		
		createButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Sequence sequence = new Sequence();
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
				
				// Make sure that all views currently not visible will have the correct editmode when they become visible
				sequenceAdapter.setEditModeEnabled(isInEditMode);

				createButton.setVisibility(isInEditMode ? View.VISIBLE : View.GONE);
				
				// Update the editmode of all visible views in the grid
				for (int i = 0; i < sequenceGrid.getChildCount(); i++) {
					View view = sequenceGrid.getChildAt(i);
					
					if (view instanceof PictogramView) {
						((PictogramView)view).setEditModeEnabled(isInEditMode);
					}
				}
			}
		});
	}

	private void loadSequences() {
		for (Child child : children) {
			List<Sequence> list = SequenceFileStore.getSequences(this, child);
			child.setSequences(list);
		}
	}
	
	public void refreshSelectedChild() {
		((TextView)findViewById(R.id.child_name)).setText(selectedChild.getName());
		
		sequences.clear();
		sequences.addAll(selectedChild.getSequences());
		sequenceAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onResume() {

		childAdapter.notifyDataSetChanged();
		refreshSelectedChild();
		
		// Remove highlighting from all images
		for (int i = 0; i < sequenceGrid.getChildCount(); i++) {
			View view = sequenceGrid.getChildAt(i);
			
			if (view instanceof PictogramView) {
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