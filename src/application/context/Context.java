package application.context;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import application.MainController;
import application.algorithm.Algorithm;
import application.context.state.AlgoState;
import application.context.state.Step;
import application.graph.Graph;
// import application.graph.Graph;

public class Context{
	
	private MainController controller;

	private Graph graph;

	private Algorithm algo = null;
	
	private ArrayList<AlgoState> states = new ArrayList<AlgoState>();
	private AtomicInteger currentState = new AtomicInteger(0);
	private ArrayList<Step> baseSteps;
	
	public final long DEFAULT_DELAY = 1500;
	private AtomicLong delay = new AtomicLong(DEFAULT_DELAY);
	
	private final AtomicBoolean isAlive = new AtomicBoolean();
	private final AtomicBoolean isPlaying = new AtomicBoolean();
	
	public Context(MainController controller) {
		isAlive.set(false);
		isPlaying.set(false);
		this.controller = controller;
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
	
	public AlgoState getStateAt(int i) {
		if(i>=0 && i<states.size()) {
			return states.get(i);			
		}
		return null;
	}

	public int getCurrentStateNum() {
		return currentState.get();
	}
	
	public AlgoState getCurrentState() {
		return states.get(currentState.get());
	}
	
	public synchronized void setCurrentState(int step) {
		currentState.set(step);
	}
	
	public int getStateCount() {
		return states.size();
	}
	
	public synchronized AlgoState next() {
		AlgoState st = null;
		if(currentState.get() + 1 < states.size()) {
			int cur = currentState.get();
			st = states.get(cur);
			currentState.set(cur+1);
		}
		notifyController();
		return st;
	}
	
	public synchronized void prev() {
		if(currentState.get() > 0) {
			currentState.set(currentState.get()-1);
		}
		notifyController();
	}
	
	public boolean isPlaying() {
		return isPlaying.get();
	}
	
	public void setSpeed(double speed) {
		speed = 1.0/speed;
		delay.set((long)(speed*delay.get()));
	}
	
	public synchronized void terminate() {
		isAlive.set(false);
		notify();
	}
	
	public void pause() {
		isPlaying.set(false);
	}
	
	public synchronized void resume() {
		isPlaying.set(true);
		notify();
	}
	
	public synchronized void play() {
		isAlive.set(true);
		isPlaying.set(true);
		
		while(isAlive.get()) {
			AlgoState st = next();
			System.out.println(st);
			if(st==null) isPlaying.set(false);
			notifyController();
			try {
				if(isPlaying.get()) {
					isPlaying.set(false);
					wait(delay.get());
					isPlaying.set(true);
				}
				else {
					isPlaying.set(false);
					wait();
					isPlaying.set(true);
				}
			} catch (InterruptedException e) {}
		}
	}
	
	private synchronized void notifyController() {
		controller.reactToContext(getCurrentState());
	}
	
}
