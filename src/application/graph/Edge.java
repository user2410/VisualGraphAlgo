package application.graph;

public class Edge {

	private int from, to;
	private long capacity;
	private long flow;
	
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

	public int getTo() {
		return to;
	}

	public long getCapacity() {
		return capacity;
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
