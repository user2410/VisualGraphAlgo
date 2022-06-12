package application.algorithm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import application.graph.Edge;
import application.graph.Graph;

public class EdmondKarp extends Algorithm{
	
	protected long maxFlow = 0;
	protected int s, t;
	
	protected long [][] cGraph;
	protected long [][] rGraph;
	protected int[] parent;
	protected boolean[] visited;
	
	public EdmondKarp(Graph graph, int s, int t) {
		this.graph = graph;
		this.s = s;
		this.t = t;

		parent = new int[graph.getNodeCount()];
		visited = new boolean[graph.getNodeCount()];
		cGraph = new long[graph.getNodeCount()][graph.getNodeCount()];
		
		for (long[] row : cGraph) {
			Arrays.fill(row, 0);
		}
		
		for(int i=0; i<graph.edges.size(); i++) {
			Edge cur = graph.edges.get(i);
			cGraph[cur.getFrom()][cur.getTo()] = cur.getCapacity();
		}
		
		rGraph = cGraph.clone();
	}
	
	private boolean bfs() {		
		Queue<Integer> q = new LinkedList<>();
		q.add(s);
		
		do {
			Integer cur = q.poll();
			if(cur==null) break;
			
			for(int next : graph.adjList.get(cur)) {
				if(!visited[next] && rGraph[cur][next] > 0) {
					q.add(next);
					parent[next] = cur;
					visited[next] = true;
				}
			}
			
		}while(true);
		
		return visited[t];
	}
	
	@Override
	public void explore() {
		long flow = 0;
		
		while(bfs()) {
			Arrays.fill(visited, false);
			long pathFlow = Long.MAX_VALUE;

			for(int v=t; v!=s; v=parent[v]) {
				int u = parent[v];
				pathFlow = pathFlow < rGraph[u][v] ? pathFlow : rGraph[u][v];
			}
			
			for(int v=t; v!=s; v=parent[v]) {
				int u = parent[v];
				rGraph[u][v] -= pathFlow;
				rGraph[v][u] += pathFlow;
			}
			
			flow += pathFlow;
		}
		
		maxFlow = flow;
	}

	@Override
	public void run() {
		explore();
	}
	
	public long getMaxFlow() {
		return maxFlow;
	}

}
