#!/bin/bash

for run in {0..196}
do
	java -jar ./bin/na.jar output/neutrality/cols/$run.out output/neutrality/cols/$run.col a
done