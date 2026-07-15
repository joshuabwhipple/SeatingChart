package applicationMain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import javax.imageio.ImageIO;
import entityClasses.Chart;
import entityClasses.Member;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class ChartEditor {

	/*-*
	
	Attributes 
	
	*/
	
	private static FileChooser fileChooser;
	
	private static double seatWidth = 80;
	private static double seatHeight = 100;
	
	private static double seatBaseX;
	private static double seatBaseY;
	
	private static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
	private static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight() - 20; // To account for scroll bar
	
	private static Pair<StackPane, Member> selectedMember = null;
	
	// Used for presenting save alerts
	private static boolean edited;
	private static Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
	
	// Chart to edit
	private static Chart chart;
	
	// GUI Elements
	
	private static ToolBar toolBar = new ToolBar();
	private static Button newButton = new Button("New");
	private static Button saveButton = new Button("Save");
	private static Button saveAsButton = new Button("Save As");
	private static Button loadButton = new Button("Open");
	private static Button editNameButton = new Button("Edit Name");
	private static Button editSeatsButton = new Button("Edit Seats");
	private static Button editMembersButton = new Button("Edit Members");
	private static Button editTypesButton = new Button("Edit Types");
	private static Button exportButton = new Button("Export");
	
	// Placed above the exported files
	private static Label chartName = new Label();
	
	private static Dialog<String> editNameDialog = new Dialog<>();
	private static Dialog<Pair<String, String>> editSeatsDialog = new Dialog<>();
	private static Dialog<String> editMembersDialog = new Dialog<>();
	private static Dialog<String> editTypesDialog = new Dialog<>();
	
	private static ArrayList<StackPane> memberSeats;
	private static ScrollPane memberScroll;
	private static HBox memberPane;
	private static ScrollPane chartScroll;
	private static Pane chartPane;

	private static ImageView conductor = new ImageView();
	
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
		editNameDialog = new Dialog<>();
		editSeatsDialog = new Dialog<>();
		editMembersDialog = new Dialog<>();
		editTypesDialog = new Dialog<>();
		toolBar = new ToolBar();
		
		if (chartEditorPage == null) {
			chartEditorPage = new ChartEditor();
		}
				
		// Update dynamic elements
		
		seatBaseX = Math.max(65, screenWidth / 2 - seatWidth / 2 - (chart.getSeatsPerRow() - 1) * (seatWidth + 10) / 2);
		seatBaseY = Math.max(20, 2 * screenHeight / 5 - seatHeight / 2 - (chart.getRows() - 1) * (seatHeight + 10) / 2);
		
		chartName.setText(chart.getName());
		
		// Calculate text dimensions
		Text helper = new Text(chartName.getText());
		helper.setFont(chartName.getFont());
		double nameWidth = helper.getLayoutBounds().getWidth();
		double nameHeight = helper.getLayoutBounds().getHeight();
		
		int chartNameBaseX = (int) seatBaseX;
		int chartWidth = 90*(chart.getSeatsPerRow()) + 40;
		if (chart.getRows() > 1) {
			chartNameBaseX -= (seatWidth+10)/2;
			chartWidth = 90*(chart.getSeatsPerRow() + 1) + 40;
		}
		
		chartName.setLayoutX(chartNameBaseX - 20 + chartWidth / 2 - nameWidth / 2);
		chartName.setLayoutY(seatBaseY - 20 - nameHeight);
		chartPane.getChildren().add(chartName);
		
		
		saveButton.setOnAction((_) -> {
			if (chart.getSaveLocation() == null) {
				saveAs();
			} else {
				save();
			}
			if (stage.getTitle().indexOf("*") != -1) {
				stage.setTitle("Seating Chart Editor - " + chart.getName());
			}
		});
		saveAsButton.setOnAction((_) -> {
			saveAs();
			if (stage.getTitle().indexOf("*") != -1) {
				stage.setTitle("Seating Chart Editor - " + chart.getName());
			}
		});
		loadButton.setOnAction((_) -> {
			if (edited) {
				saveAlert.showAndWait().ifPresent(response -> {
					if (response == ButtonType.OK) {
						load();
					} 
				});
			} else {
				load();
			}
		});
		
		saveAlert.setTitle("Unsaved Changes Confirmation");
		saveAlert.setHeaderText("Are you sure you want to exit this chart?");
		saveAlert.setContentText("Any unsaved changes will be lost.");
		
		ButtonType confirmButtonType = new ButtonType("Done", ButtonData.OK_DONE);
		
		editNameDialog.setTitle("Edit Name");
		editNameDialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField inName = new TextField();
		inName.setText(chart.getName());
		
		grid.add(new Label("Name"), 0, 0);
		grid.add(inName, 1, 0);
		
		editNameDialog.getDialogPane().setContent(grid);
		
		editNameDialog.setResultConverter(dialogButton -> {
			if (dialogButton == confirmButtonType) {
				return inName.getText();
			}
			return null;
		});
		
		editNameButton.setOnAction((_) -> {
			Optional<String> result = editNameDialog.showAndWait();
			if (result.isPresent()) {
				String newName = result.get();
				chart.setName(newName);
				edited = true;
				ChartEditor.editChart(stage, chart);
			}
		});
		
		editSeatsDialog.setTitle("Edit Seats");
		editSeatsDialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
		
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField inRows = new TextField();
		inRows.setText(chart.getRows() + "");
		TextField inSeats = new TextField();
		inSeats.setText(chart.getTotalSeats() + "");
		grid.add(new Label("Rows"), 0, 0);
		grid.add(inRows, 1, 0);
		grid.add(new Label("Total Seats"), 0, 1);
		grid.add(inSeats, 1, 1);
		editSeatsDialog.getDialogPane().setContent(grid);
		
		editSeatsDialog.setResultConverter(dialogButton -> {
			if (dialogButton == confirmButtonType) {
				return new Pair<>(inRows.getText(), inSeats.getText());
			}
			return null;
		});
		
		editSeatsButton.setOnAction((_) -> {
			Optional<Pair<String, String>> result = editSeatsDialog.showAndWait();
			if (result.isPresent()) {
				chart.setRows(result.get().getKey());
				chart.setTotalSeats(result.get().getValue());
				if (!edited) stage.setTitle(stage.getTitle() + "*");
				edited = true;
				ChartEditor.editChart(stage, chart);
			}
		});
		
		editMembersDialog.setTitle("Edit Members");
		editMembersDialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
		
		TextArea newMembersList = new TextArea();
		newMembersList.setPromptText("First Name, Last Name, Type");
		ArrayList<Member> members = chart.getMembers();
		String oldMembersList = "";
		for (int i = 0; i < members.size(); ++i) {
			Member current = members.get(i);
			oldMembersList += current.getFirstName() + ", " + current.getLastName() + ", " + current.getType() + "\n";
		}
		newMembersList.setText(oldMembersList);
		editMembersDialog.getDialogPane().setContent(newMembersList);
		
		editMembersDialog.setResultConverter(dialogButton -> {
			if (dialogButton == confirmButtonType) {
				return newMembersList.getText();
			}
			return null;
		});
		
		editMembersButton.setOnAction((_) -> {
			Optional<String> result = editMembersDialog.showAndWait();
			// Compare new members list to old to see matching entries and save that member,
			// otherwise create new member to go into member pane
			if (result.isPresent()) {
				ArrayList<Member> newMembers = new ArrayList<>();
				Scanner resultReader = new Scanner(result.get());
				while (resultReader.hasNextLine()) {
					String data = resultReader.nextLine();
					int commaIndex1 = data.indexOf(',');
					int commaIndex2 = data.substring(commaIndex1 + 1).indexOf(',') + commaIndex1 + 1;
					String firstName = data.substring(0, commaIndex1).strip();
					System.out.println(firstName);
					String lastName = data.substring(commaIndex1 + 1, commaIndex2).strip();
					System.out.println(lastName);
					String type = data.substring(commaIndex2 + 1).strip();
					System.out.println(type);
					Member readMember = new Member(firstName, lastName, type);
					for (int i = 0; i < members.size(); ++i) {
						Member current = members.get(i);
						if (current.getName().equals(firstName + " " + lastName) && current.getType().equals(type)) {
							readMember.setCoordinates(current.getCoordinates());
							break;
						}
					}
					newMembers.add(readMember);
				}
				resultReader.close();
				chart.setMembers(newMembers);
				chart.setColors();
				if (!edited) stage.setTitle(stage.getTitle() + "*");
				edited = true;
				editChart(stage, chart);
			}
		});
		
		editTypesDialog.setTitle("Edit Types");
		editTypesDialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
		
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextArea newTypesList = new TextArea();
		newTypesList.setPromptText("Type Name, Color");
		ArrayList<Pair<String, Color>> types = chart.getTypes();
		String oldTypesList = "";
		for (int i = 0; i < types.size(); ++i) {
			Pair<String, Color> current = types.get(i);
			oldTypesList += current.getKey() + ", " + colorName(current.getValue()).get() + "\n";
		}
		newTypesList.setText(oldTypesList);
		grid.add(new Label("Types"), 0, 0);
		grid.add(newTypesList, 1, 0);
		editTypesDialog.getDialogPane().setContent(grid);
		
		editTypesDialog.setResultConverter(dialogButton -> {
			if (dialogButton == confirmButtonType) {
				return newTypesList.getText();
			}
			return null;
		});
		
		editTypesButton.setOnAction((_) -> {
			Optional<String> result = editTypesDialog.showAndWait();
			if (result.isPresent()) {
				chart.setTypes(result.get());
				editChart(stage, chart);
				if (!edited) stage.setTitle(stage.getTitle() + "*");
				edited = true;
			}
		});
		
		exportButton.setOnAction((_) -> {
			fileChooser.setTitle("Export Seating Chart");
			fileChooser.setInitialFileName(chart.getName());
			FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");
			FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPG Files (*.jpg)", "*.jpg");
			FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
			fileChooser.getExtensionFilters().addAll(pngFilter, jpgFilter, pdfFilter);
			
			File file = fileChooser.showSaveDialog(stage);
			
			int screenshotWidth;
			if (chart.getRows() == 1) {
				screenshotWidth = 90*chart.getSeatsPerRow() + 40;
			} else {
				screenshotWidth = 90*(chart.getSeatsPerRow() + 1) + 40;
			}
			int screenshotHeight = 110*chart.getRows() + 40 + (int) nameHeight;
			
			int startX = (int) seatBaseX;
			if (chart.getRows() > 1) startX -= (seatWidth+10)/2;
					
			Node content = chartScroll.getContent();
			
			if (file != null) {
				try {
					if (file.getName().toLowerCase().endsWith(".pdf")) {
						SnapshotParameters parameters = new SnapshotParameters();
						parameters.setViewport(new Rectangle2D(startX - 20, (int) seatBaseY - 40 - nameHeight, screenshotWidth, screenshotHeight));
						WritableImage screenshot = new WritableImage(screenshotWidth, screenshotHeight);
						content.snapshot(parameters, screenshot);
						
						BufferedImage bufferedScreenshot = SwingFXUtils.fromFXImage(screenshot, null);
						
						double scale = Math.min(792.0 / screenshotWidth, 612.0 / screenshotHeight);
						
						double docX = (792.0 - screenshotWidth * scale) / 2.0;
						double docY = (612.0 - screenshotHeight * scale) / 2.0;
						
						try (PDDocument document = new PDDocument()) {
							PDPage page = new PDPage(new PDRectangle(792, 612));
							document.addPage(page);
							
							PDImageXObject pdScreenshot = LosslessFactory.createFromImage(document, bufferedScreenshot);
							
							try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
								contentStream.drawImage(pdScreenshot, (float) docX, (float) docY, (float) (screenshotWidth * scale), (float) (screenshotHeight * scale));
							}
							document.save(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						WritableImage screenshot = new WritableImage(screenshotWidth, screenshotHeight);
						SnapshotParameters parameters = new SnapshotParameters();
						parameters.setViewport(new Rectangle2D(startX - 20, (int) seatBaseY - 30 - nameHeight, screenshotWidth, screenshotHeight));
						content.snapshot(parameters, screenshot);
						
						if (file.getName().toLowerCase().endsWith(".png")) { 
							ImageIO.write(SwingFXUtils.fromFXImage(screenshot, null), "png", file);
						} else if (file.getName().toLowerCase().endsWith(".jpg")) {
							
							BufferedImage bufferedScreenshot = SwingFXUtils.fromFXImage(screenshot, null);
							
							BufferedImage rgbScreenshot = new BufferedImage(bufferedScreenshot.getWidth(), bufferedScreenshot.getHeight(), BufferedImage.TYPE_INT_RGB);
							rgbScreenshot.createGraphics().drawImage(bufferedScreenshot, 0, 0, null);
							
							ImageIO.write(rgbScreenshot, "jpg", file);
						} 
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fileChooser.getExtensionFilters().removeAll(pngFilter, jpgFilter, pdfFilter);
		});
		
		memberPane.setPrefHeight(screenHeight / 6);
		readChart(chart);
		
		memberScroll = new ScrollPane(memberPane);
		memberScroll.setFitToHeight(true);
		memberScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		memberScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		
		chartScroll = new ScrollPane(chartPane);
		chartScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		chartScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
		toolBar.getItems().addAll(newButton, saveButton, saveAsButton, loadButton, new Separator(),
				editNameButton, editSeatsButton, editMembersButton, editTypesButton, new Separator(),
				exportButton, new Separator());
		
		rootPane.setTop(toolBar);
		rootPane.setCenter(chartScroll);
		rootPane.setBottom(memberScroll);
		
		stage.setOnCloseRequest(event -> {
			event.consume();
			
			if (edited) {
				saveAlert.showAndWait().ifPresent(response -> {
					if (response == ButtonType.OK) {
						Platform.exit();
					} 
				});
			} else {
				Platform.exit();
			}
		});
		
		// Display page
		stage.setMaximized(true);
		stage.setTitle("Seating Chart Editor - " + chart.getName());
		if (edited) stage.setTitle(stage.getTitle() + "*");
		stage.setScene(editScene);
		stage.show();
	}
	
	private ChartEditor() {
		rootPane = new BorderPane();
		editScene = new Scene(rootPane, screenWidth, screenHeight);
		editScene.setOnDragDetected((_) -> {
			editScene.startFullDrag();
		});
		
		conductor = new ImageView(new Image(getClass().getResourceAsStream("/Conductor Image.png")));
		conductor.setFitWidth(170);
		conductor.setPreserveRatio(true);
		
		fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		
		chartName.setFont(Font.font("Poppins", 50));
		chartName.setAlignment(Pos.CENTER);
		
		newButton.setOnAction((_) -> {
			if (edited) {
				saveAlert.showAndWait().ifPresent(response -> {
					if (response == ButtonType.OK) {
						newChart.NewChartView.createChart(stage);
					} 
				});
			} else {
				newChart.NewChartView.createChart(stage);
			}
		});
	}
	
	private static void readChart(Chart chart) {
		ArrayList<Member> readMembers = chart.getMembers();	
		int rows = chart.getRows();
		int seatsPerRow = chart.getSeatsPerRow();
		
		// Base Chart
		for (int i = 0; i < rows; ++i) {
			double seatX = seatBaseX - ((i+1) % 2) * ((seatWidth+10)/2);
			int y = i;
			for (int j = 0; j < seatsPerRow + (i+1) % 2; ++j) {
				int x = j;
				Rectangle newSeat = new Rectangle(seatX + (seatWidth+10)*j, seatBaseY + (seatHeight+10)*i, seatWidth, seatHeight);
				newSeat.setFill(Color.LIGHTBLUE);
				newSeat.setStroke(Color.BLACK);
				newSeat.setOnMouseDragEntered((_) -> {
					if (selectedMember != null) {						
						newSeat.setFill(Color.RED);
					}
				});
				newSeat.setOnMouseDragExited((_) -> {
					newSeat.setFill(Color.LIGHTBLUE);
				});
				newSeat.setOnMouseDragReleased((_) -> {
					if (selectedMember != null) {
						newSeat.setFill(Color.LIGHTBLUE);
						if (selectedMember.getValue().getX() != x || selectedMember.getValue().getY() != y) {
							if (!edited) stage.setTitle(stage.getTitle() + "*");
							edited = true;
						}
						selectedMember.getValue().setCoordinates(x, y);
						selectedMember.getKey().setLayoutX(seatX + (seatWidth+10)*x);
						selectedMember.getKey().setLayoutY(seatBaseY + (seatHeight+10)*y);
						rootPane.getChildren().remove(selectedMember.getKey());
						chartPane.getChildren().add(selectedMember.getKey());
						selectedMember = null;
					}
				});
				chartPane.getChildren().add(newSeat);
			}
		}
		conductor.setLayoutX(seatBaseX + (seatsPerRow/2.0)*(seatWidth+10) - 170 / 2);
		conductor.setLayoutY(seatBaseY + (seatHeight+10)*(rows) - 10);
		chartPane.getChildren().add(conductor);
		
		// Members
		for (int i = 0; i < readMembers.size(); ++i) {
			Member current = readMembers.get(i);
			if (current.getY() > chart.getRows() || current.getX() > chart.getSeatsPerRow() + (current.getY() % 2)) {
				current.setCoordinates(-1, -1);
			}
			if (current.getX() == -1) {
				Rectangle newMember = new Rectangle(0, 0, seatWidth, seatHeight);
				newMember.setFill(current.getColor());
				newMember.setStroke(Color.BLACK);
				Text name = new Text(current.getName() + "\n" + current.getType());
				name.setWrappingWidth(seatWidth);
				name.setTextAlignment(TextAlignment.CENTER);
				StackPane completedRectangle = new StackPane();
				completedRectangle.getChildren().addAll(newMember, name);
				completedRectangle.setOnMousePressed(event -> {
					System.out.println(event.getSceneX() + ", " + event.getSceneY());
					System.out.println((event.getSceneX() - seatWidth - 2) + ", " + (event.getSceneY() - seatHeight - 2));
					memberPane.getChildren().remove(completedRectangle);
					selectedMember = new Pair<>(completedRectangle, current);
					completedRectangle.setLayoutX(event.getSceneX() - seatWidth - 2);
					completedRectangle.setLayoutY(event.getSceneY() - seatHeight - 2);
					completedRectangle.setCursor(Cursor.MOVE);
					rootPane.getChildren().add(completedRectangle);
				});
				completedRectangle.setOnMouseDragged(event -> {
					if (selectedMember != null) {
						selectedMember.getKey().setLayoutX(event.getSceneX() - seatWidth - 2);
						selectedMember.getKey().setLayoutY(event.getSceneY() - seatHeight - 2);
					}
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
				double seatX = seatBaseX - ((current.getY()+1) % 2) * ((seatWidth+10)/2);
				Rectangle newMember = new Rectangle(0, 0, seatWidth, seatHeight);
				newMember.setFill(current.getColor());
				newMember.setStroke(Color.BLACK);
				Text name = new Text(current.getName() + ", " + current.getType());
				name.setWrappingWidth(seatWidth);
				name.setTextAlignment(TextAlignment.CENTER);
				StackPane completedRectangle = new StackPane();
				completedRectangle.setLayoutX(seatX + (seatWidth+10)*current.getX());
				completedRectangle.setLayoutY(seatBaseY + (seatHeight+10)*current.getY());
				completedRectangle.getChildren().addAll(newMember, name);
				completedRectangle.setOnMousePressed(event -> {
					System.out.println(event.getSceneX() + ", " + event.getSceneY());
					System.out.println((event.getSceneX() - seatWidth - 2) + ", " + (event.getSceneY() - seatHeight - 2));
					memberPane.getChildren().remove(completedRectangle);
					selectedMember = new Pair<>(completedRectangle, current);
					completedRectangle.setLayoutX(event.getSceneX() - seatWidth - 2);
					completedRectangle.setLayoutY(event.getSceneY() - seatHeight - 2);
					completedRectangle.setCursor(Cursor.MOVE);
					rootPane.getChildren().add(completedRectangle);
				});
				completedRectangle.setOnMouseDragged(event -> {
					completedRectangle.setLayoutX(event.getSceneX() - seatWidth - 2);
					completedRectangle.setLayoutY(event.getSceneY() - seatHeight - 2);
				});
				completedRectangle.setOnMouseReleased((_) -> {
					rootPane.getChildren().remove(completedRectangle);
					selectedMember = null;
					memberPane.getChildren().add(completedRectangle);
					completedRectangle.setCursor(Cursor.DEFAULT);
				});
				HBox.setMargin(completedRectangle, new Insets(0, 10, 0, 10));
				chartPane.getChildren().add(completedRectangle);
				memberSeats.add(completedRectangle);
			}
		}
		
	}
	
	/*-*
		
		Methods
		
	 */
	
	private static void save() {
		try (FileWriter writer = new FileWriter(chart.getSaveLocation().getAbsolutePath())) {
			chart.getSaveLocation().createNewFile();
			ArrayList<Pair<String, Color>> types = chart.getTypes();
			writer.write(chart.getRows() + "," + chart.getTotalSeats() + "," + types.size() + "\n");
			writer.write(chart.getName() + "\n");
			for (int i = 0; i < types.size(); ++i) {
				writer.write(types.get(i).getKey() + "," + colorName(types.get(i).getValue()).get() + "\n");
			}
			ArrayList<Member> members = chart.getMembers();
			for (int i = 0; i < members.size(); ++i) {
				Member current = members.get(i);
				writer.write(current.getFirstName() + "," + current.getLastName() + "," + current.getType() + ",");
				writer.write(current.getX() + "," + current.getY() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		edited = false;
	}
	
	private static void saveAs() {
		fileChooser.setTitle("Select Save File");
		fileChooser.setInitialFileName(chart.getName() + ".chrt");
		FileChooser.ExtensionFilter chartFilter = new FileChooser.ExtensionFilter("Chart Files (*.chrt)", "*.chrt");
		fileChooser.getExtensionFilters().addAll(chartFilter);
		File selectedFile = fileChooser.showSaveDialog(stage);
		if (selectedFile != null) {
			try (FileWriter writer = new FileWriter(selectedFile.getAbsolutePath())) {
				selectedFile.createNewFile();
				ArrayList<Pair<String, Color>> types = chart.getTypes();
				writer.write(chart.getRows() + "," + chart.getTotalSeats() + "," + types.size() + "\n");
				writer.write(chart.getName() + "\n");
				for (int i = 0; i < types.size(); ++i) {
					writer.write(types.get(i).getKey() + "," + colorName(types.get(i).getValue()).get() + "\n");
				}
				ArrayList<Member> members = chart.getMembers();
				for (int i = 0; i < members.size(); ++i) {
					Member current = members.get(i);
					writer.write(current.getFirstName() + "," + current.getLastName() + "," + current.getType() + ",");
					writer.write(current.getX() + "," + current.getY() + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			chart.setSaveLocation(selectedFile);
			fileChooser.setInitialDirectory(selectedFile.getParentFile());
		}
		edited = false;
		fileChooser.getExtensionFilters().removeAll(chartFilter);
	}
	
	private static void load() {
		fileChooser.setTitle("Select File To Load");
		FileChooser.ExtensionFilter chartFilter = new FileChooser.ExtensionFilter("Chart Files (*.chrt)", "*.chrt");
		fileChooser.getExtensionFilters().addAll(chartFilter);
		File selectedFile = fileChooser.showOpenDialog(stage);
		fileChooser.setInitialFileName("");
		if (selectedFile != null) {
			String rows = "-1", totalSeats = "-1", typesCount = "-1";
			ArrayList<Member> members = new ArrayList<Member>();
			try (Scanner chartReader = new Scanner(selectedFile)) {
				if (chartReader.hasNextLine()) {
					String data = chartReader.nextLine();
					int commaIndex1 = data.indexOf(',');
					int commaIndex2 = data.substring(commaIndex1 + 1).indexOf(",") + commaIndex1 + 1;
					rows = data.substring(0, commaIndex1);
					totalSeats = data.substring(commaIndex1 + 1, commaIndex2);
					typesCount = data.substring(commaIndex2 + 1);
				}
				String name = chartReader.nextLine();
				String types = "";
				for (int i = 0; i < Integer.parseInt(typesCount); ++i) {
					types += chartReader.nextLine() + "\n";
				}
				while (chartReader.hasNextLine()) {
					String data = chartReader.nextLine();
					int commaIndex1 = data.indexOf(",");
					int commaIndex2 = data.substring(commaIndex1 + 1).indexOf(",") + commaIndex1 + 1;
					int commaIndex3 = data.substring(commaIndex2 + 1).indexOf(",") + commaIndex2 + 1;
					String firstName = data.substring(0, commaIndex1);
					String lastName = data.substring(commaIndex1 + 1, commaIndex2);
					String type = data.substring(commaIndex2 + 1, commaIndex3);
					String coords = data.substring(commaIndex3 + 1);
					Member newMember = new Member(firstName, lastName, type, coords);
					members.add(newMember);
				}
				Chart chart = new Chart(rows, totalSeats, members, selectedFile, types);
				chart.setName(name);
				ChartEditor.editChart(stage, chart);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			edited = false;
		}
		fileChooser.getExtensionFilters().removeAll(chartFilter);
	}
	
	// Source - https://stackoverflow.com/a/24232749
	// Posted by Pshemo, modified by community. See post 'Timeline' for change history
	// Retrieved 2026-05-28, License - CC BY-SA 3.0

	public static Optional<String> colorName(Color c) {
	    for (Field f : Color.class.getDeclaredFields()) {
	        //we want to test only fields of type Color
	        if (f.getType().equals(Color.class))
	            try {
	                if (f.get(null).equals(c))
	                    return Optional.of(f.getName().toLowerCase());
	            } catch (IllegalArgumentException | IllegalAccessException e) {
	                // shouldn't not be thrown, but just in case print its stacktrace
	                e.printStackTrace();
	            }
	    }
	    return Optional.empty();
	}

	
}
