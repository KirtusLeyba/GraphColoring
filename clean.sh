echo Removing .class files...

rm ./kleyba/genetic/*.class -f
rm ./kleyba/genetic/maxones/*.class -f
rm ./kleyba/genetic/graphColoring/*.class -f

echo Removing .jar files...

rm ./bin/*.jar -f

echo Removing manifest files..

rm ./manifest* -f
