package dk.aau.cs.giraf.zebra;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import dk.aau.cs.giraf.zebra.PictogramView.OnDeleteClickListener;
import dk.aau.cs.giraf.zebra.SequenceAdapter.OnCreateViewListener;
import dk.aau.cs.giraf.zebra.SequenceViewGroup.OnNewButtonClickedListener;

import dk.aau.cs.giraf.zebra.models.Child;
import dk.aau.cs.giraf.zebra.models.Pictogram;
import dk.aau.cs.giraf.zebra.models.Sequence;

public class SequenceActivity extends Activity {

	private Sequence originalSequence;
	private Sequence sequence;
	
	private Child child;
	
	private boolean isInEditMode;
	private boolean isNew;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final SequenceViewGroup sequenceGroup = (SequenceViewGroup) findViewById(R.id.sequenceViewGroup);
		
		sequenceGroup.setEditModeEnabled(isInEditMode);
		
		Bundle extras = getIntent().getExtras();
		long profileId = extras.getLong("profileId");
		long sequenceId = extras.getLong("sequenceId");
		isNew = extras.getBoolean("new");
		isInEditMode = extras.getBoolean("editMode");

		child = ZebraApplication.getChildFromId(profileId);
		originalSequence = child.getSequenceFromId(sequenceId);
		
		// Get a clone of the sequence so the original sequence is not modified
		sequence = originalSequence.getClone();

		//Create Adapter
		final SequenceAdapter adapter = setupAdapter();
		
		//Create Sequence Group
		@SuppressWarnings("unused")
		final SequenceViewGroup sequenceViewGroup = setupSequenceViewGroup(adapter);
		
		final TextView sequenceTitleView = (TextView) findViewById(R.id.sequence_title);
		sequenceTitleView.setText(sequence.getTitle());
		
		ImageView sequenceImageView = (ImageView) findViewById(R.id.sequence_image);
		
		if (sequence.getImage() == null) {
			sequenceImageView.setImageDrawable(getResources().getDrawable(R.drawable.sequence_placeholder));
		}
		else {
			//TODO: Get the pictogram from the factory here..
			//sequenceImageView.setImageDrawable(sequence.getImageId());
		}
		
		if (isNew) {
			Toast.makeText(this, getResources().getString(R.string.help_new_sequence), Toast.LENGTH_LONG).show();
		}
		
		initializeTopBar();
		
		sequenceImageView.setOnClickListener(new ImageView.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isInEditMode) {
					Intent intent = new Intent();
					intent.setComponent(new ComponentName("dk.aau.cs.giraf.pictoadmin", "dk.aau.cs.giraf.pictoadmin.PictoAdminMain"));
					startActivityForResult(intent, 1);
				}
			}
		});

		final ImageButton cancelButton = (ImageButton)findViewById(R.id.cancel_button);
		final ImageButton okButton = (ImageButton)findViewById(R.id.ok_button);
		
		if (!isInEditMode) {
			cancelButton.setVisibility(View.GONE);
			okButton.setVisibility(View.GONE);
		}
		
		cancelButton.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				discardChangesAndReturn();
			}
		
		});
		
		okButton.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelButton.setVisibility(View.GONE);
				okButton.setVisibility(View.GONE);

				isInEditMode = false;
				sequenceGroup.setEditModeEnabled(isInEditMode);
				
				TextView sequenceTitleView = (TextView) findViewById(R.id.sequence_title);
				sequenceTitleView.setEnabled(isInEditMode);
				
				// Saving the sequence title and changing the pointer to the original sequence
				sequence.setTitle(sequenceTitleView.getText().toString());
				originalSequence = sequence;
				
				Toast.makeText(SequenceActivity.this, getResources().getString(R.string.changes_saved), Toast.LENGTH_LONG).show();
			}
		
		});
		
		EditText sequenceTitle = (EditText) findViewById(R.id.sequence_title);		
		sequenceTitle.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					deleteButtonVisable(false);
				} else {
					// Closing the keyboard when the text field is not active anymore
					InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				    in.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				    
					deleteButtonVisable(true);
				}
			}
			
			private void deleteButtonVisable(boolean value) {
				ImageButton okButton = (ImageButton) findViewById(R.id.ok_button);
				ImageButton cancelButton = (ImageButton) findViewById(R.id.cancel_button);
				
				// Make buttons transparent
				if (value == false) {
					okButton.setAlpha(0.3f);
					cancelButton.setAlpha(0.3f);
				} else {
					okButton.setAlpha(1.0f);
					cancelButton.setAlpha(1.0f);
				}		
				
				// Disable/enable buttons 
				okButton.setEnabled(value);
				cancelButton.setEnabled(value);
			}
		});
	}
	
	private void discardChangesAndReturn() {
		// If the sequence is new and the changes are discarded, the sequence will be deleted.
		if (isNew)
			child.getSequences().remove(originalSequence);
		
		// Returning to the overview activity
		finish();
	}
	
	private SequenceViewGroup setupSequenceViewGroup(final SequenceAdapter adapter) {
		final SequenceViewGroup sequenceGroup = (SequenceViewGroup) findViewById(R.id.sequenceViewGroup);
		sequenceGroup.setEditModeEnabled(isInEditMode);
		sequenceGroup.setAdapter(adapter);
		
		//Handle rearrange
		sequenceGroup.setOnRearrangeListener(new SequenceViewGroup.OnRearrangeListener() {
			@Override
			public void onRearrange(int indexFrom, int indexTo) {
				sequence.rearrange(indexFrom, indexTo);			
				adapter.notifyDataSetChanged();
			}
		});
		
		//Handle new view
		sequenceGroup.setOnNewButtonClickedListener(new OnNewButtonClickedListener() {
			@Override
			public void onNewButtonClicked() {
				//TODO: Get proper new pictogram.
				
				Pictogram test = new Pictogram();
				
				sequence.addPictogramAtEnd(test);
				adapter.notifyDataSetChanged();
			}
		});
		
		return sequenceGroup;
	}

	private SequenceAdapter setupAdapter() {
		final SequenceAdapter adapter = new SequenceAdapter(this, sequence);
		
		//Setup delete handler.
		adapter.setOnCreateViewListener(new OnCreateViewListener() {		
			@Override
			public void onCreateView(final int position, final View view) {
				if (view instanceof PictogramView) {
					PictogramView pictoView = (PictogramView) view;
					pictoView.setOnDeleteClickListener(new OnDeleteClickListener() {
						@Override
						public void onDeleteClick() {
							sequence.deletePictogram(position);
							adapter.notifyDataSetChanged();
						}
					});
				}
			}
		});
		
		return adapter;
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK && requestCode == 1) {
			
			long[] checkoutIds = data.getExtras().getLongArray("checkoutIds");
			
			if (checkoutIds.length == 0) {
				Toast t = Toast.makeText(SequenceActivity.this, "The checkout contained no pictograms.", Toast.LENGTH_LONG);
				t.show();
			}
			else
			{
				if (checkoutIds.length != 1) {
					Toast t = Toast.makeText(SequenceActivity.this, "The checkout contained more than one pictogram. The first will be used", Toast.LENGTH_LONG);
					t.show();
				}
				
				// Set the sequence image using the first ID from the checkout.
				sequence.setImageId(checkoutIds[0]);
				
				//ImageView sequenceImageView = (ImageView)findViewById(R.id.sequence_image);
				//sequenceImageView.setImageDrawable(sequence.getImage());
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//discardChangesAndReturn();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	private void initializeTopBar() {
        TextView editText = (TextView) findViewById(R.id.sequence_title);
        editText.setEnabled(isInEditMode);
		
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
				}
			}
		});

		TextView childName = (TextView)findViewById(R.id.child_name);
		childName.setText(child.getName());
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
