package application.ui;

import java.util.ArrayList;

import application.graph.Edge;
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
	GNode selectedNode;
	GEdge selectedEdge;
	
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
			dis.x = node.getX() - x;
			dis.y = node.getY() - y;
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
			dis.x = node.getX() - x;
			dis.y = node.getY() - y;
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
		if(n != null) {	// a node clicked
			if(selectedNode != null) { // a node is already selected
				if(selectedNode != n) { // another node is clicked
					int idx = edges.indexOf(new Edge(selectedNode.getId(), n.getId()));
					if(idx != -1){ // selected edge is already created
						Edge e = edges.get(idx); // the newly selected edge
						if(selectedEdge != null) { // an edge is already selected
							selectedEdge.setSelected(false);
							if(selectedEdge != e) { // another edge is selected
								selectedEdge = ((GEdge)e);
								selectedEdge.setSelected(true);
							}else { // the same edge is selected again
								selectedEdge = null;
							}
						}else {  // newly selected edge
    						selectedEdge = (GEdge)e;
    						selectedEdge.setSelected(true);
						}
					}else {	// newly created edge
						addEdge(selectedNode, n);
						if(selectedEdge!=null) {
							selectedEdge.setSelected(false);
							selectedEdge = null;
						}
					}
				}
				selectedNode.setSelected(false);
				selectedNode = null;
			}else{	// no node is currently selected
				selectedNode = n;
				n.setSelected(true);
			}
		}else if(!checkCornerClicked(x, y)){ // free space clicked
			if(selectedNode != null) {	// a node is already selected -> clear selection
				selectedNode.setSelected(false);
				selectedNode = null;
			}else {	// create new node
				addNode(x, y);
			}
		}
	}
	
	public GNode getSelectedNode() {
		return selectedNode;
	}

	public GEdge getSelectedEdge() {
		return selectedEdge;
	}

	public void buildFromGraph(Graph g) {
		nodeCount = 0;
		
		nodes.forEach(n->((GNode)n).remove());
		nodes.clear();
		g.nodes.forEach(n->nodes.add(new GNode(this, nodeCount++, n.getX(), n.getY())));
		
		edges.forEach(e->((GEdge)e).remove());
		edges.clear();
		g.edges.forEach(e->edges.add(new GEdge(this, (GNode)nodes.get(e.getFrom()), (GNode)nodes.get(e.getFrom()))));
		
		for(ArrayList<Integer> l : adjList) {
			l.clear();
		}
		adjList.clear();
		adjList = new ArrayList<ArrayList<Integer>>(g.adjList);
		
		System.gc();
	}
	
	@Override
	public void clear() {
		for(Node n : nodes) {
			((GNode)n).remove();
		}
		
		for(Edge e : edges) {
			((GEdge)e).remove();
		}
		
		super.clear();
	}
	
}
