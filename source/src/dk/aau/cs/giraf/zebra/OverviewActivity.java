package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

public class OverviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		
		ArrayList<Child> children = getChildren();
		
		ChildAdapter childAdapter = new ChildAdapter(this, children);
		
		ListView childList = (ListView)findViewById(R.id.child_list);
		childList.setAdapter(childAdapter);
		
		ArrayList<Sequence> sequences = getSequences();
		
		SequenceAdapter sequenceAdapter = new SequenceAdapter(this, sequences);
		
		GridView sequenceGrid = (GridView)findViewById(R.id.sequence_grid);
		sequenceGrid.setAdapter(sequenceAdapter);
		
	}

	private ArrayList<Sequence> getSequences() {
		ArrayList<Sequence> sequences = new ArrayList<Sequence>();
		
		for (int i = 0; i < 20; i++) {
			Sequence sequence = new Sequence("Lækker musik");
			sequence.setImage(getResources().getDrawable(R.drawable.steve));
			sequences.add(sequence);
		}
		return sequences;
	}

	private ArrayList<Child> getChildren() {
		ArrayList<Child> children = new ArrayList<Child>();
		
		for (int i = 0; i < 20; i++) {
			Child child = new Child();
			child.setName("Sheryl Ann Cole");
			child.setSequenceCount(23);
			children.add(child);
		}
		return children;
	}
}
