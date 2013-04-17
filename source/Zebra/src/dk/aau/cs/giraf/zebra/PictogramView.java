package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Contains a pictogram and a title.
 * The view will display a delete button in the corner when the application is in editmode.
 * It also adds support for highlighting.
 */
public class PictogramView extends LinearLayout {
	
	private final float NORMAL_SCALE = 0.8f;
	private final float HIGHLIGHT_SCALE = 0.9f;
	
	private ImageView pictogram;
	private TextView title;
	private ImageButton deleteButton;
	
	private boolean isInEditMode = false;
	
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
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		SquaredRelativeLayout square = new SquaredRelativeLayout(context);
		square.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		square.addView(createImageView(image));
		square.addView(createDeleteButton());
		
		this.addView(square);
		

		if (string != null) {
			this.addView(createTextView(string));
		}
	}
	
	private View createImageView(ImageView image) {
		pictogram = image;
		pictogram.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		pictogram.setScaleX(NORMAL_SCALE);
		pictogram.setScaleY(NORMAL_SCALE);
		
		return pictogram;
	}
	
	private View createTextView(String string) {
		title = new TextView(getContext());
		title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		title.setGravity(Gravity.CENTER_HORIZONTAL);

		title.setText(string);
		title.setTextSize(26f);
		
		return title;
	}
	
	private View createDeleteButton() {
		deleteButton = new ImageButton(getContext());
		deleteButton.setImageResource(R.layout.selector_delete_pictogram);
		deleteButton.setBackgroundColor(Color.TRANSPARENT);
		deleteButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setDeleteButtonVisible(false);
		
		return deleteButton;
	}
	
	
	
	public void liftUp() {
		pictogram.setScaleX(HIGHLIGHT_SCALE);
        pictogram.setScaleY(HIGHLIGHT_SCALE);
		this.setAlpha(0.7f);
		setDeleteButtonVisible(false);
		invalidate();
	}
	
	public void placeDown() {
		pictogram.setScaleX(NORMAL_SCALE);
        pictogram.setScaleY(NORMAL_SCALE);
		this.setAlpha(1.0f);
		setDeleteButtonVisible(isInEditMode);
		invalidate();
	}
	
	private void setDeleteButtonVisible(boolean visible) {
		deleteButton.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
		invalidate();
	}
	
	public void setEditModeEnabled(boolean editMode) {
		if (editMode != isInEditMode) {
			isInEditMode = editMode;
			setDeleteButtonVisible(editMode);
		}
	}
	
	public boolean getEditModeEnabled() {
		return isInEditMode;
	}

	public void setImage(Drawable drawable) {
		pictogram.setImageDrawable(drawable);
	}
	
}
