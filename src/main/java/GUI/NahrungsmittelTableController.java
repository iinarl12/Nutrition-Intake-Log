package GUI;

import Datenhaltung.DBConnect;
import FHIR.Nahrungsmittelstammdaten;
import Fachlogik.Tagsuebersicht;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.hl7.fhir.r5.model.NutritionProduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NahrungsmittelTableController {


    @FXML
    private TableColumn<NutritionProduct, Double> fett;

    @FXML
    private TableColumn<NutritionProduct, Double> kcal;

    @FXML
    private TableColumn<NutritionProduct, Double> khydrate;

    @FXML
    private TableColumn<NutritionProduct, String> nahrung;

    @FXML
    private TableColumn<NutritionProduct, Double> protein;

    @FXML
    private TableColumn<NutritionProduct, Double> zucker;
    @FXML
    private TableColumn<NutritionProduct, String> fhirID;
    @FXML
    private Pane nahrungsmittelTable;
    @FXML
    private TextField suchTextField;
    @FXML
    private TableView<NutritionProduct> tabelview;
    @FXML
    private Button fhirupdate;

    private ObservableList<NutritionProduct> nList=FXCollections.observableArrayList();

    List<NutritionProduct> nutritionProductList=new ArrayList<>();


    public NahrungsmittelTableController() {
    }

    @FXML
    public void initialize() throws IOException {
        Nahrungsmittelstammdaten nahrungsmittelstammdaten= new Nahrungsmittelstammdaten();

        nutritionProductList=nahrungsmittelstammdaten.parseAllToNahrungsmittel();
        updateTable();



    }

    public void updateTable(){
        nList.clear();

        for(NutritionProduct nutritionProduct:nutritionProductList){

            if(nutritionProduct.getNutrient().size()>4)nList.add(nutritionProduct);
        }

        nahrung.setCellValueFactory(data-> new SimpleObjectProperty(data.getValue().getCode().getText()));
        kcal.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getNutrient().get(0).getAmount().get(0).getNumerator().getValue()));
        khydrate.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getNutrient().get(1).getAmount().get(0).getNumerator().getValue()));
        zucker.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getNutrient().get(2).getAmount().get(0).getNumerator().getValue()));
        fett.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getNutrient().get(3).getAmount().get(0).getNumerator().getValue()));
        protein.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getNutrient().get(4).getAmount().get(0).getNumerator().getValue()));
        fhirID.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getIdPart()));
        /*ballaststoff.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getNutrient().get(3).getAmount().get(0).getNumerator().getValue()));
        alkohol.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getNutrient().get(6).getAmount().get(0).getNumerator().getValue()));*/

        tabelview.setItems(nList);

    }


    @FXML
    public void suchen(Event event) {
        if(suchTextField.getText().replaceAll(" ","").equals("")){
            tabelview.setItems(nList);
        }
        ObservableList<NutritionProduct> nutritionProducts = FXCollections.observableArrayList();
        int i=0;
        while(i<nList.size()){
            if((nList.get(i).getCode().getText()!=null)&&(nList.get(i).getCode().getText()).replaceAll(" ","").toLowerCase().contains(suchTextField.getText().replaceAll(" ","").toLowerCase())){
                nutritionProducts.add(nList.get(i));
            }
            i++;
        }
        tabelview.setItems(nutritionProducts);

    }

    @FXML
    public void update(ActionEvent actionEvent) throws IOException {
        Nahrungsmittelstammdaten nahrungsmittelstammdaten= new Nahrungsmittelstammdaten();

        nutritionProductList=nahrungsmittelstammdaten.parseAllToNahrungsmittel();
        updateTable();
    }
}
