package dk.aau.cs.giraf.zebra;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ViewGroup sequenceGroup = (ViewGroup) findViewById(R.id.sequenceViewGroup);
		
		Sequence sequence = Test.createSequence(this);
		
		for (Drawable pictogram : sequence.pictograms) {
			ImageView imageView = new ImageView(getApplication());
			imageView.setImageDrawable(pictogram);
			sequenceGroup.addView(imageView);
		}
		
		TextView sequenceTitleView = (TextView) findViewById(R.id.sequence_title);
		sequenceTitleView.setText(sequence.getName());
		ImageView sequenceImageView = (ImageView) findViewById(R.id.sequence_image);
		sequenceImageView.setImageDrawable(sequence.pictograms.get(0));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

}
