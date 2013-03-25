package dk.aau.cs.giraf.zebra;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;

public class OverviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		
		
		TableLayout table = (TableLayout)findViewById(R.id.child_table);
		
		
	}
	
	

}
