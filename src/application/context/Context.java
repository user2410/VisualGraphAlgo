package application.context;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import application.algorithm.Algorithm;
import application.context.state.AlgoState;
// import application.graph.Graph;

public class Context{

	private Algorithm algo = null;
	
	private ArrayList<AlgoState> states = new ArrayList<AlgoState>();
	private int currentState = 0;
	
	private long delay = 1000;
	
	private final AtomicBoolean isAlive = new AtomicBoolean();
	private final AtomicBoolean isPlaying = new AtomicBoolean();
	
	public Context() {
		isAlive.set(true);
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
	
	public boolean isPlaying() {
		return isPlaying.get();
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
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
		isPlaying.set(true);
		while(isAlive.get()) {
			AlgoState st = next();
			System.out.println(st);
			if(st==null) {
				// System.out.println("the end!");
				isPlaying.set(false);
			}
			try {
				if(isPlaying.get()) {					
					wait(delay);
				}
				else {
					// System.out.println("PAUSE");
					wait();
				}
			} catch (InterruptedException e) {
				
			}
		}
	}
}
