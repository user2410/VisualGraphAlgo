package application.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import application.graph.Graph;
import javafx.util.Pair;

public abstract class Algorithm extends Thread {
	
	protected Graph graph;
	protected int s, t;
	
	protected long[][] cGraph;
	
	public ArrayList<Pair<Integer, Integer>> minCuts = new ArrayList<Pair<Integer, Integer>>();
	
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
	
}
