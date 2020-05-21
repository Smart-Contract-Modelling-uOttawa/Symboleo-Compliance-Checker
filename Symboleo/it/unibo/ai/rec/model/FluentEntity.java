package it.unibo.ai.rec.model;
	
public interface FluentEntity extends Comparable {
	
	public String getName();
	public boolean holdsAt(String state, long time);
	public double getMinimumStart();
}
