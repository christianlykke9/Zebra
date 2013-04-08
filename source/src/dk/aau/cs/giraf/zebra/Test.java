package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class Test {
	public static Sequence createSequence(Context context) {
		
		Sequence sequence = new Sequence("Test Sequence");
		
		int[] drawableIds = new int[] {
				R.drawable.vask_1,
				R.drawable.vask_2,
				R.drawable.vask_3,
				R.drawable.vask_4,
				R.drawable.vask_2,
				R.drawable.vask_4,
				R.drawable.vask_1,
				R.drawable.vask_3
			};
		
		for (int i : drawableIds) {
			sequence.addPictogramAtEnd(context.getResources().getDrawable(i));
		}
		
		return sequence;
	}
	
	public static List<Sequence> getSequences(Context context) {
		ArrayList<Sequence> list = new ArrayList<Sequence>();
		list.add(createSequence(context));
		return list;
	}
}