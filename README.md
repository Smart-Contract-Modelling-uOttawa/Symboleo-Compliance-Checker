# What is Symboleo?
Symboleo is a formal specification language for legal contracts. It describes contracts in terms of contractual norms such as obligations, powers, etc.  Symboleo is an event-based language that inherits event-calculus predicates to specify normsin terms of situations. Contractual norms evolve over time according to events occurring at time points specified in the language. In other words, internal (e.g., deadlines) and external events (e.g., payments) may change the state of the norms during time. Three state machines represent the evolution semantics of norms while corresponding formal specifications have been axiomatized in the Axiom.txt file and the tutorial document. The state machines are given in our accepted paper titled "Symboleo: Towards a Specification Language for Legal Contracts".

# Symboleo Compliance Checker
This tool is a compliance checker for formal contract specifications written in Symboleo. It was inspired by the tool  jREC. It evaluates whether given sequences of events (i.e., traces) are compliant with given contract specifications. To this aim, the tool supports Symboleo's primitive predicates (such as **within** and **occur**), axiomatized semantics, description of contracts, and compliance scenarios. For each contract specification, we have a part consisting of reusable, domain-independent axioms and a contract-specific part, both written in Prolog. The reusable axioms are replicated in each contract. They formalize the state machines of norms. The contract-specific part describes  the list of parameterized obligations and powers that is specific to each contract. Symboleo proposes templates for contracts, obligations, powers, and events. For example, the signature of an obligation is " \<trigger\> -> \<name\>:O(debtor, creditor, antecedent, consequent)", and is modeled in the tool as shown below:

	o(X) :- o1(X).
	o1(oDel).
	associate(oDel, cArgToCan).
	
	initially(debtor(X, P)) 		:- o1(X), initially(bind(seller, P)).
	initially(creditor(X, P)) 		:- o1(X), initially(bind(buyer, P)).
	
	initiates(E, trigger(oDel))		:- happens(E, _), initiates(E, inEffect(cArgToCan)). 
	ant(oDel) :- true.	
	initiates(E, cons(oDel)) 		:- happens(E, T), delivered(E), deliveryDueDate(T1), T<T1.
	deadline(cons(oDel), 10).
	happens(deliveryDuePassed, 10).
  
where oDel and cArgToCan are an instance of delivery obligation and Sales of Good contract respectively.\
As mentioned, the execution of contracts is controlled by events. Sequences of events alongside expected properties are defined in batch files that are fed sequentially into the Prolog programs described above. Attributes of events distinguish events that can happen in any order because of the reactive nature of the tool. Traces put contracts in new situations that should be compliant with expected properties.

# Current State
The tool is able to:\
1- Specify and instantiates multiple contracts, conditional/unconditional powers and obligations in a hierarchical structure\
2- Provide a list of compliance scenarios for positive scenarios. These scenarios check situations that must be true after running a trace of events.

