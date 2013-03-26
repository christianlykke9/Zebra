package dk.aau.cs.giraf.zebra;

import android.content.Context;

public class Test {
	public static Sequence createSequence(Context context) {
		
		Sequence sequence = new Sequence("Test Sequence");
		
		int[] drawableIds = new int[] {
				R.drawable.vask_1,
				R.drawable.vask_2,
				R.drawable.vask_3,
				R.drawable.vask_4
			};
		
		for (int i : drawableIds) {
			sequence.pictograms.add(context.getResources().getDrawable(i));
		}
		
		return sequence;
	}
}