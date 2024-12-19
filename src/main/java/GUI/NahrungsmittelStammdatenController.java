package GUI;

import FHIR.Nahrungsmittelstammdaten;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.hl7.fhir.r5.model.NutritionProduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NahrungsmittelStammdatenController {

    @javafx.fxml.FXML
    private TableView<NutritionProduct> table;
    @javafx.fxml.FXML
    private TableColumn<NutritionProduct, String> name;
    @javafx.fxml.FXML
    private TableColumn<NutritionProduct, Double> kalorien;
    @FXML
    private TextField suchFeld;

    private ObservableList<NutritionProduct> nList= FXCollections.observableArrayList();
    private NutritionProduct nutritionProduct;

    private MenusetController menusetController;
    private NahrungsmittelHinzufugenController nahrungsmittelHinzufugenController;


    List<NutritionProduct> nutritionProductList=new ArrayList<>();



    @FXML
    public void initialize() throws IOException {
        Nahrungsmittelstammdaten nahrungsmittelstammdaten= new Nahrungsmittelstammdaten();

        nutritionProductList=nahrungsmittelstammdaten.parseAllToNahrungsmittel();
        updateTable();

    }

    public void updateTable(){
        //System.out.println(nutritionProductList.size()+"    nsize");
        nList.clear();
        for(NutritionProduct nutritionProduct:nutritionProductList){
            if(nutritionProduct.getNutrient().size()>4)nList.add(nutritionProduct);
        }
        name.setCellValueFactory(data-> new SimpleObjectProperty(data.getValue().getCode().getText()));
        kalorien.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getNutrient().get(0).getAmount().get(0).getNumerator().getValue()));
        table.setItems(nList);

        table.setRowFactory(t-> {
            TableRow<NutritionProduct> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2 && (!row.isEmpty())) {
                        nutritionProduct = row.getItem();
                        Stage stage=(Stage)row.getTableView().getScene().getWindow();
                        if(menusetController!=null)menusetController.setNutritionProduct(nutritionProduct);
                        if(nahrungsmittelHinzufugenController!=null)nahrungsmittelHinzufugenController.setNutritionProduct(nutritionProduct);
                        //System.out.println(nutritionProduct.getCategory().get(0).getCoding().get(0).getDisplay());
                        stage.close();
                    }
                }
            });
            return row;
        });
    }

    public NutritionProduct setNutritionProduct(){
        return nutritionProduct;
    }

    public void setMenusetController(MenusetController menusetController){
        this.menusetController=menusetController;
    }

    public void setNahrungsmittelHinzufugenController(NahrungsmittelHinzufugenController nahrungsmittelHinzufugenController){
        this.nahrungsmittelHinzufugenController=nahrungsmittelHinzufugenController;
    }



    @FXML
    public void suchen(Event event) {
        if(suchFeld.getText().replaceAll(" ","").equals("")){
            table.setItems(nList);
        }
        ObservableList<NutritionProduct> nutritionProducts = FXCollections.observableArrayList();
        int i=0;
        while(i<nList.size()){
            if((nList.get(i).getCode().getText()!=null)&&(nList.get(i).getCode().getText()).replaceAll(" ","").toLowerCase().contains(suchFeld.getText().replaceAll(" ","").toLowerCase())){
                nutritionProducts.add(nList.get(i));
            }
            i++;
        }
        table.setItems(nutritionProducts);
    }
}
