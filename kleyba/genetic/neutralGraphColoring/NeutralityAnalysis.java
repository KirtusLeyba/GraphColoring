package kleyba.genetic.neutralGraphColoring;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.NumberFormatException;
import java.util.ArrayList;

public class NeutralityAnalysis
{
	public static void main(String[] args)
	{
		//Input
		//1. Graph File
		//2. Color File
		//3. Graph Name
		
		//Output
		//GraphName|coloring|neutrality

		String inputFileName = args[0];
		String colorFileName = args[1];
		String graphName = args[2];
		int numColors = 0;
		int numNodes = 50;
		Graph g = new Graph();

		for(int i = 0; i < numNodes; i++)
		{
			g.addNode(new Node(0, i));
		}

		//Read input graph file:
		try
		{
			FileReader fileReader = new FileReader(inputFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			int lineCount = 0;
			while((line = bufferedReader.readLine()) != null)
			{
				if(lineCount == 0)
				{
					String[] splited = line.split("\\s+");
					graphName = splited[0];
					numColors = Integer.parseInt(splited[1]);
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
						//numNodes++;
					}
					else
					{
						n1 = g.getVertices().get(g.getVertices().indexOf(n1));
					}
					if(!(g.getVertices().contains(n2)))
					{
						g.addNode(n2);
						//numNodes++;
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
			System.out.println("number error");
			help();
		}

		//Read input coloring file:
		int[] coloring = new int[numNodes];
		try
		{
			FileReader fileReader = new FileReader(colorFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while((line = bufferedReader.readLine()) != null)
			{
				coloring = new int[line.length()];
				for(int i = 0; i < line.length(); i++)
				{
					coloring[i] = (int)(line.charAt(i)-'0');
				}
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

		System.out.print(graphName + "|");
		for(int i = 0; i < coloring.length; i++)
		{
			System.out.print("" + coloring[i]);
		}

		System.out.print("|" + determineNeutrality(coloring, numColors, g));
		System.out.println();


	}

	public static void help()
	{
		System.out.println("Incorrect Input! Input Graph, Coloring File, graph name");
	}

	public static void applyColoring(Graph graph, int[] coloring)
    {
    	ArrayList<Node> nodeList = graph.getVertices();
    	for(int n = 0; n < nodeList.size(); n++)
    	{
    		nodeList.get(n).setColor(coloring[n]);
    	}
    }

    public static double determineNeutrality(int[] coloring, int numColors, Graph g)
    {
    	double neutrality  = 0.0;
    	double total = 0.0;
    	for(int i = 0; i < coloring.length; i++)
    	{
    		for(int j = coloring[i]; j < numColors+coloring[i]; j++)
    		{
    			int dj = j;
    			if(dj >= numColors)
    			{
    				dj = j - numColors;
    			}
    			//Make new copy
    			int[] copy = new int[coloring.length];
    			for(int k = 0; k < coloring.length; k++)
    			{
    				copy[k] = coloring[k];
    			}
    			copy[i] = dj;
    			if(isValid(copy, g))
    			{
    				neutrality += 1.0;
    			}
    			total += 1.0;
    		}
    	}
    	return neutrality/total;
    }

    public static boolean isValid(int[] coloring, Graph g)
    {

    	//Apply coloring to the graph
    	applyColoring(g, coloring);

    	for(int nodeIndex = 0; nodeIndex < coloring.length; nodeIndex++)
    	{
    		Node n = g.getVertices().get(nodeIndex);
    		ArrayList<Node> neighbors = n.getNeighbors();
    		for(Node b : neighbors)
    		{
    			if(b.getNodeID() > n.getNodeID())
    			{
    				if(b.getColor() == n.getColor())
    				{
    					return false;
    				}
    			}
    		}
    	}
    	return true;
    }

}