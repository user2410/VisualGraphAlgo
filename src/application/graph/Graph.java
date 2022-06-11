package application.graph;

import java.util.ArrayList;

public class Graph {

	private int nextNodeID = 0;
	public ArrayList<Node> nodes = new ArrayList<Node>();
	public ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>();
	public ArrayList<Edge> edges = new ArrayList<Edge>();

	public synchronized void addNode(int x, int y) {
		nodes.add(new Node(x, y, nextNodeID++));
		adjList.add(new ArrayList<Integer>());
	}
	
	public Node getNode(int id) {
		try {
			Node node =  nodes.get(id);
			return node;
		}catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public synchronized void addEdge(int from, int to, long cap) throws Exception {
		
		if(from < 0 || from >= nextNodeID) {				
			throw new Exception("Node " + from + " out of range [0~" + (nextNodeID>0?nextNodeID-1:0) +"]");
		}
		if(to < 0 || to >= nextNodeID) {
			throw new Exception("Node " + to + " out of range [0~" + (nextNodeID>0?nextNodeID-1:0) +"]");
		}
		if(cap < 0) {
			throw new Exception("Invalid edge capacity: " + cap);
		}
		
		Edge newEdge = new Edge(from, to, cap);
		if(edges.contains(newEdge)) {
			throw new Exception("This edge" + from + " -> " + to +  " already existed");
		}
		edges.add(newEdge);
		adjList.get(from).add(to);
	}
	
	@Override
	public String toString() {
		return "Adjacent list: \n" + adjList + "\n Edges:\n" + edges;
	}
}
