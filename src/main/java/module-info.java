module com.example.telematiktelemedizin {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.telematiktelemedizin to javafx.fxml;
    exports com.example.telematiktelemedizin;
}