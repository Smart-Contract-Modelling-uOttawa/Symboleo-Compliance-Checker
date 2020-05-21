package it.unibo.ai.rec.model;


public class MultiValuedFluent extends Fluent {

	public MultiValuedFluent(String name) {
		super(name);
	}
	
	public FluentState getFluentState(String state) {
		return states.get(state);
	}
	
	public boolean registerState(String state) {
		if(states.containsKey(state))
			return false;
		else {
			states.put(state, new FluentState(state));
			return true;
		}
	}
	

}
