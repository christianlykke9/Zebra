package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
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
 
//    @Override
//    protected void onDraw(Canvas canvas) {
//    	int width = getWidth() - getPaddingLeft() - getPaddingRight();
//    	int height = getHeight() - getPaddingTop() - getPaddingBottom();
//    	
//    	float fCornerRadius = cornerRadius;
//    	if (cornerRadius == -1) 
//    		fCornerRadius = Math.min(width, height) / 11.f;
//
//    	imageRect.set(0, 0, width, height);
//    	
//    	opacity.setFlags(Paint.ANTI_ALIAS_FLAG);
//        opacity.setColor(Color.RED);
//        
//        canvas.drawRoundRect(imageRect, fCornerRadius, fCornerRadius, opacity);
//        opacity.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.saveLayer(imageRect, opacity, Canvas.ALL_SAVE_FLAG);
//        
//        super.onDraw(canvas);
//        
//        canvas.restore();
//    }    
}