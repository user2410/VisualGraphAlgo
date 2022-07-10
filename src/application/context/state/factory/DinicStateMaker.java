package application.context.state.factory;

import java.util.ArrayList;
import java.util.Arrays;

import application.algorithm.Algorithm;
import application.context.state.AlgoState;
import application.context.state.Step;
import application.graph.Edge;

public class DinicStateMaker extends StateMaker {

	public static final ArrayList<Step> steps = new ArrayList<Step>(Arrays.asList(
			new Step("initMaxFlow", 0),
			new Step("while t is reachable from s in the residual graph", 0),
			new Step("find the level graph (BFS)", 1),
			new Step("for each blocking flow in the level graph (DFS)", 1),
			new Step("update the capacity in the blocking flow", 2),
			new Step("increase maxflow by bottleneck", 2)
	));

	@Override
	public ArrayList<Step> getSteps() {
		return steps;
	}

	public AlgoState makeState2(Algorithm algo, int[] level) {
		return new AlgoState(algo.getRGraph(), currentFlowText(algo), "Got the level graph (blue edges). Sink is level " + level[algo.getT()], steps.get(2));
	}
	
	public AlgoState makeState3(Algorithm algo, long bottleneck) {
		return new AlgoState(algo.getRGraph(), currentFlowText(algo), "Found a blocking flow. Bottleneck is " + bottleneck, steps.get(3));
	}
	
	public AlgoState makeState4(Algorithm algo, long bottleneck, Edge edge) {
		long[][] RGraph = algo.getRGraph();
		int from = edge.getFrom();
		int to = edge.getTo();
		long pushed = edge.getCapacity();
		
		RGraph[from][to] += pushed;
		RGraph[to][from] -= pushed;
		
		return new AlgoState(RGraph, currentFlowText(algo), "Bottleneck is " + bottleneck + ". Updating edge from " + from + " to " + to + '.', steps.get(4));
	}
	
	public AlgoState makeState5(Algorithm algo, long pushed) {
		return new AlgoState(algo.getRGraph(), currentFlowText(algo), "Maximum flow increased by " + pushed, steps.get(5));
	}
	
	public AlgoState makeState6(Algorithm algo) {
		return new AlgoState(algo.getRGraph(), currentFlowText(algo), "No more flow from source to sink.", new Step("", -1));
	}
}
