package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.algorithm.Algorithm;
import application.context.Context;
import application.context.state.AlgoState;
import application.context.state.Step;
import application.context.state.factory.DinicStateMaker;
import application.context.state.factory.EKStateMaker;
import application.context.state.factory.FFStateMaker;
import application.graph.Edge;
import application.graph.Graph;
import application.ui.GEdge;
import application.ui.GGraph;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {
	
	/*
	 * FXML objects
	 * */
	
	@FXML
	Menu openModelsMenu;
	
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
	TableView<GEdge> graphTable;
	
	@FXML
	TextField srcNodeInput;
	@FXML
	TextField sinkNodeInput;
	@FXML
	HBox algoSelector;	
	@FXML
	VBox algoStateArea;
	@FXML
	Text algoStateLine1;
	@FXML
	Text algoStateLine2;
	@FXML
	TreeView<String> algoStepTree;
	
	@FXML
	Button goBtn;
	
	@FXML
	Label leftStatusLine;
	@FXML
	Label rightStatusLine;
	
	
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
	 * Initialization
	 * */
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		isExploring = false;
		tGraph = new GGraph(mainDrawPane, graphTable);
		context = new Context(this);

		// init openModelsMenu
		File[] fileLs= new File("./data/models/").listFiles();
		for(File file : fileLs) {
			String[] tokens = file.getName().split("[.]", -1);
			if(!file.isDirectory() && tokens[tokens.length-1].equals("graph")) {
				System.out.println("File: " + file.getName());
				MenuItem model = new MenuItem(file.getName());
				model.setOnAction(e->{
					if(isExploring) return;
					try {
						Graph g = Graph.deserialize("data/models/"+file.getName());
						tGraph.buildFromGraph(g);
    				}catch(Exception ex) {
    					// warn user
    					ex.printStackTrace();
    				}
				});
				openModelsMenu.getItems().add(model);
			}
		}
		
		// init draw pane
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
		
		// init sliders
		speedSlider.setMin(0.25);
		speedSlider.setMax(2.0);
		speedSlider.setMinorTickCount(1);
		speedSlider.setMajorTickUnit(0.5);
		speedSlider.setShowTickMarks(true);
		speedSlider.setShowTickLabels(true);

		speedSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				double value = speedSlider.getValue();
				context.setSpeed(value);
				rightStatusLine.setText("Speed: " + String.format("%.2f", value) + 'x');
			}
		});
		
		progressSlider.setMin(0);
		progressSlider.setMinorTickCount(0);
		progressSlider.setMajorTickUnit(1);
		progressSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				context.setCurrentState((int)progressSlider.getValue());
			}
		});
		
		initAlgoArea();
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
	private void clearMenuClicked() {
		if(isExploring) return;
		tGraph.clear();
	}
	
	@FXML
	private void quitMenuClicked() {
		Main.closeProgram();
	}
	
	@FXML
	private void aboutMenuClicked(ActionEvent event) {
		try {
    		Stage window = new Stage();
    		Parent root = new FXMLLoader(getClass().getResource("about.fxml")).load();
    		Scene scene = new Scene(root, 600, 400);
    		Button okBtn = (Button)scene.lookup("#okBtn");
    		okBtn.setOnAction(e->{
    			window.close();
    		});
    		window.setScene(scene);
    		
    		window.initModality(Modality.APPLICATION_MODAL);
    		window.setTitle("About this app");
    		window.show();
		}catch(Exception e) {}
	}
	
	
	/*
	 * FXML Handlers
	 * */
	

	@FXML
	private void goBtnClicked() {
		if(isExploring) {
			isExploring = false;
			playbackPane.setDisable(true);
			// graphTable.setDisable(false);
			for(Edge e : tGraph.edges) {((GEdge)e).setModifying(true);}
			algoSelector.setDisable(false);
			srcNodeInput.setDisable(false);
			sinkNodeInput.setDisable(false);
			
			leftStatusLine.setText("Edit mode");
			
			context.terminate();
			
			for(Edge e : tGraph.edges) {
				System.out.println("Resetting edges");
				((GEdge)e).updateLabel(Long.valueOf(e.getCapacity()).toString());
				((GEdge)e).setSelected(false);
			}
			
			goBtn.setText("Go");
		}else {
			int srcNode = validateNodeInput(srcNodeInput.getText());
			int sinkNode = validateNodeInput(sinkNodeInput.getText());
			if(srcNode!=Integer.MAX_VALUE && sinkNode != Integer.MAX_VALUE) {
				isExploring = true;
				playbackPane.setDisable(false);
				// graphTable.setDisable(true);
				for(Edge e : tGraph.edges) {((GEdge)e).setModifying(false);}
				algoSelector.setDisable(true);
				srcNodeInput.setDisable(true);
				sinkNodeInput.setDisable(true);
				playpauseBtn.setText("||");
				speedSlider.setValue(1.0);
				
				Algorithm algo = Algorithm.makeAlgo(context, tGraph, srcNode, sinkNode, algoType);
				context.setAlgo(algo);
				Thread t = new Thread() {
					@Override
					public void run() {
						context.exploreAlgo();						
					}
				};
				t.start();
				leftStatusLine.setText("Solving the graph");
				while(t.getState() != Thread.State.TERMINATED);
				leftStatusLine.setText("Play mode");
				
				rightStatusLine.setText("Speed: 1.0x");
				
				progressSlider.setMax(context.getStateCount()-1);
				progressSlider.setShowTickMarks(true);
				
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
				System.out.println("stop");
				context.pause();
				playpauseBtn.setText("|>");
			}else {
				System.out.println("resume");
				if(context.getCurrentStateNum()+1 == context.getStateCount()) context.setCurrentState(0);
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

	public void handleDeleteKeyPressed() {
		if(isExploring) return;
		try {
			if(!tGraph.deleteNode())
				tGraph.deleteEdge();
		}catch(Exception e) {
			// warn user
			e.printStackTrace();
		}
	}
	
	/*
	 * Observer reactions
	 * */
	
	public void reactToContext(AlgoState as) {
		if(as==null) {
			context.pause();
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {playpauseBtn.setText("|>");}
			});
			return;
		}
		
		// update draw pane
		long[][] rGraph = as.getrGraph();
		ArrayList<Edge> path = as.getPath();
		for(Edge e : tGraph.edges){
			((GEdge)e).updateLabel(Long.valueOf(rGraph[e.getFrom()][e.getTo()]).toString()
					+ '|' + Long.valueOf(rGraph[e.getTo()][e.getFrom()]).toString());
			if(path!=null) {
				boolean selected = path.contains(e);
				if(path.contains(new Edge(e.getTo(), e.getFrom()))){
					selected = true;
				}
				((GEdge)e).setSelected(selected);
			}
		};
		
		// update progressSlider
		progressSlider.setValue(context.getCurrentStateNum());
		
		// update algo Area
		algoStateLine1.setText(as.getStateText1());
		algoStateLine2.setText(as.getStateText2());
		MultipleSelectionModel<TreeItem<String>> msm = algoStepTree.getSelectionModel();
		int row = as.getStep().getStepnum();
		if(row!=-1) msm.select(row);
		else
			msm.clearSelection();
	}
	
}
