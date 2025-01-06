module com.example.telematiktelemedizin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires hapi.fhir.base;
    requires org.hl7.fhir.r5;


    exports GUI;
    opens GUI to javafx.fxml;
}