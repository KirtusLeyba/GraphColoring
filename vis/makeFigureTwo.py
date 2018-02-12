import numpy as np
import math
import matplotlib.pyplot as plt

def getMean(a):
	out = 0.0
	for i in range(len(a)):
		out += a[i]
	return out/float(len(a))

def getSTD(a):
	mean = getMean(a)
	std = 0.0
	for i in range(len(a)):
		std += (a[i] - mean)*(a[i] - mean)
	return math.sqrt((std/float(len(a))))


fileList = []

for i in range(50):
	fileList.append("./output/multiRun/m" + str(i+1) + ".out")

numGens = 500

## The standard deviations for all runs at each generation
stdMin = []
stdAvg = []
stdMax = []

## Same but for means
meanMin = []
meanAvg = []
meanMax = []

for gen in range(numGens):
	minList = []
	avgList = []
	maxList = []
	for inFile in fileList:
		with open(inFile, 'r') as fp:
			linesRead = 0
			line = fp.readline()
			while(line[0] == '#'):
				line = fp.readline()
			while(linesRead < gen):
				line = fp.readline()
				linesRead += 1

			lineSplit = line.split('|')
			minList.append(float(lineSplit[0]))
			avgList.append(float(lineSplit[1]))
			maxList.append(float(lineSplit[2]))

			while(line):
				line = fp.readline()
	runCount = len(minList)
	meanMin.append(getMean(minList))
	meanAvg.append(getMean(avgList))
	meanMax.append(getMean(maxList))
	stdMin.append(getSTD(minList))
	stdAvg.append(getSTD(avgList))
	stdMax.append(getSTD(maxList))

xRange = np.arange(1,gen+2)

plt.figure(1, figsize = (16, 8))
plt.suptitle("Variation Across Runs")

## subplots
plt.subplot(1,3,1)
plt.plot(meanMin, color = 'blue')

bottom = []
top = []
for i in range(len(meanMin)):
	bottom.append(meanMin[i] - stdMin[i]/2)
	top.append(meanMin[i] + stdMin[i]/2)

plt.fill_between(xRange, bottom, top, color = 'blue', alpha  = 0.25)
plt.title("Minimum Fitness")
plt.xlabel("Generation")
plt.ylabel("Fitness")

plt.subplot(1,3,2)
plt.plot(meanAvg, color = 'red')

bottom = []
top = []

for i in range(len(meanAvg)):
	bottom.append(meanAvg[i] - stdAvg[i]/2)
	top.append(meanAvg[i] + stdAvg[i]/2)

plt.fill_between(xRange, bottom, top, color = 'red', alpha  = 0.25)
plt.title("Average Fitness")
plt.xlabel("Generation")
plt.ylabel("Fitness")


plt.subplot(1,3,3)
plt.plot(meanMax, color = 'green')
bottom = []
top = []

for i in range(len(meanMax)):
	bottom.append(meanMax[i] - stdMax[i]/2)
	top.append(meanMax[i] + stdMax[i]/2)
plt.fill_between(xRange, bottom, top, color = 'green', alpha  = 0.25)
plt.title("Maximum Fitness")
plt.xlabel("Generation")
plt.ylabel("Fitness")

plt.savefig("stdPlot.svg")