package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class TagebuchController{


    @FXML
    private Tab tTab;
    @FXML
    private Tab bTab;
    @FXML
    private Tab nTab;
    @FXML
    private Tab menuTab;
    @FXML
    private Tab alleMenuTab;

    @FXML
    private BenutzerController benutzerController;
    @FXML
    private TagebuchTableController tagebuchTableController;
    @FXML
    private MenusetController menusetController;
    @FXML
    private MenusetTableController menusetTableController;
    @FXML
    private Pane tagebuch;
    @FXML
    private NahrungsmittelHinzufugenController nahrungsmittelHinzufugenController;


    @FXML
    private TabPane tabpane;
    @FXML
    private Tab tfhirtab;
    @FXML
    private TagebuchTableFHIRController tagebuchTableFHIRController;
    @FXML
    private NahrungsmittelTableController nahrungsmittelTableController;


    @FXML
    private void initialize() {
        benutzerController.getNahrungsmittelController(nahrungsmittelHinzufugenController);
        benutzerController.getMenusetController(menusetController);
        benutzerController.getMenusetTableController(menusetTableController);
        benutzerController.getTagebuchTableController(tagebuchTableController);
        benutzerController.getTagebuchTableFHIRController(tagebuchTableFHIRController);

        //nahrungsmittelHinzufugenController.getBenutzerController(benutzerController);
        tagebuchTableFHIRController.getBenutzerController(benutzerController);
        tagebuchTableController.getBenutzerController(benutzerController);
        menusetController.getBenutzerController(benutzerController);
        menusetController.getMenusetTableController(menusetTableController);
        menusetTableController.getBenutzerController(benutzerController);
    }
}
