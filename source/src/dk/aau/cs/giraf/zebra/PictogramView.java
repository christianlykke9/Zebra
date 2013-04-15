package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Contains a pictogram and a string.
 * The view will display a delete button in the corner when the application is in editmode.
 * It also adds support for highlighting.
 */
public class PictogramView extends RelativeLayout {
	
	private final float NORMAL_SCALE = 0.8f;
	private final float HIGHLIGHT_SCALE = 0.9f;
	
	private ImageView pictogram;
	private TextView title;
	private ImageButton deleteButton;
	private boolean isLifted = false;
	
	
	
	public PictogramView(Context context) {
		super(context);
		
		ImageView image = new ImageView(getContext());
		image.setBackgroundColor(Color.GREEN);
		
		initialize(context, image, "Test-pictogram");
	}

	public PictogramView(Context context, ImageView image) {
		super(context);
		
		initialize(context, image, null);
	}
	
	public PictogramView(Context context, ImageView image, String string) {
		super(context);
		
		initialize(context, image, string);
	}
	
	
	
	private void initialize(Context context, ImageView image, String string) {
		this.setWillNotDraw(false);
		
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		initializeImageView(image);
		initializeDeleteButton();
		//this.addView(topBox);
		
		//initializeTextView(string);
	}
	
	private void initializeImageView(ImageView image) {
		pictogram = image;
		pictogram.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		pictogram.setScaleX(NORMAL_SCALE);
		pictogram.setScaleY(NORMAL_SCALE);
		
		addView(pictogram);
	}
	
	private void initializeTextView(String string) {
		if (string != null) {
			title = new TextView(getContext());
			title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			title.setText(string);
			
			this.addView(title);
		}
	}
	
	private void initializeDeleteButton() {
		deleteButton = new ImageButton(getContext());
		deleteButton.setImageResource(R.layout.selector_delete_pictogram);
		deleteButton.setBackgroundColor(Color.TRANSPARENT);
		deleteButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		addView(deleteButton);
	}
	
	
	
	public void liftUp() {
		isLifted = true;
		pictogram.setScaleX(HIGHLIGHT_SCALE);
        pictogram.setScaleY(HIGHLIGHT_SCALE);
		this.setAlpha(0.7f);
	}
	
	public void placeDown() {
		isLifted = false;
		pictogram.setScaleX(NORMAL_SCALE);
        pictogram.setScaleY(NORMAL_SCALE);
		this.setAlpha(1.0f);
	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (isLifted) {
			deleteButton.setVisibility(View.INVISIBLE);
		} else {
			if (EditMode.get()) {
				deleteButton.setVisibility(View.VISIBLE);
			} else {
				deleteButton.setVisibility(View.INVISIBLE);
			}
		}
		
		super.onDraw(canvas);
	}
}
