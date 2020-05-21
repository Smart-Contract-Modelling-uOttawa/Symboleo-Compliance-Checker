package it.unibo.ai.rec.model;
import java.util.*;

public class HappenedEventSet implements Comparable<HappenedEventSet> {
	private Vector<String> events;
	private long time;

	public HappenedEventSet(long t) {
		events = new Vector<String>();
		time = t;
	}

	public Vector<String> getEvents() {
		return events;
	}
	
	public String getDescription() {
		StringBuffer b = new StringBuffer();
		for(String event: events) {
			b.append(event);
			b.append(", ");
		}
		return b.substring(0, b.length()-2);
	}

	
	public long getTime() {
		// TODO Auto-generated method stub
		return time;
	}

	
	public String toString() {
		return "h([" +  getDescription() + "]," + time + ").";
	}
	
	public int compareTo(HappenedEventSet ev) {
		return (int)(getTime() - ev.getTime());
	}
}
