module seatingChart {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires java.desktop;
	requires javafx.swing;
	
	opens applicationStart to javafx.graphics, javafx.fxml;
}
