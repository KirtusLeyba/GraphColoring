package kleyba.genetic.balGraphColoring;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.NumberFormatException;

public class balGraphColoringTest
{
	public static void main(String[] args)
	{


		int numNodes = 0;
		int numColors = 0;
		int popSize = 0;
		int k = 0;
		double p = 0.0;
		double m = 0.0;
		int generationCount = 0;
		int currentGeneration = 0;
		String fileName = "";
		String inputFileName = "";

		if(args.length != 7)
		{
			help();
		}

		inputFileName = args[0];
		int lineCount = 0;

		Graph g = new Graph();

		//Read input file:
		try
		{
			FileReader fileReader = new FileReader(inputFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while((line = bufferedReader.readLine()) != null)
			{
				if(lineCount == 0)
				{
					numColors = Integer.parseInt(line);
				}
				else
				{
					String[] splited = line.split("\\s+");
					int node1 = Integer.parseInt(splited[0]);
					int node2 = Integer.parseInt(splited[1]);
					Node n1 = new Node(0, node1);
					Node n2 = new Node(0, node2);
					if(!(g.getVertices().contains(n1)))
					{
						g.addNode(n1);
						numNodes++;
					}
					else
					{
						n1 = g.getVertices().get(g.getVertices().indexOf(n1));
					}
					if(!(g.getVertices().contains(n2)))
					{
						g.addNode(n2);
						numNodes++;
					}
					else
					{
						n2 = g.getVertices().get(g.getVertices().indexOf(n2));
					}
					n1.addNeighbor(n2);
					n2.addNeighbor(n1);
				}
				lineCount++;
			}
			bufferedReader.close();
		}
		catch(FileNotFoundException ex)
		{
			System.out.println("Could not find the input file...");
			help();
		}
		catch(IOException ex)
		{
			System.out.println("Error reading file...");
			help();
		}
		catch(NumberFormatException n)
		{
			help();
		}

		//set params from command line args
		popSize = Integer.parseInt(args[1]);
		generationCount = Integer.parseInt(args[2]);
		k = Integer.parseInt(args[3]);
		p = Double.parseDouble(args[4]);
		m = Double.parseDouble(args[5]);
		fileName = args[6];

		double[] avgFitness = new double[generationCount];
		double[] minFitness = new double[generationCount];
		double[] maxFitness = new double[generationCount];
		int[] bestGraph = new int[numNodes];

		//Run the GA
		balGraphColoringGA gc = new balGraphColoringGA(k, p, m, popSize, numColors, numNodes, g);
		int[][] currentPop = gc.initPopulation();
		bestGraph = currentPop[0];
		double bestFitness = gc.fitnessFunction(bestGraph);
		while(currentGeneration < generationCount)
		{
			avgFitness[currentGeneration] = getAvgFitness(gc.getPop(), gc);
			minFitness[currentGeneration] = getMinFitness(gc.getPop(), gc);
			maxFitness[currentGeneration] = getMaxFitness(gc.getPop(), gc, bestGraph, bestFitness);
			bestFitness = gc.fitnessFunction(bestGraph);
			//Status prints
			if(currentGeneration %100 == 0)
			{
				System.out.println("Generation " + currentGeneration + " best individual:");
				printBestIndividual(gc.getPop(), gc);
			}
			gc.setPop(gc.generateNextPop());
			currentGeneration += 1;
		}

		//Save Results
        try
        {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            //Write header:
            bufferedWriter.write("#graphName " + inputFileName + " numNodes " + numNodes + "\n");
            bufferedWriter.write("#PopulationSize " + popSize + " genCount " + generationCount + "\n" );
            bufferedWriter.write("#k " + k + " p " + p + " m " + m + "\n");
            bufferedWriter.write("#bestIndividual: ");
            for(int i = 0; i < numNodes; i++)
            {
            	bufferedWriter.write("" + bestGraph[i]);
            }
            bufferedWriter.write("\n");

            //Write Data:
            for (int i = 0; i < generationCount; i++)
            {
                bufferedWriter.write("" + minFitness[i] + "|" + avgFitness[i] + "|" + maxFitness[i] + "\n");
            }
            bufferedWriter.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }


	}

	//Returns the avg. fitness of a population
	private static double getAvgFitness(int[][] pop, balGraphColoringGA graphC)
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
	private static double getMinFitness(int[][] pop, balGraphColoringGA graphC)
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
	private static double getMaxFitness(int[][] pop, GraphColoringGA graphC, int[] bestIndividual, double bestFitness)
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
		if(max > bestFitness)
		{
			bestIndividual = pop[index];
		}
		return max;
	}

	private static void help()
	{
		String helpMessage = "Inputs are Input Graph File, Popsize, Generation Count, k, p, m and output file";
		System.out.println(helpMessage);
		System.exit(0);
	}

	private static void printBestIndividual(int[][] pop, GraphColoringGA graphC)
	{
		int maxI = 0;
		for(int i = 0; i < pop.length; i++)
		{
			if(graphC.fitnessFunction(pop[i]) > graphC.fitnessFunction(pop[maxI]))
			{
				maxI = i;
			}
		}
		for(int j = 0; j < pop[maxI].length; j++)
		{
			System.out.print(pop[maxI][j] + " ");
		}
		System.out.println("fitness: " + graphC.fitnessFunction(pop[maxI]));
	}

}