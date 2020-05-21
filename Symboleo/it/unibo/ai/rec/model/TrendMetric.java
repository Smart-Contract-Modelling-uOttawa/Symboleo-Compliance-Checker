package it.unibo.ai.rec.model;
import it.unibo.ai.rec.model.*;

public interface TrendMetric {
	public double getValue(FluentsModel monitoringState, long time);
	public String getName();
}
