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
	GGraph		g;
	Circle		c;
	Text		label;
	
	public GNode(GGraph g, int id, int x, int y) {
		super(id, x, y);	
		this.g = g;
		draw();
	}
	
	public void draw() {
		c = new Circle(getX(), getY(), R);
		c.setFill(Color.WHITE);
		c.setStroke(Color.BLACK);
		String idStr = Integer.valueOf(getId()).toString();
		label = new Text(c.getCenterX()-(R>>2)-idStr.length()+1, c.getCenterY()+(R>>2), idStr);
		g.drawPane.getChildren().addAll(c, label);
	}
	
	public void updateLabel(String newVal) {
		label.setText(newVal);
	}
	
	public void setSelected(boolean selected) {
		if(selected) {
			c.setFill(Color.CHARTREUSE);		
		}else {
			c.setFill(Color.WHITE);
		}
	}
	
	public void remove() {
		g.drawPane.getChildren().removeAll(c, label);
	}
}
