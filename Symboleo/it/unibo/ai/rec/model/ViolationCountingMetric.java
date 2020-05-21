package it.unibo.ai.rec.model;

import it.unibo.ai.rec.model.*;


public class ViolationCountingMetric implements
		TrendMetric {

	private static String violStateId = "viol";
	private static String satStateId = "sat";
	private double gradient;
	private boolean countAll;

	public ViolationCountingMetric() {
		this(false);
	}
	
	public ViolationCountingMetric(boolean countAll) {
		this(countAll, 1);
	}
	
	public ViolationCountingMetric(int gradient) {
		this(false, gradient);
	}
	
	public ViolationCountingMetric(boolean countAll, int gradient) {
		this.countAll = countAll;
		this.gradient = gradient;
	}
	
	private boolean inState(Fluent f, String stateId, long time) {
		FluentState state = f.getStates().get(stateId);
		return state != null && state.holdsAt2(time);
	}
	
	@Override
	public double getValue(FluentsModel monitoringState, long time) {
		double totalViolTrend = 0;
		double total = 0;
		for(FluentGroup g: monitoringState.getFluentGroups().values()) {
			double totalNo = g.getFluents().size();
			double violNo = 0;
			double satNo = 0;
			for(Fluent f: g.getFluents().values()) {
				if(inState(f, violStateId, time))
					violNo++; //violNo+=weight!!!
				if(inState(f, satStateId, time))
					satNo++; //satNo+=weight!!!
			}
			double instanceNo = 0;
			if(countAll)
				instanceNo = totalNo;
			else
				instanceNo = violNo + satNo;
			if(instanceNo > 0) {
				totalViolTrend += violNo/instanceNo;	
				total++;
			}
		}
		return 1 - totalViolTrend/total;
		
	}
	
	
	public String getName() {
		return "system health";
	}

 
}
