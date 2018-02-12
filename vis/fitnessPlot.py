import sys
import matplotlib.pyplot as plt

## Setup vars
minFitness = []
avgFitness = []
maxFitness = []

fileIn = sys.argv[1]

i = 0

with open(fileIn, 'r') as fp:
	line = fp.readline()
	while(line):
		if(not line[0] == '#'):
			lineSplit = line.split('|')
			minFitness.append(float(lineSplit[0]))
			avgFitness.append(float(lineSplit[1]))
			maxFitness.append(float(lineSplit[2]))
		else:
			headerLine = line[1:]
			headerSplit = headerLine.split(' ')
			if(i == 0):
				gName = headerSplit[1].split('/')[-1]
			i += 1
		line = fp.readline()

plt.figure()
plt.plot(minFitness, label = "Min Fitness")
plt.plot(avgFitness, label = "Avg Fitness")
plt.plot(maxFitness, label = "Max Fitness")l
plt.title("Fitness with graph: " + gName)
plt.xlabel("Generation")
plt.ylabel("Fitness")
plt.legend()
plt.show()