package application;

import java.net.URL;
import java.util.ResourceBundle;

import application.ui.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class MainController implements Initializable {
	
	Session s;
	
	@FXML
	Pane mainDrawPane;
	@FXML
	SplitPane playbackPane;
	@FXML
	TreeView<String> graphTree;
	@FXML
	HBox algoSelector;
	@FXML
	Button goBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		s = new Session(mainDrawPane, playbackPane, graphTree, algoSelector, goBtn);
	}

}
