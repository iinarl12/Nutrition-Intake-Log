module com.example.telematiktelemedizin {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.telematiktelemedizin to javafx.fxml;
    exports com.example.telematiktelemedizin;
    exports com.example.telematiktelemedizin;
    opens com.example.telematiktelemedizin to javafx.fxml;
    exports com.example.telematiktelemedizin.GUI;
    opens com.example.telematiktelemedizin.GUI to javafx.fxml;
}