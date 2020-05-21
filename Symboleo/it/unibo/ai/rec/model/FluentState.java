package it.unibo.ai.rec.model;
import java.util.Vector;

public class FluentState {
	private String name;
	private Vector<MVI> mvis;
	
	public FluentState(String name) {
		this.name = name;
		mvis = new Vector<MVI>();
	}
	
	public String getName() {
		return name;
	}
	
	public void addMVI(MVI mvi) {
		int i=0;
		for(;i<mvis.size() && mvis.get(i).compareTo(mvi)<0;i++);
		mvis.add(i, mvi);
	}
	
	public MVI[] getMVIs() {
		return mvis.toArray(new MVI[mvis.size()]);
	}
	
//	public Vector<MVI> getMVIs() {
//		return mvis;
//	}
	
	public boolean holdsAt(long time) {
		for(MVI mvi: mvis)
			if(mvi.contains(time, MVI.ExtremeType.OPEN, MVI.ExtremeType.CLOSED))
				return true;
		return false;
	}
	
	public boolean holdsAt2(long time) {
		for(MVI mvi: mvis)
			if(mvi.contains(time, MVI.ExtremeType.CLOSED, MVI.ExtremeType.OPEN))
				return true;
		return false;
	}
	
}
