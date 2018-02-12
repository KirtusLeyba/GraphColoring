echo Building Project...

javac kleyba/genetic/GeneticAlgorithm.java kleyba/genetic/graphColoring/GraphColoringGA.java kleyba/genetic/graphColoring/GraphColoringTest.java

echo Making Manifest File...

echo Main-Class: kleyba.genetic.graphColoring.GraphColoringTest > manifest.txt

jar cvfm ./bin/out.jar manifest.txt kleyba/genetic/graphColoring/*.class kleyba/genetic/*.class

echo Building balanced version...

javac kleyba/genetic/GeneticAlgorithm.java kleyba/genetic/balGraphColoring/BalGraphColoringGA.java kleyba/genetic/balGraphColoring/BalGraphColoringTest.java

echo Making Manifest File...

echo Main-Class: kleyba.genetic.balGraphColoring.BalGraphColoringTest > balmanifest.txt

jar cvfm ./bin/outbal.jar balmanifest.txt kleyba/genetic/balGraphColoring/*.class kleyba/genetic/*.class

echo Building Neutrality Analysis...

javac kleyba/genetic/neutralGraphColoring/NeutralityAnalysis.java

echo Neutrality Analysis Manifest File...

echo Main-Class: kleyba.genetic.neutralGraphColoring.NeutralityAnalysis > neutralmanifest.txt

jar cvfm ./bin/na.jar neutralmanifest.txt kleyba/genetic/neutralGraphColoring/*.class kleyba/genetic/*.class

echo Building Neutrality Graph version...

javac kleyba/genetic/GeneticAlgorithm.java kleyba/genetic/neutralGraphColoring/NeutralGraphColoringGA.java kleyba/genetic/neutralGraphColoring/NeutralGraphColoringTest.java

echo Making Manifest File...

echo Main-Class: kleyba.genetic.neutralGraphColoring.NeutralGraphColoringTest > neutmanifest.txt

jar cvfm ./bin/outneut.jar neutmanifest.txt kleyba/genetic/neutralGraphColoring/*.class kleyba/genetic/*.class