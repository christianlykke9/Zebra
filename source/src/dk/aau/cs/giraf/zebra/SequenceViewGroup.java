package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
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

		if (numChildren > 0) {
			width = numChildren * itemWidth;
			if (numChildren > 1) {
				width += (numChildren - 1) * horizontalSpacing;
			}
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

			int prevAccumulatedSpacing = 0;
			int prevAccumulatedWidth = 0;
			if (i > 0) {
				prevAccumulatedSpacing = i * horizontalSpacing;
				prevAccumulatedWidth = i * itemWidth;
			}
			int x = prevAccumulatedSpacing + prevAccumulatedWidth;
			x += getPaddingLeft();

			int y = getPaddingTop() + offsetY;

			child.layout(x, y, x + child.getMeasuredWidth(),
					y + child.getMeasuredHeight());
		}
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
