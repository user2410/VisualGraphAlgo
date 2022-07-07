package application.ui;

import java.util.ArrayList;

import application.graph.Graph;
import application.graph.Node;

public class GGraph extends Graph{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4967818067696204778L;
	Session s;
	private GNode selectedNode;
	
	public GGraph(Session s) {
		super();
		this.s = s;
		selectedNode = null;
	}
	
	public static double distance(int x1, int y1, int x2, int y2) {
		int absX = x1>x2 ? x1-x2 : x2-x1;
		int absY = y1>y2 ? y1-y2 : y2-y1;
		return Math.sqrt(absX*absX + absY*absY); 
	}
	
	private GNode getNodeAt(int x, int y) {
		GNode n = null;
		for(Node node : nodes) {
			if(distance(node.getX(), node.getY(), x, y) < GNode.R){
				n = (GNode)node;
				break;
			}
		}
		return n;
	}
	
	@Override
	public void addNode(int x, int y) {
		// check intersection with other nodes
		boolean intersect = false;
		for(Node node : nodes) {
			if(distance(node.getX(), node.getY(), x, y) < (GNode.R<<1)){
				intersect = true;
				break;
			}
		}
		if(intersect) return;
		
		// check whether 3 point on the same line
		
		nodes.add(new GNode(this, nextNodeID++, x, y));
		adjList.add(new ArrayList<Integer>());
	}
	
	private void addEdge(GNode n1, GNode n2) {
		GEdge edge = new GEdge(this, n1, n2);
		if(edges.contains(edge)) {
			return;
		}
		edges.add(edge);
		adjList.get(n1.getId()).add(n2.getId());
	}
	
	private boolean checkCornerClicked(int x, int y) {
		if(x<=GNode.R || x>=s.drawPane.getWidth()-GNode.R ||
			y<=GNode.R || y>=s.drawPane.getHeight()-GNode.R) {
			return true;
		}
		return false;
	}
	
	public void processClick(int x, int y) {
		// check if any node is clicked
		GNode n = getNodeAt(x, y);
		if(n != null) {
			// if clicked -> toggle click that node
			if(selectedNode != null) {
				if(selectedNode == n) {
					n.toggleSelected();
				}else{
					addEdge(selectedNode, n);
					selectedNode.toggleSelected();
				}
				selectedNode = null;
			}else{
				selectedNode = n;
				n.toggleSelected();
			}
		}else if(!checkCornerClicked(x, y)){
			// if not -> create new node
			addNode(x, y);
			if(selectedNode != null) {
				selectedNode.toggleSelected();
				selectedNode = null;
			}
		}
		
	}
}
