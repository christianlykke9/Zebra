package dk.aau.cs.giraf.zebra;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ViewGroup sequenceGroup = (ViewGroup) findViewById(R.id.sequenceViewGroup);
		
		int[] drawableIds = new int[] {
			R.drawable.vask_1,
			R.drawable.vask_2,
			R.drawable.vask_3,
			R.drawable.vask_4
		};
		
		Resources resources = getResources();
		
		for (int i = 0; i < drawableIds.length; i++) {
			Drawable pictoImage = resources.getDrawable(drawableIds[i]);
			ImageView imageView = new ImageView(getApplication());
			imageView.setImageDrawable(pictoImage);
			sequenceGroup.addView(imageView);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

}
