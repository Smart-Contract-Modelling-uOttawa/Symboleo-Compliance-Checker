package it.unibo.ai.rec.common;

public enum TimeGranularity {
	MILLIS	(1),
	SECONDS	(1000),
	MINUTES	(60*1000),
	HOURS	(60*60*1000),
	DAYS	(24*60*60*1000);
	
	private long factor;
	private TimeGranularity(int factor) {
		this.factor = factor;
	}
	
	public long getFactor() {
		return factor;
	}
}