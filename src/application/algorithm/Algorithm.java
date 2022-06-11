package application.algorithm;

import application.graph.Graph;

public abstract class Algorithm extends Thread {
	
	protected int phase;
	protected Graph graph;
	
	public abstract void explore();
}
