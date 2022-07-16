package application.graph;

import java.io.Serializable;

public class Edge implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 592333285344128711L;
	
	int from, to;
	long capacity;
	long flow;
	
	public Edge(int from, int to) {		
		this.from = from;
		this.to = to;
	}
	
	public Edge(int from, int to, long capacity) {
		super();
		this.from = from;
		this.to = to;
		this.capacity = capacity;
		this.flow = 0;
	}

	public long getFlow() {
		return flow;
	}

	public void setFlow(long flow) {
		this.flow = flow;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public long getCapacity() {
		return capacity;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	@Override
	public boolean equals(Object o) {
		return this.from == ((Edge)o).from && this.to == ((Edge)o).to;
	}
	
	@Override
	public String toString() {
		return "[" + from + " -> " + to + ", capacity: " + capacity + ", flow: " + flow + "]";
	}
}
