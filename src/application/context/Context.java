package application.context;

import java.util.ArrayList;

import application.algorithm.Algorithm;
import application.context.state.AlgoState;
// import application.graph.Graph;

public class Context{

	private Algorithm algo = null;
	
	private ArrayList<AlgoState> states = new ArrayList<AlgoState>();
	private int currentState = 0;
	
	public Context() {}
	public Context(Algorithm algo) {
		setAlgo(algo);
	}
	
	public void setAlgo(Algorithm algo) {
		this.algo = algo;
		states.clear();
		currentState = 0;
	}
	
	public synchronized void exploreAlgo() {
		states.clear();
		algo.explore();
		currentState = 0;
	}
	
	public void addState(AlgoState s) {
		states.add(s);
	}

	public AlgoState getCurrentState() {
		return states.get(currentState);
	}
	
	public synchronized void next() {
		if(currentState + 1 < states.size())
			currentState++;
	}
	
	public synchronized void prev() {
		if(currentState > 0)
			currentState--;
	}
}
