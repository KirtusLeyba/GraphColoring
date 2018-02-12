#!/bin/bash
echo Beginning Multi run...

for run in {1..50}
do
	echo run $run ...
	java -jar ./bin/out.jar ./input/DataSets/le450_5a.g 200 500 2 0.85 0.001 ./output/multiRun/m$run.out
done