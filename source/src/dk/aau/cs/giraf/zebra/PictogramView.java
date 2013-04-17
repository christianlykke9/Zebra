package dk.aau.cs.giraf.zebra;

import android.content.Context;
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
	
	private RoundedImageView pictogram;
	private TextView title;
	private ImageButton deleteButton;
	
	private boolean isInEditMode = false;
	
	public PictogramView(Context context) {
		super(context);
		
		initialize(context, 0);
	}

	public PictogramView(Context context, float radius) {
		super(context);
		
		initialize(context, radius);
	}
	
	
	
	private void initialize(Context context, float radius) {
		this.setWillNotDraw(false);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		
		SquaredRelativeLayout square = new SquaredRelativeLayout(context);
		square.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		square.addView(createImageView(radius));
		square.addView(createDeleteButton());
		
		
		this.addView(square);
		
		this.addView(createTextView());
	}
	
	private View createImageView(float radius) {
		pictogram = new RoundedImageView(getContext(), radius);
		pictogram.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		pictogram.setScaleX(NORMAL_SCALE);
		pictogram.setScaleY(NORMAL_SCALE);
		
		return pictogram;
	}
	
	private View createTextView() {
		title = new TextView(getContext());
		title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		title.setGravity(Gravity.CENTER_HORIZONTAL);
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
	
	public void setTitle(String newTitle)
	{
		title.setText(newTitle);
	}
}
