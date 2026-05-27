module seatingChart {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	
	opens applicationStart to javafx.graphics, javafx.fxml;
}
