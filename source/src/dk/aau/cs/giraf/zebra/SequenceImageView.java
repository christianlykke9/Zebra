package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.graphics.Canvas;
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
	
	public SequenceImageView(Context context) {
		super(context);
		
		this.setWillNotDraw(false);
		
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
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
		imageView.setScaleX(HIGHLIGHT_SCALE);
        imageView.setScaleY(HIGHLIGHT_SCALE);
		this.setAlpha(0.7f);
	}
	
	public void placeDown() {
		deleteButton.setVisibility(deleteButtonVisibility);
		imageView.setScaleX(NORMAL_SCALE);
        imageView.setScaleY(NORMAL_SCALE);
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
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (EditMode.get()) {
			setDeleteButtonVisibility(View.VISIBLE);
		} else {
			setDeleteButtonVisibility(View.INVISIBLE);
		}
		
		super.onDraw(canvas);
	}
}
