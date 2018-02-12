package kleyba.genetic.maxones;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MaxOnesTest
{
	public static void main(String[] args)
	{


		int popSize = 0;
		int individualLength = 0;
		int k = 0;
		int generationCount = 0;
		int currentGeneration = 0;
		double p = 0.0;
		double m = 0.0;
		String fileName = "";


		if(args.length != 7)
		{
			help();
		}
		popSize = Integer.parseInt(args[0]);
		individualLength = Integer.parseInt(args[1]);
		generationCount = Integer.parseInt(args[2]);
		k = Integer.parseInt(args[3]);
		p = Double.parseDouble(args[4]);
		m = Double.parseDouble(args[5]);
		fileName = args[6];

		double[] avgFitness = new double[generationCount];

		MaxOnes mOnes = new MaxOnes(k, p, m, popSize, individualLength);
		int[][] currentPop = mOnes.initPopulation();
		while(currentGeneration < generationCount)
		{
			avgFitness[currentGeneration] = getAvgFitness(mOnes.getPop(), mOnes);
			mOnes.setPop(mOnes.generateNextPop());
			currentGeneration += 1;
		}

        try
        {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int i = 0; i < generationCount; i++)
            {
                bufferedWriter.write("" + avgFitness[i] + "\n");
            }
            bufferedWriter.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }


	}


	private static double getAvgFitness(int[][] pop, MaxOnes m)
	{
		int size = pop.length;
		double avg = 0.0;
		for(int i = 0; i < size; i++)
		{
			avg += m.fitnessFunction(pop[i]);
		}
		return (avg/size);
	}

	private static void help()
	{
		String helpMessage = "Inputs are Popsize, Individual Length, Generation Count, k, p, m and output file";
		System.out.println(helpMessage);
		System.exit(0);
	}

}