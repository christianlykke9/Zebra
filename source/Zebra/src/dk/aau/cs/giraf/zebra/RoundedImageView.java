package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Rounds the corner of the image set.
 * 
 * Maybe cut of parts of the image.
 */
public class RoundedImageView extends ImageView {

	private static final int DEFAULT_CORNER_RADIUS = -1;
	
	private float cornerRadius;
	
	RectF imageRect = new RectF();
	Paint opacity = new Paint();
	
 
    public RoundedImageView(Context context, float radius) {
        super(context);
        cornerRadius = radius;
        ensureClipPathSupport();
    }
    
    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        cornerRadius = getCornerRadius(context, attrs);
        ensureClipPathSupport();
    }
 
    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        cornerRadius = getCornerRadius(context, attrs);
        ensureClipPathSupport();
    }
    
	private int getCornerRadius(Context context, AttributeSet attrs) {
		int cornerRadius = DEFAULT_CORNER_RADIUS;
		
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.RoundedImageView);
		try {
			cornerRadius = a.getDimensionPixelSize(
					R.styleable.RoundedImageView_cornerRadius, DEFAULT_CORNER_RADIUS);
		} finally {
			a.recycle();
		}
		
		return cornerRadius;
	}
    
    private void ensureClipPathSupport()
    {
    	// Disable hardware acceleration to support clipPath in newer versions of android
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
    	   this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
    	// Round some corners betch!
    			Drawable maiDrawable = getDrawable();
    			if (maiDrawable instanceof BitmapDrawable) {
    				
    				int width = getWidth() - getPaddingLeft() - getPaddingRight();
    		    	int height = getHeight() - getPaddingTop() - getPaddingBottom();
    		    	
    		    	float fCornerRadius = cornerRadius;
    		    	if (cornerRadius == -1) 
    		    		fCornerRadius = Math.min(width, height) / 11.f;
    				
    				Paint paint = ((BitmapDrawable) maiDrawable).getPaint();
    		        final int color = 0xff000000;
    		        Rect bitmapBounds = maiDrawable.getBounds();
    		        final RectF rectF = new RectF(bitmapBounds);
    		        
    		        // Create an off-screen bitmap to the PorterDuff alpha blending to work right
    				int saveCount = canvas.saveLayer(rectF, null,
    	                    Canvas.MATRIX_SAVE_FLAG |
    	                    Canvas.CLIP_SAVE_FLAG |
    	                    Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
    	                    Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
    	                    Canvas.CLIP_TO_LAYER_SAVE_FLAG);
    				
    				// Resize the rounded rect we'll clip by this view's current bounds
    				// (super.onDraw() will do something similar with the drawable to draw)
    				getImageMatrix().mapRect(rectF);
    	 
    		        paint.setAntiAlias(true);
    		        canvas.drawARGB(0, 0, 0, 0);
    		        paint.setColor(color);
    		        canvas.drawRoundRect(rectF, fCornerRadius, fCornerRadius, paint);
    	 
    				Xfermode oldMode = paint.getXfermode();
    				// This is the paint already associated with the BitmapDrawable that super draws
    		        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    		        super.onDraw(canvas);
    		        paint.setXfermode(oldMode);
    		        canvas.restoreToCount(saveCount);
    			} else {
    				super.onDraw(canvas);
    			}
    }    
}