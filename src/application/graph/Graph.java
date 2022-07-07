package application.graph;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Graph implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2189050753577717068L;
	
	protected int nextNodeID = 0;
	public ArrayList<Node> nodes = new ArrayList<Node>();
	public ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>();
	public ArrayList<Edge> edges = new ArrayList<Edge>();

	public void addNode(int x, int y) {
		nodes.add(new Node(nextNodeID++, x, y));
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
	
	public void addEdge(int from, int to, long cap) throws Exception {
		
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
	
	public int getNodeCount() {
		return nextNodeID;
	}
	
	@Override
	public String toString() {
		return "Adjacent list: \n" + adjList + "\n Edges:\n" + edges;
	}
	
	public void serialize(String filename) throws IOException {
        
		//Saving of object in a file
		FileOutputStream file = new FileOutputStream("data/graphs/"+filename);
		ObjectOutputStream out = new ObjectOutputStream(file);
		
		// Method for serialization of object
		out.writeObject(this);
		
		out.close();
		file.close();
		
		System.out.println("Object has been serialized");
	}
	
	public static Graph deserialize(String filename) throws IOException, ClassNotFoundException {
		Graph g = null;
		
		// Reading the object from a file
		FileInputStream file = new FileInputStream("data/graphs/"+filename);
		ObjectInputStream in = new ObjectInputStream(file);
		
		// Method for deserialization of object
		g = (Graph)in.readObject();
		
		in.close();
		file.close();
		
		return g;
	}
	
	
}
