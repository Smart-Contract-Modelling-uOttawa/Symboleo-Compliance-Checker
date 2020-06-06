package it.unibo.ai.rec.engine.tuprolog;

public class App {	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {		
		if(args.length<3) {		
			System.out.println("Reasoner is running ...");
			ComplianceChecker checker = new ComplianceChecker("sample/sog_freight/axioms.txt", "sample/sog_freight/domdependentAxioms.txt", "sample/sog_freight/traces.json");
			checker.check();
		}
		else if(args.length == 3) {
			System.out.println("Reasoner is running ...");
			ComplianceChecker checker = new ComplianceChecker(args[0], args[1], args[2]);
			checker.check();
		}
		else
			System.out.println("Error: command is wrong. Enter 'java -jar Symboleo.jar <axioms file> <domain axioms file> <traces file>");
	}

}
