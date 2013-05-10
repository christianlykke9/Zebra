package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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
    	int width = getWidth() - getPaddingLeft() - getPaddingRight();
    	int height = getHeight() - getPaddingTop() - getPaddingBottom();
    	
    	float fCornerRadius = cornerRadius;
    	if (cornerRadius == -1) 
    		fCornerRadius = Math.min(width, height) / 11.f;

    	Drawable maiDrawable = getDrawable();
    	Paint paint = ((BitmapDrawable)maiDrawable).getPaint();
    	final int color = 0xff000000;
    	
    	imageRect.set(0, 0, width, height);
    	
    	int saveCount = canvas.saveLayer(imageRect, null,
                Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);
    	
    	paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
//        
//        // Bagground
//        RectF bg = new RectF();
//        bg.set(0, 0, width, height);
//        Paint bgPaint = new Paint();
//        bgPaint.setColor(Color.GREEN);
        
        //canvas.drawRoundRect(bg, fCornerRadius, fCornerRadius, bgPaint);
        canvas.drawRoundRect(imageRect, fCornerRadius, fCornerRadius, paint);
        
        Xfermode oldMode = paint.getXfermode();
        
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));   
        
        super.onDraw(canvas);
        
        paint.setXfermode(oldMode);
        canvas.restoreToCount(saveCount);
    }    
}