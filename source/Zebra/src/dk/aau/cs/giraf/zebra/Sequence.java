package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Represents a ordered collection (sequence) of pictograms.
 *
 */
public class Sequence {

	private int sequenceId;
	private String name;
	private long imageId;
	
	public Sequence() {
	}
	
	public static Sequence fromSequenceId(long sequenceId) {
		Sequence sequence = new Sequence();
		
		// TODO: GET FROM DATABASE
		
		return sequence;		
	}
	
	// Ordered list of pictograms
	private List<Pictogram> pictograms = new ArrayList<Pictogram>();
	
	public int getSequenceId() {
		return sequenceId;
	}
	
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getImageId() {
		return imageId;
	}
	
	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public List<Pictogram> getPictograms() {
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
		
		Pictogram temp = pictograms.remove(oldIndex);
		pictograms.add(newIndex, temp);
	}
	
	public void addPictogramAtEnd(Pictogram pictogram) {
		pictograms.add(pictogram);
	}
	
	public void deletePictogram(Pictogram pictogram) {
		pictograms.remove(pictogram);
	}
	
	public void deletePictogram(int position) {
		pictograms.remove(position);
	}
}
