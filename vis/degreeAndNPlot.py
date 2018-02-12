import sys
import matplotlib.pyplot as plt

## Setup vars
neutrality = []
avgDegree = []

fileIn = sys.argv[1]

with open(fileIn, 'r') as fp:
	line = fp.readline()
	while(line):
		splitted = line.split('|')
		avgDegree.append(float(splitted[0]))
		neutrality.append(float(splitted[2]))
		line = fp.readline()

plt.figure()
plt.plot(avgDegree, neutrality, 'ro')
plt.title("Average Degree vs Neutrality")
plt.xlabel("Average Degree")
plt.ylabel("Neutrality")
plt.savefig("dAndNPlot.svg")