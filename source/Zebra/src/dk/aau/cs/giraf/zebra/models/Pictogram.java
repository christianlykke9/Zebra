package dk.aau.cs.giraf.zebra.models;

import android.graphics.drawable.Drawable;

public class Pictogram {
	private long pictogramId;
	private Drawable image;
	
	public long getPictogramId() {
		return pictogramId;
	}
	
	public void setPictogramId(long pictogramId) {
		this.pictogramId = pictogramId;
		this.image = null;
	}
	
	public Pictogram getClone() {
		Pictogram clone = new Pictogram();
		clone.pictogramId = this.pictogramId;
		
		return clone;
	}

	public void setImage(Drawable image) {
		this.image = image;		
	}

	public Drawable getImage() {
		return image;
	}
}
