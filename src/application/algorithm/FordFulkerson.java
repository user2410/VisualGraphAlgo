package application.algorithm;

import java.util.Arrays;

import application.graph.Graph;

public class FordFulkerson extends EdmondKarp {

	public FordFulkerson(Graph graph, int s, int t) {
		super(graph, s, t);
	}

	private boolean dfs(int node) {
		if(node == t) return true;
		visited[node] = true;
		
		for(int next = 0; next<graph.getNodeCount(); next++) {
			if(!visited[next] && rGraph[node][next]>0) {
				parent[next] = node;
				if(dfs(next)) return true;
			}
		}
		
		return false;
	}

	@Override
	public void explore() {
		long flow = 0;

		while (dfs(s)) {
			Arrays.fill(visited, false);
			long pathFlow = Long.MAX_VALUE;

			for (int v = t; v != s; v = parent[v]) {
				int u = parent[v];
				pathFlow = pathFlow < rGraph[u][v] ? pathFlow : rGraph[u][v];
			}

			for (int v = t; v != s; v = parent[v]) {
				int u = parent[v];
				rGraph[u][v] -= pathFlow;
				rGraph[v][u] += pathFlow;
			}

			flow += pathFlow;
		}

		maxFlow = flow;
		getMinCut(this.cGraph, this.rGraph);
	}

}
