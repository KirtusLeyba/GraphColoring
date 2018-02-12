package kleyba.genetic.neutralGraphColoring;

import java.util.ArrayList;


//Node class for the Graph Coloring Application
public class Node
{
	private ArrayList<Node> neighbors;
	private int color;
	private int nodeID;

	public Node(int color, int nodeID)
	{
		this.color = color;
		this.nodeID = nodeID;
		neighbors = new ArrayList();
	}

	public void addNeighbor(Node n)
	{
		neighbors.add(n);
	}
	public void setColor(int c)
	{
		color = c;
	}
	public ArrayList<Node> getNeighbors()
	{
		return neighbors;
	}
	public int getColor()
	{
		return color;
	}

	public int getNodeID()
	{
		return nodeID;
	}
	public void setNodeID(int nodeID)
	{
		this.nodeID = nodeID;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		final Node other = (Node) obj;
		if(other.nodeID == nodeID)
			return true;
		return false;
	}

}