package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.zebra.models.Child;

import android.app.Application;

public class ZebraApplication extends Application {

	private static List<Child> children = new ArrayList<Child>();
	
	private static List<Child> getTestChildren() {
		ArrayList<Child> children = new ArrayList<Child>();
		
		Child child = new Child(10);
		children.add(child);
		
		return children;
	}
	
	public static List<Child> getChildren() {
		
		// TODO: Remove ME
		if (children.size() == 0)
			children = getTestChildren();
		
		return children;
	}
	
	public static void setChildren(List<Child> value) {
		children = value;
	}
	
	public static Child getChildFromId(long id) {
		for (Child child : children) {
			if (child.getProfileId() == id)
				return child;
		}
		
		return null;
	}
	

}
