package application.algorithm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import application.graph.Graph;

public class EdmondKarp extends Algorithm {

	protected long maxFlow = 0;

	protected long[][] rGraph;
	protected int[] parent;
	protected boolean[] visited;

	public EdmondKarp(Graph graph, int s, int t) {
		this.graph = graph;
		this.s = s;
		this.t = t;

		int n = graph.getNodeCount();
		parent = new int[n];
		visited = new boolean[n];
		cGraph = new long[n][n];
		rGraph = new long[n][n];
		
		/* Phase 0 */
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
		
		/* Phase 1 - initMaxFlow */
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
		long flow = 0;

		while (bfs()) {/* Phase 2 - while there is an augmenting path */
			Arrays.fill(visited, false);
			long pathFlow = Long.MAX_VALUE;

			for (int v = t; v != s; v = parent[v]) {
				int u = parent[v];
				pathFlow = pathFlow < rGraph[u][v] ? pathFlow : rGraph[u][v];
			}
			/* Phase 3 - construct path and find bottleneck */
			for (int v = t; v != s; v = parent[v]) {
				int u = parent[v];
				rGraph[u][v] -= pathFlow;
				rGraph[v][u] += pathFlow;
			}
			/*
			 * Phase 4 for each edge u->v in the path decrease rGraph[u][v] by bottleneck
			 * increase rGraph[v][u] by bottleneck
			 */
			flow += pathFlow;
		}

		maxFlow = flow;
		getMinCut(this.cGraph, this.rGraph);
	}

	@Override
	public void run() {
		explore();
	}

	public long getMaxFlow() {
		return maxFlow;
	}

	@Override
	public long[][] getRGraph() {
		int n = graph.getNodeCount();
		long[][] RGraph = new long[n][n];

		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				RGraph[i][j] = rGraph[i][j];
			}
		}

		return RGraph;
	}

}
