package it.unibo.ai.rec.model;
import java.util.*;

public class FluentGroup implements FluentEntity,Comparable {

	private Map<String, Fluent> fluents;
	private String groupName;
	
	public FluentGroup(String groupName) {
		this.groupName = groupName;
		fluents = new Hashtable<String, Fluent>();
	}
	
	
	@Override
	public double getMinimumStart() {
		if(fluents.isEmpty())
			return 0;
		double absoluteMin = Double.MAX_VALUE;
		//= fluents.get(0).getMinimumStart();
		double min;
		for(Fluent f: fluents.values()) {
			min = f.getMinimumStart();
			if (min < absoluteMin)
				absoluteMin = min;
		}
		return absoluteMin;
	}
	
	public Map<String,Fluent> getFluents() {
		return fluents;
	}

	@Override
	public String getName() {
		return groupName;
	}

	@Override
	public boolean holdsAt(String state, long time) {
		for(Fluent f: fluents.values())
			if(f.holdsAt(state, time))
				return true;
		return false;
	}
	
	public boolean equals(FluentGroup g) {
		return getName().equals(g.getName());
	}


	public int compareTo(Object g) {
		if(g instanceof FluentGroup)
			return (int)(getMinimumStart() - ((FluentGroup)g).getMinimumStart());
		else
			return -1;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		for(Fluent f: fluents.values()) {
			b.append("******");
			b.append(f);
		}
		return b.toString();
	}

	

}
