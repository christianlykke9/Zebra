package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SequenceImageView extends RelativeLayout {

	private final int PADDING = 25;
	private final int LIFT_PADDING = 15; 
	
	private ImageView imageView;
	private ImageButton deleteButton;
	
	public SequenceImageView(Context context) {
		super(context);
		
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		initializeImageView();
		initializeDeleteButton();
	}
	
	private int deleteButtonVisibility = View.INVISIBLE;
	
	public void setDeleteButtonVisibility(int visibility) {
		deleteButtonVisibility = visibility;
		deleteButton.setVisibility(visibility);
	}
	
	public int getDeleteButtonVisibility() {
		return deleteButtonVisibility;
	}
	
	public void liftUp() {
		deleteButton.setVisibility(View.INVISIBLE);
		imageView.setPadding(LIFT_PADDING, LIFT_PADDING, LIFT_PADDING, LIFT_PADDING);
		this.setAlpha(0.7f);
	}
	
	public void placeDown() {
		deleteButton.setVisibility(deleteButtonVisibility);
		imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
		this.setAlpha(1.0f);
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
		imageView = new ImageView(getContext());
		imageView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
		
		addView(imageView);
	}
	
	public void setImageDrawable(Drawable drawable)
	{
		imageView.setImageDrawable(drawable);
	}
}
