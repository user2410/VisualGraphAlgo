package application.ui;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class Session {

	Pane drawPane;
	// Button goBtn;
	private boolean isExploring;

	public GGraph graph;

	public Session(Pane drawPane, SplitPane playbackPane, TreeView<String> graphTree, HBox algoSelector, Button goBtn) {
		this.drawPane = drawPane;
		graph = new GGraph(this);
		isExploring = false;
		initDrawPane();
		initAlgoSelector(algoSelector);
		initGoBtn(goBtn, graphTree, algoSelector, playbackPane);
	}

	private void initDrawPane() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
//				System.out.printf("getSceneX: %f, getSceneY: %f\n", e.getSceneX(), e.getSceneY());
//				System.out.printf("getScreenX: %f, getScreenY: %f\n", e.getScreenX(), e.getScreenY());
//				System.out.printf("getX: %f, getY: %f\n", e.getX(), e.getY());
				if (!isExploring) {
					int x = (int) e.getX();
					int y = (int) e.getY();
					graph.processClick(x, y);
				}
			}
		};
		drawPane.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
	}

	private void initGoBtn(Button goBtn, TreeView<String> graphTree, HBox algoSelector, SplitPane playbackPane) {
		goBtn.setOnAction(e -> {
			if(!isExploring) {
				isExploring = true;
				playbackPane.setDisable(false);
				graphTree.setDisable(true);
				algoSelector.setDisable(true);
				goBtn.setText("Stop");
			}else {
				isExploring = false;
				playbackPane.setDisable(true);
				graphTree.setDisable(false);
				algoSelector.setDisable(false);
				goBtn.setText("Go");				
			}
		});
	}
	
	private void initAlgoSelector(HBox algoSelector) {
		ObservableList<Node> list = algoSelector.getChildren();
		ToggleGroup group = new ToggleGroup();
		for(Node n : list) {
			RadioButton r = (RadioButton)n;
			r.setOnAction(e->{
				// modify algo step zone
			});
			r.setToggleGroup(group);
		}
	}
}
