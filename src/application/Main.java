package application;

import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main extends Application{
	
	public static Stage mainWindow;
	public static Scene mainScene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			mainWindow = primaryStage;
			mainWindow.setOnCloseRequest(e -> {
				e.consume();
				closeProgram();
			});
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("application.fxml"));
			Parent root = fxmlLoader.load();
			mainScene = new Scene(root, 1000, 600);
			
			mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			MainController mc = fxmlLoader.getController();
			mainScene.setOnKeyPressed(e->{
				if(e.getCode() == KeyCode.DELETE)
					mc.handleDeleteKeyPressed();
			});
			
			mainWindow.setScene(mainScene);
			mainWindow.setTitle("VisualGraphAlgo");
			mainWindow.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void closeProgram() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Do you want quit application ?");
		alert.setContentText("Tip: You might want to save changes before exiting");
		
		ButtonType buttonTypeQuit = new ButtonType("Quit", ButtonData.YES);

		alert.getButtonTypes().set(0,buttonTypeQuit);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == buttonTypeQuit) {			
			Main.mainWindow.close();
		}
	}
}
