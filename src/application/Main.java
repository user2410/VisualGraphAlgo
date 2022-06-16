package application;

// import application.graph.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));
		Scene scene = new Scene(root, 800, 450);
		
		Button accBtn = (Button)scene.lookup("#account");
		accBtn.setOnAction(e->{
			System.out.println("Account button clicked");
		});
		
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
//		Graph g = new Graph();
//		g.addNode(1, 1);
//		g.addNode(2, 2);
//		g.addNode(3, 2);
//		g.addNode(2, 0);
//		g.addNode(3, 1);
//		g.addNode(4, 1);
//		
//		try {
//			g.addEdge(0, 1, 5);
//			g.addEdge(0, 3, 15);
//			g.addEdge(1, 2, 10);
//			g.addEdge(1, 4, 5);
//			// g.addEdge(1, 8, 5);
//			g.addEdge(2, 3, 3);
//			g.addEdge(2, 5, 20);
//			g.addEdge(3, 1, 5);
//			g.addEdge(3, 4, 5);
//			g.addEdge(4, 2, 2);
//			g.addEdge(4, 5, 5);
//		}catch(Exception e) {
//			System.err.println(e.getMessage());
//		}

//		System.out.println(g);
	}
}
