package application;

import application.algorithm.Dinic;
import application.algorithm.EdmondKarp;
import application.algorithm.FordFulkerson;
import application.graph.Graph;

public class Main{
	
	public static void main(String[] args) {
		Graph g = new Graph();
		g.addNode(1, 1);
		g.addNode(2, 2);
		g.addNode(3, 2);
		g.addNode(2, 0);
		g.addNode(3, 1);
		g.addNode(4, 1);
		
		try {
			g.addEdge(0, 1, 5);
			g.addEdge(0, 3, 15);
			g.addEdge(1, 2, 10);
			g.addEdge(1, 4, 5);
			// g.addEdge(1, 8, 5);
			g.addEdge(2, 3, 3);
			g.addEdge(2, 5, 20);
			g.addEdge(3, 1, 5);
			g.addEdge(3, 4, 5);
			g.addEdge(4, 2, 2);
			g.addEdge(4, 5, 5);
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		FordFulkerson ff = new FordFulkerson(g, 0, 5);
		ff.start();
		try {
			ff.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(ff.getMaxFlow());
		
		EdmondKarp ek = new EdmondKarp(g, 0, 5);
		ek.start();
		try {
			ek.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(ek.getMaxFlow());

		Dinic d = new Dinic(g, 0, 5);
		d.start();
		try {
			d.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(d.getMaxFlow());
	}
}
