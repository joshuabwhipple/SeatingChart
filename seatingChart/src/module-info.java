module seatingChart {
	requires javafx.controls;
	requires javafx.graphics;
	
	opens applicationStart to javafx.graphics, javafx.fxml;
}
