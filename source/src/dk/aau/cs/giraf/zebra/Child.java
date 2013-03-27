package dk.aau.cs.giraf.zebra;

import android.graphics.drawable.Drawable;

public class Child {
	
	private String name;
	private int sequenceCount;
	private Drawable image;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getSequenceCount() {
		return sequenceCount;
	}
	
	public void setSequenceCount(int count) {
		this.sequenceCount = count;
	}

	public Drawable getImage() {
		return image;
	}
	
	public void setImage(Drawable image) {
		this.image = image;
	}

}
