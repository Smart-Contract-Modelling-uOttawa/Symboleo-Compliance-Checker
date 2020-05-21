package it.unibo.ai.rec.model;

public class SingleGroupingStrategy implements FluentGroupingStrategy {

	private String groupName = "Situations";
	public String getGroup(String fluentName) {
		return groupName;
	}

}