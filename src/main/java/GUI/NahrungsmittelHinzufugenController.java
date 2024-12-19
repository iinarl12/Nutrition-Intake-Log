package GUI;

import Datenhaltung.DBConnect;
import Datenhaltung.DBTagesuebersicht;
import FHIR.Tagebuch;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NahrungsmittelHinzufugenController {

    @FXML
    private Text benutzer;
    @FXML
    private ScrollPane nahrungsmittelHinzufugen;
    @FXML
    private Pane pane;
    @FXML
    DatePicker datum;
    @FXML
    private Text zeit;


    private List<Daten> datenList = new ArrayList<>();

    private Benutzer selectedBenutzer = new Benutzer();

    private NahrungsmittelStammdatenController nahrungsmittelStammdatenController;

    private NutritionProduct nutritionProduct=new NutritionProduct();

    HashMap<Integer, MenuItem> menusetList = new HashMap<>();

    DBConnect dbConnect=new DBConnect();
    private DBTagesuebersicht dbTagesuebersicht = new DBTagesuebersicht(dbConnect);


    @FXML
    public void initialize() {
        nahrungsmittelHinzufugen.setContent(pane);

        Button hinzufugen = new Button();
        Button speichern = new Button();

        add(hinzufugen, speichern);

        pane.getChildren().add(hinzufugen);
        pane.getChildren().add(speichern);
    }

    public void setDbConnect(DBConnect dbConnect){
        this.dbConnect=dbConnect;
    }

    public void setNutritionProduct(NutritionProduct nutritionProduct){
        this.nutritionProduct=nutritionProduct;
    }

    public void setBenutzerTitle(Benutzer benutzer) {
        this.benutzer.setText(benutzer.getVorname());
    }

    public void setBenutzer(Benutzer selectedBenutzer) {
        this.selectedBenutzer = selectedBenutzer;
    }

    public void refresh(){
        datenList.clear();
        datum.setValue(null);
        pane.getChildren().removeAll(pane.getChildren());
        pane.getChildren().add(datum);
        pane.getChildren().add(zeit);
        pane.getChildren().add(benutzer);
        Button hinzufugen = new Button();
        Button speichern = new Button();

        add(hinzufugen, speichern);

        pane.getChildren().add(hinzufugen);
        pane.getChildren().add(speichern);
        nahrungsmittelHinzufugen.setContent(pane);
    }



    public List<MenuItem> populateMenuset() throws SQLException {
        List<MenuItem> menuItems= new ArrayList<>();

        int idBenutzer= selectedBenutzer.getBenutzerId();
        Statement mystatement  = DBConnect.getInstance().getCon().createStatement();
        try{
            ResultSet benutzerDB=mystatement.executeQuery("select * from menuset where id_benutzer="+idBenutzer);
            while(benutzerDB.next()){
                MenuItem mi=new MenuItem();
                mi.setText(benutzerDB.getString(3));
                menusetList.put(benutzerDB.getInt(1),mi);
                menuItems.add(mi);
            }
            if(menusetList.size()==0){
                throw new Exception();
            }

            return menuItems;
        }
        catch (Exception e){
            Alert a1 = new Alert(Alert.AlertType.WARNING,
                    "Keine gespeicherte Menusets",ButtonType.OK);
            a1.show();

        }
        return null;
    }

    public boolean datumValid(){
        if(datum.getValue().isBefore(LocalDate.now())||datum.getValue().isEqual(LocalDate.now())) return true;
        Alert a1 = new Alert(Alert.AlertType.WARNING,
                "Bitte gultiges Datum eingeben",ButtonType.OK);
        a1.show();
        return false;
    }

    public boolean zeitValid(){
        for (Daten daten : datenList) {
            try {
                LocalTime time = LocalTime.parse(daten.getHour().getText() + ":" + daten.getMinute().getText());
                return true;
            } catch (Exception e) {
                Alert a1 = new Alert(Alert.AlertType.WARNING,
                        "Bitte gultige Zeit eingeben(MAX: 23:59)", ButtonType.OK);
                a1.show();

            }
        }
        return false;
    }


    public void add(Button hinzufugen, Button speichern) {

        Daten d = new Daten();
        datenList.add(d);
        int ind = datenList.indexOf(d);

        d.setZeit(ind, pane);
        d.setNahrungsmittelAuswaehlenButton(ind, pane);


        MenuItem selberEintragenI = new MenuItem();
        selberEintragenI.setText("neu eintragen");
        d.getNahrungsmittelAuswaehlenButton().getItems().add(selberEintragenI);
        selberEintragenI.setOnAction(actionEvent -> {

            if(d.getMenusetButton()!=null)d.deleteMenuButton(pane);
            if(d.getSuchErgebnis()!=null)d.deleteSuchErgebnis(pane);

            d.setSelberEintragen(datenList.indexOf(d), pane);

        });


        MenuItem suchen = new MenuItem();
        suchen.setText("suchen");
        d.getNahrungsmittelAuswaehlenButton().getItems().add(suchen);
        suchen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(d.getSelbstEingabeTextfield()!=null)d.deleteSelbstEingabe(pane);
                if(d.getMenusetButton()!=null)d.deleteMenuButton(pane);



                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NahrungsmittelStammdaten.fxml"));

                Scene scene = null;
                Stage stage= new Stage();
                try {
                    scene = new Scene(fxmlLoader.load());
                    nahrungsmittelStammdatenController=fxmlLoader.getController();
                    nahrungsmittelStammdatenController.setNahrungsmittelHinzufugenController(NahrungsmittelHinzufugenController.this);
                    nutritionProduct=nahrungsmittelStammdatenController.setNutritionProduct();

                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(pane.getScene().getWindow());
                    stage.setTitle("Nahrungsmittel suchen");
                    stage.showAndWait();

                    NutritionProduct nutritionProductSave= nutritionProduct;
                    d.setNutritionProduct(nutritionProductSave);
                    d.getNahrungsmittelAuswaehlenButton().setText("Nahrungsmittel suchen");


                    if(!pane.getChildren().contains(d.getSuchErgebnis())) {
                        d.setSuchErgebnis(datenList.indexOf(d),pane);
                        if(nutritionProductSave!=null)d.getSuchErgebnis().setText(nutritionProductSave.getCode().getText());

                        NahrungsmittelHinzufugenController.this.nutritionProduct = new NutritionProduct();
                    }
                    else{
                        d.getSuchErgebnis().setText(nutritionProductSave.getCategory().get(0).getCoding().get(0).getDisplay());
                        NahrungsmittelHinzufugenController.this.nutritionProduct = new NutritionProduct();
                    }
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        MenuItem menuSetI = new MenuItem();
        d.getNahrungsmittelAuswaehlenButton().getItems().add(menuSetI);
        menuSetI.setText("aus Menuset auswählen");
        menuSetI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(d.getSelbstEingabeTextfield()!=null){
                    System.out.println(d.getSelbstEingabeTextfield());
                    d.deleteSelbstEingabe(pane);
                }
                if(d.getSuchErgebnis()!=null)d.deleteSuchErgebnis(pane);


                try {
                    if (populateMenuset() != null) {
                        d.setMenusetButton(datenList.indexOf(d), pane, populateMenuset());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                for (Map.Entry<Integer, MenuItem> set : menusetList.entrySet()) {
                    set.getValue().setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            System.out.println(d.getSelbstEingabeTextfield()+"  tes");
                            List<Menuset> menusets=new ArrayList<>();
                            Statement mystatement = null;
                            try {
                                d.getMenusetButton().setText(set.getValue().getText());
                                mystatement = DBConnect.getInstance().getCon().createStatement();
                                ResultSet menuDB = mystatement.executeQuery("select * from menuset where id_benutzer=" + selectedBenutzer.getBenutzerId() + " and menuname=\"" + set.getValue().getText() + "\";");
                                while (menuDB.next()) {
                                    List<Menu> menuList= new ArrayList<>();
                                    Menuset menuset= new Menuset();
                                    Statement mystatementMenu = DBConnect.getInstance().getCon().createStatement();
                                    ResultSet menuA = mystatementMenu.executeQuery("select * from menu where id_menuset="+menuDB.getInt(1)+";");

                                    while(menuA.next()){
                                        String nah = menuA.getString(3);
                                        int me = menuA.getInt(4);
                                        Einheit ei;
                                        if (menuA.getString(5).toString().equals("gramm")) {
                                            ei = Einheit.gramm;
                                        } else if (menuA.getString(5).toString().equals("milliliter")){
                                            ei = Einheit.milliliter;
                                        } else ei=Einheit.stueck;
                                        String nutritionProductId= menuA.getString(6);
                                        Menu m= new Menu(nah,me,ei,nutritionProductId);
                                        menuList.add(m);
                                    }
                                    menuset.setMenu(menuList);
                                    menusets.add(menuset);
                                }
                                d.setGescpeicherteMenusetListe(menusets);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });

        d.setBeschwerde(ind, pane);
        d.setDeleteButton(ind, pane);
        d.getDeleteButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                pane.setPrefHeight(471 + (64 * datenList.size()));

                d.deleteAlle(pane);

                if (d.getSelbstEingabeTextfield() != null)d.deleteSelbstEingabe(pane);
                if (d.getMenusetButton() != null)d.deleteMenuButton(pane);
                if(d.getSuchErgebnis()!=null)d.deleteSuchErgebnis(pane);

                datenList.remove(d);


                hinzufugen.setLayoutY(170 + (64 * (datenList.size() - 1)));
                speichern.setLayoutY(170 + (64 * (datenList.size() - 1)));


                for (int i = 0; i < datenList.size(); i++) {
                    datenList.get(i).setYaxis(i, pane);
                }


            }
        });


        hinzufugen.setLayoutX(17);
        hinzufugen.setLayoutY(170 + (64 * (datenList.size() - 1)));
        hinzufugen.setText("Hinzufugen");
        hinzufugen.setPrefWidth(80);
        hinzufugen.setPrefHeight(25);

        speichern.setLayoutX(682);
        speichern.setLayoutY(170 + (64 * (datenList.size() - 1)));
        speichern.setText("Speichern");
        speichern.setPrefWidth(70);
        speichern.setPrefHeight(25);


        pane.setPrefHeight(400 + (64 * datenList.size()));

        hinzufugen.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                add(hinzufugen, speichern);
            }
        });

        speichern.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(datumValid()&&zeitValid()){
                    LocalDate date = datum.getValue();
                    for (int i = 0; i < datenList.size(); i++) {

                        LocalTime time = LocalTime.parse(datenList.get(i).getHour().getText() + ":" + datenList.get(i).getMinute().getText());

                        String nm = "";
                        int menge = 0;
                        Einheit ei = null;
                        if (datenList.get(i).getSelbstEingabeTextfield() != null) {
                            nm = datenList.get(i).getSelbstEingabeTextfield().getText();
                            menge = Integer.parseInt(datenList.get(i).getMenge().getText());
                            System.out.println("Nahrungsmitellhinzufugen menge " + menge);
                            if (datenList.get(i).getEinheiten().getText() == "Gramm") {
                                ei = Einheit.gramm;
                            } else if(datenList.get(i).getEinheiten().getText() == "Milliliter"){
                                ei = Einheit.milliliter;
                            }
                            else{
                                ei=Einheit.stueck;
                            }
                            System.out.println("tes   "+ nm);

                            String bes = datenList.get(i).getBeschwerde().getText();
                            Tagsuebersicht tagsuebersicht= new Tagsuebersicht(date,selectedBenutzer,time,nm, menge,ei,bes);

                            tagsuebersicht.setNutritionFHIRId(null);
                            dbTagesuebersicht.addTagsuebersicht(tagsuebersicht);
                            Tagebuch tagebuch= new Tagebuch();
                            tagebuch.saveTagebuch(selectedBenutzer,tagsuebersicht);

                            Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,
                                    "Tagebuch würde erfolgreich gespeichert",ButtonType.OK);
                            a1.show();
                        }
                        else if(datenList.get(i).getMenusetButton() != null){
                            for (Menuset m : datenList.get(i).getGespeicherteMenusetListe()) {
                                for(Menu me:m.getMenu()){
                                    nm = me.getNahrungsmittel();
                                    menge = me.getMenge();
                                    ei = me.getEinheit();
                                    String bes = datenList.get(i).getBeschwerde().getText();
                                    Tagsuebersicht tagsuebersicht= new Tagsuebersicht(date,selectedBenutzer,time,nm, menge,ei,bes);
                                    if(me.getNutritionProductId()!=null)tagsuebersicht.setNutritionFHIRId(me.getNutritionProductId());
                                    dbTagesuebersicht.addTagsuebersicht(tagsuebersicht);
                                    Tagebuch tagebuch= new Tagebuch();
                                    tagebuch.saveTagebuch(selectedBenutzer,tagsuebersicht);
                                }
                            }
                            Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,
                                    "Tagebuch würde erfolgreich gespeichert",ButtonType.OK);
                            a1.show();
                        }
                        else{
                            nm = datenList.get(i).getNutritionProduct().getCode().getText();
                            menge = Integer.parseInt(datenList.get(i).getMenge().getText());
                            System.out.println("Nahrungsmitellhinzufugen menge " + menge);
                            if (datenList.get(i).getEinheiten().getText().equals( "Gramm")) {
                                ei = Einheit.gramm;
                            } else if(datenList.get(i).getEinheiten().getText().equals("Milliliter")){
                                ei = Einheit.milliliter;
                            }
                            else{
                                ei=Einheit.stueck;
                            }

                            String bes = datenList.get(i).getBeschwerde().getText();
                            Tagsuebersicht tagsuebersicht= new Tagsuebersicht(date,selectedBenutzer,time,nm, menge,ei,bes);
                            String [] id= datenList.get(i).getNutritionProduct().getId().split("/");
                            //System.out.println(datenList.get(i).getNutritionProduct().getId());
                            tagsuebersicht.setNutritionFHIRId(id[5]);


                            dbTagesuebersicht.addTagsuebersicht(tagsuebersicht);
                            Tagebuch tagebuch= new Tagebuch();
                            tagebuch.saveTagebuch(selectedBenutzer,tagsuebersicht);

                            Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,
                                    "Tagebuch würde erfolgreich gespeichert",ButtonType.OK);
                            a1.show();

                        }

                    }

                }
                refresh();
            }

        });
    }


}
