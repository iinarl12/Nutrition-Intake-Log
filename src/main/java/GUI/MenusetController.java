package GUI;

import Datenhaltung.DBBenutzer;
import Datenhaltung.DBConnect;
import Datenhaltung.DBMenuset;
//import Datenhaltung.Datenbank;
import Fachlogik.*;
import Fachlogik.Menu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hl7.fhir.r5.model.NutritionProduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenusetController {

    @FXML
    private ScrollPane menuset;
    @FXML
    private Pane pane;
    @FXML
    private TextField name;
    @FXML
    private Text benutzer;

    private List<Benutzer> benutzerList=new ArrayList<>();
    private List<MenusetInput> menuInput=new ArrayList<>();
    private List<Menu> menuList= new ArrayList<>();
    private Benutzer selectedBenutzer;



    private BenutzerController benutzerController;
    private MenusetTableController menusetTableController;
    private NahrungsmittelStammdatenController nahrungsmittelStammdatenController;

    private NutritionProduct nutritionProduct=new NutritionProduct();

    DBConnect dbConnect = new DBConnect();
    DBMenuset dbMenuset=new DBMenuset(dbConnect);


    @FXML
    public void initialize(){
        menuset.setContent(pane);
        Button hinzufugen=new Button();
        Button speichern= new Button();
        add(hinzufugen,speichern);
        pane.getChildren().add(hinzufugen);
        pane.getChildren().add(speichern);
    }

    public void refresh(){
        menuInput.clear();
        pane.getChildren().removeAll(pane.getChildren());
        pane.getChildren().add(benutzer);
        pane.getChildren().add(name);
        menuset.setContent(pane);
        Button hinzufugen=new Button();
        Button speichern= new Button();
        add(hinzufugen,speichern);
        pane.getChildren().add(hinzufugen);
        pane.getChildren().add(speichern);
    }

    public void getBenutzerController(BenutzerController benutzerController){
        this.benutzerController=benutzerController;
    }

    public void getMenusetTableController(MenusetTableController menusetTableController){
        this.menusetTableController=menusetTableController;
    }


    public void setDbConnect(DBConnect dbConnect){
        this.dbConnect=dbConnect;
    }



    public void setBenutzerTitle(Benutzer benutzer){
        this.benutzer.setText(benutzer.getVorname());
    }

    public void setBenutzer(Benutzer selectedBenutzer){
        this.selectedBenutzer=selectedBenutzer;
    }

    public void add(Button hinzufugen, Button speichern) {

        MenusetInput m=new MenusetInput();
        menuInput.add(m);
        int ind=menuInput.indexOf(m);



        m.setNahrungsmittelAuswahl(ind, pane);
        m.getSelberEintragen().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                m.getNahrungsmittelAuswahlen().setText("neu eintragen");
                if(m.getStammdaten()!=null){
                    pane.getChildren().remove(m.getStammdaten());
                    menuInput.remove(m.getStammdaten());
                }
                m.setNahrungsmittel(menuInput.indexOf(m), pane);
            }
        });

        m.getSuchen().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NahrungsmittelStammdaten.fxml"));

                Scene scene = null;
                Stage stage= new Stage();
                try {
                    scene = new Scene(fxmlLoader.load());
                    nahrungsmittelStammdatenController=fxmlLoader.getController();
                    nahrungsmittelStammdatenController.setMenusetController(MenusetController.this);
                    nutritionProduct=nahrungsmittelStammdatenController.setNutritionProduct();

                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(pane.getScene().getWindow());
                    stage.setTitle("Nahrungsmittel suchen");
                    stage.showAndWait();

                    NutritionProduct nutritionProductSave= nutritionProduct;
                    m.setNutritionProduct(nutritionProductSave);
                    m.getNahrungsmittelAuswahlen().setText("Nahrungsmittel suchen");

                    if(m.getNahrungsmittel()!=null){
                        pane.getChildren().remove(m.getNahrungsmittel());
                        menuInput.remove(m.getNahrungsmittel());
                    }
                    if(!pane.getChildren().contains(m.getStammdaten())&&nutritionProductSave!=null) {
                        m.getStammdaten().setLayoutX(248);
                        m.getStammdaten().setLayoutY(115 + (50 * menuInput.indexOf(m)));
                        m.getStammdaten().setText(nutritionProductSave.getCode().getText());
                        pane.getChildren().add(m.getStammdaten());
                        MenusetController.this.nutritionProduct = new NutritionProduct();
                    }
                    else if(nutritionProductSave!=null){
                        m.getStammdaten().setText(nutritionProductSave.getCode().getText());
                        MenusetController.this.nutritionProduct = new NutritionProduct();
                    }
                }

                catch (IOException e) {
                    e.printStackTrace();
                }




            }
        });


        m.setMenge(ind,pane);
        m.setEinheiten(ind,pane);
        m.setDelete(ind,pane);
        m.getDelete().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                menuInput.remove(m);

                pane.setPrefHeight(471 + (64* menuInput.size()));

                pane.getChildren().remove(m.getNahrungsmittelAuswahlen());
                if(m.getNahrungsmittel()!=null)pane.getChildren().remove(m.getNahrungsmittel());
                if(m.getStammdaten()!=null)pane.getChildren().remove(m.getStammdaten());
                pane.getChildren().remove(m.getMenge());
                pane.getChildren().remove(m.getEinheiten());
                pane.getChildren().remove(m.getDelete());
                menuInput.remove(m);


                hinzufugen.setLayoutY(146 + (50* (menuInput.size()-1)));
                speichern.setLayoutY(146 + (50*(menuInput.size()-1)));


                for(int i=0;i<menuInput.size();i++){
                    menuInput.get(i).setYaxis(i,pane);
                }


            }
        });

        hinzufugen.setLayoutX(50);
        hinzufugen.setLayoutY(144 + (50* (menuInput.size()-1)));
        hinzufugen.setText("Hinzufugen");
        hinzufugen.setPrefWidth(78);
        hinzufugen.setPrefHeight(25);

        speichern.setLayoutX(679);
        speichern.setLayoutY(144 + (50*(menuInput.size()-1)));
        speichern.setText("Speichern");
        speichern.setPrefWidth(77);
        speichern.setPrefHeight(25);

        pane.setPrefHeight(471 + (50*menuInput.size()));

        hinzufugen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                add(hinzufugen,speichern);
            }
        });

        speichern.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                for (int i = 0; i < menuInput.size(); i++) {
                    //System.out.println("nahrungsmittel " + menuInput.get(i).getNahrungsmittel() + " menge: " + menuInput.get(i).getMenge() + " einheiten:" + menuInput.get(i).getEinheiten());
                    String nm = "";
                    String nutritionProductId=null;

                    if(menuInput.get(i).getNahrungsmittel()!=null){
                        nm= menuInput.get(i).getNahrungsmittel().getText();

                    }
                    else{
                        nm=menuInput.get(i).getNutritionProduct().getCode().getText();
                        String[] id=menuInput.get(i).getNutritionProduct().getId().split("/");
                        nutritionProductId=id[5];
                    }
                    int menge = 0;
                    Einheit ei = null;
                    menge = Integer.parseInt(menuInput.get(i).getMenge().getText());
                    if (menuInput.get(i).getEinheiten().getText() == "Gramm") {
                        ei = Einheit.gramm;
                    } else if (menuInput.get(i).getEinheiten().getText() == "Milliliter") {
                        ei = Einheit.milliliter;
                    } else if (menuInput.get(i).getEinheiten().getText() == "Stück") {
                        ei = Einheit.stueck;
                    }
                    Menu menu = new Menu(nm, menge, ei,nutritionProductId);
                    menuList.add(menu);

                }
                Menuset menuset= new Menuset(selectedBenutzer, name.getText(),menuList);

                dbMenuset.addMenuDB(menuset);
                Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,
                        "Menuset würde erfolgreich gespeichert",ButtonType.OK);
                a1.show();
                name.setText("");
                menuInput.clear();
                menuList.clear();
                menusetTableController.populateTable();
                refresh();

            }
        });
    }

    public void setNutritionProduct(NutritionProduct nutritionProduct){
        this.nutritionProduct=nutritionProduct;
    }



}
