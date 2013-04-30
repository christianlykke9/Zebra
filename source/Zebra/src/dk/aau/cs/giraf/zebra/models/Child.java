package dk.aau.cs.giraf.zebra.models;

import java.util.ArrayList;
import java.util.List;


import android.graphics.drawable.Drawable;

public class Child {
	
	private long profileId;
	private String name;
	private Drawable picture;
	
	private List<Sequence> sequences = new ArrayList<Sequence>();
	
	public Child(long profileId) {
		this.profileId = profileId;
		
		//TODO: FOR TESTING ONLY:
		this.name = "Søren Knudsen";
		
		//TODO: Get the name from the database
	}
	
	public long getProfileId() {
		return profileId;
	}
	
	public String getName() {
		return name;
	}
	
	public Drawable getPicture() {
		return picture;
	}
	
	public List<Sequence> getSequences() {
		return sequences;
	}
	
	public void setSequences(List<Sequence> sequences) {
		this.sequences = sequences;
	}
	
	public int getSequenceCount() {
		return sequences.size();
	}
	
	public long getNextSequenceId() {
		if (sequences.size() != 0)
			return sequences.get(sequences.size() - 1).getSequenceId();
		
		return 1;
	}
	
	public Sequence getSequenceFromId(long sequenceId) {
		for (Sequence sequence : sequences) {
			if (sequence.getSequenceId() == sequenceId)
				return sequence;
		}
		
		return null;
	}
	
	public void preloadSequenceImages() {
		
		// TODO: Logic goes here :]
	}
}
