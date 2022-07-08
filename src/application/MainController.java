package application;

import java.io.IOException;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


import application.ui.Session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {
	
	Session s;
//	private Stage stage;
//	private Scene scene;
//	private Parent root;
//	
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
	
	
	String fileName_save;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		s = new Session(mainDrawPane, playbackPane, graphTree, algoSelector, goBtn);
	}
	
	@FXML
	private void quitMenuClicked(ActionEvent event ) {
	
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Do you quit application ?");
		alert.setContentText("Choose your option");
		
		ButtonType buttonTypeQuit = new ButtonType("Quit",ButtonData.YES);
//		ButtonType buttonTypeCancel = new ButtonType("Cancel",ButtonData.CANCEL_CLOSE);
		
		
//		alert.getButtonTypes().setAll(buttonTypeQuit,buttonTypeCancel);
		alert.getButtonTypes().set(0,buttonTypeQuit);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == buttonTypeQuit)
			System.exit(0);
//		else if(result.get() == buttonTypeCancel)	
//			alert.close();
		alert.show();
		
	}
	
	@FXML
	private void saveMenuClicked(ActionEvent event)    {
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Do you save ?");
		alert.show();
		
		
		
		
//		 Parent root = FXMLLoader.load(getClass().getResource("save.fxml"));
//		 
//		 Scene scene = new Scene(root);
//		 Stage  stage = new Stage();
//		 Stage stage = (Stage)((MenuItem)event.getSource()).getScene().getWindow();
//		 stage.setScene(scene);
//		 stage.show();
		
	}
	
	@FXML
	private void contactMenuClicked(ActionEvent event) {
		Stage window = new Stage();
		Text text  = new Text("Mọi thắc mắc về dự án xin liên hệ với chúng tôi về địa chỉ sau đây:");
		Text text1 = new Text("Phạm Lê Danh Chính : chinh.pld2019xxxx@sis.hust.edu.vn" );
		Text text2 = new Text("Nguyễn Kim Bảo     : bao.nk194486@sis.hust.edu.vn" );
		Text text3 = new Text("Nguyễn Thị Hoài Thu: thu.nth2019xxxx@sis.hust.edu.vn");
		text.setFont(new Font(20));
		text.setFill(Color.RED);
		text1.setFont(new Font(16));
		text2.setFont(new Font(16));
		text3.setFont(new Font(16));
		Scene scene1;
		VBox layout1 = new VBox();
		
		layout1.getChildren().addAll(text,text1,text2,text3);
		scene1 = new Scene(layout1,600,200);
		window.setScene(scene1);
		window.show();
		}
}
