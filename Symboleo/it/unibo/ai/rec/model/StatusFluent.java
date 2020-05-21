package it.unibo.ai.rec.model;

public class StatusFluent extends FluentState {

	private String status;
	
	public StatusFluent(int a, String name, String status) {
		super(name);
	}
	
	public String getStatus() {
		return status;
	}

}
