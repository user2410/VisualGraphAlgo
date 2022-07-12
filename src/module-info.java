module VisualGraphAlgo {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.base, javafx.graphics, javafx.fxml;
	opens application.ui to javafx.base;
	opens application.graph to javafx.base;
}
