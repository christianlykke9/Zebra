package dk.aau.cs.giraf.zebra;

import android.graphics.drawable.Drawable;

public class Child {
	
	private String name;
	private int sequenceCount;
	private Drawable image;
	
	public Child(String name) {
		if (name == null) throw new IllegalArgumentException("Child must have name");
		this.name = name;
	}
	
	public String getName() {
		return name;
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
