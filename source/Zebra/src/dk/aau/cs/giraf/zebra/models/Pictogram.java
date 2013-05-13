package dk.aau.cs.giraf.zebra.models;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class Pictogram {
	private long pictogramId;
	private String imagePath;
	
	public long getPictogramId() {
		return pictogramId;
	}
	
	public void setPictogramId(long pictogramId) {
		this.pictogramId = pictogramId;
		this.imagePath = null;
	}
	
	public Pictogram getClone() {
		Pictogram clone = new Pictogram();
		clone.pictogramId = this.pictogramId;
		
		return clone;
	}
	
	public Drawable getImage(Context context) {
		
		if (imagePath == null) {
			imagePath = PictoFactory.getPictogram(context, pictogramId).getImagePath();
		}
		
		return Drawable.createFromPath(imagePath);
	}
}
