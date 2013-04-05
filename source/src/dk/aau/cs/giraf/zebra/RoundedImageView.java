package dk.aau.cs.giraf.zebra;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {
	
	private float cornerRadius;
 
    public RoundedImageView(Context context, float radius) {
        super(context);
        
        cornerRadius = radius;
        ensureClipPathSupport();
    }
 
    public RoundedImageView(Context context, AttributeSet attrs, float radius) {
        super(context, attrs);
        
        cornerRadius = radius;
        ensureClipPathSupport();
    }
 
    public RoundedImageView(Context context, AttributeSet attrs, int defStyle, float radius) {
        super(context, attrs, defStyle);

        cornerRadius = radius;
        ensureClipPathSupport();
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
        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        clipPath.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        
        super.onDraw(canvas);
        
    }    
}