package it.unibo.ai.rec.engine;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import it.unibo.ai.rec.model.ClosedMVI;
import it.unibo.ai.rec.model.FluentGroupingStrategy;
import it.unibo.ai.rec.model.FluentsModel;
import it.unibo.ai.rec.model.MVI;
import it.unibo.ai.rec.model.OpenMVI;

public class FluentsConverter {
	public static String INF_TIME = "inf";
	
	private FluentGroupingStrategy strategy;
		
	public FluentsConverter(FluentGroupingStrategy strategy) {
		this.strategy = strategy;
	}
	

	@SuppressWarnings("null")
	public List<String> findActiveFluents(String result) throws Exception {
		List<String> activeFluents = new ArrayList<String>();
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
	        if(endTime.equals(INF_TIME))
	        	activeFluents.add(fluent);
		} 
		return activeFluents;
	}
	
	
	
	public FluentsModel toFluentsModel(String result) throws Exception {
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
	}
}
