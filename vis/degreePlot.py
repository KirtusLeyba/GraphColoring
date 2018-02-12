import sys
import matplotlib.pyplot as plt

## Setup vars
avgMaxFitness = []
averageDegree = []

fileIn = sys.argv[1]

with open(fileIn, 'r') as fp:
	line = fp.readline()
	line = fp.readline()
	while(line):
		splitted = line.split('|')
		avgMaxFitness.append(float(splitted[1]))
		averageDegree.append(float(splitted[0]))
		line = fp.readline()

plt.figure()
plt.plot(averageDegree, avgMaxFitness)
plt.title("Average Max Fitness vs Average Degree")
plt.xlabel("Average Degree")
plt.ylabel("Fitness")
plt.savefig("degreePlot.svg")