package application.algorithm;

import application.graph.Graph;

public abstract class Algorithm extends Thread {
	
	protected Graph graph;
	protected int s, t;
	
	public abstract void explore();
	
	public abstract long[][] getRGraph();
	
	public int getS() {
		return s;
	}

	public int getT() {
		return t;
	}
	
}
