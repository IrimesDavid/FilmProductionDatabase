module PS.T2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens org.mvvm.model to javafx.base;
    opens org.mvvm.view to javafx.fxml, javafx.base;
    exports org.mvvm.view;
}