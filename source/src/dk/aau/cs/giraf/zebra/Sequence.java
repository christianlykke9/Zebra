package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

/**
 * Represents a ordered collection (sequence) of drawables.
 *
 */
public class Sequence {
	private String name;
	private Drawable image;
	public List<Drawable> pictograms = new ArrayList<Drawable>();
	
	public Sequence(String name) {
		this.name = name;
	}
	
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
	
	/**
	 * Swaps the position of two drawables.
	 * @param oldIndex
	 * @param newIndex
	 */
	public void rearrange(int oldIndex, int newIndex) {
		Drawable temp = pictograms.get(oldIndex);
		pictograms.set(oldIndex, pictograms.get(newIndex));
		pictograms.set(newIndex, temp);
	}
	
}