package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import application.algorithm.Algorithm;
import application.context.Context;
import application.context.state.Step;
import application.context.state.factory.DinicStateMaker;
import application.context.state.factory.EKStateMaker;
import application.context.state.factory.FFStateMaker;
import application.ui.GGraph;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController implements Initializable {
	
	/*
	 * FXML objects
	 * */
	
	@FXML
	Pane mainDrawPane;			
	@FXML
	SplitPane playbackPane;		 
	@FXML
	Button playpauseBtn;
	@FXML
	Slider progressSlider;
	@FXML
	Slider speedSlider;
	
	@FXML
	TreeTableView<String> graphTree;
	
	@FXML
	TextField srcNodeInput;
	@FXML
	TextField sinkNodeInput;
	@FXML
	HBox algoSelector;	
	@FXML
	VBox algoStateArea;
	@FXML
	Text stateLine1;
	@FXML
	Text stateLine2;
	@FXML
	TreeView<String> algoStepTree;
	
	@FXML
	Button goBtn;
	
	
	
	/*
	 * Program variables
	 * */
	
	boolean isExploring;	
	GGraph tGraph;
	Algorithm.Type algoType;
	Context context;
	
	// Root nodes of treeview of each algorithm
	TreeItem<String> ffRootNode = null;
	TreeItem<String> ekRootNode = null;
	TreeItem<String> dRootNode = null;

	
	/*
	 * Methods for initialization
	 * */
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		isExploring = false;
		tGraph = new GGraph(mainDrawPane);
		context = new Context();

		initDrawPane();
		initSlider(progressSlider);
		initSlider(speedSlider);
		initAlgoArea();
	}
	
	private void initDrawPane() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (!isExploring) {
					int x = (int) e.getX();
					int y = (int) e.getY();
					tGraph.processClick(x, y);
				}
			}
		};
		mainDrawPane.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
	}

	private void initSlider(Slider slider){
		slider.setMin(0);
		slider.setMax(100);
//		slider.setMajorTickUnit(0.5);
//      slider.setMinorTickCount(0);
//      slider.setShowTickMarks(true);
//      slider.setShowTickLabels(true);
        slider.setMinHeight(Slider.USE_PREF_SIZE);

        slider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				System.out.println(slider.getValue());
			}
        });
	}
	
	private void initAlgoArea() {
		srcNodeInput.textProperty().addListener((arg0, oldVal, newVal)->{
			if(validateNodeInput(srcNodeInput.getText()) == Integer.MAX_VALUE) {
				srcNodeInput.setText("");
			}
		});
		sinkNodeInput.textProperty().addListener((arg0, oldVal, newVal)->{
			if(validateNodeInput(sinkNodeInput.getText()) == Integer.MAX_VALUE) {
				sinkNodeInput.setText("");
			}
		});
		
		ffRootNode = initAlgoSteps(Algorithm.Type.FF);
		ekRootNode = initAlgoSteps(Algorithm.Type.EK);
		dRootNode = initAlgoSteps(Algorithm.Type.DINIC);
		
		ObservableList<Node> list = algoSelector.getChildren();
		ToggleGroup group = new ToggleGroup();
		
		list.forEach(n -> {
			RadioButton r = (RadioButton)n;
			
			r.setOnAction(e->{
				// System.out.println(r.getText());
				switch(r.getText()) {
				case "Ford-Fulkerson":
					algoType = Algorithm.Type.FF;
					algoStepTree.setRoot(ffRootNode);
					break;
				case "Edmond-Karp":
					algoType = Algorithm.Type.EK;
					algoStepTree.setRoot(ekRootNode);
					break;
				case "Dinic":
					algoType = Algorithm.Type.DINIC;
					algoStepTree.setRoot(dRootNode);
					break;
				}
				algoStepTree.setShowRoot(false);
			});
			
			r.setToggleGroup(group);
		});
		
		((RadioButton)list.get(0)).setSelected(true);
		algoStepTree.setRoot(ffRootNode);
		algoStepTree.setShowRoot(false);
		algoType = Algorithm.Type.FF;
	}
	
	private int validateNodeInput(String input) {
		try {
			int c = Integer.parseInt(input);
			if(c>=0 && c<tGraph.getNodeCount())
				return c;
		}catch(NumberFormatException e) {			
			return Integer.MAX_VALUE;
		}
		return Integer.MAX_VALUE;
	}
	
	private TreeItem<String> initAlgoSteps(Algorithm.Type type) {
		ArrayList<Step> steps = null;
		switch(type) {
		case FF:
			steps = FFStateMaker.steps;
			break;
		case EK:
			steps = EKStateMaker.steps;
			break;
		case DINIC:
			steps = DinicStateMaker.steps;
			break;
		}
		
		TreeItem<String> root = new TreeItem<>();
		steps.forEach(s->{
			int ident = s.getIdent();
			if(ident!=-1) {
				root.getChildren().add(new TreeItem<>("\t".repeat(ident) + s.getStepText()));
			}
		});
		return root;
	}
	
	
	/*
	 * Menubar handler
	 * */
	
	@FXML
	private void quitMenuClicked(ActionEvent event) {
	
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
	
	@FXML
	private void saveMenuClicked(ActionEvent event) {		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Do you want to save ?");
		alert.show();		
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
	
	
	/*
	 * FXML Handlers
	 * */
	

	@FXML
	private void goBtnClicked() {
		if(isExploring) {
			isExploring = false;
			playbackPane.setDisable(true);
			graphTree.setDisable(false);
			algoSelector.setDisable(false);
			srcNodeInput.setDisable(false);
			sinkNodeInput.setDisable(false);
			
			context.terminate();
			
			goBtn.setText("Go");
		}else {
			int srcNode = validateNodeInput(srcNodeInput.getText());
			int sinkNode = validateNodeInput(sinkNodeInput.getText());
			if(srcNode!=Integer.MAX_VALUE && sinkNode != Integer.MAX_VALUE) {
				isExploring = true;
				playbackPane.setDisable(false);
				graphTree.setDisable(true);
				algoSelector.setDisable(true);
				srcNodeInput.setDisable(true);
				sinkNodeInput.setDisable(true);
				Algorithm algo = Algorithm.makeAlgo(context, tGraph, srcNode, sinkNode, algoType);
				context.setAlgo(algo);
				context.exploreAlgo();
				new Thread() {
					@Override
					public void run() {
						setName("Algo_playback_Thread");
						context.play();
					}
				}.start();
				goBtn.setText("Stop");
			}
		}
	}
	
	@FXML
	private void playpauseBtnHandler() {
		if(isExploring) {
			if(context.isPlaying()) {
				context.pause();
				playpauseBtn.setText("|>");
			}else {
				context.resume();
				playpauseBtn.setText("||");				
			}
		}
	}
	
	@FXML
	private void rewindBtnHandler() {
		if(isExploring && !context.isPlaying()) {
			context.prev();
		}
	}
	
	@FXML
	private void forwardBtnHandler() {
		if(isExploring && !context.isPlaying()) {
			context.next();
		}
	}
	
}
