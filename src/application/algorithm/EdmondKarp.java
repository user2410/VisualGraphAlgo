package application.algorithm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import javafx.util.Pair;

import application.graph.Edge;
import application.graph.Graph;

public class EdmondKarp extends Algorithm{
	
	protected long maxFlow = 0;
	protected int s, t;
	
	protected long [][] cGraph;
	protected long [][] rGraph;
	
	public EdmondKarp(Graph graph, int s, int t) {
		this.graph = graph;
		this.s = s;
		this.t = t;
		
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
	
	private long bfs(int[] parent) {
		Arrays.fill(parent, -1);
		parent[s] = -2;
		
		Queue<Pair<Integer, Long>> q = new LinkedList<>();
		q.add(new Pair<Integer, Long>(s, Long.MAX_VALUE));
		
		do {
			Pair<Integer, Long> curNode = q.poll();
			if(curNode==null) break;
			int cur = curNode.getKey();
			long flow = curNode.getValue();
			
			for(int next : graph.adjList.get(cur)) {
				if(parent[next] == -1 && rGraph[cur][next] != 0) {
					parent[next] = cur;
					long newFlow = flow < rGraph[cur][next] ? flow : rGraph[cur][next];
					if(next == t)
						return newFlow;
					q.add(new Pair<Integer, Long>(next, newFlow));
				}
			}
			
		}while(true);
		
		return 0;
	}
	
	@Override
	public void explore() {
		long flow = 0;
		int[] parent = new int[graph.getNodeCount()];
		long newFlow;
		
		while((newFlow = bfs(parent))>0) {
			flow += newFlow;
			int cur = t;
			while(cur != s) {
				int prev = parent[cur];
				rGraph[prev][cur] -= newFlow;
				rGraph[cur][prev] += newFlow;
				cur = prev;
			}
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
