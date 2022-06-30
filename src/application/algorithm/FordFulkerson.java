package application.algorithm;

import java.util.ArrayList;
import java.util.Arrays;

import application.context.state.factory.FFStateMaker;
import application.graph.Edge;
import application.graph.Graph;

public class FordFulkerson extends EdmondKarp {
	
	private FFStateMaker stMaker;
	
	public FordFulkerson(Graph graph, int s, int t) {
		super(graph, s, t);
		stMaker = new FFStateMaker();
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
		
		addState(stMaker.makeState0(this));
		
		addState(stMaker.makeState1(this));
    	
		maxFlow = 0;

		ArrayList<Edge> path = new ArrayList<Edge>();
		
		while (dfs(s)) {
			Arrays.fill(visited, false);
			long pathFlow = Long.MAX_VALUE;

			path.clear();
			for (int v = t; v != s; v = parent[v]) {
				int u = parent[v];
				pathFlow = pathFlow < rGraph[u][v] ? pathFlow : rGraph[u][v];
			}
			
			addState(stMaker.makeState2(this, pathFlow, path));

			for (int v = t; v != s; v = parent[v]) {
				int u = parent[v];
				rGraph[u][v] -= pathFlow;
				addState(stMaker.makeState3(this, pathFlow, u, v, false));
				rGraph[v][u] += pathFlow;
				addState(stMaker.makeState3(this, pathFlow, v, u, true));
			}

			maxFlow += pathFlow;
			addState(stMaker.makeState4(this, pathFlow));
		}

		addState(stMaker.makeState5(this));
		
		getMinCut(this.cGraph, this.rGraph);
		addState(stMaker.makeStateFinal(this));
	}
	
}
