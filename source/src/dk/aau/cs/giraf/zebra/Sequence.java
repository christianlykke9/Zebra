package dk.aau.cs.giraf.zebra;

import java.util.List;

import android.graphics.drawable.Drawable;

public class Sequence {
	private String name;
	private Drawable image;
	private List<Drawable> pictograms;
	
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
	
	public List<Drawable> getPictograms() {
		return pictograms;
	}
	
	public void setPictograms(List<Drawable> pictograms) {
		this.pictograms = pictograms;
	}	
}
