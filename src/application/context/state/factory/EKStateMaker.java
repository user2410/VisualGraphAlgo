package application.context.state.factory;

import java.util.ArrayList;
import java.util.Arrays;

import application.algorithm.Algorithm;
import application.context.state.AlgoState;
import application.context.state.Step;
import application.graph.Edge;

public class EKStateMaker extends StateMaker{

	private static final ArrayList<Step> steps = new ArrayList<Step>(Arrays.asList(
			new Step("initMaxFlow", 0),
			new Step("while there is an augmenting path", 0),
			new Step("find an augmenting path using BFS", 1),
			new Step("for each edge u->v in the path", 1),
			new Step("decrease capacity u->v by bottleneck", 2),
			new Step("increase capacity v->u by bottleneck", 2),
			new Step("increase maxflow by bottleneck", 1)
	));

	@Override
	public ArrayList<Step> getSteps() {
		return steps;
	}

	public AlgoState makeState2(Algorithm algo, long bottleneck, ArrayList<Edge> path) {
		String stateText = "Got an augmenting path. Bottleneck is " + bottleneck;
		AlgoState newState = new AlgoState(algo.getRGraph(), currentFlowText(algo), stateText, getSteps().get(2));
		newState.setPath(path);
		return newState;
	}

	public AlgoState makeState3(Algorithm algo, long bottleneck, int u, int v, boolean inc) {
		String stateText = "Bottleneck is " + bottleneck + ". Updating edge from " + u + " to " + v + '.';
		return new AlgoState(algo.getRGraph(), currentFlowText(algo), stateText, getSteps().get(inc ? 5 : 4));
	}
	
	public AlgoState makeState4(Algorithm algo, long bottleneck) {
		String stateText = "Maximum flow increased by " + bottleneck + '.';
		return new AlgoState(algo.getRGraph(), currentFlowText(algo), stateText, getSteps().get(6));
	}
	
	public AlgoState makeState5(Algorithm algo) {
		return new AlgoState(algo.getRGraph(), currentFlowText(algo), "No more augmenting path.", new Step("", -1));
	}
}
