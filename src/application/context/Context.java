package application.context;

import java.util.ArrayList;

import application.algorithm.Algorithm;
import application.context.state.AlgoState;
// import application.graph.Graph;

public class Context{

	private Algorithm algo = null;
	
	private ArrayList<AlgoState> states = new ArrayList<AlgoState>();
	private int currentState = 0;
	
	private long delay = 1000;
	
	private boolean isAlive;
	private boolean isPlaying;
	
	public Context() {
		isAlive = true;
		isPlaying = true;
	}
	
	public Context(Algorithm algo) {
		this();
		setAlgo(algo);
	}
	
	public void setAlgo(Algorithm algo) {
		this.algo = algo;
		states.clear();
		currentState = 0;
	}
	
	public synchronized void exploreAlgo() {
		algo.explore();
		currentState = 0;
	}
	
	public void addState(AlgoState s) {
		states.add(s);
	}

	public AlgoState getCurrentState() {
		return states.get(currentState);
	}
	
	public synchronized AlgoState next() {
		AlgoState st = null;
		if(currentState + 1 < states.size())
			st = states.get(++currentState);
		
		return st;
	}
	
	public AlgoState prev() {
		AlgoState st = null;
		if(currentState > 0)
			st = states.get(--currentState);
		
		return st;
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public void terminate() {
		this.isAlive = false;
	}
	
	public synchronized void togglePlaying() {
		isPlaying = !isPlaying;
		if(isPlaying)
			notify();
	}
	
	public synchronized void play() {
		while(true) {
			if(!isAlive) break;
			AlgoState st = next();
			System.out.println(st);
			if(st==null) {
				isPlaying = false;
			}
			try {
				if(isPlaying)
					wait(delay);
				else 
					wait();
			} catch (InterruptedException e) {
				
			}
		}
	}
}
