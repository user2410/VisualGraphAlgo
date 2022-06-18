package application.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import application.graph.Graph;
import application.context.Context;
import application.context.state.AlgoState;
import javafx.util.Pair;

public abstract class Algorithm {

	// Properties of Algorithm
	protected Graph graph;

	protected int s, t;
	protected long maxFlow = 0;
	
	protected long[][] cGraph;
	
	public ArrayList<Pair<Integer, Integer>> minCuts = new ArrayList<Pair<Integer, Integer>>();
	
	// State properties
	protected AlgoState currentState;
	protected Context context;
	
	// Algorithm constructor
	public enum Type{
		FF, EK, DINIC 
	};
	public Type type;
	public static Algorithm makeAlgo(Context c, Graph graph, int s, int t, Type type) {
		Algorithm algo = null;
		
		switch(type) {
		case FF:
			algo = new FordFulkerson(graph, s, t);
			type = Type.FF;
			break;
		case EK:
			algo = new EdmondKarp(graph, s, t);
			type = Type.EK;
			break;
		case DINIC:
			algo = new Dinic(graph, s, t);
			type = Type.DINIC;
			break;
		}
		
		algo.context = c;
		
		return algo;
	}
	
	public abstract void explore();
	
	public abstract long[][] getRGraph();
	
	protected void getMinCut(long[][] cGraph, long[][]rGraph) {
		int n = graph.getNodeCount();

		Stack<Integer> st = new Stack<>();
		st.add(s);

		boolean[] visited = new boolean[n];
		Arrays.fill(visited, false);

		// dfs
		while (!st.empty()) {
			Integer cur = st.pop();

			if (!visited[cur]) {
				visited[cur] = true;
			}

			ArrayList<Integer> adj = graph.adjList.get(cur);
			for (int i = 0; i < adj.size(); i++) {
				int node = adj.get(i);
				if (!visited[node] && (rGraph[cur][node] != 0))
					st.add(node);
			}

		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (visited[i] && !visited[j] && (cGraph[i][j] > 0))
					minCuts.add(new Pair<Integer, Integer>(i, j));
			}
		}
	}
	
	public int getS() {
		return s;
	}

	public int getT() {
		return t;
	}
	
	public long getMaxFlow() {
		return maxFlow;
	}
	
	public Context getContext() {
		return this.context;
	}
	
	protected void addState(AlgoState st) {
		context.addState(st);
	}
}
