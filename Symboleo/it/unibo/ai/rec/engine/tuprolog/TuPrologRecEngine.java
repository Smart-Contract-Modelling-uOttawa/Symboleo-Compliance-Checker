package it.unibo.ai.rec.engine.tuprolog;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.event.OutputEvent;
import alice.tuprolog.event.OutputListener;
import it.unibo.ai.rec.engine.FluentsConverter;
import it.unibo.ai.rec.engine.RecEngine;
import it.unibo.ai.rec.model.FluentGroupingStrategy;
import it.unibo.ai.rec.model.FluentsModel;
import it.unibo.ai.rec.model.HappenedEventSet;
import it.unibo.ai.rec.model.RecTrace;

public class TuPrologRecEngine implements RecEngine {

	//private static String INF_TIME = "inf";
	private Logger logger = Logger.getLogger(TuPrologRecEngine.class);
	private Prolog engine;
	private Theory recTheory;
	private FluentGroupingStrategy strategy;
	private FluentsConverter converter;
	private long lastStats;	
	private List<String> activeFluents = null;
	
	public TuPrologRecEngine(FluentGroupingStrategy strategy) throws IOException{
		//BasicConfigurator.configure();
		logger.debug("Building 2Prolog Rec Engine...");
		recTheory = new Theory(getClass().getResourceAsStream("/it/unibo/ai/rec/engine/tuprolog/rec.pl"));	
		this.strategy = strategy;
		converter = new FluentsConverter(this.strategy);
	}
	
	private Prolog createProlog() throws Exception {
		Prolog p = new Prolog();
		p.addOutputListener(new OutputListener(){
			public void onOutput(OutputEvent e) {
				logger.debug("TuProlog ---->"+e.getMsg());
			}});
		p.addTheory(recTheory);
		return p;
	}
	
	@Override
	public boolean setModel(String model) throws Exception {
		try {
			engine = createProlog();			
			engine.addTheory(new Theory(model));
			logger.debug("Model correctly loaded!");
			return true;
		}
		catch(Exception e) {
			logger.error("Error loading model");
			logger.error(e);
			return false;
		}
	} 
	
	public FluentsModel start() throws Exception {
		lastStats = -1;
		return evaluate("start.");
	}
	
	@Override
	public FluentsModel evaluateTrace(RecTrace trace) throws Exception {
		logger.debug("Trying to evaluate the trace...");
		String goal = "update("+convertTrace(trace)+").";
		FluentsModel model = evaluate(goal);
		if(!trace.isOpen())
			engine.solve("reset.");
		return model;
	}
	
	private FluentsModel evaluate(String goal) throws Exception {
		logger.debug(goal);
		
		long before = System.currentTimeMillis();
		boolean ok = engine.solve(goal).isSuccess();
		long after = System.currentTimeMillis();
		lastStats = after - before;
		//System.out.print(after-before);
		//System.out.println();
		//return null;
		
		logger.debug("Trace evaluated: "+ok);
		if(!ok)
			throw new Exception("Fatal error when reasoning upon the trace");
		SolveInfo resultInfo = engine.solve("status(MVIs).");
		Term result = resultInfo.getVarValue("MVIs");
		ok = !ok;
		logger.debug("Produced result: "+result);		
		FluentsModel resultingModel = converter.toFluentsModel(result.toString());		
		activeFluents = converter.findActiveFluents(result.toString());		
		return resultingModel;
	}

	/**
	 * @return List<String>
	 * @author Ali
	 * return active situations
	 */
	public List<String> getActiveFluents() {
		return activeFluents;
	}

	@Override
	public boolean shutDown() throws Exception {
		engine.solve("reset.");
		lastStats = -1;
		return true; //do nothing
	}

	public long getStats() {
		return lastStats;
	}
	
	
	protected String convertTrace(RecTrace trace) {
		StringBuffer b = new StringBuffer();
		boolean first = true;
		b.append("[");
		for(HappenedEventSet set: trace.getHappenedEvents()) {
			for(String event: set.getEvents()) {
				if(first)
					first = false;
				else
					b.append(",");
				b.append("happens(");
				b.append(event);
				b.append(",");
				b.append(set.getTime());
				b.append(")");
			}
		}
		b.append("]");
		return b.toString();
	}
	
	
	/*
	private FluentsModel toFluentsModel(String result) throws Exception {
		
		FluentsModel m = new FluentsModel(strategy);
		result = result.subSequence(1, result.length()-1).toString(); //remove "[" and "]"	
		Scanner scanner = new Scanner(result);
		scanner.useDelimiter("mholds_for");
		while (scanner.hasNext()) {
	        String mviStr = scanner.next(); 
	        mviStr = mviStr.subSequence(1, mviStr.length()-1).toString(); //remove "(" and ")"
	        int removeTail = scanner.hasNext() ? 2:1;
	        mviStr = mviStr.substring(0,mviStr.length()-removeTail); //remove "]," or "]"
	        StringTokenizer tokenizer = new StringTokenizer(mviStr,"[",true);
	        
	        String fluent = "";
	        String startTime = "";
	        String endTime = "";
	        String token;
	        while(tokenizer.hasMoreTokens()) {
	        	token = tokenizer.nextToken();
	        	if(tokenizer.hasMoreTokens())
	        		fluent += token;
	        	else {
	        		fluent = fluent.substring(0,fluent.length() - 2); //remove ,[
	        		startTime = token.split(",")[0];
	        		endTime = token.split(",")[1];
	        	}
	        }
	        long startTimeFloat = Long.parseLong(startTime);
	        if(endTime.equals(INF_TIME))
	        	addMVI(m, fluent, startTimeFloat);
	        else
	        	addMVI(m, fluent, startTimeFloat, Long.parseLong(endTime));
		} 
		return m;
	}
	
	
	private void addMVI(FluentsModel model, String fluentString, long start, long end) throws ParseException {
		addMVI(model, fluentString, new ClosedMVI(start, end));
	}
	
	private void addMVI(FluentsModel model, String fluentString, long start) throws ParseException {
		addMVI(model, fluentString, new OpenMVI(start));
	}
	
	private void addMVI(FluentsModel model, String fluentString, MVI mvi) throws ParseException {
		model.addMVI(fluentString, mvi);
	}*/
}
