# Requirements
1- JAVA SE Runtime Environment 18.3 or higher\
2- Eclipse IDE

# Execute in Eclipse IDE
1- Make a folder\
2- Clone source code in the folder\
Git clone https://github.com/Smart-Contract-Modelling-uOttawa/Symboleo-Compliance-Checker.git \
3- Open the project in Eclipse IDE\
4- under Symboleo/it.unibo.ai.rec.engine.tuprolog right click on App.java and run as java application.

# Execute runnable Jar file
1- Download Symboleo.jar from [release 0.1](https://github.com/Smart-Contract-Modelling-uOttawa/Symboleo-Compliance-Checker/releases/tag/0.1)\
2- Download sales_of_good sample\
3- Execute "java -jar Symboleo.jar \<axioms file\> \<domain axioms file\> \<traces file\>" command. e.g.,\
"java -jar Symboleo.jar axioms.txt domdependentAxioms.txt traces.json"
