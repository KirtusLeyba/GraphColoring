package kleyba.genetic.graphColoring;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;
import kleyba.genetic.GeneticAlgorithm;

//uses the Genetic Algorithm class to implement
//a graph coloring GA
public class GraphColoringGA extends GeneticAlgorithm
{
	private int numColors;
	private int numNodes;
	private Graph g;

	public GraphColoringGA(int k, double p, double m, int popSize, int numColors, int numNodes, Graph g)
    {
    	super(k,p,m,popSize,numNodes);
    	this.numNodes = numNodes;
    	this.numColors = numColors;
    	this.g = g;
    }

    //Initializes the population (an array of bit strings) for the GA
    public int[][] initPopulation()
    {
    	int[][] pop = new int[popSize][individualLength];

        for(int i = 0; i < popSize; i++)
        {
        	for(int j = 0; j < individualLength; j++)
        	{
        		int c = r.nextInt(numColors);
        		pop[i][j] = c;
        	}
        }

        this.population = pop;
        return pop;
    }

    //Returns the fitness of an individual according to defined fitness function
    public double fitnessFunction(int[] individual)
    {
    	double fitness = 0.0;
    	int edgeCount = 0;

    	//Apply coloring to the graph
    	applyColoring(g, individual);

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
    	return fitness/edgeCount;
    }

    public void applyColoring(Graph graph, int[] coloring)
    {
    	ArrayList<Node> nodeList = graph.getVertices();
    	for(int n = 0; n < nodeList.size(); n++)
    	{
    		nodeList.get(n).setColor(coloring[n]);
    	}
    }

    //Selection for the GA
    public int[] selection()
    {
    	int[][] tournament = new int[k][population[0].length];
        for (int i = 0; i < k; i++)
        {
            tournament[i] = population[r.nextInt(population.length)];
        }
        double roll = r.nextDouble();
        sortByFitness(tournament);
        for (int i = k - 1; i >= 0; i--)
        {
            double prob = p * (Math.pow(1.0 - p, i));
            if (roll < prob)
            {
                return tournament[i];
            }
        }
        return tournament[k-1];
    }
    
    //Crossover for the GA
    //Simple 2 point crossover
    public int[][] crossover(int[] a, int[] b)
    {
    	int pivotA = r.nextInt(a.length);
        int pivotB = 0;
        if(pivotA > 0)
        {
            pivotB = r.nextInt(pivotA);
        }
        int[][] offSpring = new int[2][a.length];
        for(int i = 0; i < a.length; i++)
        {
            if(i <= pivotB)
            {
                offSpring[0][i] = a[i];
                offSpring[1][i] = b[i];
            }
            else if(i <= pivotA)
            {
                offSpring[0][i] = b[i];
                offSpring[1][i] = a[i];
            }
            else
            {
                offSpring[0][i] = a[i];
                offSpring[1][i] = b[i];
            }
        }

        return offSpring;
    }

    //Mutation for the GA
    public void mutate(int[] individual)
    {
    	for(int i = 0; i < individual.length; i++)
        {
            double roll = r.nextDouble();
            if(roll < m)
            {
                individual[i] = r.nextInt(numColors);
            }
        }
    }


    private void sortByFitness(int[][] pop)
    {
        Arrays.sort(pop, new FitnessCompare());
    }

    private class FitnessCompare implements Comparator<int []>
    {

        @Override
        public int compare(int[] a, int[] b)
        {
            double fitA = fitnessFunction(a);
            double fitB = fitnessFunction(b);
            if(fitA > fitB) return -1;
            if (fitA < fitB) return 1;
            return 0;
        }
    }

    public int[][] getPop()
    {
    	return population;
    }
    public void setPop(int[][] pop)
    {
    	population = pop;
    }

}