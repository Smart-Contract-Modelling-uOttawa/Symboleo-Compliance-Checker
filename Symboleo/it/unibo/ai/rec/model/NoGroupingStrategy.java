package it.unibo.ai.rec.model;

public class NoGroupingStrategy implements FluentGroupingStrategy {

	public String getGroup(String fluentName) {
		return fluentName;
	}

}
