package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;

import android.database.Observable;

public class EditMode extends Observable<EditMode.EditModeObserver> {
	
	private boolean editMode = false;
	
	private static EditMode instance = new EditMode();
	
	private EditMode() {
		//Private Constructor
	}

	public static boolean get() {
		return instance.editMode;
	}

	public static void set(boolean mode) {
		if (mode != instance.editMode) {
			instance.editMode = mode;
			notifyChanged();
		}
	}
	
	public static void toggle()
	{
		set(!get());
		notifyChanged();
	}
	
	protected static void notifyChanged() {
		ArrayList<EditModeObserver> observers = instance.mObservers;
		synchronized (observers) {
			for (int i = observers.size() - 1; i >= 0; i--) {
				observers.get(i).onEditModeChange(get());
			}
		}
	}
	
	public interface EditModeObserver {
		public void onEditModeChange(boolean editMode);
	}
	
}
