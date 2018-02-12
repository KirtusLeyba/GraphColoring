package kleyba.genetic.maxones;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import kleyba.genetic.GeneticAlgorithm;


//Uses the GeneticAlgorithm class to implement
//a "max ones" GA
public class MaxOnes extends GeneticAlgorithm
{

	public MaxOnes(int k, double p, double m, int popSize, int individualLength)
    {
    	super(k,p,m,popSize,individualLength);
    }



    //Initializes the population (an array of bit strings) for the GA
    public int[][] initPopulation()
    {
    	int[][] pop = new int[popSize][individualLength];

        for(int i = 0; i < popSize; i++)
        {
            int numOnes = r.nextInt(individualLength+1);
            for(int j = 0; j < numOnes; j++)
            {
                pop[i][j] = 1;
            }
            for(int j = numOnes; j < individualLength; j++)
            {
                pop[i][j] = 0;
            }
            shuffle(pop[i]);
        }

        this.population = pop;
        return pop;
    }

    //Returns the fitness of an individual according to defined fitness function
    public double fitnessFunction(int[] individual)
    {
    	double fitness = 0.0;
        for(int i = 0; i < individual.length; i++)
        {
            if (individual[i] == 1)
            {
                fitness += 1.0;
            }
        }
        fitness = fitness/individual.length;
        return fitness;
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
    public int[][] crossover(int[] a, int[] b)
    {
    	int pivot = r.nextInt(a.length);
        int[][] offSpring = new int[2][a.length];
        for(int i = 0; i < a.length; i++)
        {
            if(i <= pivot)
            {
                offSpring[0][i] = a[i];
                offSpring[1][i] = b[i];
            }
            else
            {
                offSpring[0][i] = b[i];
                offSpring[1][i] = a[i];
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
                if(individual[i] == 0)
                {
                    individual[i] = 1;
                }
                else
                {
                    individual[i] = 0;
                }
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


    //Gets the population
    public int[][] getPop()
    {
    	return population;
    }
    //sets the population
    public void setPop(int[][] population)
    {
    	this.population = population;
    }

    //Implementation of Fisher-Yates shuffle
    private void shuffle(int[] a)
    {
        for(int i = a.length-1; i >= 1; i--)
        {
            int j = r.nextInt(i+1);
            int temp = a[j];
            a[j] = a[i];
            a[i] = a[j];
        }
    }


}