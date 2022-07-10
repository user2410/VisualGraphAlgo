package application.ui;

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
	int			x, y;
	GGraph		g;
	Circle		c;
	Text		label;
	boolean		selected;
	
	public GNode(GGraph g, int id, int x, int y) {
		super(id, x, y);
		this.x = x;
		this.y = y;		
		this.g = g;
		draw();
		selected = false;
	}
	
	public void draw() {
		c = new Circle(x, y, R);
		c.setFill(Color.WHITE);
		c.setStroke(Color.BLACK);
		String idStr = Integer.valueOf(getId()).toString();
		label = new Text(c.getCenterX()-(R>>2)-idStr.length()+1, c.getCenterY()+(R>>2), idStr);
		g.drawPane.getChildren().addAll(c, label);
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
		g.drawPane.getChildren().removeAll(c, label);
	}
}
