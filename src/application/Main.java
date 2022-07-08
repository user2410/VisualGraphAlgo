package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	
	Stage mainWindow;
	
	@Override
	public void start(Stage primaryStage) {
		try {
//			mainWindow = primaryStage;
			
			Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
			Scene scene = new Scene(root, 1000, 600);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("JavaFx demo");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
