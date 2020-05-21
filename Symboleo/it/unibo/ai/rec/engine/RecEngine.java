package it.unibo.ai.rec.engine;

import it.unibo.ai.rec.model.FluentsModel;
import it.unibo.ai.rec.model.RecTrace;

public interface RecEngine {
	public boolean setModel(String model) throws Exception;
	public FluentsModel start() throws Exception;
	public FluentsModel evaluateTrace(RecTrace trace) throws Exception;
	public long getStats();
	public boolean shutDown() throws Exception;
}
