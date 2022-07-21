package application.ui;

import application.graph.Edge;
import application.ui.math.Vector2;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
	TextField capField;
	Button deleteBtn;
	
	public GEdge(GGraph g, GNode n1, GNode n2, long cap) {
		super(n1.getId(), n2.getId(), cap);
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
		
		capField = new TextField(Long.valueOf(getCapacity()).toString());
		capField.focusedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (!arg2) // not focused anymore
		        {
		            // System.out.println("Textfield out focus");
					String newVal = capField.getText();
					try {
						long c = Long.parseLong(newVal);
						if(c<=0) {
							// warn user
							return;
						}
						setCapacity(c);
						updateLabel(newVal);
					}catch(NumberFormatException e) {
						// warn user
					}
		        }
			}
		});
		deleteBtn = new Button("Remove");
		deleteBtn.setOnAction(e->{
			try {
				g.deleteEdge(this);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}
	
	public GEdge(GGraph g, GNode n1, GNode n2) {
		this(g, n1, n2, 1);
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

	public TextField getCapField() {
		return capField;
	}

	public void setCapField(TextField capField) {
		this.capField = capField;
	}

	public Button getDeleteBtn() {
		return deleteBtn;
	}

	public void setDeleteBtn(Button deleteBtn) {
		this.deleteBtn = deleteBtn;
	}
	
	public void setModifying(boolean set) {
		capField.setDisable(!set);
		deleteBtn.setDisable(!set);
	}

}
