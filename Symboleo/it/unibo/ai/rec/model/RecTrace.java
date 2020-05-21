package it.unibo.ai.rec.model;

import java.util.Vector;

public class RecTrace {
	private boolean open;
	private Vector<HappenedEventSet> events;
	
	public RecTrace(boolean open) {
		this.open = open;
		events = new Vector<HappenedEventSet>();
	}
	
	public void addHappenedEvent(String event, long time) {
		int i = 0;
		for(i = 0; i < events.size() && events.get(i).getTime() < time; i++);
		if(i >= events.size() || events.get(i).getTime() != time) {
			HappenedEventSet es = new HappenedEventSet(time);
			es.getEvents().add(event);
			events.add(i, es);
		}
		else 
			events.get(i).getEvents().add(event);
	}
	
	public Vector<HappenedEventSet> getHappenedEvents() {
		return events;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		boolean first = true;
		for(HappenedEventSet e: events) {
			if(first) {
				b.append("[");
				first = false;
			}
			else
				b.append(", ");
			b.append(e.toString());
		}
		b.append("]");
		return b.toString();
	}
	
	/**
	 * @author Alireza
	 * clear trace
	 */
	public void clear() {
		events.clear();
	}
	
}
