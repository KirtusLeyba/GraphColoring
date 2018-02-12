package kleyba.genetic.neutralGraphColoring;
import java.util.ArrayList;
import kleyba.genetic.neutralGraphColoring.Node;

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