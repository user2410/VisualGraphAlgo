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
	
	protected int nodeCount = 0;
	public ArrayList<Node> nodes = new ArrayList<Node>();
	public ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>();
	public ArrayList<Edge> edges = new ArrayList<Edge>();

	public void addNode(int x, int y) {
		nodes.add(new Node(nodeCount++, x, y));
		adjList.add(new ArrayList<Integer>());
	}
	
	public void deleteNode(int id) throws Exception{
		if(id>=0 && id<nodeCount) {
			nodes.remove(id);
			adjList.remove(id);
			adjList.forEach(a -> a.removeIf(e -> e==id));
			edges.removeIf(e->(e.getFrom()==id || e.getTo()==id));
			for(int i=id; i<nodes.size(); i++) {
				nodes.get(i).id--;
				for(int j=0; j<adjList.size(); j++) {
					ArrayList<Integer> a = adjList.get(j);
					for(int k=0; k<a.size(); j++) {
						if(a.get(k) == i+1) {
							a.set(k, Integer.valueOf(i));
							break;
						}
					}
				}
				edges.forEach(e->{
					if(e.from == id+1) e.from--;
					if(e.to == id+1) e.to--;
				});
			}
			nodeCount--;
		}else {
			throw new Exception("Node " + id + " out of range [0~" + (nodeCount>0?nodeCount-1:0) +"]");
		}
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
		
		if(from < 0 || from >= nodeCount) {				
			throw new Exception("Node " + from + " out of range [0~" + (nodeCount>0?nodeCount-1:0) +"]");
		}
		if(to < 0 || to >= nodeCount) {
			throw new Exception("Node " + to + " out of range [0~" + (nodeCount>0?nodeCount-1:0) +"]");
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
	
	public void deleteEdge(int from, int to) throws Exception{
		if(from < 0 || from >= nodeCount) {				
			throw new Exception("Node " + from + " out of range [0~" + (nodeCount>0?nodeCount-1:0) +"]");
		}
		if(to < 0 || to >= nodeCount) {
			throw new Exception("Node " + to + " out of range [0~" + (nodeCount>0?nodeCount-1:0) +"]");
		}
		adjList.get(from).removeIf(t -> t == to);
		edges.removeIf(e -> (e.getFrom()==from && e.getTo()==to));
	}
	
	public int getNodeCount() {
		return nodeCount;
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
	
	public void clear() {
		nodeCount = 0;
		nodes.clear();
		edges.clear();
		for(ArrayList<Integer> l : adjList) {
			l.clear();
		}
		adjList.clear();
	}
	
	@Override
	public String toString() {
		return "Adjacent list: \n" + adjList + "\n Edges:\n" + edges;
	}
}
