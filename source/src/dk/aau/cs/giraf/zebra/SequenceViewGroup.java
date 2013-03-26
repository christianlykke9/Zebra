package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

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
	private int centerOffset;
	
	private int draggingIndex = -1;
	private int curDragIndexPos = -1;
	private int newX = -1;
	private int oldX = -1;
	
	private int[] newPositions;

	
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
			dragging = childAtPoint((int) x, (int) y);
			if (dragging != null) {
				handled = true;
				
				requestDisallowInterceptTouchEvent(true);
				
				draggingIndex = indexOfChild(dragging);
				curDragIndexPos = draggingIndex;
				
				newPositions = new int[getChildCount()];
				for (int i = 0; i < newPositions.length; i++) {
					newPositions[i] = i;
				}
				
				newX = (int) x;
				oldX = newX;
				
				dragStartX = (int) x;
				centerOffset = getCenterX(draggingIndex) - (int) x;
				
				//((SequenceImageView)dragging).liftUp();
				dragging.invalidate();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (draggingIndex != -1) {
				handled = true;
				
				int xDelta = (int) (x - dragStartX);
				oldX = newX;
				newX = (int) x;
				
				int newLeft = calcChildLeftPosition(draggingIndex) + xDelta;
				dragging.layout(newLeft, dragging.getTop(), newLeft + dragging.getMeasuredWidth(), dragging.getTop() + dragging.getMeasuredHeight());
				
				checkForSwap((int) x);
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (draggingIndex != -1) {
				handled = true;
				
				//((SequenceImageView)dragging).placeDown();				
				dragging.invalidate();
				
				int deltaX = (int) (x - dragStartX);
				int targetDeltaX = calcChildLeftPosition(curDragIndexPos) - calcChildLeftPosition(draggingIndex);
				
				TranslateAnimation move = new TranslateAnimation(
						0, 
						targetDeltaX - deltaX, 
						0, 
						0);
				move.setDuration(500);
				
				move.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						if (draggingIndex != curDragIndexPos) {
							//Rearrange
							View[] views = new View[getChildCount()];
							for (int i = 0; i < newPositions.length; i++) {
								views[i] = getChildAt(i);
								getChildAt(i).clearAnimation();
							}
							removeAllViews();
							for (int i = 0; i < newPositions.length; i++) {
								addView(views[newPositions[i]]);
							}		
						}
						
						draggingIndex = -1;
						curDragIndexPos = -1;
						dragging = null;
					}
				});
				
				dragging.startAnimation(move);
				
			}	
			break;
		}
		return handled;
	}
	
	private void checkForSwap(int x) {

		int dragCenterX = x + centerOffset;
		
		//boolean isDraggingRight = x - dragStartX > 0;
		boolean isDraggingRight = newX > oldX;
		if (isDraggingRight) {
			int checkIndex = curDragIndexPos + 1;
			while (checkIndex < getChildCount() && dragCenterX > ((getCenterX(curDragIndexPos) + getCenterX(checkIndex)) / 2)) {
				//Swap position array
				int temp = newPositions[checkIndex];
				newPositions[checkIndex] = newPositions[curDragIndexPos];
				newPositions[curDragIndexPos] = temp;
				
				doMoveTo(checkIndex, curDragIndexPos);
				curDragIndexPos++;
				checkIndex++;
			}
		} else {
			int checkIndex = curDragIndexPos - 1;
			while (checkIndex >= 0 && dragCenterX < ((getCenterX(curDragIndexPos) + (getRightX(checkIndex))) / 2)) {
				int temp = newPositions[checkIndex];
				newPositions[checkIndex] = newPositions[curDragIndexPos];
				newPositions[curDragIndexPos] = temp;
				
				doMoveTo(checkIndex, curDragIndexPos);
				curDragIndexPos--;
				checkIndex--;
			}
		}
	}

	private void doMoveTo(int indexFrom, int indexTo) {
		int translateX = calcChildLeftPosition(indexTo) - calcChildLeftPosition(indexFrom);
		
		TranslateAnimation anim = new TranslateAnimation(
				Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, translateX,
				Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0);
		
		anim.setDuration(500);
		anim.setFillEnabled(true);
		anim.setFillAfter(true);
		getChildAt(indexFrom).startAnimation(anim);
		//getChildAt(i).animate().translationXBy(translateX);
	}
	
	/*
	private List<View> getViewsMovedPast(View view) {
		ArrayList<View> viewsMovedPast = new ArrayList<View>();
		
		int curLeft = view.getLeft();
		int curRight = view.getRight();
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
			
			if (direction == 1 && checkCenter < curRight) {
				viewsMovedPast.add(getChildAt(checkIndex));
			} else if (direction == -1 && checkCenter > curLeft) {
				viewsMovedPast.add(getChildAt(checkIndex));
			} else {
				break;
			}
		}
		
		return viewsMovedPast;
	}
	*/

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
	
	private int getIndexAtPoint(int x, int y) {
		View child = childAtPoint(x, y);
		if (child == null)
			return -1;
		else
			return indexOfChild(child);
	}
	
	private int getIndexAtX(int x) {
		return getIndexAtPoint(x, calcChildTopPosition() + itemHeight / 2);
	}
	
	private int getCenterX(int index) {
		return calcChildLeftPosition(index) + itemWidth / 2;
	}
	
	private int getLeftX(int index) {
		return calcChildLeftPosition(index);
	}
	
	private int getRightX(int index) {
		return calcChildLeftPosition(index) + itemWidth;
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
