package applicationMain;

import java.util.ArrayList;

import entityClasses.Chart;
import entityClasses.Member;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ChartEditor {

	/*-*
	
	Attributes 
	
	*/
	
	private static int seatWidth = 40;
	private static int seatHeight = 50;
	
	private static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
	private static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
	
	// Chart to edit
	private static Chart chart;
	
	// GUI Elements
	
	private static ToolBar toolBar = new ToolBar();
	
	private static ArrayList<StackPane> members;
	private static Pane memberPane;
	private static Pane chartPane;
	
	// Application Elements
	
	private static ChartEditor chartEditorPage;
		
	private static Stage stage;
		
	private static BorderPane rootPane;
	private static Scene editScene;
	
	/*-*
	
	Constructors 
	
	*/
	
	public static void editChart(Stage inStage, Chart inChart) {
		// Establish reference to GUI
		stage = inStage;
		
		chart = inChart;
		
		if (chartEditorPage == null) {
			chartEditorPage = new ChartEditor();
		}
		
		// Update dynamic elements
		memberPane = new Pane();
		chartPane = new Pane();
		readChart(chart);
		
		rootPane.setCenter(chartPane);
		rootPane.setBottom(memberPane);
		
		// Display page
		stage.setMaximized(true);
		stage.setTitle("Seating Chart Editor");
		stage.setScene(editScene);
		stage.show();
	}
	
	private ChartEditor() {
		rootPane = new BorderPane();
		editScene = new Scene(rootPane, screenWidth, screenHeight);
		
		toolBar.getItems().add(new Button("New"));
		rootPane.setTop(toolBar);
		
		
		rootPane.getChildren().addAll();
		
	}
	
	private static void readChart(Chart chart) {
		ArrayList<Member> readMembers = chart.getMembers();	
		int rows = chart.getRows();
		int seatsPerRow = chart.getSeatsPerRow();
		
		// Unassigned members
		for (int i = 0; i < readMembers.size(); ++i) {
			Member current = readMembers.get(i);
			if (current.getX() == -1) {
				Rectangle newMember = new Rectangle(0, 0, seatWidth, seatHeight);
				Text name = new Text(current.getName());
				StackPane completedRectangle = new StackPane();
				completedRectangle.getChildren().addAll(newMember, name);
				members.add(completedRectangle);
			}
		}
		
		// Base Chart
		for (int i = 0; i < rows; ++i) {
			int baseX = 100;
			int baseY = 
			for (int j = 0; j < seatsPerRow; ++j) {
				Rectangle newSeat = new Rectangle(0, 0, seatWidth, seatHeight);
				chartPane.getChildren().add(newSeat);
			}
		}
		
	}
	
}
