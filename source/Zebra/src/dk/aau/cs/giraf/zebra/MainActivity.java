package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import dk.aau.cs.giraf.oasis.lib.controllers.ProfilesHelper;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class MainActivity extends Activity {

	private List<Child> children;
	private List<Sequence> sequences;
	
	private ProfilesHelper profileHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		
		profileHelper = new ProfilesHelper(this);
		
		children = getChildren();
		sequences = getSequences(children.get(0));
		
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
								
				Child child = childAdapter.getItem(arg2);
				
				((TextView)findViewById(R.id.child_name)).setText(child.getName());

				sequences.clear();
				sequences.addAll(getSequences(child));
				sequenceAdapter.notifyDataSetChanged();
			}
		});
		
		//Load Sequence
		sequenceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Sequence sequence = sequenceAdapter.getItem(arg2);
				
				Intent intent = new Intent(getApplication(), SequenceActivity.class);
				
				//TODO: LÆKKER MUSIK
				intent.putExtra("profileId", sequence.getChild().getProfile().getId());
				intent.putExtra("sequenceId", sequence.getSequenceId());
				//intent.putExtra("sequence", sequence);
				
				//TODO: Put sequence id in extras.
				startActivity(intent);
			}
			
		});
		
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
