package dk.aau.cs.giraf.zebra;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class SequenceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SequenceViewGroup sequenceGroup = (SequenceViewGroup) findViewById(R.id.sequenceViewGroup);
		
		//TODO: At some point use ID from extras to get right sequence
		final Sequence sequence = Test.getSequences(this).get(0);
		
		for (Drawable pictogram : sequence.getPictograms()) {
			SequenceImageView imageView = new SequenceImageView(getApplication());
			imageView.setImageDrawable(pictogram);
			imageView.setDeleteButtonVisibility(View.VISIBLE);
			//sequenceGroup.addView(imageView);
		}
		
		final SequenceAdapter adapter = new SequenceAdapter(this, sequence);
		sequenceGroup.setAdapter(adapter);
		
		//sequenceGroup.setEditModeEnabled(true);
		
		sequenceGroup.setOnRearrangeListener(new SequenceViewGroup.OnRearrangeListener() {
			@Override
			public void onRearrange(int indexFrom, int indexTo) {
				sequence.rearrange(indexFrom, indexTo);			
				adapter.notifyDataSetChanged();
			}
		});
		
		TextView sequenceTitleView = (TextView) findViewById(R.id.sequence_title);
		sequenceTitleView.setText(sequence.getName());
		ImageView sequenceImageView = (ImageView) findViewById(R.id.sequence_image);
		sequenceImageView.setImageDrawable(sequence.getPictograms().get(0));
		
		
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
		        	EditText editText = (EditText) findViewById(R.id.sequence_title);
		        	
		            editText.clearFocus();
		        }
		        return false;
		    }
		});
		
		
		// Create listeners on every view to remove focus from the EditText when touched
		createClearFocusListeners(findViewById(R.id.parent_container));
		
		// Create listener to hide the keyboard and save when the EditText loses focus
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
				{
					
                    EditText editText = (EditText) findViewById(R.id.sequence_title);
		        	
                    
                    hideSoftKeyboardFromView(editText);
                    
                    // TODO Save changes in the title
				}
				
			}
		});
		
		// Get the childID parameter or choose default
		long childId = 16; // Default child for debugging (Ida Christiansen)
		Bundle extras = getIntent().getExtras();
		if (extras != null) {       
			childId = extras.getLong("currentChildId"); 
		}
		
		// Get the full name from the database
		String name = getFullNameFromProfileId(childId);
		
		TextView childName = (TextView) findViewById(R.id.child_name);
		childName.setText(name);
	}
	
	public void hideSoftKeyboardFromView(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	/**
	 * Creates listeners to remove focus from EditText when something else is touched (to hide the softkeyboard)
	 * @param view The parent container. The function runs recursively on its children
	 */
	public void createClearFocusListeners(View view) {
		// Create listener to remove focus from EditText when something else is touched
	    if(!(view instanceof EditText)) {
	        view.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					EditText editText = (EditText) findViewById(R.id.sequence_title);
		            editText.clearFocus();
		            
					return false;
				}

	        });
	    }

	    // If the view is a container, run the function recursively on the children
	    if (view instanceof ViewGroup) {

	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

	            View innerView = ((ViewGroup) view).getChildAt(i);

	            createClearFocusListeners(innerView);
	        }
	    }
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
        Helper helper = new Helper(SequenceActivity.this);
		
		Profile profile = helper.profilesHelper.getProfileById(profileId);
		
		String name = "";
		name = addWordToString(name, profile.getFirstname());
		name = addWordToString(name, profile.getMiddlename());
		name = addWordToString(name, profile.getSurname());
		
		return name;
	}
}
