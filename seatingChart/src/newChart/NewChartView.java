package newChart;

import entityClasses.Chart;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class NewChartView {

	/*-*
	
	Attributes 
	
	*/
	
	// GUI Elements
	
	private static Label title = new Label();
	private static Button confirmButton = new Button("Continue");
	private static Button backButton = new Button("Back");
	
	private static Label promptName = new Label();
	private static Label promptRows = new Label();
	private static Label promptTotalSeats = new Label();
	private static Label promptMemberTypes = new Label();
	private static Label promptMembers = new Label();
	
	private static CheckBox customTypes = new CheckBox("Custom Member Types");
	
	private static TextField inputName = new TextField();
	private static TextField inputRows = new TextField();
	private static TextField inputTotalSeats = new TextField();
	private static TextArea inputMemberTypes = new TextArea();
	private static TextArea inputMembers = new TextArea();
	// fileLocation (will probably be allocated to first save in application)
	
	private static Alert emptyFieldAlert = new Alert(AlertType.WARNING);
	
	// Application Elements
	
	private static NewChartView newChartPage;
	
	private static Stage stage;
	
	private static Pane rootPane;
	private static Scene createScene;
	
	/*-*
	Constructors 
	*/
	
	public static void createChart(Stage inStage) {
		// Establish reference to GUI
		stage = inStage;
		
		if (newChartPage == null) {
			newChartPage = new NewChartView();
		}
		
		// Display page
		stage.setTitle("Create New Chart");
		stage.setScene(createScene);
		stage.show();
	}
	
	private NewChartView() {
		
		rootPane = new Pane();
		createScene = new Scene(rootPane, 800, 600);
		
		title.setText("New Chart");
		setupLabelUI(title, "Poppins", 32, 400, Pos.CENTER, 200, 25);
		
		setupButtonUI(confirmButton, "Poppins", 16, 200, Pos.CENTER, 575, 545);
		confirmButton.setOnAction((_) -> {
			if (inputRows.getText().isEmpty() || inputTotalSeats.getText().isEmpty() || inputMembers.getText().isEmpty()
					|| (inputMemberTypes.getText().isEmpty() && customTypes.isSelected()) || inputName.getText().isEmpty()) {
				emptyFieldAlert.showAndWait();
			} else if (customTypes.isSelected()) {
				try {
					Chart chart = new Chart(inputRows.getText(), inputTotalSeats.getText(), inputMembers.getText(), inputMemberTypes.getText());
					chart.setName(inputName.getText());
					applicationMain.ChartEditor.editChart(stage, chart);
				} catch (StringIndexOutOfBoundsException e) {
					Alert formattingAlert = new Alert(Alert.AlertType.ERROR);
					formattingAlert.setTitle("Error Reading Members");
					formattingAlert.setHeaderText("An error has occurred trying to read the list of members.");
					formattingAlert.setContentText("This most commonly occurs when there is some formatting mistake. Check your list and try again.");
					formattingAlert.showAndWait();
				}
			} else {
				try {
					Chart chart = new Chart(inputRows.getText(), inputTotalSeats.getText(), inputMembers.getText());
					chart.setName(inputName.getText());
					applicationMain.ChartEditor.editChart(stage, chart);
				} catch (StringIndexOutOfBoundsException e) {
					Alert formattingAlert = new Alert(Alert.AlertType.ERROR);
					formattingAlert.setTitle("Error Reading Members");
					formattingAlert.setHeaderText("An error has occurred trying to read the list of members.");
					formattingAlert.setContentText("This most commonly occurs when there is some formatting mistake. Check your list and try again.");
					formattingAlert.showAndWait();
				}
			}
		});
		
		setupButtonUI(backButton, "Poppins", 16, 200, Pos.CENTER, 25, 545);
		backButton.setOnAction((_) -> {
			applicationStart.Main.display(stage);
		});
		
		setupCheckBoxUI(customTypes, "Poppins", 16, 200, Pos.CENTER, 25, 380);
		customTypes.setSelected(false);
		customTypes.setAllowIndeterminate(false);
		customTypes.setOnAction((_) -> {
			if (customTypes.isSelected()) {
				rootPane.getChildren().addAll(promptMemberTypes, inputMemberTypes);
			} else {
				rootPane.getChildren().removeAll(promptMemberTypes, inputMemberTypes);
			}
		});
		
		promptName.setText("Name: ");
		setupLabelUI(promptName, "Poppins", 16, 50, Pos.TOP_LEFT, 25, 100);
		promptRows.setText("Rows: ");
		setupLabelUI(promptRows, "Poppins", 16, 50, Pos.TOP_LEFT, 25, 140);
		promptTotalSeats.setText("Total Seats:");
		setupLabelUI(promptTotalSeats, "Poppins", 16, 50, Pos.TOP_LEFT, 25, 180);		
		promptMembers.setText("Members: ");
		setupLabelUI(promptMembers, "Poppins", 16, 50, Pos.TOP_LEFT, 25, 220);
		promptMemberTypes.setText("Member Types:");
		setupLabelUI(promptMemberTypes, "Poppins", 16, 50, Pos.TOP_LEFT, 25, 420);		
		
		setupTextFieldUI(inputName, "Poppins", 16, 75, Pos.TOP_LEFT, 140, 95);
		inputName.setMinWidth(300);
		setupTextFieldUI(inputRows, "Poppins", 16, 75, Pos.TOP_LEFT, 140, 135);	
		setupTextFieldUI(inputTotalSeats, "Poppins", 16, 75, Pos.TOP_LEFT, 140, 175);
		inputMembers.setPromptText("First Name, Last Name, Part");
		setupTextAreaUI(inputMembers, "Poppins", 16, 600, 150, 140, 215);	
		
		inputMemberTypes.setPromptText("Part Name, Color");
		setupTextAreaUI(inputMemberTypes, "Poppins", 16, 600, 110, 140, 415);	
		inputMemberTypes.setText(
				"Soprano, Light Cyan\n" +  
				"Soprano 1, Light Cyan\n" +  
				"Soprano 2, Light Sky Blue\n" +  
				"Alto, Light Sea Green\n" +  
				"Alto 1, Light Sea Green\n" +  
				"Alto 2, Light Green\n" +  
				"Tenor, Light Coral\n" +  
				"Tenor 1, Light Coral\n" +  
				"Tenor 2, Light Salmon\n" +  
				"Bass, Light Steel Blue\n" +  
				"Bass 1, Light Steel Blue\n" +  
				"Bass 2, Steel Blue\n" +  
				"Part 1, Light Cyan\n" +  
				"Part 2, Light Sea Green" 
);
		
		emptyFieldAlert.setTitle("Empty Field(s)");
		emptyFieldAlert.setHeaderText("One or more fields have been read as empty.");
		emptyFieldAlert.setContentText("Please fill them in and then continue.");
		
		rootPane.getChildren().addAll(title, confirmButton,
				backButton, promptName, 
				inputName, promptRows,
				inputRows, promptTotalSeats,
				inputTotalSeats, promptMembers,
				inputMembers, customTypes);
		
	}
	
	/*-*******************************************************************************************

	Helper methods used to minimizes the number of lines of code needed above
	
	*/

	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Label
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x,
			double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x,
			double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
	
	/**********
	 * Private local method to initialize the standard fields for a text field
	 * 
	 * @param tf	The TextField object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the TextField
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupTextFieldUI(TextField tf, String ff, double f, double w, Pos p, double x,
			double y){
		tf.setFont(Font.font(ff, f));
		tf.setMaxWidth(w);
		tf.setAlignment(p);
		tf.setLayoutX(x);
		tf.setLayoutY(y);		
	}
	
	/**********
	 * Private local method to initialize the standard fields for a text area
	 * 
	 * @param ta	The TextArea object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the TextArea
	 * @param h 	The height of the TextArea
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupTextAreaUI(TextArea ta, String ff, double f, double w, double h, double x,
			double y){
		ta.setFont(Font.font(ff, f));
		ta.setMaxWidth(w);
		ta.setMaxHeight(h);
		ta.setLayoutX(x);
		ta.setLayoutY(y);		
	}
	
	/**********
	 * Private local method to initialize the standard fields for a check box
	 * 
	 * @param cb	The CheckBox object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the CheckBox
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupCheckBoxUI(CheckBox cb, String ff, double f, double w, Pos p, double x,
			double y){
		cb.setFont(Font.font(ff, f));
		cb.setMinWidth(w);
		cb.setAlignment(p);
		cb.setLayoutX(x);
		cb.setLayoutY(y);		
	}
	
}
