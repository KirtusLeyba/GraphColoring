package kleyba.genetic;

import java.util.Random;

public abstract class GeneticAlgorithm
{
    //Private Fields
    protected Random r; //RNG
    protected int[][] population; //Holds the current population
    protected int k; //tournament size
    protected int popSize;
    protected int individualLength;
    protected double p; //selection probability
    protected double m; //mutation probability

    public GeneticAlgorithm(int k, double p, double m, int popSize, int individualLength)
    {
        r = new Random(); //initialize RNG, seed is 1995 for reproducability
        this.k = k;
        this.p = p;
        this.m = m;
        this.popSize = popSize;
        this.individualLength = individualLength;
    }

    /**
    * ABSTRACT METHODS:
    */

    //Initializes the population (an array of bit strings) for the GA
    public abstract int[][] initPopulation();

    //Returns the fitness of an individual according to defined fitness function
    public abstract double fitnessFunction(int[] individual);

    //Selection for the GA
    public abstract int[] selection();
    
    //Crossover for the GA
    public abstract int[][] crossover(int[] a, int[] b);

    //Mutation for the GA
    public abstract void mutate(int[] individual);

    /**
    * DEFINED METHODS:
    */

    //Prints the current population
    public void printPopulation()
    {
    	for(int i = 0; i < population.length; i++)
    	{
    		for(int j = 0; j < population[0].length; j++)
    		{
    			System.out.print(population[i][j]);
    		}
    		System.out.print(", " + fitnessFunction(population[i]));
    		System.out.println();
    	}
    }

    //Generates the next population using selection, crossover, and mutation
    public int[][] generateNextPop()
    {
    	//Tournament style selection
        //choose to random members
        int newPopSize = 0;
        int[][] newPop = new int[popSize][individualLength];

        while(newPopSize < popSize)
        {
            int[] parentA = selection();
            int[] parentB = selection();
            int[][] offSpring = crossover(parentA, parentB);

            mutate(offSpring[0]);
            newPop[newPopSize] = offSpring[0];
            newPopSize++;
            if(newPopSize == popSize)
            {
                break;
            }
            else
            {
                mutate(offSpring[0]);
                newPop[newPopSize] = offSpring[1];
                newPopSize++;
            }
        }

        return newPop;
    }

}
