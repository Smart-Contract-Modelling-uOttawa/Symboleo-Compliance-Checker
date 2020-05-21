package it.unibo.ai.rec.model;
import java.util.Hashtable;
import java.util.Map;
	
public abstract class Fluent implements FluentEntity{
	
	protected Map<String,FluentState> states;
	protected String name;
	
	public Fluent(String name) {
		states = new Hashtable<String,FluentState>();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean holdsAt(String state, long time) {
		FluentState fState = states.get(state);
		return fState != null && fState.holdsAt(time);
	}
	
	public Map<String,FluentState> getStates() {
		return states;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Situation "+name+"\n");
		for(String state: states.keySet()) {
			b.append("\tState '"+state+"':\n");
			MVI[] mvis = states.get(state).getMVIs();
			for(MVI mvi: mvis)
				b.append("\t\t-> "+mvi+"\n");
		}
		return b.toString();
	}
	
	public double getMinimumStart() {
		double min = Double.MAX_VALUE;
		double cur;
		for(String state: states.keySet()) {
			MVI[] mvis = states.get(state).getMVIs();
			if(mvis.length > 0) {
				cur = mvis[0].getStart();
				if(cur < min)
					min = cur;
			}
		}
		return min;
	}
	
	public boolean equals(Fluent f) {
		return getName().equals(f.getName()) && getClass().equals(f.getClass());
	}
	
	public int compareTo(Object f) {
		if(f instanceof Fluent)
			return (int)(getMinimumStart() - ((Fluent)f).getMinimumStart());
		else
			return -1;
	}
	
}
