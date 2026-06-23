module seatingChart {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires java.desktop;
	requires javafx.swing;
	requires org.apache.pdfbox;
	requires org.apache.commons.logging;
	requires org.apache.pdfbox.io;
	
	opens applicationStart to javafx.graphics, javafx.fxml;
}
