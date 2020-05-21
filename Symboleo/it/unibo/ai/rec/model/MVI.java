package it.unibo.ai.rec.model;

public abstract class MVI implements Comparable<MVI> {
	public static enum ExtremeType {
		OPEN,
		CLOSED;
	}

	private long start;
	
	public MVI(long start) {
		this.start = start;
	}
	
	public long getStart() {
		return start;
	}
	
	
	public abstract boolean contains(long time, ExtremeType left, ExtremeType right);
	public abstract boolean contains(MVI mvi);
	
	public int compareTo(MVI mvi) {
		return (int)(getStart() - mvi.getStart());
	}

}
