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
		g.addNode(4, 1);
		g.addNode(4, 1);
		g.addNode(4, 1);
		g.addNode(4, 1);
		
		try {
			g.addEdge(0, 1, 5);
			g.addEdge(0, 2, 8);
			g.addEdge(0, 9, 7);
			g.addEdge(0, 3, 3);
			g.addEdge(0, 4, 5);
			g.addEdge(0, 5, 7);
			g.addEdge(1, 9, 4);
			g.addEdge(2, 9, 9);
			g.addEdge(3, 6, 1);
			g.addEdge(4, 7, 4);
			g.addEdge(5, 6, 1);
			g.addEdge(5, 7, 2);
			g.addEdge(5, 8, 6);
			g.addEdge(6, 9, 1);
			g.addEdge(7, 9, 6);
			g.addEdge(8, 9, 5);
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		FordFulkerson ff = new FordFulkerson(g, 0, 9);
		ff.start();
		try {
			ff.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(ff.getMaxFlow());
		System.out.println(ff.minCuts);
		
		EdmondKarp ek = new EdmondKarp(g, 0, 9);
		ek.start();
		try {
			ek.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(ek.getMaxFlow());
		System.out.println(ek.minCuts);
		
		Dinic d = new Dinic(g, 0, 9);
		d.start();
		try {
			d.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(d.getMaxFlow());
		System.out.println(d.minCuts);
		
	}
}
