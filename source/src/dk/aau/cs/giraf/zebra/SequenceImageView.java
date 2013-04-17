package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class SequenceImageView extends RelativeLayout {
	
	private final float NORMAL_SCALE = 0.8f;
	private final float HIGHLIGHT_SCALE = 0.9f;
	private final float CORNER_RADIUS = 30f;
	
	private RoundedImageView imageView;
	private ImageButton deleteButton;
	private boolean inEditMode = false;
	
	public SequenceImageView(Context context) {
		super(context);
		
		this.setWillNotDraw(false);
		
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		initializeImageView();
		initializeDeleteButton();
	}
	
	private int deleteButtonVisibility = View.INVISIBLE;
	
	private void setDeleteButtonVisible(boolean visible) {
		deleteButton.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}
	
	public void liftUp() {
		deleteButton.setVisibility(View.INVISIBLE);
		imageView.setScaleX(HIGHLIGHT_SCALE);
        imageView.setScaleY(HIGHLIGHT_SCALE);
		this.setAlpha(0.7f);
		invalidate();
	}
	
	public void placeDown() {
		deleteButton.setVisibility(deleteButtonVisibility);
		imageView.setScaleX(NORMAL_SCALE);
        imageView.setScaleY(NORMAL_SCALE);
		this.setAlpha(1.0f);
		invalidate();
	}
	
	private void initializeDeleteButton() {
		deleteButton = new ImageButton(getContext());
		deleteButton.setImageResource(R.layout.selector_delete_pictogram);
		deleteButton.setBackgroundColor(Color.TRANSPARENT);
		deleteButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		deleteButton.setVisibility(deleteButtonVisibility);
		
		addView(deleteButton);
	}
	
	private void initializeImageView() {
		imageView = new RoundedImageView(getContext(), CORNER_RADIUS);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		imageView.setScaleX(NORMAL_SCALE);
		imageView.setScaleY(NORMAL_SCALE);
		
		addView(imageView);
	}
	
	public void setImageDrawable(Drawable drawable)
	{
		imageView.setImageDrawable(drawable);
	}
	
	public void setEditModeEnabled(boolean editMode) {
		if (editMode != inEditMode) {
			inEditMode = editMode;
			setDeleteButtonVisible(editMode);
			invalidate();
		}
	}
	
	public boolean getEditModeEnabled() {
		return inEditMode;
	}
}
