package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NahrungsmittelHinzufugen2Controller {

    @FXML
    private TextField zeit;

    @FXML
    private MenuButton nahrungsmittel;

    @FXML
    private TextArea beschwerde;

    @FXML
    private Button hinzufugen;

    @FXML
    private Button speichern;

    public TextField getZeit() {
        return zeit;
    }

    public MenuButton getNahrungsmittel() {
        return nahrungsmittel;
    }

    public TextArea getBeschwerde() {
        return beschwerde;
    }

    public Button getHinzufugen() {
        return hinzufugen;
    }

    public Button getSpeichern() {
        return speichern;
    }
}
