# Requirements
1- JDK 12 or higher\
2- Eclipse IDE

# Execute in Eclipse IDE
1- Make a folder\
2- Clone source code in the folder\
Git clone https://github.com/Smart-Contract-Modelling-uOttawa/Symboleo-Compliance-Checker.git \
3- Open the project in Eclipse IDE\
4- Follow windows->preference->java->installed JREs and add JDK 12 or upper.\
5- Right click on the project-> Run As-> Maven Clean\
6- Right click on the project-> Run As-> Maven Build ->  set 'compile' in Goal field and then press 'run' button\
7- under Symboleo/it.unibo.ai.rec.engine.tuprolog right click on App.java and run as java application.

# Execute runnable Jar file
1- Download Symboleo.jar from [release 0.1](https://github.com/Smart-Contract-Modelling-uOttawa/Symboleo-Compliance-Checker/releases/tag/0.1)\
2- Download sales_of_good sample\
3- Execute "java -jar Symboleo.jar \<axioms file\> \<domain axioms file\> \<traces file\>" command. e.g.,\
"java -jar Symboleo.jar axioms.txt domdependentAxioms.txt traces.json"
