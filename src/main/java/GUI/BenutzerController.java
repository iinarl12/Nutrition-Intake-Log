package GUI;

import Datenhaltung.DBBenutzer;
import Datenhaltung.DBConnect;
import FHIR.Patienten;
import FHIR.Tagebuch;
import Fachlogik.Benutzer;
import Fachlogik.Geschlecht;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.hl7.fhir.r5.model.Patient;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class BenutzerController implements Initializable {
    @FXML
    private TableColumn<Benutzer, String> tvorname;
    @FXML
    private TableColumn<Benutzer, String> tnachname;
    @FXML
    private TableColumn<Benutzer, Double> tgewicht;
    @FXML
    private TableColumn<Benutzer, LocalDate> tgeburtsdatum;
    @FXML
    private TableColumn<Benutzer, Geschlecht> tgeschlecht;

    @FXML
    private TableColumn<Benutzer,String> fhirId;
    @FXML
    private TableView<Benutzer> btable;
    // TextField
    @FXML
    private TextField vorname;
    @FXML
    private TextField nachname;
    @FXML
    private TextField suchfeld;
    @FXML
    private TextField gewicht;
    @FXML
    private DatePicker gebDatum;
    @FXML
    private MenuButton geschlecht;



    private double gew=0;
    private Geschlecht ges;


    private List<Benutzer> benutzerList= new ArrayList<Benutzer>();
    private HashMap<MenuItem, Benutzer> benutzerMap = new HashMap<>();
    private Benutzer selectedBenutzer;


    private NahrungsmittelHinzufugenController nahrungsmittelHinzufugenController;
    private MenusetTableController menusetTableController;
    private TagebuchTableController tagebuchTableController;
    private TagebuchTableFHIRController tagebuchTableFHIRController;
    private MenusetController menusetController;


    DBConnect dbConnect = new DBConnect();

    private DBBenutzer dbBenutzer = new DBBenutzer(dbConnect);
    private ObservableList<Benutzer> bList = FXCollections.observableArrayList();

    private Patienten patienten=new Patienten();
    @FXML
    private MenuItem weiblich;
    @FXML
    private MenuItem maennlich;
    @FXML
    private MenuItem divers;
    @FXML
    private Button speichern;
    @FXML
    private Button loeschen;
    @FXML
    private Button neu;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dbConnect.connect();
        selectedBenutzer= new Benutzer();

        try {
            populateBenutzer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setCellValueFromTableToTextField();

    }


    public void getNahrungsmittelController(NahrungsmittelHinzufugenController nahrungsmittelHinzufugenController){
        this.nahrungsmittelHinzufugenController=nahrungsmittelHinzufugenController;

    }
    public void getMenusetTableController(MenusetTableController menusetTableControllerController){
        this.menusetTableController=menusetTableControllerController;
    }
    public void getMenusetController(MenusetController menusetControllerController){
        this.menusetController=menusetControllerController;

    }
    public void getTagebuchTableController(TagebuchTableController tagebuchTableController){
        this.tagebuchTableController=tagebuchTableController;

    }
    public void getTagebuchTableFHIRController(TagebuchTableFHIRController tagebuchTableFHIRController){
        this.tagebuchTableFHIRController=tagebuchTableFHIRController;

    }
    @FXML
    private void refreshTable() {
            try {
                bList.clear();
                Patienten patienten= new Patienten();
                bList.addAll(patienten.getPatient());



                Statement mystatement  = DBConnect.getInstance().getCon().createStatement();
                ResultSet benutzerDB=mystatement.executeQuery("select * from benutzer");

                while (benutzerDB.next()) {
                    for(Benutzer benutzer:bList){
                        if(benutzer.getFhirId().equals(benutzerDB.getString(7))){
                            benutzer.setBenutzerId(benutzerDB.getInt(1));
                        }
                    }

                }


            }catch (Exception exception) {
                exception.printStackTrace();
            }

            System.out.println("liste " +bList);
        }


    public void populateBenutzer() throws SQLException {
        refreshTable();


        tvorname.setCellValueFactory(data ->  new SimpleObjectProperty<>(data.getValue().getVorname()));
        tnachname.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNachname()));
        tgewicht.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getGewicht()));
        tgeburtsdatum.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getGeburtsdatum()));
        tgeschlecht.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getGeschlecht()));
        fhirId.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getFhirId()));

        btable.setItems(bList);

    }
    private void setCellValueFromTableToTextField(){
        btable.setOnMouseClicked(e ->{
            Benutzer b1=btable.getItems().get(btable.getSelectionModel().getSelectedIndex());
            selectedBenutzer=b1;
            vorname.setText(""+selectedBenutzer.getVorname());
            nachname.setText(""+selectedBenutzer.getNachname());
            gewicht.setText(""+selectedBenutzer.getGewicht());
            gew=selectedBenutzer.getGewicht();
            gebDatum.setValue(selectedBenutzer.getGeburtsdatum());
            geschlecht.setText(""+selectedBenutzer.getGeschlecht());
            ges=selectedBenutzer.getGeschlecht();
            setAllBenutzer();
            tagebuchTableFHIRController.refresh();
            tagebuchTableController.refresh();
            nahrungsmittelHinzufugenController.refresh();
            menusetTableController.populateTable();
            menusetController.refresh();
        });

    }



    public void setAllBenutzer(){
        nahrungsmittelHinzufugenController.setBenutzer(selectedBenutzer);
        nahrungsmittelHinzufugenController.setBenutzerTitle(selectedBenutzer);

        tagebuchTableController.setBenutzer(selectedBenutzer);
        tagebuchTableController.setBenutzerTitle(selectedBenutzer);

        tagebuchTableFHIRController.setBenutzer(selectedBenutzer);
        tagebuchTableFHIRController.setBenutzerTitle(selectedBenutzer);

        menusetController.setBenutzer(selectedBenutzer);
        menusetController.setBenutzerTitle(selectedBenutzer);

        menusetTableController.setBenutzer(selectedBenutzer);
        menusetTableController.setBenutzerTitle(selectedBenutzer);

    }
    @FXML
    public void neue(ActionEvent actionEvent){
        clearEingabe();
        geschlecht.setText("Geschlecht");
        selectedBenutzer=new Benutzer();
        setAllBenutzer();
    }
    @FXML
    public void suchen(){
        if(suchfeld.getText().replaceAll(" ","").equals("")){
            btable.setItems(bList);
        }
        ObservableList<Benutzer> benutzer1 = FXCollections.observableArrayList();
        int i=0;
        while(i<bList.size()){
            if((bList.get(i).getNachname()).replaceAll(" ","").toLowerCase().contains(suchfeld.getText().replaceAll(" ","").toLowerCase())){
                benutzer1.add(bList.get(i));
            }
            i++;
        }
        btable.setItems(benutzer1);
    }


    @FXML
    public void delete(ActionEvent actionEvent) throws SQLException {

        if(bList.contains(selectedBenutzer)){
            bList.remove(selectedBenutzer);
            clearEingabe();
        }
        else{
            clearEingabe();
            bList.remove(selectedBenutzer);
        }
        setCellValueFromTableToTextField();
        Boolean fhirDelete=patienten.deletePatient(selectedBenutzer);
        Boolean dbDelete = false;
        if(fhirDelete)dbDelete=dbBenutzer.benutzerdelete(selectedBenutzer);

        if(fhirDelete&&dbDelete){
            Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,
                    "Benutzer wurde erfolgreich gelöscht",ButtonType.YES);
            a1.show();
        }
        else{
            Alert a1 = new Alert(Alert.AlertType.WARNING,
                    "Something went wrong",ButtonType.YES);
            a1.show();
        }
        populateBenutzer();


    }

    @FXML
    public void save(ActionEvent actionEvent) throws SQLException {
        if(bList.contains(selectedBenutzer)) {
            if (!vorname.getText().isEmpty() && !nachname.getText().isEmpty() && this.gewichtValid() && ges != null && datumValid()) {

                selectedBenutzer.setVorname(vorname.getText());
                selectedBenutzer.setNachname(nachname.getText());
                selectedBenutzer.setGewicht(gew);
                LocalDate date = gebDatum.getValue();
                selectedBenutzer.setGeburtsdatum(date);
                selectedBenutzer.setGeschlecht(ges);

                Boolean fhirUpdated=patienten.savePatient(selectedBenutzer);
                Boolean dbUpdated=dbBenutzer.updateBenutzer(selectedBenutzer);


                if(fhirUpdated&&dbUpdated){
                    Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,
                            "Benutzer wurde erfolgreich in Datenbank und FHIR gespeichert", ButtonType.OK);
                    a1.show();
                    setCellValueFromTableToTextField();
                }

                else{
                    Alert a1 = new Alert(Alert.AlertType.WARNING,
                            "Something went wrong",ButtonType.YES);
                    a1.show();
                }
                populateBenutzer();
            }
        }
        else{
            if (!vorname.getText().isEmpty() && !nachname.getText().isEmpty() && this.gewichtValid() && ges != null && datumValid())
            {
                Benutzer newBenutzer= new Benutzer();
                newBenutzer.setNachname(nachname.getText());
                newBenutzer.setGewicht(gew);
                LocalDate date = gebDatum.getValue();
                newBenutzer.setGeburtsdatum(date);
                newBenutzer.setGeschlecht(ges);
                newBenutzer.setVorname(vorname.getText());


                Boolean fhirSaved=patienten.savePatient(newBenutzer);
                Boolean dbSaved=dbBenutzer.addBenutzerDB(newBenutzer);

                if(fhirSaved&&dbSaved){
                    Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,
                            "Benutzer wurde erfolgreich in Datenbank und FHIR gespeichert", ButtonType.OK);
                    a1.show();
                    setCellValueFromTableToTextField();
                }
                else{
                    Alert a1 = new Alert(Alert.AlertType.WARNING,
                            "Something went wrong",ButtonType.YES);
                    a1.show();
                }
                populateBenutzer();

            }

        }
        if(ges==null){
            Alert a1 = new Alert(Alert.AlertType.WARNING,
                    "Bitte Geschlecht auswählen",ButtonType.OK);
            a1.show();
        }
        if(vorname.getText().isEmpty()){
            Alert a1 = new Alert(Alert.AlertType.WARNING,
                    "Bitte Vorname eingeben",ButtonType.OK);
            a1.show();
        }
        if(nachname.getText().isEmpty()){
            Alert a1 = new Alert(Alert.AlertType.WARNING,
                    "Bitte Nachname eingeben",ButtonType.OK);
            a1.show();
        }

        clearEingabe();
        selectedBenutzer=new Benutzer();

    }

    public boolean gewichtValid(){
        try {
            gew = Double.parseDouble(gewicht.getText());
            return true;
        }
        catch( Exception wrongInput){
            Alert a1 = new Alert(Alert.AlertType.WARNING,
                    "Gewicht muss eine Zahl(Punkt statt Koma)",ButtonType.OK);
            a1.show();
            return false;
        }

    }

    public boolean datumValid(){
        if(gebDatum.getValue().isBefore(LocalDate.now())) return true;
        Alert a1 = new Alert(Alert.AlertType.WARNING,
                "Bitte gultiges Datum eingeben",ButtonType.OK);
        a1.show();
        gebDatum.setValue(selectedBenutzer.getGeburtsdatum());
        return false;
    }

    public void clearEingabe(){

        vorname.setText("");
        nachname.setText("");
        gewicht.setText("");
        gebDatum.setValue(null);
        geschlecht.setText("");
    }


    @FXML
    public void weiblich(ActionEvent actionEvent) {
        ges=Geschlecht.weiblich;
        geschlecht.setText("Weiblich");
    }

    @FXML
    public void maennlich(ActionEvent actionEvent) {
        ges=Geschlecht.maennlich;
        geschlecht.setText("Männlich");
    }

    @FXML
    public void divers(ActionEvent actionEvent) {
        ges=Geschlecht.divers;
        geschlecht.setText("Divers");
    }

    public void update(ActionEvent actionEvent) throws SQLException {
        populateBenutzer();
    }
}
