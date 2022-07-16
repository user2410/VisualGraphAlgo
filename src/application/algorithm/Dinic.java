package application.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import application.context.state.factory.DinicStateMaker;
import application.graph.Edge;
import application.graph.Graph;

public class Dinic extends Algorithm {
	
	private ArrayList<ArrayList<Integer>> adj;
	private ArrayList<Edge> edges;
	private int[] level;
	
	private DinicStateMaker stMaker;

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
		
		_stMaker = stMaker = new DinicStateMaker();
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
				int to = edge.getTo();
				
				// insufficient capacity
				if (edge.getCapacity() - edge.getFlow() <= 0)
					continue;
				// already traversed node
				if (level[to] != -1)
					continue;
				
				level[to] = level[cur] + 1;
				if(to == t) break;
				q.add(to);
			}
		} while (true);

		return level[t] != -1;
	}

	private long dfs(int cur, long pushed, ArrayList<Edge> path) {
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

			long tr = dfs(to, remain < pushed ? remain : pushed, path);
			if (tr == 0)
				continue;

			edges.get(node).setFlow(flow + tr);
			path.add(new Edge(cur, to, tr));
			edges.get(node ^ 1).setFlow(edges.get(node ^ 1).getFlow() - tr);

			return tr;
		}

		return 0;
	}

	@Override
	public void explore() {
		
		addState(stMaker.makeState0(this));
		
		addState(stMaker.makeState1(this));
		
		maxFlow = 0;

		ArrayList<Edge> path = new ArrayList<Edge>();
		
		while (true) {
			Arrays.fill(level, -1);
			level[s] = 0;

			// if t is unreachable, break the loop
			if (!bfs())
				break;
			
			addState(stMaker.makeState2(this, level));
			
			// find an abitrary blocking flow
			long pushed;
			path.clear();
			while ((pushed = dfs(s, Long.MAX_VALUE, path)) > 0) {
				
				addState(stMaker.makeState3(this, pushed, path));
				
				for(Edge edge : path) {
					addState(stMaker.makeState4(this, pushed, edge, path));
				}
				
				maxFlow += pushed;
				addState(stMaker.makeState5(this, pushed));
				path.clear();
			}
		}
		
		addState(stMaker.makeState6(this));

		getMinCut(cGraph, getRGraph());
		addState(stMaker.makeStateFinal(this, minCuts));
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