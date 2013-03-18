package dk.aau.cs.giraf.zebra;

import java.util.List;

import android.graphics.drawable.Drawable;

public class Sequence {
	private String name;
	private Drawable image;
	public List<Drawable> pictograms;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Drawable getImage() {
		return image;
	}
	
	public void setImage(Drawable image) {
		this.image = image;
	}
}
