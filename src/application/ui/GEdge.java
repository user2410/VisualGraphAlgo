package application.ui;

import application.graph.Edge;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class GEdge extends Edge{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8049073912384547909L;
	
	GGraph g;
	GNode nFrom, nTo;
	Line line;
	Text label;
	
	public GEdge(GGraph g, GNode n1, GNode n2) {
		super(n1.getId(), n2.getId());
		this.g = g;
		nFrom = n1; nTo = n2;
		capacity = 1;
		line = null;
		label = null;
		draw();
	}
	
	public void draw() {
		
		if(line != null) g.s.drawPane.getChildren().remove(line); 
		if(label != null) g.s.drawPane.getChildren().remove(label);
		
		int fromX = nFrom.getX();
		int fromY = nFrom.getY();		
		int toX = nTo.getX();
		int toY = nTo.getY();
		
		double dis = GGraph.distance(fromX, fromY, toX, toY);
		double rat = GNode.R/dis;
		int disX = (int)(rat * (toX-fromX));
		int disY = (int)(rat * (toY-fromY));
		
		line = new Line(
				fromX + disX, fromY + disY,
				toX - disX, toY - disY);
		
		label = new Text(
				(nFrom.getX() + nTo.getX())>>1,
				(nFrom.getY() + nTo.getY())>>1,
				Long.valueOf(capacity).toString()
				);
		
		g.s.drawPane.getChildren().addAll(line, label);
		
	}
	
	public void remove() {
		g.s.drawPane.getChildren().removeAll(line, label);
		g.edges.remove(this);
	}

}
