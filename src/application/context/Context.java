package application.context;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import application.algorithm.Algorithm;
import application.context.state.AlgoState;
import application.context.state.Step;
import application.graph.Graph;
// import application.graph.Graph;

public class Context{

	private Graph graph;

	private Algorithm algo = null;
	
	private ArrayList<AlgoState> states = new ArrayList<AlgoState>();
	private AtomicInteger currentState = new AtomicInteger(0);
	private ArrayList<Step> baseSteps;
	
	private AtomicLong delay = new AtomicLong(1000);
	
	private final AtomicBoolean isAlive = new AtomicBoolean();
	private final AtomicBoolean isPlaying = new AtomicBoolean();
	
	public Context() {
		isAlive.set(false);
		isPlaying.set(false);
	}
	
	public Context(Algorithm algo) {
		this();
		setAlgo(algo);
	}
	
	public void setAlgo(Algorithm algo) {
		this.algo = algo;
		this.graph = algo.getGraph();
		this.baseSteps = algo.getBaseSteps();
		
		states.clear();
		currentState.set(0);
		
		isAlive.set(false);
		isPlaying.set(false);
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public Step getStep(int i) {
		return baseSteps.get(i);
	}
	
	public synchronized void exploreAlgo() {
		algo.explore();
		currentState.set(0);
	}
	
	public void addState(AlgoState s) {
		states.add(s);
	}

	public AlgoState getCurrentState() {
		return states.get(currentState.get());
	}
	
	public synchronized AlgoState next() {
		AlgoState st = null;
		if(currentState.get() + 1 < states.size()) {
			int cur = currentState.get();
			st = states.get(cur);
			currentState.set(cur+1);
		}
		
		return st;
	}
	
	public synchronized AlgoState prev() {
		AlgoState st = null;
		if(currentState.get() > 0) {
			int cur = currentState.get(); 
			st = states.get(cur);
			currentState.set(cur+1);
		}
		
		return st;
	}
	
	public boolean isPlaying() {
		return isPlaying.get();
	}
	
	public void setDelay(long delay) {
		this.delay.set(delay);;
	}
	
	public synchronized void terminate() {
		isAlive.set(false);
		notify();
	}
	
	public synchronized void togglePlaying() {
		boolean _isPlaying = isPlaying.get();
		isPlaying.set(!_isPlaying);
		if(!_isPlaying)
			notify();
	}
	
	public synchronized void play() {
		isAlive.set(true);
		isPlaying.set(true);
		
		while(isAlive.get()) {
			AlgoState st = next();
			System.out.println(st);
			if(st==null) {
				isPlaying.set(false);
			}
			try {
				if(isPlaying.get()) {					
					wait(delay.get());
				}
				else {
					wait();
				}
			} catch (InterruptedException e) {
				
			}
		}
	}
}
