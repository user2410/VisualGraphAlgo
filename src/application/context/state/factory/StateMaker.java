package application.context.state.factory;

import java.util.ArrayList;

import application.algorithm.Algorithm;
import application.context.state.AlgoState;
import application.context.state.Step;

public abstract class StateMaker {

	public abstract ArrayList<Step> getSteps();
	
	protected String currentFlowText(Algorithm algo) {
		return "Maximum flow from " + algo.getS() + " to " + algo.getT() + " is currently " + algo.getMaxFlow();
	}
	
	public AlgoState makeState0(Algorithm algo) {
		return new AlgoState(algo.getRGraph(), currentFlowText(algo), "The original flow graph.", getSteps().get(0));
	}

	public AlgoState makeState1(Algorithm algo) {
		return new AlgoState(algo.getRGraph(), currentFlowText(algo), "Preparing residual graph.", getSteps().get(0));
	}
	
	public AlgoState makeStateFinal(Algorithm algo) {
		String stateText1 = "Maximum flow = Minimum Cut from " + algo.getS() + " to " + algo.getT() + " is " + algo.getMaxFlow();
		String stateText2 = "Edge(s) that cross(es) the ST-cut is/are highlighted.";
		return new AlgoState(algo.getRGraph(), stateText1, stateText2, new Step("", -1, -1));
	}

}
