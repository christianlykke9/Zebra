package dk.aau.cs.giraf.zebra;

public class EditMode {
	
	private static boolean editMode = false;

	public static boolean get() {
		return editMode;
	}

	public static void set(boolean mode) {
		editMode = mode;
		
	}
	
	public static void toggle()
	{
		set(!get());
	}

}
