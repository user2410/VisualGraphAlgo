package application.ui;

import java.util.ArrayList;

import application.graph.Edge;
import application.graph.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class GNode extends Node{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4572159178583259675L;
	static final int	R = 18;
	GGraph		g;
	Circle		c;
	Text		label;
	boolean		selected;
	
	public GNode(GGraph g,int id, int x, int y) {
		super(id, x, y);
		this.g = g;
		draw();
		selected = false;
	}
	
	public void draw() {
		c = new Circle(getX(), getY(), R);
		c.setFill(Color.WHITE);
		c.setStroke(Color.BLACK);
		String idStr = Integer.valueOf(getId()).toString();
		label = new Text(c.getCenterX()-(R>>2)-idStr.length(), c.getCenterY()+(R>>2), idStr);
		g.s.drawPane.getChildren().addAll(c, label);
	}
	
	public void toggleSelected() {
		if(selected) {	
    		c.setFill(Color.WHITE);
		}else {
			c.setFill(Color.CHARTREUSE);			
		}
		selected = !selected;
	}
	
	public void remove() {
		int id = getId();
		for(int i=0; i<g.edges.size(); i++) {
			GEdge e = (GEdge)g.edges.get(i);
			if(e.getFrom() == id || e.getTo() == id) {
				g.edges.remove(i);
				break;
			}
		}
		
		g.adjList.remove(id);
		for(ArrayList<Integer> list : g.adjList) {
			list.remove(Integer.valueOf(id));
		}
		
		g.nodes.remove(id);
		for(int i=id; i<g.nodes.size(); i++) {
			GNode n = (GNode)g.nodes.get(i);
			n.setId(i);
			n.draw();
		}
		
		for(Edge e : g.edges) {
			((GEdge)e).draw();
		}
		
		g.s.drawPane.getChildren().removeAll(c, label);
	}
}
