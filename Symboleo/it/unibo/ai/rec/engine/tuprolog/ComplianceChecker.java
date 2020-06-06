package it.unibo.ai.rec.engine.tuprolog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.*;

import it.unibo.ai.rec.model.FluentsModel;
import it.unibo.ai.rec.model.RecTrace;
import it.unibo.ai.rec.model.SingleGroupingStrategy;

public class ComplianceChecker {

	private String axiomPathFile = "";
	private String domainAxiomPathFile = "";
	private String tracePathFile = "";
	
	public ComplianceChecker(String axiomPath, String domainAxiomPath, String tracePath) {
		axiomPathFile = axiomPath;
		domainAxiomPathFile = domainAxiomPath;
		tracePathFile = tracePath;
	}
	
	/**
	 * @param String
	 * @return String
	 * @throws IOException 
	 * @author Alireza
	 * read axioms from a file and return all axioms in string format
	 */
	private String readAxioms() throws IOException {
		File axiomFile = new File(axiomPathFile);
		
		// read domain independent axioms
		BufferedReader buffer = new BufferedReader(new FileReader(axiomFile));
		String axioms = "";
		String str = "";
		while((str = buffer.readLine()) != null)		
			if(str.length()>0) {
				str = str.trim();
				if(str.length()>0 && str.charAt(0) != '#')
					axioms += str + "\n";
			}
		buffer.close();
		
		// read domain dependent axioms
		axioms += readDomDependAxioms();
		return axioms;
	}
	
	/**
	 * @param 
	 * @return 
	 * @author Alireza
	 * parse domain dependent axioms
	 * @throws IOException 
	 */
	String readDomDependAxioms() throws IOException {
		File axiomFile = new File(domainAxiomPathFile);
		BufferedReader buffer = new BufferedReader(new FileReader(axiomFile));
		String str = "";
		String axioms = "";
		while((str = buffer.readLine()) != null)		
			if(str.length()>0) {
				str = str.trim();
				if(str.length()>0 && str.charAt(0) != '#')
					axioms += str + "\n";
			}
		buffer.close();
		return axioms;
	}
	
	/**
	 * @param String
	 * @return Dictionary
	 * @author Alireza
	 * read traces and expected results from a file and store in a data dictionary 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Trace[] readTraces() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Trace[] traces = mapper.readValue(new File(tracePathFile), Trace[].class);
		return traces;
	}
	
	public void check() throws Exception {
		List<String> activeSituations = new ArrayList<String>();
		List<String> expectedSituations = new ArrayList<String>();
		TuPrologRecEngine engine = new TuPrologRecEngine(new  SingleGroupingStrategy());
		
		String axioms = readAxioms();
		Trace[] traces = readTraces();
		
		for(int t=0; t<traces.length; t++) {	//evaluate each trace
			String initialAxioms = initializeAxioms(traces[t].initials);
			String attributes= initializeAxioms(traces[t].attributes);
			engine.setModel(initialAxioms + axioms + attributes);
			
			engine.start();
			RecTrace trace = new RecTrace(true);
		
			// clear vectors
			expectedSituations.clear();
			activeSituations.clear();
			trace.clear();
			
			String traceName = traces[t].name;
			String traceDescription = traces[t].description;
			for(int e=0; e<traces[t].events.size(); e++) {
				String[] items = traces[t].events.get(e).split(",");
				trace.addHappenedEvent(items[0], Long.parseLong(items[1]));
			}	
						
			for(int e=0; e<traces[t].expectedStates.size(); e++) {
				expectedSituations.add(traces[t].expectedStates.get(e));
			}						
						
			FluentsModel model = engine.evaluateTrace(trace);
			activeSituations = engine.getActiveFluents();
			
			System.out.println(model);			
			System.out.println("Trace"+ (t+1) +": " + traceName);
			System.out.println("Description:" + traceDescription);
			System.out.println("Active Situations: " + activeSituations);
			System.out.println("Expected Situations: " + expectedSituations);			
						
			// check compliance
			boolean isCompliant = true;
			for(int ef=0; ef<expectedSituations.size(); ef++)
				if(!activeSituations.contains(expectedSituations.get(ef))) {
						System.out.println("\n-->not compliant: '" + expectedSituations.get(ef) + "' is not true");
						isCompliant = false;
				}
			if (isCompliant)
				System.out.println("\n--> It is compliant");
			System.out.println("\n############################################");
			// Shutdown engine
			engine.shutDown();
		}
		System.out.println("\n\n\n All traces executed.");
	}
	
	String initializeAxioms(List<String> initials) {
		String axioms = "";
		for(int i=0; i<initials.size(); i++) {
			//create initiates predicate
			axioms += "initially(" + initials.get(i) +").\n";		
		}
		return axioms;
	}
}
