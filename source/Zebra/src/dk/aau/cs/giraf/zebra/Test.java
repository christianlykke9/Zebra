package dk.aau.cs.giraf.zebra;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class Test {
	public static Sequence createWashSequence(Child child, Context context) {
		
		Sequence sequence = new Sequence(context, child, "Vask hænder");
		sequence.setSequenceId(1);
		sequence.setImageId(1);
		
		int[] drawableIds = new int[] {
				R.drawable.sleeven_up,
				R.drawable.wash_hands,
				R.drawable.soap,
				R.drawable.scrup_hands,
				R.drawable.dry_hands,
				R.drawable.sleeve_down
			};
		
		for (int i : drawableIds) {
			sequence.addPictogramAtEnd(context.getResources().getDrawable(i));
		}
		
		return sequence;
	}
	
	public static Sequence createSequence(Child child, int id, Context context) {
		Sequence s = null;
		
		switch (id) {
			case 1:
				s=createWashSequence(child, context);
				break;
			case 2:
				s= createHomeworkSequence(child, context);
				break;
			case 3:
				s=createPackSequence(child, context);
				break;			
		}
		
		return s;
	}
	
	public static Sequence createHomeworkSequence(Child child, Context context) {
		
		Sequence sequence = new Sequence(context, child, "Lektier før leg");
		sequence.setSequenceId(2);
		sequence.setImageId(2);
		
		int[] drawableIds = new int[] {
				R.drawable.school_work,
				R.drawable.no_mobile,
				R.drawable.ten_minutes,
				R.drawable.play_train
			};
		
		for (int i : drawableIds) {
			sequence.addPictogramAtEnd(context.getResources().getDrawable(i));
		}
		
		return sequence;
	}
	
	public static Sequence createPackSequence(Child child, Context context) {
		
		Sequence sequence = new Sequence(context, child, "Pak sammen");
		sequence.setSequenceId(3);
		sequence.setImageId(3);
		
		int[] drawableIds = new int[] {
				R.drawable.pack_cloth,
				R.drawable.drive,
				R.drawable.car_travel
			};
		
		for (int i : drawableIds) {
			sequence.addPictogramAtEnd(context.getResources().getDrawable(i));
		}
		
		return sequence;
	}
	
	public static List<Sequence> getSequences(Child child, int count, Context context) {
		ArrayList<Sequence> list = new ArrayList<Sequence>();

		if (count > 0)
			list.add(createWashSequence(child, context));
		
		if (count > 1)
			list.add(createHomeworkSequence(child, context));
		
		if (count > 2)
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
			list.add(createPackSequence(child, context));
		
		return list;
	}
}