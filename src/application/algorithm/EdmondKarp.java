package application.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import application.context.state.factory.EKStateMaker;
import application.context.state.factory.FFStateMaker;
import application.graph.Edge;
import application.graph.Graph;

public class EdmondKarp extends Algorithm {

	protected long[][] rGraph;
	protected int[] parent;
	protected boolean[] visited;
	
	private EKStateMaker stMaker;

	public EdmondKarp(Graph graph, int s, int t) {
		this.graph = graph;
		this.s = s;
		this.t = t;

		int n = graph.getNodeCount();
		parent = new int[n];
		visited = new boolean[n];
		cGraph = new long[n][n];
		rGraph = new long[n][n];
		
		for (long[] row : cGraph) {
			Arrays.fill(row, 0);
		}
		
		for (long[] row : rGraph) {
			Arrays.fill(row, 0);
		}
		
		graph.edges.forEach((edge) -> {
			int from = edge.getFrom();
			int to = edge.getTo();
			cGraph[from][to] = rGraph[from][to] = edge.getCapacity();
		});
		
		_stMaker = stMaker = new FFStateMaker();
	}

	private boolean bfs() {
		Queue<Integer> q = new LinkedList<>();
		q.add(s);

		do {
			Integer cur = q.poll();
			if (cur == null)
				break;

			for (int next = 0; next < graph.getNodeCount(); next++) {
				if (!visited[next] && rGraph[cur][next] > 0) {
					q.add(next);
					parent[next] = cur;
					visited[next] = true;
				}
			}

		} while (true);

		return visited[t];
	}

	@Override
	public void explore() {

		addState(stMaker.makeState0(this));
		
		addState(stMaker.makeState1(this));
		
		maxFlow = 0;

		ArrayList<Edge> path = new ArrayList<Edge>();
		
		while (bfs()) {
			Arrays.fill(visited, false);
			long pathFlow = Long.MAX_VALUE;

			path.clear();
			for (int v = t; v != s; v = parent[v]) {
				int u = parent[v];
				path.add(new Edge(u,v));
				pathFlow = pathFlow < rGraph[u][v] ? pathFlow : rGraph[u][v];
			}
			
			addState(stMaker.makeState2(this, pathFlow, path));
			
			for (int v = t; v != s; v = parent[v]) {
				int u = parent[v];
				rGraph[u][v] -= pathFlow;
				addState(stMaker.makeState3(this, pathFlow, u, v, path, false));
				rGraph[v][u] += pathFlow;
				addState(stMaker.makeState3(this, pathFlow, v, u, path, true));
			}
			
			maxFlow += pathFlow;
			addState(stMaker.makeState4(this, pathFlow));
		}

		addState(stMaker.makeState5(this));
		
		getMinCut(this.cGraph, this.rGraph);
		addState(stMaker.makeStateFinal(this, minCuts));
	}


	@Override
	public long[][] getRGraph() {
		int n = graph.getNodeCount();
		long[][] RGraph = new long[n][n];
		
		for(int i=0; i<n; i++) {
			long[] row = rGraph[i];
			System.arraycopy(row, 0, RGraph[i], 0, n);
		}
		
		return RGraph;
	}

}
