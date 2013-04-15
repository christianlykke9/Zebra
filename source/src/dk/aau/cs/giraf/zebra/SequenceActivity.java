package dk.aau.cs.giraf.zebra;

import java.util.List;
import java.util.Random;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.controllers.ProfilesHelper;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class SequenceActivity extends Activity {
	
	private Sequence sequence;
	
	private ProfilesHelper profileHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		profileHelper = new ProfilesHelper(this);
		
		SequenceViewGroup sequenceGroup = (SequenceViewGroup) findViewById(R.id.sequenceViewGroup);
		
//		sequence = getIntent().getParcelableExtra("sequence");
		
//		//TODO: MIDLERTIDIG MÅDE!!!! HENT FRA DATABASE
		long profileId = getIntent().getExtras().getLong("profileId");
		int sequenceId = getIntent().getExtras().getInt("sequenceId");
		
		Profile p = profileHelper.getProfileById(profileId);
//		
//		Profile p = new Profile();
//		p.setFirstname("Noah");
//		p.setMiddlename("");
//		p.setSurname("Nielsen");
		
		Child child = new Child(p);
		sequence = Test.createSequence(child, sequenceId, this);

		for (Drawable pictogram : sequence.getPictograms()) {
			RoundedImageView image = new RoundedImageView(this, 15f);
			image.setImageDrawable(pictogram);
			
			PictogramView pictogramView = new PictogramView(this, image, "Testing");
			
			sequenceGroup.addView(pictogramView);
		}
		
		sequenceGroup.setOnRearrangeListener(new SequenceViewGroup.OnRearrangeListener() {
			@Override
			public void onRearrange(int indexFrom, int indexTo) {
				sequence.rearrange(indexFrom, indexTo);			
			}
		});
		
		TextView sequenceTitleView = (TextView) findViewById(R.id.sequence_title);
		sequenceTitleView.setText(sequence.getName());
		ImageView sequenceImageView = (ImageView) findViewById(R.id.sequence_image);
		sequenceImageView.setImageDrawable(sequence.getPictograms().get(0));
		
		
		initializeTopBar();
		

		
		ImageButton button = (ImageButton) findViewById(R.id.imageButton1);
		button.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditMode.toggle();

				SequenceViewGroup sequenceGroup = (SequenceViewGroup) findViewById(R.id.sequenceViewGroup);
				for (int i = 0; i < sequenceGroup.getChildCount(); i++) {
					sequenceGroup.getChildAt(i).invalidate();
				}
			}
		});
		
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

		TextView childName = (TextView)findViewById(R.id.child_name);
		childName.setText(sequence.getChild().getName());
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
}
