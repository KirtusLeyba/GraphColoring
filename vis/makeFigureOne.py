import sys
import matplotlib.pyplot as plt

## Setup vars
fileList = ['./output/myciel3.out', './output/2-Insertions_3.out', './output/le450_5a.out', './output/hard-graph-4-7-2.out', './output/jean.out', './output/queen5_5.out']
dataList = []
nameList = []

i = 0


for fileIn in fileList:
	minFitness = []
	avgFitness = []
	maxFitness = []
	i = 0
	plotNum = 0
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
					nameList.append(gName)
				i += 1
			line = fp.readline()
	dataList.append([minFitness, avgFitness, maxFitness])
	plotNum += 1


fig = plt.figure(1, figsize = (16.0, 10.0))
plt.suptitle("Fitness For Several Graphs")

plt.subplot(2,3,1)
plt.plot(dataList[0][0], label = "Min Fitness")
plt.plot(dataList[0][1], label = "Avg Fitness")
plt.plot(dataList[0][2], label = "Max Fitness")
plt.title("Fitness with graph: " + nameList[0])
plt.xlabel("Generation")
plt.ylabel("Fitness")
plt.legend()

plt.subplot(2,3,2)
plt.plot(dataList[1][0], label = "Min Fitness")
plt.plot(dataList[1][1], label = "Avg Fitness")
plt.plot(dataList[1][2], label = "Max Fitness")
plt.title("Fitness with graph: " + nameList[1])
plt.xlabel("Generation")
plt.ylabel("Fitness")
plt.legend()

plt.subplot(2,3,3)
plt.plot(dataList[2][0], label = "Min Fitness")
plt.plot(dataList[2][1], label = "Avg Fitness")
plt.plot(dataList[2][2], label = "Max Fitness")
plt.title("Fitness with graph: " + nameList[2])
plt.xlabel("Generation")
plt.ylabel("Fitness")
plt.legend()

plt.subplot(2,3,4)
plt.plot(dataList[3][0], label = "Min Fitness")
plt.plot(dataList[3][1], label = "Avg Fitness")
plt.plot(dataList[3][2], label = "Max Fitness")
plt.title("Fitness with graph: " + nameList[3])
plt.xlabel("Generation")
plt.ylabel("Fitness")
plt.legend()

plt.subplot(2,3,5)
plt.plot(dataList[4][0], label = "Min Fitness")
plt.plot(dataList[4][1], label = "Avg Fitness")
plt.plot(dataList[4][2], label = "Max Fitness")
plt.title("Fitness with graph: " + nameList[4])
plt.xlabel("Generation")
plt.ylabel("Fitness")
plt.legend()

plt.subplot(2,3,6)
plt.plot(dataList[5][0], label = "Min Fitness")
plt.plot(dataList[5][1], label = "Avg Fitness")
plt.plot(dataList[5][2], label = "Max Fitness")
plt.title("Fitness with graph: " + nameList[5])
plt.xlabel("Generation")
plt.ylabel("Fitness")
plt.legend()


plt.savefig("FitnessFigure.svg")