package kleyba.genetic.balGraphColoring;
import java.util.ArrayList;
import kleyba.genetic.balGraphColoring.Node;

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