package kleyba.genetic.graphColoring;
import java.util.ArrayList;
import kleyba.genetic.graphColoring.Node;

//Graph class for the Graph Coloring GA application
public class Graph
{
	private ArrayList<Node> vertices;

	public Graph()
	{
		vertices = new ArrayList();
	}

	public void addNode(Node n)
	{
		vertices.add(n);
	}

	public ArrayList<Node> getVertices()
	{
		return vertices;
	}

}