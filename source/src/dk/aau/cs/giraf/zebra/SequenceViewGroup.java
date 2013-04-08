package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

/**
 * Layouts its children with fixed sizes and fixed spacing between each child in
 * the horizontal dimension.
 * 
 * TODO: Draw dragged on top
 */
public class SequenceViewGroup extends ViewGroup {

	private final int DEFAULT_ITEM_WIDTH = 250;
	private final int DEFAULT_ITEM_HEIGHT = 250;
	private final int DEFAULT_HORIZONTAL_SPACING = 100;
	
	private final int ANIMATION_TIME = 350;

	private int horizontalSpacing;
	private int itemWidth;
	private int itemHeight;

	private int offsetY = 0;
	
	private View dragging = null;
	private int draggingIndex = -1;
	private int curDragIndexPos = -1;
	private int dragStartX;
	private int centerOffset;
	private int touchX = -1;
	private int touchDeltaX = 0;
	
	private boolean isInEditMode = false;
	private View addNewPictoGramView = null;
	
	private OnRearrangeListener rearrangeListener = null;
	
	private boolean animatingDragReposition = false;
	
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
		
		setWillNotDraw(false);
	}
	
	private int calcChildLeftPosition(int childIndex) {
		int prevAccumulatedSpacing = childIndex * horizontalSpacing;
		int prevAccumulatedWidth = childIndex * itemWidth;
		return prevAccumulatedSpacing + prevAccumulatedWidth + getPaddingLeft();
	}

	private int calcChildTopPosition() {
		return getPaddingTop() + offsetY;
	}
	
	private void checkForSwap() {
		
		int dragCenterX = touchX - centerOffset;
		
		boolean isDraggingRight = dragCenterX - getCenterX(curDragIndexPos) > 0;
		
		if (isDraggingRight) {
			int checkIndex = curDragIndexPos + 1;
			//Don't swap with  new sequence diagram view.
			while (checkIndex < getDraggableChildCount() && dragCenterX > ((getCenterX(curDragIndexPos) + getCenterX(checkIndex)) / 2)) {
				//Animate before swapping indices
				doAnimateTranslation(checkIndex, curDragIndexPos);
				swapIndexPositions(checkIndex, curDragIndexPos);
				curDragIndexPos++;
				checkIndex++;
			}
		} else {
			int checkIndex = curDragIndexPos - 1;
			while (checkIndex >= 0 && dragCenterX < ((getCenterX(curDragIndexPos) + (getCenterX(checkIndex))) / 2)) {
				//Animate before swapping indices
				doAnimateTranslation(checkIndex, curDragIndexPos);
				swapIndexPositions(checkIndex, curDragIndexPos);
				curDragIndexPos--;
				checkIndex--;
			}
		}
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
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
	
	private void doAnimateTranslation(int indexFrom, int indexTo) {
		
		//This is where the current view occupying indexFrom originally came from.
		int realFrom = newPositions[indexFrom];
		
		int destOffset = calcChildLeftPosition(indexTo) - calcChildLeftPosition(realFrom);
		
		//This is the view currently occuping indexFrom
		View view = getChildAt(realFrom);
		//Before deleting current animation, get old translation
		int currentTranslatedX = 0;
		Animation animation = view.getAnimation();
		if (animation != null) {
			currentTranslatedX = getAnimationTranslatedX(animation);
		}
		view.clearAnimation();
		
		//Move from current offset to new offset based on it original location before dragging.
		TranslateAnimation anim = new TranslateAnimation(
				Animation.ABSOLUTE, currentTranslatedX,
				Animation.ABSOLUTE, destOffset,
				Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, 0);
		
		anim.setDuration(ANIMATION_TIME);
		anim.setFillEnabled(true);
		anim.setFillAfter(true);
		view.startAnimation(anim);
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
	
	private int getAnimationTranslatedX(Animation animation) {
		if (animation == null) throw new NullPointerException("No translation x for null animation");
		Transformation transformation = new Transformation();
		animation.getTransformation(AnimationUtils.currentAnimationTimeMillis(), transformation);
		float[] matrix = new float[9];
		transformation.getMatrix().getValues(matrix);
		return (int) matrix[Matrix.MTRANS_X];
	}
	
	private int getCenterX(int index) {
		return calcChildLeftPosition(index) + itemWidth / 2;
	}
	
	public int getHorizontalSpacing() {
		return horizontalSpacing;
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
	
	public int getItemHeight() {
		return itemHeight;
	}

	public int getItemWidth() {
		return itemWidth;
	}

	private int getLeftX(int index) {
		return calcChildLeftPosition(index);
	}

	/**
	 * Returns the current OnRearrangeListener or null if not set.
	 * @param rearrangeListener
	 * @return 
	 */
	public OnRearrangeListener getOnRearrangeListener(OnRearrangeListener rearrangeListener) {
		return this.rearrangeListener;
	}
	
	private int getRightX(int index) {
		return calcChildLeftPosition(index) + itemWidth;
	}

	private void layoutChild(int i) {
		View child = getChildAt(i);
		
		int x = calcChildLeftPosition(i);
		int y = calcChildTopPosition();
		
		child.layout(x, y, x + child.getMeasuredWidth(), y + child.getMeasuredHeight());
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			if (i == draggingIndex) continue;
			layoutChild(i);
		}
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
	public boolean onTouchEvent(MotionEvent event) {
	
		//If performing drag animation then consume event to not disrupt
		if (animatingDragReposition) return true;
		
		boolean handled = false;
		
		float x = event.getX();
		float y = event.getY();
		
		//End drag if UP, Cancel or multiple pointers or pointer is gone
		if (event.getActionMasked() == MotionEvent.ACTION_UP || 
				event.getActionMasked() == MotionEvent.ACTION_CANCEL || 
				event.getPointerCount() != 1 ||
				x >= getWidth() || x <= 0) {
			
			//Be careful with coordinates from the event if getPointerCount != 1
			
			if (draggingIndex != -1) {
				handled = true;
				
				//Disallow movement when repositioning dragged view.
				animatingDragReposition = true;
				
				TranslateAnimation move = new TranslateAnimation(
						0,
						calcChildLeftPosition(curDragIndexPos) - dragging.getLeft(), 
						0, 
						0);
				move.setDuration(ANIMATION_TIME);
				
				move.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation animation) {
						if (draggingIndex != curDragIndexPos) {
							if (rearrangeListener != null)
								rearrangeListener.onRearrange(draggingIndex, curDragIndexPos);
							
							//TODO: Something here causes flicker on the index originally moved
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
							//This prevents lots of flicker
							onLayout(true, getLeft(), getTop(), getRight(), getBottom());
						} else {
							//Must clear animation to prevent flicker - even though it just ended.
							getChildAt(draggingIndex).clearAnimation();
							layoutChild(draggingIndex);
						}
						
						draggingIndex = -1;
						curDragIndexPos = -1;
						animatingDragReposition = false;
						dragging = null;
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationStart(Animation animation) {
					}
				});
				
				dragging.startAnimation(move);
				
			}
			return handled;
		}
		
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			dragging = childAtPoint((int) x, (int) y);
			if (dragging != null && dragging != addNewPictoGramView) {
				handled = true;
				
				requestDisallowInterceptTouchEvent(true);
				
				//Grap original drag position
				draggingIndex = indexOfChild(dragging);
				curDragIndexPos = draggingIndex;
				
				//Everything is in the right place at the start.
				newPositions = new int[getChildCount()];
				for (int i = 0; i < newPositions.length; i++) {
					newPositions[i] = i;
				}
				
				touchX = (int) x;			
				dragStartX = touchX;
				centerOffset = touchX - getCenterX(draggingIndex);
				
				dragging.invalidate();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (draggingIndex != -1) {
				handled = true;
				
				touchDeltaX = (int) (x - dragStartX);
				touchX = (int) x;
				
				//Layout the dragging element. Is excluded from normal layout			
				int newLeft = calcChildLeftPosition(draggingIndex) + touchDeltaX;
				dragging.layout(newLeft, dragging.getTop(), newLeft + dragging.getMeasuredWidth(), dragging.getTop() + dragging.getMeasuredHeight());
				
				checkForSwap();
			}
			break;
		}
		return handled;
	}

	public void setHorizontalSpacing(int spacing) {
		if (spacing > 0 && spacing != horizontalSpacing) {
			horizontalSpacing = spacing;
			requestLayout();
		}
	}

	public void setItemHeight(int height) {
		if (height > 0 && height != itemHeight) {
			itemHeight = height;
			requestLayout();
		}
	}

	public void setItemWidth(int width) {
		if (width > 0 && width != itemWidth) {
			itemWidth = width;
			requestLayout();
		}
	}
	
	/**
	 * Sets the OnRearrangeListener that is called when Views are rearranged.
	 * Can be null
	 * @param rearrangeListener
	 */
	public void setOnRearrangeListener(OnRearrangeListener rearrangeListener) {
		this.rearrangeListener = rearrangeListener;
	}
	
	private void swapIndexPositions(int indexA, int indexB) {
		int temp = newPositions[indexA];
		newPositions[indexA] = newPositions[indexB];
		newPositions[indexB] = temp;
	}
	
	public interface OnRearrangeListener {
		public void onRearrange(int indexFrom, int indexTo);
	}
	
	private int getDraggableChildCount() {
		return isInEditMode ? getChildCount() - 1 : getChildCount();
	}
	
	public void setEditModeEnabled(boolean editEnabled) {
		if (isInEditMode != editEnabled) {
			if (editEnabled) {
				addNewPictoGramView = new View(getContext()) {
					Paint paint = new Paint();
					
					@Override
					protected void onDraw(Canvas canvas) {
						super.onDraw(canvas);
						paint.setColor(Color.RED);
						paint.setStyle(Style.STROKE);
						paint.setStrokeWidth(10f);
						canvas.drawRect(5, 5, getMeasuredWidth()-5, getMeasuredHeight()-5, paint);
						paint.setStrokeWidth(1);
						paint.setColor(Color.BLUE);
						canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
					}
				};
				addView(addNewPictoGramView);
			} else {
				removeViewAt(getChildCount() - 1);
				addNewPictoGramView = null;
			}
			
			isInEditMode = editEnabled;
			invalidate();
		}
	}
	
	//TODO: Check this for types
	public void addNewPictogramAtEnd(View view) {
		if (isInEditMode) {
			addView(view, getChildCount()-1);
		} else {
			addView(view);
		}
	}

}
