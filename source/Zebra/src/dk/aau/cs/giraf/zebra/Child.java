package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.List;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class Child {
	
	private List<Sequence> sequences = new ArrayList<Sequence>();
	private Profile profile;
	
	public Child(Profile profile) {
		this.profile = profile;
	}
	
	public String getName() {
		String name = "";
		name = addWordToString(name, getProfile().getFirstname());
		name = addWordToString(name, getProfile().getMiddlename());
		name = addWordToString(name, getProfile().getSurname());
		
		return name;
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public int getSequenceCount() {
		return sequences.size();
	}
	
	public List<Sequence> getSequences() {
		return sequences;
	}
	
	public void setSequences(List<Sequence> sequences) {
		this.sequences = sequences;
	}
	
	public void preloadSequenceImages() {
		for (Sequence sequence : sequences) {
			sequence.preloadImage();
		}
	}
	
	private String addWordToString(String string, String word) {
		if (word != null) {
			if (!string.isEmpty())
			{
				string += " ";
			}
			string += word;
		}
		
		return string;
	}
}
