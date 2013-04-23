package dk.aau.cs.giraf.zebra;

import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.Duration;

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
import dk.aau.cs.giraf.oasis.lib.controllers.ProfilesHelper;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class MainActivity extends Activity {

	private List<Child> children;
	private List<Sequence> sequences;
	
	private ProfilesHelper profileHelper;
	
	Child child;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		
		profileHelper = new ProfilesHelper(this);
		
		children = getChildren();
		//TODO: Make this robust.
		child = children.get(0);
		sequences = getSequences(child);
		
		ListView childList = (ListView)findViewById(R.id.child_list);
		final ChildAdapter childAdapter = new ChildAdapter(this, children);
		childList.setAdapter(childAdapter);
		
		GridView sequenceGrid = (GridView)findViewById(R.id.sequence_grid);
		final SequenceListAdapter sequenceAdapter = new SequenceListAdapter(this, sequences);
		sequenceGrid.setAdapter(sequenceAdapter);
		
		//Load Child sequences
		childList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								
				child = childAdapter.getItem(arg2);
				
				((TextView)findViewById(R.id.child_name)).setText(child.getName());
								
				sequences.clear();
				sequences.addAll(getSequences(child));
				sequenceAdapter.notifyDataSetChanged();
				
				final GridView sequenceGridView = ((GridView)findViewById(R.id.sequence_grid));
				
				//TODO: This is ridicilous
				sequenceGridView.smoothScrollToPositionFromTop(0, 0, 0);
				
			}
		});
		
		//Load Sequence
		sequenceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Sequence sequence = sequenceAdapter.getItem(arg2);
				
				enterSequence(sequence);
			}		
		});
		
		
		// Starts a clean sequence activity - ready to add pictograms.
		((ImageButton)findViewById(R.id.add_button)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intent newSequenceIntent = new Intent(getApplication(), SequenceActivity.class);
//				newSequenceIntent.putExtra("profileId", null);
//				newSequenceIntent.putExtra("sequenceId", 0);
				 
				Sequence newSequence = new Sequence(child, "Ny Sekvens");
				//TODO: Fix this id mess.
				newSequence.setSequenceId(1);
				
				enterSequence(newSequence);
			}
		});
		
	}

	private void enterSequence(Sequence sequence) {
		Intent intent = new Intent(getApplication(), SequenceActivity.class);
		
		//TODO: LÆKKER MUSIK
		intent.putExtra("profileId", sequence.getChild().getProfile().getId());
		intent.putExtra("sequenceId", sequence.getSequenceId());
		//intent.putExtra("sequence", sequence);
		
		//TODO: Put sequence id in extras.
		startActivity(intent);
	}
	
	private List<Sequence> getSequences(Child child) {
		return child.getSequences();
	}

	private List<Child> getChildren() {
		
		List<Profile> profiles = profileHelper.getChildren();
		
		ArrayList<Child> children = new ArrayList<Child>();
		
		int i = 3;
		
		for (Profile p : profiles) {
			Child c = new Child(p);
			children.add(c);
			
			c.setSequences(Test.getSequences(c, (i % 4), this));
			i++;
		}
		
		return children;
	}
}
