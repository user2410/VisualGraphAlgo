package application.ui;

import application.graph.Edge;
import application.ui.math.Vector2;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public class GEdge extends Edge{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8049073912384547909L;
	
	GGraph g;
	GNode nFrom, nTo;
	Arrow line;
	Text label;
	
	public GEdge(GGraph g, GNode n1, GNode n2) {
		super(n1.getId(), n2.getId(), 1);
		this.g = g;
		nFrom = n1; nTo = n2;
		
		if(line != null) g.drawPane.getChildren().remove(line); 
		if(label != null) g.drawPane.getChildren().remove(label);
				
		Vector2 dis = new Vector2(nTo.getX()-nFrom.getX(), nTo.getY()-nFrom.getY());
		double rat = (GNode.R+2)/dis.length();
		int disX = (int)(rat * (nTo.getX()-nFrom.getX()));
		int disY = (int)(rat * (nTo.getY()-nFrom.getY()));
		
		line = new Arrow(
				nFrom.getX() + disX, nFrom.getY() + disY,
				nTo.getX() - disX, nTo.getY() - disY);
		
		label = new Text(
				(nFrom.getX() + nTo.getX())>>1,
				(nFrom.getY() + nTo.getY())>>1,
				Long.valueOf(getCapacity()).toString()
				);
		
		label.getTransforms().add(new Rotate(dis.getAlpha(), (nFrom.getX() + nTo.getX())>>1, (nFrom.getY() + nTo.getY())>>1));
		
		g.drawPane.getChildren().addAll(line, label);
		
	}
	
	public void updateLabel(String newVal) {
		label.setText(newVal);
	}
	
	public void setSelected(boolean selected) {
		if(selected) {
			line.setFill(Color.CRIMSON);
		}else {
			line.setFill(Color.BLACK);
		}
	}
	
	public void remove() {
		g.drawPane.getChildren().removeAll(line, label);
	}

}
