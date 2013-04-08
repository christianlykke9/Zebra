package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

public class MainActivity extends Activity {

	List<Child> children;
	List<Sequence> sequences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		
		children = getChildren();
		sequences = getSequences();
		
		ListView childList = (ListView)findViewById(R.id.child_list);
		final ChildAdapter childAdapter = new ChildAdapter(this, children);
		childList.setAdapter(childAdapter);
		
		GridView sequenceGrid = (GridView)findViewById(R.id.sequence_grid);
		final SequenceAdapter sequenceAdapter = new SequenceAdapter(this, sequences);
		sequenceGrid.setAdapter(sequenceAdapter);
		
		//Load Child sequences
		childList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Child child = childAdapter.getItem(arg2);
				sequences.clear();
				sequences.addAll(getSequencesForChild(child));
				sequenceAdapter.notifyDataSetChanged();
			}
		});
		
		//Load Sequence
		sequenceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//Sequence sequence = sequenceAdapter.getItem(arg2);
				Intent intent = new Intent(getApplication(), SequenceActivity.class);
				//TODO: Put sequence id in extras.
				startActivity(intent);
			}
			
		});
		
	}

	private List<Sequence> getSequences() {
		/*ArrayList<Sequence> sequences = new ArrayList<Sequence>();
		
		for (int i = 0; i < 20; i++) {
			Sequence sequence = new Sequence("Lækker musik");
			sequence.setImage(getResources().getDrawable(R.drawable.steve));
			sequences.add(sequence);
		}
		return sequences;*/
		return Test.getSequences(this);
	}
	
	private List<Sequence> getSequencesForChild(Child child) {
		if (child == children.get(0))
			return new ArrayList();
		return getSequences();
	}

	private List<Child> getChildren() {
		ArrayList<Child> children = new ArrayList<Child>();
		
		for (int i = 0; i < 20; i++) {
			Child child = new Child("Sheryl Ann Cole");
			child.setSequenceCount(23);
			children.add(child);
		}
		return children;
	}
}
