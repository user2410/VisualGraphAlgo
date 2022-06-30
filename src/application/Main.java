package application;

import java.io.IOException;

import application.algorithm.*;
import application.context.Context;
import application.context.ContextController;
import application.graph.Graph;

public class Main{
	
	public static void main(String[] args) {
		Graph g = null;
//		g.addNode(1, 1);
//		g.addNode(2, 2);
//		g.addNode(3, 2);
//		g.addNode(2, 0);
//		g.addNode(3, 1);
//		g.addNode(4, 1);
//		g.addNode(4, 1);
//		g.addNode(4, 1);
//		g.addNode(4, 1);
//		g.addNode(4, 1);
//		
//		try {
//			g.addEdge(0, 1, 5);
//			g.addEdge(0, 2, 8);
//			g.addEdge(0, 9, 7);
//			g.addEdge(0, 3, 3);
//			g.addEdge(0, 4, 5);
//			g.addEdge(0, 5, 7);
//			g.addEdge(1, 9, 4);
//			g.addEdge(2, 9, 9);
//			g.addEdge(3, 6, 1);
//			g.addEdge(4, 7, 4);
//			g.addEdge(5, 6, 1);
//			g.addEdge(5, 7, 2);
//			g.addEdge(5, 8, 6);
//			g.addEdge(6, 9, 1);
//			g.addEdge(7, 9, 6);
//			g.addEdge(8, 9, 5);
//		}catch(Exception e) {
//			System.err.println(e.getMessage());
//		}
		
		try {
			g = Graph.deserialize("dinicShowcase.graph");
		}catch (IOException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException ce) {
			ce.printStackTrace();
		}
		
		Context c = new Context();
//		Algorithm a = Algorithm.makeAlgo(c, g, 0, 9, Algorithm.Type.FF);
//		Algorithm a = Algorithm.makeAlgo(c, g, 0, 9, Algorithm.Type.EK);
		Algorithm a = Algorithm.makeAlgo(c, g, 0, 9, Algorithm.Type.DINIC);
		c.setAlgo(a);
		c.exploreAlgo();
		
		ContextController cc = new ContextController(c);
		cc.start();
		c.setDelay(500);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\nPAUSE\n");
		c.togglePlaying();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\nRESUME\n");
		c.togglePlaying();
		
		while(c.isPlaying());
		// cc.notify();
		// System.out.println("Thread terminated");
		c.terminate();
//		System.out.println(a.minCuts);
		
	}
}
