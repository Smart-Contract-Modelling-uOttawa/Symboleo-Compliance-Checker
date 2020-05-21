package it.unibo.ai.rec.model;

public class StandardFluent extends Fluent {

	public StandardFluent(String name) {
		super(name);
		states.put("true", new FluentState("true"));
	}
	
	public FluentState getState() {
		return states.get("true");
	}
	
	public boolean holdsAt(long time) {
		return super.holdsAt("true", time);
	}
	
	
}
