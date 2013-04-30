package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Represents a ordered collection (sequence) of drawables.
 *
 */
public class Sequence {

	private String name;
	private Context context;
	private List<Drawable> pictograms = new ArrayList<Drawable>();
	private Child child;
	private int sequenceId;
	
	private long imageId;
	private Drawable image;
	
	public Sequence(Context context, Child child, String name) {		
		this.context = context;
		
		this.child = child;
		this.name = name;
	}
	
	public int getSequenceId() {
		return sequenceId;
	}
	
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public Child getChild() {
		return child;
	}
	
	public void setChild(Child child) {
		this.child = child;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setImageId(long imageId) {
		image = null;
		this.imageId = imageId;
	}
	
	public Drawable getImage() {
		if (image == null) {
			Pictogram pictogram = PictoFactory.getPictogram(context, imageId);
			String path = pictogram.getImagePath();
			image = Drawable.createFromPath(path);
		}
		
		return image;
	}

	public List<Drawable> getPictograms() {
		return Collections.unmodifiableList(pictograms);
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
	
	public void deletePictogram(int position) {
		pictograms.remove(position);
	}

	public void preloadImage() {
		if (image == null) {
			getImage();
		}
	}

	public long getImageId() {
		return imageId;
	}
}
