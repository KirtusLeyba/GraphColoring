package kleyba.genetic.neutralGraphColoring;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.NumberFormatException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class NeutralGraphColoringTest
{
	public static void main(String[] args)
	{

		//Inputs:
		//ER graphs
		//n, number of nodes
		//d, aprox. avg degree
		//numColors = 3 always
		int numNodes = 50;
		int numColors = 3;
		int numGraphs = 20;


		//GA Inputs:
		int popSize = 0;
		int k = 0;
		double p = 0.0;
		double m = 0.0;
		int generationCount = 0;

		//outputFile
		String fileName = "";
		//output dir for corret solutions
		String outdir = "";

		popSize = Integer.parseInt(args[0]);
		k = Integer.parseInt(args[1]);
		p = Double.parseDouble(args[2]);
		m = Double.parseDouble(args[3]);
		generationCount = Integer.parseInt(args[4]);
		fileName = args[5];
		outdir = args[6];

		double[] avgMaxFitness = new double[20];
		double[] avgDegree = new double[20];
		int iter = 0;

		ArrayList<Graph> solvedGraphs = new ArrayList<Graph>();
		ArrayList<Integer[]> colorings = new ArrayList<Integer[]>();
		ArrayList<Double> dList = new ArrayList<Double>();

		for(double averageDegree = 0.25; averageDegree <= 5.0; averageDegree += 0.25)
		{
			System.out.println(averageDegree);
			Graph[] graphs = new Graph[numGraphs];
			for(int i = 0; i < numGraphs; i++)
			{
				boolean solved = false;
				graphs[i] = erdosRenyi(averageDegree, numNodes);
				NeutralGraphColoringGA gc = new NeutralGraphColoringGA(k,p,m,popSize,numColors,numNodes,graphs[i]);
				int[][] currentPop = gc.initPopulation();
				int currentGeneration = 0;
				int[] bestGraph = currentPop[0];
				double bestFitness = gc.fitnessFunction(bestGraph);
				while(currentGeneration < generationCount)
				{
					double maxFitness = getMaxFitness(gc.getPop(), gc);
					if(maxFitness > bestFitness)
					{
						bestGraph = getBestIndividual(gc.getPop(), gc, bestGraph);
						bestFitness = maxFitness;
					}

					if(!solved)
					{
						if(isCorrect(bestGraph, gc, graphs[i]))
						{
							solved = true;
							solvedGraphs.add(graphs[i]);
							Integer[] col = Arrays.stream( bestGraph ).boxed().toArray( Integer[]::new );
							colorings.add(col);
							dList.add(averageDegree);
						}
					}

					gc.setPop(gc.generateNextPop());
					currentGeneration += 1;
				}
				avgMaxFitness[iter] += bestFitness;
			}
			avgMaxFitness[iter] = avgMaxFitness[iter]/numGraphs;
			avgDegree[iter] = averageDegree;
			iter++;
		}

		//Save Results
        try
        {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            //Write header:
            bufferedWriter.write("# degree test \n");
            for(int i = 0; i < numGraphs; i++)
            {
            	bufferedWriter.write("" + avgDegree[i] + "|" + avgMaxFitness[i] + "\n");
            }

            bufferedWriter.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        //Save Results
        try
        {
        	for(int i = 0; i < solvedGraphs.size(); i++)
        	{
	            FileWriter fileWriter = new FileWriter("" + outdir + "/" + i + ".out");
	            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

	            bufferedWriter.write("" + dList.get(i) + " " + numColors  + "\n");

	            ArrayList<Node> v = solvedGraphs.get(i).getVertices();

	            for(int j = 0; j < v.size(); j++)
	            {
	            	int nid = v.get(j).getNodeID();
	            	ArrayList<Node> neighbors = v.get(j).getNeighbors();
	            	for(int w = 0; w < neighbors.size(); w++)
	            	{
	            		if(neighbors.get(w).getNodeID() > nid)
	            		{
	            			bufferedWriter.write("" + nid + " " + neighbors.get(w).getNodeID() + "\n");
	            		}
	            	}
	            }

	            bufferedWriter.close();

	            fileWriter = new FileWriter("" + outdir + "/" + i + ".col");
	            bufferedWriter = new BufferedWriter(fileWriter);
	            for(int j = 0; j < colorings.get(i).length; j++)
	            {
	            	bufferedWriter.write("" + colorings.get(i)[j]);
	            }
	            bufferedWriter.close();

	        }
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }


	}

	//Returns the avg. fitness of a population
	private static double getAvgFitness(int[][] pop, NeutralGraphColoringGA graphC)
	{
		int size = pop.length;
		double avg = 0.0;
		for(int i = 0; i < size; i++)
		{
			avg += graphC.fitnessFunction(pop[i]);
		}
		return (avg/size);
	}

	//Returns the Min fitness of a populations
	private static double getMinFitness(int[][] pop, NeutralGraphColoringGA graphC)
	{
		int size = pop.length;
		int index = 0;
		double min = graphC.fitnessFunction(pop[index]);
		for(int i = 0; i < size; i++)
		{
			double temp = graphC.fitnessFunction(pop[i]);
			if(temp < min)
			{
				min = temp;
				index = i;
			}
		}
		return min;
	}

	//Returns the Max fitness of a population, and sets bestGraph to the result if its
	//fitness is higher
	private static double getMaxFitness(int[][] pop, NeutralGraphColoringGA graphC)
	{
		int size = pop.length;
		int index = 0;
		double max = graphC.fitnessFunction(pop[index]);
		for(int i = 0; i < size; i++)
		{
			double temp = graphC.fitnessFunction(pop[i]);
			if(temp > max)
			{
				max = temp;
				index = i;
			}
		}
		return max;
	}

	private static void help()
	{

		String helpMessage = "Inputs are popSize, tournament size, selection prob, mutation prob, generationCount, and output file";
		System.out.println(helpMessage);
		System.exit(0);
	}

	private static int[] getBestIndividual(int[][] pop, NeutralGraphColoringGA graphC, int[] prevBest)
	{
		int maxI = 0;
		for(int i = 0; i < pop.length; i++)
		{
			if(graphC.fitnessFunction(pop[i]) > graphC.fitnessFunction(pop[maxI]))
			{
				maxI = i;
			}
		}
		if(graphC.fitnessFunction(pop[maxI]) > graphC.fitnessFunction(prevBest))
		{
			return pop[maxI];
		}
		return prevBest;
	}

	private static boolean isCorrect(int[] individual, NeutralGraphColoringGA graphC, Graph g)
	{
		double fitness = 0.0;
    	int edgeCount = 0;
    	//Apply coloring to the graph
    	graphC.applyColoring(g, individual);

    	for(int nodeIndex = 0; nodeIndex < individual.length; nodeIndex++)
    	{
    		Node n = g.getVertices().get(nodeIndex);
    		ArrayList<Node> neighbors = n.getNeighbors();
    		for(Node b : neighbors)
    		{
    			if(b.getNodeID() > n.getNodeID())
    			{
    				edgeCount++;
    				if(b.getColor() != n.getColor())
    				{
    					fitness += 1.0;
    				}
    			}
    		}
    	}
    	fitness = fitness/edgeCount;

    	if(fitness >= 1.0)
    	{
    		return true;
    	}
    	return false;
	}

	//Generates an Erdos Renyi graph with d as average degree d and numNodes n
	private static Graph erdosRenyi(double d, int n)
	{
		Random r = new Random();
		Node[] nodes = new Node[n];
		Graph g = new Graph();
		for(int i = 0; i < n; i++)
		{
			nodes[i] = new Node(0, i);
		}
		for(int i = 0; i < n; i++)
		{
			for(int j = i+1; j < n; j++)
			{
				double prob = d/n;
				double roll = r.nextDouble();
				if(roll < prob)
				{
					nodes[i].addNeighbor(nodes[j]);
					nodes[j].addNeighbor(nodes[i]);
				}
			}
			g.addNode(nodes[i]);
		}
		return g;
	}


}