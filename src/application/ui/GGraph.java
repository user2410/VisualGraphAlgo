package application.ui;

import java.util.ArrayList;

import application.graph.Edge;
import application.graph.Graph;
import application.graph.Node;
import application.ui.math.Vector2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class GGraph extends Graph{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4967818067696204778L;
	Pane drawPane;
	TableView<GEdge> graphTable;
	GNode selectedNode;
	GEdge selectedEdge;
	
	@SuppressWarnings("unchecked")
	public GGraph(Pane drawPane, TableView<GEdge> graphTable) {
		super();
		this.drawPane = drawPane;
		
		this.graphTable = graphTable;
		TableColumn<GEdge, Integer> fromCol = new TableColumn<>("From");
		fromCol.setPrefWidth(50);
    	fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
		TableColumn<GEdge, Integer> toCol = new TableColumn<>("To");
		toCol.setPrefWidth(50);
		toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
		TableColumn<GEdge, TextField> capCol = new TableColumn<>("Capacity");
		capCol.setPrefWidth(120);
		capCol.setCellValueFactory(new PropertyValueFactory<>("capField"));
		TableColumn<GEdge, TextField> removeCol = new TableColumn<>("Remove");
		removeCol.setPrefWidth(80);
		removeCol.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
		graphTable.getColumns().addAll(fromCol, toCol, capCol, removeCol);
		graphTable.setOnSort(e->{
			e.consume();
		});
		
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
	
	public boolean deleteNode() throws Exception {
		if(selectedNode == null) return false;
		for(Edge edge : edges) {
			GEdge e = (GEdge)edge;
			if(e.getFrom() == selectedNode.getId() || e.getTo() == selectedNode.getId())
				e.remove();
		}
		super.deleteNode(selectedNode.getId());
		for(int i=selectedNode.getId(); i<nodes.size(); i++) {
			GNode n = (GNode)nodes.get(i);
			n.updateLabel(Integer.valueOf(n.getId()).toString());
		}
		selectedNode.remove();
		selectedNode = null;
		updateGraphTable();
		return true;
	}
	
	private void addEdge(GNode n1, GNode n2) {
		GEdge edge = new GEdge(this, n1, n2);
		if(edges.contains(edge)) {
			return;
		}
		edges.add(edge);
		adjList.get(n1.getId()).add(n2.getId());
		updateGraphTable();
	}
	
	public boolean deleteEdge() throws Exception {
		if(selectedEdge == null) return false;
		super.deleteEdge(selectedEdge.getFrom(), selectedEdge.getTo());
		selectedEdge.remove();
		selectedEdge = null;
		updateGraphTable();
		return true;
	}
	
	public void deleteEdge(GEdge e) throws Exception{
		super.deleteEdge(e.getFrom(), e.getTo());
		e.remove();
		updateGraphTable();
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
								graphTable.getSelectionModel().clearSelection();
								graphTable.getSelectionModel().select(idx);
							}else { // the same edge is selected again
								selectedEdge = null;
							}
						}else {  // newly selected edge
    						selectedEdge = (GEdge)e;
    						selectedEdge.setSelected(true);
    						graphTable.getSelectionModel().clearSelection();
    						graphTable.getSelectionModel().select(idx);
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
	
	public void updateGraphTable() {
		graphTable.getItems().clear();
		ObservableList<GEdge> list = FXCollections.observableArrayList();
		for(Edge e : edges) {
			GEdge edge = (GEdge)e;
			edge.capField.setText(Long.valueOf(e.getCapacity()).toString());
			list.add(edge);
		}
		graphTable.setItems(list);
		// graphTable.getSortOrder().add(graphTable.getColumns().get(0));
	}

	public void buildFromGraph(Graph g) {		
		nodes.forEach(n->((GNode)n).remove());
		edges.forEach(e->((GEdge)e).remove());
		clear();
		for(Node n : g.nodes) {
			this.nodes.add(new GNode(this, nodeCount++, n.getX(), n.getY()));
		}
		for(Edge e : g.edges) {
			this.edges.add(new GEdge(this, (GNode)nodes.get(e.getFrom()), (GNode)nodes.get(e.getTo()), e.getCapacity()));
		}
		adjList = new ArrayList<ArrayList<Integer>>(g.adjList);
		updateGraphTable();
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
		updateGraphTable();
	}
	
}
