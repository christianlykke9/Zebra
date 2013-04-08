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
		if (oldIndex < 0 || oldIndex >= pictograms.size()) throw new IllegalArgumentException("oldIndex out of range");
		if (newIndex < 0 || newIndex >= pictograms.size()) throw new IllegalArgumentException("newIndex out of range");
		
		Drawable temp = pictograms.remove(oldIndex);
		pictograms.add(newIndex, temp);
	}
	
	public void addPictogramAtEnd(Drawable pictogram) {
		pictograms.add(pictogram);
	}
	
	public void deletePictogram(Drawable pictogram) {
		pictograms.remove(pictogram);
	}
}
