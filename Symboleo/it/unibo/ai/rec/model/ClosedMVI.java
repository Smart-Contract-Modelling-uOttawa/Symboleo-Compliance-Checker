package it.unibo.ai.rec.model;


public class ClosedMVI extends MVI{
	private long end;
	
	
	public ClosedMVI(long start, long end) {
		super(start);
		this.end = end;
	}
	
	public long getEnd() {
		return end;
	}
	
	public boolean contains(long time, ExtremeType left, ExtremeType right) {
		boolean contains = left.equals(ExtremeType.OPEN) ? time > getStart() : time >= getStart();
		return contains && right.equals(ExtremeType.OPEN) ? time < getEnd() : time <= getEnd();
	}
	
	public boolean contains(MVI mvi) {
		boolean contains = getStart() <= mvi.getStart();
		if(contains && mvi instanceof ClosedMVI)
			contains = contains && getEnd() >= ((ClosedMVI)mvi).getEnd();
		return contains;
	}
	
	public String toString() {
		return "("+String.format("%d", getStart())+", "+String.format("%d", getEnd())+"]";
	}
	
	
}
