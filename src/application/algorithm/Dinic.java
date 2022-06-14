package application.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import application.graph.Edge;
import application.graph.Graph;

public class Dinic extends Algorithm {
	private long maxFlow = 0;

	private int[] level;
	private ArrayList<ArrayList<Integer>> adj;
	private ArrayList<Edge> edges;

	public Dinic(Graph graph, int s, int t) {
		this.graph = graph;
		this.s = s;
		this.t = t;

		int n = graph.getNodeCount();
		int m = graph.edges.size();

		level = new int[n];
		
		adj = new ArrayList<ArrayList<Integer>>(n);
		for (int i = 0; i < n; i++) {
			adj.add(new ArrayList<Integer>());
		}
		
		edges = new ArrayList<Edge>(m << 1);

		cGraph = new long[n][n];
		for (long[] row : cGraph) {
			Arrays.fill(row, 0);
		}
		
		int nextID = 0;
		for (int i = 0; i < m; i++) {
			Edge edge = graph.edges.get(i);
			int from = edge.getFrom();
			int to = edge.getTo();
			cGraph[from][to] = edge.getCapacity();
			edges.add(new Edge(from, to, edge.getCapacity()));
			edges.add(new Edge(to, from, 0));
			adj.get(from).add(nextID);
			adj.get(to).add(nextID + 1);
			nextID += 2;
		}
	}

	private boolean bfs() {
		Queue<Integer> q = new LinkedList<>();
		q.add(s);

		do {
			Integer cur = q.poll();
			if (cur == null)
				break;
			for (int next : adj.get(cur)) {

				Edge edge = edges.get(next);
				// insufficient capacity
				if (edge.getCapacity() - edge.getFlow() <= 0)
					continue;
				// already traversed node
				if (level[edge.getTo()] != -1)
					continue;

				level[edge.getTo()] = level[cur] + 1;
				q.add(edge.getTo());
			}
		} while (true);

		return level[t] != -1;
	}

	private long dfs(int cur, long pushed) {
		if (pushed == 0)
			return 0;

		if (cur == t)
			return pushed;

		for (int i = 0; i < adj.get(cur).size(); i++) {
			int node = adj.get(cur).get(i);
			int to = edges.get(node).getTo();
			long cap = edges.get(node).getCapacity();
			long flow = edges.get(node).getFlow();

			long remain = cap - flow;

			if (((level[cur] + 1) != level[to]) || (remain <= 0)) {
				continue;
			}

			long tr = dfs(to, remain < pushed ? remain : pushed);
			if (tr == 0)
				continue;

			edges.get(node).setFlow(flow + tr);
			edges.get(node ^ 1).setFlow(edges.get(node ^ 1).getFlow() - tr);

			return tr;
		}

		return 0;
	}

	@Override
	public void explore() {
		long flow = 0;

		while (true) {
			Arrays.fill(level, -1);
			level[s] = 0;

			// if t is unreachable, break the loop
			if (!bfs())
				break;

			// find an abitrary blocking flow
			long pushed;
			while ((pushed = dfs(s, Long.MAX_VALUE)) > 0) {
				flow += pushed;
			}
		}

		maxFlow = flow;
		getMinCut(cGraph, getRGraph());
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

		for (int i = 0; i < edges.size(); i++) {
			Edge e = edges.get(i);
			int from = e.getFrom();
			int to = e.getTo();
			RGraph[from][to] = cGraph[from][to] - e.getFlow();
		}

		return RGraph;
	}
}
