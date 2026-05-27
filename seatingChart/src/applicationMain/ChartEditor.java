package applicationMain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import entityClasses.Chart;
import entityClasses.Member;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

public class ChartEditor {

	/*-*
	
	Attributes 
	
	*/
	
	private static FileChooser fileChooser;
	private static final String S = System.getProperty("file.separator");
	
	private static double seatWidth = 80;
	private static double seatHeight = 100;
	
	private static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
	private static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight() - 20; // To account for scroll bar
	
	private static Pair<StackPane, Member> selectedMember = null;
	
	// Chart to edit
	private static Chart chart;
	
	// GUI Elements
	
	private static ToolBar toolBar = new ToolBar();
	private static Button newButton = new Button("New");
	private static Button saveButton = new Button("Save");
	private static Button loadButton = new Button("Load");
	
	private static ArrayList<StackPane> memberSeats;
	private static ScrollPane memberScroll;
	private static HBox memberPane;
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
		
		chartPane = new Pane();
		memberSeats = new ArrayList<StackPane>();
		memberPane = new HBox();

		if (chartEditorPage == null) {
			chartEditorPage = new ChartEditor();
		}
		
		// Update dynamic elements
		memberPane.setPrefHeight(screenHeight / 6);
		readChart(chart);
		
		memberScroll = new ScrollPane(memberPane);
		memberScroll.setFitToHeight(true);
		memberScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		memberScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		
		rootPane.setCenter(chartPane);
		rootPane.setBottom(memberScroll);
		
		// Display page
		stage.setMaximized(true);
		stage.setTitle("Seating Chart Editor");
		stage.setScene(editScene);
		stage.show();
	}
	
	private ChartEditor() {
		rootPane = new BorderPane();
		editScene = new Scene(rootPane, screenWidth, screenHeight);
		editScene.setOnDragDetected((_) -> {
			editScene.startFullDrag();
		});
		
		
		fileChooser = new FileChooser();
		fileChooser.setTitle("Select Save File/Folder");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+S+"Downloads"));
		
		newButton.setOnAction((_) -> {
			newChart.NewChartView.createChart(stage);
		});
		saveButton.setOnAction((_) -> {
			File selectedFile = fileChooser.showSaveDialog(stage);
			fileChooser.setInitialFileName("chart.txt");
			if (selectedFile != null) {
				System.out.println("Selected File: " + selectedFile.getAbsolutePath());
				try (FileWriter writer = new FileWriter(selectedFile.getAbsolutePath())) {
					selectedFile.createNewFile();
					writer.write(chart.getRows() + "," + chart.getTotalSeats() + "\n");
					ArrayList<Member> members = chart.getMembers();
					for (int i = 0; i < members.size(); ++i) {
						Member current = members.get(i);
						writer.write(current.getName() + "," + current.getPart() + ",");
						writer.write(current.getX() + "," + current.getY() + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		toolBar.getItems().addAll(newButton, saveButton, loadButton);
		rootPane.setTop(toolBar);
		
		rootPane.getChildren().addAll();
	}
	
	private static void readChart(Chart chart) {
		ArrayList<Member> readMembers = chart.getMembers();	
		int rows = chart.getRows();
		int seatsPerRow = chart.getSeatsPerRow();
		
		double seatBaseX = screenWidth / 2 - seatWidth / 2 - (seatsPerRow - 1) * (seatWidth / 2);
		double seatBaseY = screenHeight / 3 - seatHeight / 2 + (rows - 1) * seatHeight;
		
		// Unassigned members
		for (int i = 0; i < readMembers.size(); ++i) {
			Member current = readMembers.get(i);
			if (current.getX() == -1) {
				Rectangle newMember = new Rectangle(0, 0, seatWidth, seatHeight);
				newMember.setFill(Color.LIGHTBLUE);
				newMember.setStroke(Color.BLACK);
				Text name = new Text(current.getName());
				name.setWrappingWidth(seatWidth);
				name.setTextAlignment(TextAlignment.CENTER);
				StackPane completedRectangle = new StackPane();
				completedRectangle.getChildren().addAll(newMember, name);
				completedRectangle.setOnMousePressed(event -> {
					memberPane.getChildren().remove(completedRectangle);
					selectedMember = new Pair<>(completedRectangle, current);
					completedRectangle.setLayoutX(event.getSceneX() - 1.1*seatWidth - 1);
					completedRectangle.setLayoutY(event.getSceneY() - 1.5*seatHeight - 1);
					completedRectangle.setCursor(Cursor.MOVE);
					rootPane.getChildren().add(completedRectangle);
				});
				completedRectangle.setOnMouseDragged(event -> {
					completedRectangle.setLayoutX(event.getSceneX() - 1.1*seatWidth - 1);
					completedRectangle.setLayoutY(event.getSceneY() - 1.5*seatHeight - 1);
				});
				completedRectangle.setOnMouseReleased((_) -> {
					rootPane.getChildren().remove(completedRectangle);
					selectedMember = null;
					memberPane.getChildren().add(completedRectangle);
					completedRectangle.setCursor(Cursor.DEFAULT);
				});
				HBox.setMargin(completedRectangle, new Insets(0, 10, 0, 10));
				memberPane.getChildren().add(completedRectangle);
				memberSeats.add(completedRectangle);
			} else {
				Rectangle newMember = new Rectangle(0, 0, seatWidth, seatHeight);
				newMember.setFill(Color.LIGHTBLUE);
				newMember.setStroke(Color.BLACK);
				Text name = new Text(current.getName());
				name.setWrappingWidth(seatWidth);
				name.setTextAlignment(TextAlignment.CENTER);
				StackPane completedRectangle = new StackPane();
				completedRectangle.getChildren().addAll(newMember, name);
				HBox.setMargin(completedRectangle, new Insets(0, 10, 0, 10));
				chartPane.getChildren().add(completedRectangle);
				memberSeats.add(completedRectangle);
			}
		}
		
		// Base Chart
		for (int i = 0; i < rows; ++i) {
			double seatX = seatBaseX - ((i) % 2) * ((seatWidth+10)/2);
			int y = i;
			for (int j = 0; j < seatsPerRow + i % 2; ++j) {
				int x = j;
				Rectangle newSeat = new Rectangle(seatX + (seatWidth+10)*j, seatBaseY - (seatHeight+10)*i, seatWidth, seatHeight);
				newSeat.setFill(Color.LIGHTBLUE);
				newSeat.setStroke(Color.BLACK);
				newSeat.setOnMouseDragEntered((_) -> {
					if (selectedMember == null) {						
						newSeat.setFill(Color.YELLOW);
					} else {
						newSeat.setFill(Color.RED);
					}
				});
				newSeat.setOnMouseDragExited((_) -> {
					newSeat.setFill(Color.LIGHTBLUE);
				});
				newSeat.setOnMouseDragReleased((_) -> {
					newSeat.setFill(Color.LIGHTBLUE);
					selectedMember.getValue().setCoordinates(x, y);
					selectedMember.getKey().setLayoutX(seatX + (seatWidth+10)*x);
					selectedMember.getKey().setLayoutY(seatBaseY - (seatHeight+10)*y);
					rootPane.getChildren().remove(selectedMember.getKey());
					chartPane.getChildren().add(selectedMember.getKey());
				});
				chartPane.getChildren().add(newSeat);
			}
		}
		
	}
	
}
