package dk.aau.cs.giraf.zebra.models;

public class Pictogram {
	private long pictogramId;
	
	public long getPictogramId() {
		return pictogramId;
	}
	
	public void setPictogramId(long pictogramId) {
		this.pictogramId = pictogramId;
	}
	
	public Pictogram getClone() {
		Pictogram clone = new Pictogram();
		clone.pictogramId = this.pictogramId;
		
		return clone;
	}
}
