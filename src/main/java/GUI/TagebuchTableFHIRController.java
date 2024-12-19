package GUI;

import Datenhaltung.DBConnect;
import Datenhaltung.DBTagesuebersicht;
import FHIR.Tagebuch;
import Fachlogik.Benutzer;
import Fachlogik.Einheit;
import Fachlogik.Tagsuebersicht;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TagebuchTableFHIRController {
    @FXML
    private Text benutzer;
    @FXML
    private DatePicker datum;
    @FXML
    private TableView<Tagsuebersicht> table;
    @FXML
    private TableColumn<Tagsuebersicht, LocalTime> zeit;
    @FXML
    private TableColumn <Tagsuebersicht,String> nahrungsmittel;
    @FXML
    private TableColumn <Tagsuebersicht, Integer>menge;

    @FXML
    private TableColumn<Tagsuebersicht, Einheit> einheiten;
    @FXML
    private TableColumn<Tagsuebersicht, String> beschwerde;
    @FXML
    private TableColumn<Tagsuebersicht, String> fhirId;

    @FXML
    private Pane pane;


    private List<Tagsuebersicht> tagebuchList=new ArrayList<>();
    private Benutzer selectedBenutzer;
    private ObservableList<Tagsuebersicht> nList= FXCollections.observableArrayList();

    private String query = null;

    private BenutzerController benutzerController;

    private DBConnect dbConnect=new DBConnect();

    DBTagesuebersicht dbTagesuebersicht = new DBTagesuebersicht(dbConnect);

    @FXML
    private Button loeschen;
    @FXML
    private Button update;

    public void getBenutzerController(BenutzerController benutzerController){
        this.benutzerController=benutzerController;
    }

    public void setBenutzerTitle(Benutzer benutzer){
        this.benutzer.setText(benutzer.getVorname());
    }

    public void setBenutzer(Benutzer selectedBenutzer){
        this.selectedBenutzer=selectedBenutzer;
    }


    @FXML
    public void initialize(){


        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    public void refresh(){
        nList.clear();
        datum.setValue(null);
    }


    @FXML
    private void refreshTable() {
        if(selectedBenutzer!=null) {

            nList.clear();

            Tagebuch tagebuch= new Tagebuch();
            nList.addAll(tagebuch.getTagebuch(selectedBenutzer,datum.getValue()));

        }
    }



    public void populateTable(){
        if(datum.getValue().isBefore(LocalDate.now())||datum.getValue().isEqual(LocalDate.now())){

            if(selectedBenutzer!=null){
                refreshTable();
                table.setEditable(true);

                zeit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getZeit()));
                nahrungsmittel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNahrungsmittel()));
                menge.setCellValueFactory(mg -> new SimpleObjectProperty<>(mg.getValue().getMenge()));
                einheiten.setCellValueFactory(einheit -> new SimpleObjectProperty(einheit.getValue().getEinheit()));
                beschwerde.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBeschwerde()));
                beschwerde.setEditable(true);
                beschwerde.setCellFactory(TextFieldTableCell.forTableColumn());
                beschwerde.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tagsuebersicht, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Tagsuebersicht, String> tagsuebersichtStringCellEditEvent) {
                        ((Tagsuebersicht) tagsuebersichtStringCellEditEvent.getTableView().getItems().get(tagsuebersichtStringCellEditEvent.getTablePosition().getRow())).setBeschwerde(tagsuebersichtStringCellEditEvent.getNewValue());
                        Tagsuebersicht tu= tagsuebersichtStringCellEditEvent.getTableView().getItems().get(tagsuebersichtStringCellEditEvent.getTablePosition().getRow());
                        DBTagesuebersicht dbtb= new DBTagesuebersicht(dbConnect);
                        boolean db=dbtb.updateTagebuch(tu);
                        Tagebuch tb= new Tagebuch();
                        boolean fhir=tb.updateNutritionIntake(tu);
                        if(db&&fhir){
                            Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,
                                    "Tagebuch wurde erfolgreich in Datenbank und FHIR geupdated", ButtonType.OK);
                            a1.show();
                        }

                    }
                });
                fhirId.setCellValueFactory(fhir -> new SimpleObjectProperty<>(fhir.getValue().getFhirID()));

                table.setItems(nList);

            }
            else{
                Alert a1 = new Alert(Alert.AlertType.WARNING,
                        "Benutzer auswählen",ButtonType.OK);
                a1.show();
            }
        }
        else{
            Alert a1 = new Alert(Alert.AlertType.WARNING,
                    "Bitte gultiges Datum auswählen",ButtonType.OK);
            a1.show();
        }

        table.setRowFactory(t-> {
            TableRow<Tagsuebersicht> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2 && (!row.isEmpty())) {
                        Tagsuebersicht tagsuebersicht = row.getItem();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TagebuchNaehrwerte.fxml"));

                        Scene scene = null;
                        Stage stage = new Stage();
                        try {
                            scene = new Scene(fxmlLoader.load());
                            TagebuchNaehrwerteController tagebuchNaehrwerteController = fxmlLoader.getController();
                            tagebuchNaehrwerteController.setTagsuebersicht(tagsuebersicht);
                            tagebuchNaehrwerteController.setNaehrwerte();

                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.initModality(Modality.WINDOW_MODAL);
                            stage.initOwner(pane.getScene().getWindow());
                            stage.setTitle("Nährwerte");
                            stage.showAndWait();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            return row;
        });

    }

    @FXML
    public void datum(ActionEvent actionEvent) {
        populateTable();
    }

    @FXML
    public void delete(ActionEvent actionEvent) {


        for(Tagsuebersicht ne:table.getSelectionModel().getSelectedItems()){

            Tagebuch tagebuch= new Tagebuch();
            if(tagebuch.delete(ne)){

                Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,
                        "Tagebuch wurde erfolgreich gelöscht",ButtonType.OK);
                a1.show();
            }
        }
        table.getItems().removeAll(table.getSelectionModel().getSelectedItems());

    }

    @FXML
    public void update(ActionEvent actionEvent) {

        refreshTable();
    }
}





