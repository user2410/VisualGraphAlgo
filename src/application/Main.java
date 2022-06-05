package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
// import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
// import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Visual Graph Algorithms");
			
			Button b = new Button();
			b.setText("Click me");
			
			StackPane layout = new StackPane();
			layout.getChildren().add(b);
			
			Scene scence = new Scene(layout, 300, 250);
			primaryStage.setScene(scence);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
