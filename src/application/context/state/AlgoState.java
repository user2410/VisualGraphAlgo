package application.context.state;

import java.util.ArrayList;

import application.graph.Edge;

public class AlgoState {

	private long[][] rGraph;
	private String stateText1;
	private String stateText2;
	private Step step;
	private ArrayList<Edge> path = null;
	
	public AlgoState(long[][] rGraph, String stateText1, String stateText2, Step step) {
		super();
		this.rGraph = rGraph;
		this.stateText1 = stateText1;
		this.stateText2 = stateText2;
		this.step = step;
	}

	public long[][] getrGraph() {
		return rGraph;
	}

	public Step getStep() {
		return step;
	}

	public String getStateText1() {
		return stateText1;
	}

	public String getStateText2() {
		return stateText2;
	}

	public ArrayList<Edge> getPath() {
		return path;
	}

	public void setPath(ArrayList<Edge> path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return "===============\n" + 
				stateText1 + '\n' + stateText2 + '\n' +
				step + '\n';
	}
}
