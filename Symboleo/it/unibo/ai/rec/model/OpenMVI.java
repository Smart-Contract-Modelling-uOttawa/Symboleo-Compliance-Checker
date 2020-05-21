package it.unibo.ai.rec.model;

import it.unibo.ai.rec.model.MVI.ExtremeType;


public class OpenMVI extends MVI {

	public OpenMVI(long start) {
		super(start);
	}
	
	public boolean contains(long time, ExtremeType left, ExtremeType right) { 
		return left.equals(ExtremeType.OPEN) ? time > getStart() : time >=getStart();
	}
	
	public boolean contains(MVI mvi) {
		return getStart() <= mvi.getStart() && mvi instanceof OpenMVI;
	}
	
	public String toString() {
		return "("+String.format("%d",getStart())+", ...";
	}

}
