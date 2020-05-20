# What is Symboleo?
Symboleo is a formal specification language for legal contracts. This event-based language inherits event-calculus predicates to specify contractual norms(e.g., obligations, powers, assignments, subcontract) in terms of situations. Contractual norms evolve over time thanks to supporting time points and time intervals in this language. In other words, internal(e.g., deadlines) and external events(e.g., payment) may change states of these norms during the time. Three state machines represent the evolution semantics of norms while corresponding formal specifications have been axiomatized in Axiom.txt file and tutorial document. These state machines are available in a paper titled "Symboleo: Towards a Specification Language for Legal Contracts".

# Symboleo Compliance Checker
This tool is a compliance checker for formal contract specifications written in Symboleo is created inspired by [jREC](https://www.inf.unibz.it/~montali/tools.html#jrec). It evaluates whether a sequence of events(i.e., a trace) is compliant with the contract specification. To this aim, the tool supports Symboleo's primitive predicates(e.g., within and occur), axiomatized semantics, description of contracts, and compliance scenarios.
Reusable axioms are defined with Prolog language independent of the type of contract and are replicated for all contracts. These domain-independent axioms formalize state machines of norms. However, any contract is a list of parameterized obligations and powers that shall be described individually per contract. Symboleo proposes a template for contracts, obligations, powers, and events. For example, the signature of an obligation is "<trigger> -> <name>:O(debtor, creditor, antecedent, consequent)", and is modeled as below in the tool:
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
  
  where oDel and cArgToCan are an instance of delivery obligation and Sales of Good contract respectively.
 
Events control the execution of a contract. Sequences of events alongside expected properties are defined in a batch file and sequentially are fed into the specification. Attributes of events distinguish events that can happen in any order thanks to the reactive nature of the tool. Traces put contracts in new situations that shall be compliant with expected properties.

# Current State
The tool is able to:
1- Specify and instantiates multiple contracts, conditional/unconditional powers and obligations in a hierarchical structure
2- Provide a list of compliance scenarios for positive scenarios

# Future Work
In the close future, we will improve the tool to investigate negative scenarios, runtime operations such as subcontracting, graphical user interface. Negative scenarios determine situations that a trace never brings about.

