package application.ui;

import java.util.ArrayList;

import application.graph.Graph;
import application.graph.Node;
import application.ui.math.Vector2;
import javafx.scene.layout.Pane;

public class GGraph extends Graph{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4967818067696204778L;
	Pane drawPane;
	private GNode selectedNode;
	private GEdge selectedEdge;
	
	public GGraph(Pane drawPane) {
		super();
		this.drawPane = drawPane;
		selectedNode = null;
		selectedEdge = null;
	}
	
	private GNode getNodeAt(int x, int y) {
		GNode n = null;
		Vector2 dis = Vector2.Zero();
		for(Node node : nodes) {
			dis.x = ((GNode)node).x - x;
			dis.y = ((GNode)node).y - y;
			if(dis.length() < GNode.R){
				n = (GNode)node;
				break;
			}
		}
		return n;
	}
	
	public void addNode(int x, int y) {
		// check intersection with other nodes
		boolean intersect = false;
		Vector2 dis = Vector2.Zero();
		for(Node node : nodes) {
			dis.x = ((GNode)node).x - x;
			dis.y = ((GNode)node).y - y;
			if(dis.length() < (GNode.R<<1)){
				intersect = true;
				break;
			}
		}
		if(intersect) return;
		
		// check whether 3 point on the same line
		nodes.add(new GNode(this, nodeCount++, x, y));
		adjList.add(new ArrayList<Integer>());
	}
	
	public void deleteNode() throws Exception {
		super.deleteNode(selectedNode.getId());
		selectedNode.remove();
		selectedNode = null;
	}
	
	private void addEdge(GNode n1, GNode n2) {
		GEdge edge = new GEdge(this, n1, n2);
		if(edges.contains(edge)) {
			return;
		}
		edges.add(edge);
		adjList.get(n1.getId()).add(n2.getId());
	}
	
	public void deleteEdge() throws Exception {
		super.deleteEdge(selectedEdge.getFrom(), selectedEdge.getTo());
		selectedEdge.remove();
		selectedEdge = null;
	}
	
	void setSelectedEdge(GEdge e) {
		selectedEdge = e;
	}
	
	private boolean checkCornerClicked(int x, int y) {
		if(x<=GNode.R || x>=drawPane.getWidth()-GNode.R ||
			y<=GNode.R || y>=drawPane.getHeight()-GNode.R) {
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
			if(selectedNode != null) {
				selectedNode.toggleSelected();
				selectedNode = null;
			}else {
				addNode(x, y);
			}
		}
	}
	
	public void fromGraph(Graph g) {
		nodeCount = 0;
		
		nodes.forEach(n->((GNode)n).remove());
		nodes.clear();
		g.nodes.forEach(n->nodes.add(new GNode(this, nodeCount++, n.getX(), n.getY())));
		
		edges.forEach(e->((GEdge)e).remove());
		edges.clear();
		g.edges.forEach(e->edges.add(new GEdge(this, (GNode)nodes.get(e.getFrom()), (GNode)nodes.get(e.getFrom()))));
		
		adjList = new ArrayList<ArrayList<Integer>>(g.adjList);
		
		System.gc();
	}
	
}
