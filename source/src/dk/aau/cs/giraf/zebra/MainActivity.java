package dk.aau.cs.giraf.zebra;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

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
		
		
		initializeTopBar();
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	
	private void initializeTopBar() {
        TextView editText = (TextView) findViewById(R.id.sequence_title);
		
        // Create listener to remove focus when "Done" is pressed on the keyboard
		editText.setOnEditorActionListener(new OnEditorActionListener() {        
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if(actionId==EditorInfo.IME_ACTION_DONE) {
		        	TextView editText = (TextView) findViewById(R.id.sequence_title);
		        	
		            editText.clearFocus();
		        }
		        return false;
		    }
		});
		
		
		// Create listener to hide the keyboard and save when the textbox loses focus
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
				{
                    TextView editText = (TextView) findViewById(R.id.sequence_title);
		        	
		        	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    
                    // TODO Save changes in the title
				}
				
			}
		});
		
		long childId;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {        	   
			childId = extras.getLong("currentChildID");
		} else {
        	childId = 16; // Default child for debugging (Ida Christiansen)
        }
		
		
		String name = getFullNameFromProfileId(childId);
		
		TextView childName = (TextView) findViewById(R.id.child_name);
		childName.setText(name);
	}
	
	private String addWordToString(String string, String word) {
		if (word != null) {
			if (!string.isEmpty())
			{
				string += " ";
			}
			string += word;
		}
		
		return string;
	}
	
	public String getFullNameFromProfileId(long profileId)
	{
        Helper helper = new Helper(MainActivity.this);
		
		Profile profile = helper.profilesHelper.getProfileById(profileId);
		
		String name = "";
		name = addWordToString(name, profile.getFirstname());
		name = addWordToString(name, profile.getMiddlename());
		name = addWordToString(name, profile.getSurname());
		
		return name;
	}
}
