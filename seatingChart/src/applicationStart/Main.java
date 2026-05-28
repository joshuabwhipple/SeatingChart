package applicationStart;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import applicationMain.ChartEditor;
import entityClasses.Chart;
import entityClasses.Member;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import newChart.NewChartView;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;


public class Main extends Application {
	
	/*-*******************************************************************************************
	 
	Attributes
	   
	*/
	
	private static Label title = new Label();
	
	private static Button newChart = new Button("New Chart");
	private static Button openChart = new Button("Load Chart");
	
	private static FileChooser fileChooser = new FileChooser();
	private static final String S = System.getProperty("file.separator");
	
	@Override
	public void start(Stage stage) {
		try {
			Pane root = new Pane();
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			title.setText("Seating Chart Maker");
			setupLabelUI(title, "Arial", 32, 400, Pos.CENTER, 200, 25);
			
			setupButtonUI(newChart, "Arial", 16, 200, Pos.CENTER, 300, 250);
			newChart.setOnAction((_) -> {NewChartView.createChart(stage);});
			
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+S+"Downloads"));
			
			setupButtonUI(openChart, "Arial", 16, 200, Pos.CENTER, 300, 350);
			openChart.setOnAction((_) -> {
				fileChooser.setTitle("Select File To Load");
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
						ChartEditor.editChart(stage, chart);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			});
			
			root.getChildren().addAll(title, newChart, openChart);
			
			stage.setScene(scene);
			stage.setTitle("Seating Chart Maker");
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
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
}
