package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Layouts its children with fixed sizes and fixed spacing between each child in
 * the horizontal dimension.
 * 
 */
public class SequenceViewGroup extends ViewGroup {

	private final int DEFAULT_ITEM_WIDTH = 250;
	private final int DEFAULT_ITEM_HEIGHT = 250;
	private final int DEFAULT_HORIZONTAL_SPACING = 100;

	private int horizontalSpacing;
	private int itemWidth;
	private int itemHeight;

	private int offsetY = 0;
	
	private View dragging = null;
	private int dragStartX;
	private int dragStartY;
	private int dragStartLeft;
	private int dragStartTop;

	private HashSet<View> beingAnimated = new HashSet<View>();
	
	public SequenceViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SequenceViewGroup);
		try {
			horizontalSpacing = a.getDimensionPixelSize(
					R.styleable.SequenceViewGroup_horizontalSpacing,
					DEFAULT_HORIZONTAL_SPACING);
			itemWidth = a.getDimensionPixelSize(
					R.styleable.SequenceViewGroup_itemWidth, DEFAULT_ITEM_WIDTH);
			itemHeight = a.getDimensionPixelSize(
					R.styleable.SequenceViewGroup_itemHeight, DEFAULT_ITEM_HEIGHT);
		} finally {
			a.recycle();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean handled = false;
		
		final float x = event.getX();
		final float y = event.getY();
		
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			View touched = childAtPoint((int) x, (int) y);
			if (touched != null) {
				handled = true;
				dragStarted(touched, x, y);
				
				requestDisallowInterceptTouchEvent(true);
				
				Log.e("LALAW", "Clicked: " + x);
				Log.e("LALAW", "Left: " + touched.getLeft());
				
				//touched.bringToFront();
				touched.invalidate();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (dragging != null) {
				int xDelta = (int) (x - dragStartX);
				int yDelta = (int) (y - dragStartY);
				int newLeft = dragStartLeft + xDelta;
				int newTop = dragStartTop + yDelta;
				dragging.setLeft(newLeft);
				dragging.setTop(newTop);
				dragging.setRight(newLeft + itemWidth);
				dragging.setBottom(newTop + itemHeight);
				
				for (View v : getViewsMovedPast(dragging)) {
					if (!beingAnimated.contains(v)) {
						v.animate().xBy(itemWidth+horizontalSpacing);
						beingAnimated.add(v);
						Log.e("LALAW", "PAST: " + indexOfChild(v) + " - " + indexOfChild(dragging));	
					}
					
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (dragging != null) {
				handled = true;
				//dragging.setTranslationX(0);
				beingAnimated.clear();
				dragEnded();
			}	
			break;
		}
		
		return handled;
	}

	private void dragEnded() {
		dragging = null;
		//Handle snapping
		
	}

	private void dragStarted(View touched, float x, float y) {
		final int xi = (int) x;
		final int yi = (int) y;
		dragging = touched;
		dragStartX = xi;
		dragStartY = yi;
		dragStartLeft = touched.getLeft();
		dragStartTop = touched.getTop();
	
	}
	
	
	
	private List<View> getViewsMovedPast(View view) {
		ArrayList<View> viewsMovedPast = new ArrayList<View>();
		
		int curLeft = view.getLeft();
		int viewIndex = indexOfChild(view);
		
		int layoutLeft = calcChildLeftPosition(indexOfChild(view));
		int centerX = view.getLeft() + view.getMeasuredWidth() / 2;
		
		//Moved left or right?
		int direction = curLeft < layoutLeft ? -1 : 1;
		
		int numChildren = getChildCount();
		
		//Check if moved past
		for (int checkIndex = viewIndex+direction; checkIndex >= 0 && checkIndex < numChildren; checkIndex+=direction) {
			View checkView = getChildAt(checkIndex);
			if (checkView == null) continue;
			int checkCenter = calcChildLeftPosition(checkIndex) + checkView.getMeasuredWidth() / 2;
			
			if (direction == 1 && checkCenter < centerX) {
				viewsMovedPast.add(getChildAt(checkIndex));
			} else if (direction == -1 && checkCenter > centerX) {
				viewsMovedPast.add(getChildAt(checkIndex));
			} else {
				break;
			}
		}
		
		return viewsMovedPast;
	}

	private View childAtPoint(int x, int y) {
		final int numChildren = getChildCount();
		for (int i = 0; i < numChildren; i++) {
			View child = getChildAt(i);
			Rect hitRect = new Rect();
			child.getHitRect(hitRect);
			if (hitRect.contains(x, y))
				return child;
		}
		return null;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/*
		 * We ignore which measure mode we are in for now. This ViewGroup
		 * requires all children to have same size, itemWidth and itemHeight.
		 * 
		 * TODO: What happens when we insist on larger space than parent will
		 * permit? (Problem outside of scrollers?)
		 */

		int minHeight = itemHeight + getPaddingTop() + getPaddingBottom();
		int height = getHeight();

		if (height < minHeight)
			height = minHeight;

		int surplusHeight = height - minHeight;

		offsetY = surplusHeight / 2;
		if (offsetY < 0)
			offsetY = 0;

		int numChildren = getChildCount();

		int width = getPaddingLeft() + getPaddingRight();

		width += numChildren * itemWidth;
		if (numChildren > 1) {
			width += (numChildren - 1) * horizontalSpacing;
		}

		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWidth,
				MeasureSpec.EXACTLY);
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(itemHeight,
				MeasureSpec.EXACTLY);

		for (int i = 0; i < numChildren; i++) {
			View child = getChildAt(i);
			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
		}

		setMeasuredDimension(resolveSize(width, widthMeasureSpec),
				resolveSize(height, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);

			if (child == dragging) continue;
			
			int x = calcChildLeftPosition(i);
			int y = calcChildTopPosition();
			
			child.layout(x, y, x + child.getMeasuredWidth(), y + child.getMeasuredHeight());
		}
		if (dragging != null) {
			dragging.layout(dragging.getLeft(), dragging.getTop(), dragging.getMeasuredWidth(), dragging.getMeasuredHeight());
		}
	}
	
	private int calcChildTopPosition() {
		return getPaddingTop() + offsetY;
	}
	
	private int calcChildLeftPosition(int childIndex) {
		int prevAccumulatedSpacing = childIndex * horizontalSpacing;
		int prevAccumulatedWidth = childIndex * itemWidth;
		return prevAccumulatedSpacing + prevAccumulatedWidth + getPaddingLeft();
	}

	public void setHorizontalSpacing(int spacing) {
		if (spacing > 0 && spacing != horizontalSpacing) {
			horizontalSpacing = spacing;
			requestLayout();
		}
	}

	public int getHorizontalSpacing() {
		return horizontalSpacing;
	}

	public void setItemWidth(int width) {
		if (width > 0 && width != itemWidth) {
			itemWidth = width;
			requestLayout();
		}
	}

	public int getItemWidth() {
		return itemWidth;
	}

	public void setItemHeight(int height) {
		if (height > 0 && height != itemHeight) {
			itemHeight = height;
			requestLayout();
		}
	}

	public int getItemHeight() {
		return itemHeight;
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p.width, p.height);
	}

}
