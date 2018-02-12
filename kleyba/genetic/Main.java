package kleyba.genetic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Main
{

    private static Random r = new Random();

    public static void main(String[] args)
    {
        //Start Run
        //Generate Population
        int popSize = 1000;
        int individualLength = 64;
        for(int k = 2; k <= 16; k = k*2)
        {
            double p = 0.85; //selection probability
            double m = 0.1; //mutation probability
            int generations = 100;

            int[][] population = initPopulation(popSize, individualLength); //initial pop

            int iter = 0;

            double[] fitnessPerGen = new double[generations];

            while (iter < generations)
            {
                fitnessPerGen[iter] = calcAvgFitness(population);
                int[][] newPop = generateNextPop(population, k, p, m);
                population = newPop;
                iter++;
            }

            String fileName = "results" + k + ".txt";
            try
            {
                FileWriter fileWriter = new FileWriter(fileName);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                for (int i = 0; i < generations; i++)
                {
                    bufferedWriter.write("" + fitnessPerGen[i] + "\n");
                }
                bufferedWriter.close();
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Initializes a population for the GA
     * @param popSize
     * The size of the population to initialize.
     * @param individualLength
     * The length of a single individual.
     * @return
     * The population as a 2D array [individual][bit]
     */
    private static int[][] initPopulation(int popSize, int individualLength)
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

        return pop;
    }

    //Implementation of Fisher-Yates shuffle
    private static void shuffle(int[] a)
    {
        for(int i = a.length-1; i >= 1; i--)
        {
            int j = r.nextInt(i+1);
            int temp = a[j];
            a[j] = a[i];
            a[i] = a[j];
        }
    }

    //Simply prints a population with each individual's fitness
    private static void printPopulation(int[][] pop)
    {
        for(int i = 0; i < pop.length; i++)
        {
            for(int j = 0; j < pop[0].length; j++)
            {
                System.out.print(pop[i][j]);
            }
            System.out.print(", " + fitnessFunction(pop[i]));
            System.out.println();
        }
    }

    /**
     * Max ones fitness function
     * @param individual
     * the individual in the population
     * @return
     * the fitness of the individual(double)
     */
    private static double fitnessFunction(int[] individual)
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

    /**
     * Generates the next population using selection, crossover, and mutation
     * @param prev
     * the previous population
     * @param k
     * tournament size
     * @param p
     * tournament selection probability (between 0 and 1)
     * @param m
     * mutation likelihood
     * @return
     */
    private static int[][]  generateNextPop(int[][] prev, int k, double p, double m)
    {
        //Tournament style selection
        //choose to random members
        int popSize = prev.length;
        int newPopSize = 0;

        int[][] newPop = new int[prev.length][prev[0].length];

        while(newPopSize < popSize)
        {
            int[] parentA = selection(prev, k, p);
            int [] parentB = selection(prev, k, p);
            int[][] offSpring = crossover(parentA, parentB);

            mutate(offSpring[0], m);
            newPop[newPopSize] = offSpring[0];
            newPopSize++;
            if(newPopSize == popSize)
            {
                break;
            }
            else
            {
                mutate(offSpring[0], m);
                newPop[newPopSize] = offSpring[1];
                newPopSize++;
            }
        }

        return newPop;
    }

    private static int[] selection(int[][] prev, int k, double p)
    {
        int[][] tournament = new int[k][prev[0].length];
        for (int i = 0; i < k; i++)
        {
            tournament[i] = prev[r.nextInt(prev.length)];
        }
        double roll = r.nextDouble();
        sortByFitness(tournament);
        int[] parentA;
        for (int i = k - 1; i >= 0; i--)
        {
            double prob = p * (Math.pow(1.0 - p, i));
            if (roll < prob)
            {
                System.out.println("roll: " + roll + ", prob:" + prob + ", i: " + i);
                return tournament[i];
            }
        }
        return tournament[k-1];
    }

    private static int[][] crossover(int[] a, int[] b)
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

    private static void mutate(int[] indiv, double m)
    {
        for(int i = 0; i < indiv.length; i++)
        {
            double roll = r.nextDouble();
            if(roll < m)
            {
                if(indiv[i] == 0)
                {
                    indiv[i] = 1;
                }
                else
                {
                    indiv[i] = 0;
                }
            }
        }
    }

    /**
     * Sorts the pop by fitness
     * @param pop
     * the pop to be sorted
     */
    private static void sortByFitness(int[][] pop)
    {
        Arrays.sort(pop, new FitnessCompare());
        //mergeSortRecursive(pop, 0, pop.length-1);
    }

    private static double calcAvgFitness(int[][] pop)
    {
        double fitness = 0.0;
        for(int i = 0; i < pop.length; i++)
        {
            fitness += fitnessFunction(pop[i]);
        }
        fitness = fitness/pop.length;
        return fitness;
    }

    private static class FitnessCompare implements Comparator<int []>
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

}
