package newChart;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
	private static Button confirmButton = new Button();
	
	private static Label promptRows = new Label();
	private static Label promptTotalSeats = new Label();
	private static Label promptSingerTypes = new Label();
	private static Label promptSingers = new Label();
	
	private static TextField inputRows = new TextField();
	private static TextField inputTotalSeats = new TextField();
	private static TextArea inputSingerTypes = new TextArea();
	private static TextArea inputSingers = new TextArea();
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
		
		// Update dynamic elements
		confirmButton.setText("Continue");
		
		// Display page
		stage.setTitle("Create New Chart");
		stage.setScene(createScene);
		stage.show();
	}
	
	private NewChartView() {
		
		rootPane = new Pane();
		createScene = new Scene(rootPane, 800, 600);
		
		title.setText("New Chart");
		setupLabelUI(title, "Arial", 32, 400, Pos.CENTER, 200, 25);
		
		setupButtonUI(confirmButton, "Arial", 16, 200, Pos.CENTER, 575, 545);
		confirmButton.setOnAction((_) -> {
			if (inputRows.getText().isEmpty() || inputTotalSeats.getText().isEmpty() || inputSingers.getText().isEmpty() || inputSingerTypes.getText().isEmpty()) {
				emptyFieldAlert.showAndWait();
			}
		});
		promptRows.setText("Rows: ");
		setupLabelUI(promptRows, "Arial", 16, 50, Pos.TOP_LEFT, 25, 100);
		promptTotalSeats.setText("Total Seats:");
		setupLabelUI(promptTotalSeats, "Arial", 16, 50, Pos.TOP_LEFT, 25, 140);		
		promptSingers.setText("Singers: ");
		setupLabelUI(promptSingers, "Arial", 16, 50, Pos.TOP_LEFT, 25, 180);
		promptSingerTypes.setText("Singer Types:");
		setupLabelUI(promptSingerTypes, "Arial", 16, 50, Pos.TOP_LEFT, 25, 340);		
		
		setupTextFieldUI(inputRows, "Arial", 16, 75, Pos.TOP_LEFT, 130, 95);	
		setupTextFieldUI(inputTotalSeats, "Arial", 16, 75, Pos.TOP_LEFT, 130, 135);
		inputSingers.setPromptText("Singer Name, Part");
		setupTextAreaUI(inputSingers, "Arial", 16, 600, 150, 130, 175);	
		inputSingerTypes.setPromptText("Part Name, Color");
		setupTextAreaUI(inputSingerTypes, "Arial", 16, 600, 150, 130, 335);	
		
		emptyFieldAlert.setTitle("Empty Field(s)");
		emptyFieldAlert.setHeaderText("One or more fields have been read as empty.");
		emptyFieldAlert.setContentText("Please fill them in and then continue.");
		
		rootPane.getChildren().addAll(title, confirmButton,
				promptRows, inputRows,
				promptTotalSeats, inputTotalSeats,
				promptSingers, inputSingers,
				promptSingerTypes, inputSingerTypes);
		
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
	
}
