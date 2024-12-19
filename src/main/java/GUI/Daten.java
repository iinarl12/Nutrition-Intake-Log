package GUI;

import Fachlogik.Menuset;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.hl7.fhir.r5.model.NutritionProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class Daten{

    private TextField hour;
    private TextField minute;
    private MenuButton nahrungsmittelAuswaehlenButton;
    private TextArea beschwerde;
    private Button deleteButton;

    double yZeitNahrungsmittelBeschwerde=92;
    double addY=64;


    private MenuButton einheiten;
    private TextField menge;

    private TextField selbstEingabeTextfield;

    private Text suchErgebnis;
    private NutritionProduct nutritionProduct;

    private MenuButton menusetButton;
    private List<Menuset> gescpeicherteMenusetListe =new ArrayList<>();


    public Daten() {}

    public TextField getHour() {
        return hour;
    }
    public TextField getMinute() {
        return minute;
    }

    public void setZeit(int n, Pane pane) {
        TextField hour=new TextField();
        hour.setTextFormatter(new TextFormatter<>(change -> {
            if(change.getText().matches("[0-9]*")) return change;
            return null;

        }));

        hour.setLayoutX(17);
        hour.setLayoutY(yZeitNahrungsmittelBeschwerde + (addY*n));
        hour.setPromptText("HH");
        hour.setPrefWidth(37);
        hour.setPrefHeight(25);
        pane.getChildren().add(hour);


        TextField minute=new TextField();
        minute.setLayoutX(60);
        minute.setLayoutY(yZeitNahrungsmittelBeschwerde + (addY*n));
        minute.setPromptText("MM");
        minute.setPrefWidth(37);
        minute.setPrefHeight(25);
        minute.setTextFormatter(new TextFormatter<>(change -> {
            if(change.getText().matches("[0-9]*")) return change;
            return null;

        }));
        pane.getChildren().add(minute);
        this.hour = hour;
        this.minute=minute;
    }

    public void deleteAlle(Pane pane){
        pane.getChildren().remove(hour);
        pane.getChildren().remove(minute);
        pane.getChildren().remove(nahrungsmittelAuswaehlenButton);
        pane.getChildren().remove(beschwerde);
        pane.getChildren().remove(deleteButton);

    }

    public void setNahrungsmittelAuswaehlenButton(int n, Pane pane) {
        MenuButton nahrungsmittelI= new MenuButton();
        nahrungsmittelI.setLayoutX(106);
        nahrungsmittelI.setLayoutY(yZeitNahrungsmittelBeschwerde + (addY*n));
        nahrungsmittelI.setText("Nahrungsmittel");
        nahrungsmittelI.setPrefWidth(115);
        nahrungsmittelI.setPrefHeight(25);
        pane.getChildren().add(nahrungsmittelI);
        this.nahrungsmittelAuswaehlenButton =nahrungsmittelI;
    }

    public MenuButton getNahrungsmittelAuswaehlenButton() {
        return nahrungsmittelAuswaehlenButton;
    }

    public TextArea getBeschwerde() {
        return beschwerde;
    }

    public void setBeschwerde(int n, Pane pane) {
        TextArea beschwerdeI=new TextArea();
        beschwerdeI.setLayoutX(532);
        beschwerdeI.setLayoutY(yZeitNahrungsmittelBeschwerde + (addY*n));
        beschwerdeI.setPromptText("Beschwerde");
        beschwerdeI.setPrefWidth(153);
        beschwerdeI.setPrefHeight(58);
        pane.getChildren().add(beschwerdeI);
        this.beschwerde = beschwerdeI;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(int n, Pane pane) {
        Button deleteB = new Button();
        deleteB.setLayoutX(691);
        deleteB.setLayoutY(yZeitNahrungsmittelBeschwerde + (64*n));
        deleteB.setText("Löschen");
        deleteB.setTextFill(Paint.valueOf("RED"));
        pane.getChildren().add(deleteB);
        this.deleteButton =deleteB;
    }
    public void setYaxis(int n,Pane pane){
        hour.setLayoutY(92 + (64*n));
        minute.setLayoutY(92 + (64*n));
        nahrungsmittelAuswaehlenButton.setLayoutY(92 + (64*n));
        if(selbstEingabeTextfield !=null) selbstEingabeTextfield.setLayoutY(92 + (64*n));
        if(einheiten!=null) einheiten.setLayoutY(92 + (64*n));
        if(menge!=null) menge.setLayoutY(92 + (64*n));
        if(menusetButton !=null) menusetButton.setLayoutY(92 + (64*n));
        if(suchErgebnis!=null) suchErgebnis.setLayoutY(109+(64*n));
        beschwerde.setLayoutY(92 + (64*n));
        deleteButton.setLayoutY(92 + (64*n));
    }



    public TextField getMenge() {
        return menge;
    }

    public void setMenge(int n, Pane pane) {
        TextField menge=new TextField();
        menge.setLayoutX(373);
        menge.setLayoutY(yZeitNahrungsmittelBeschwerde + (addY*n));
        menge.setPromptText("Menge");
        menge.setPrefWidth(54);
        menge.setPrefHeight(25);
        menge.setTextFormatter(new TextFormatter<>(change -> {
            if(change.getText().matches("[0-9]*")) return change;
            return null;

        }));
        pane.getChildren().add(menge);
        this.menge = menge;
    }

    public MenuButton getEinheiten() {
        return einheiten;
    }

    public void setEinheiten(int n, Pane pane) {
        einheiten= new MenuButton();
        einheiten.setLayoutX(436);
        einheiten.setLayoutY(yZeitNahrungsmittelBeschwerde + (addY*n));
        einheiten.setText("Einheiten");
        einheiten.setPrefWidth(83);
        einheiten.setPrefHeight(25);
        pane.getChildren().add(einheiten);

        MenuItem gramm= new MenuItem();
        gramm.setText("Gramm");
        einheiten.getItems().add(gramm);
        gramm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                einheiten.setText("Gramm");
            }
        });
        MenuItem mililiter= new MenuItem();
        mililiter.setText("Milliliter");
        einheiten.getItems().add(mililiter);
        mililiter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                einheiten.setText("Milliliter");
            }
        });
        MenuItem stueck= new MenuItem();
        stueck.setText("Stück");
        einheiten.getItems().add(stueck);
        stueck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                einheiten.setText("Stück");
            }
        });
    }



    public TextField getSelbstEingabeTextfield() {
        return selbstEingabeTextfield;
    }

    public void setSelberEintragen(int n, Pane pane){
        setSelbstEingabeTextfiedl(n,pane);
        setMenge(n,pane);
        setEinheiten(n,pane);
    }

    public void setSelbstEingabeTextfiedl(int n, Pane pane) {
        TextField eingabeI=new TextField();
        eingabeI.setLayoutX(231);
        eingabeI.setLayoutY(yZeitNahrungsmittelBeschwerde + (addY*n));
        eingabeI.setPromptText("Nahrungsmittel");
        eingabeI.setPrefWidth(130);
        eingabeI.setPrefHeight(25);
        pane.getChildren().add(eingabeI);
        this.selbstEingabeTextfield = eingabeI;
    }

    public void deleteSelbstEingabe(Pane pane){

        pane.getChildren().remove(selbstEingabeTextfield);
        pane.getChildren().remove(menge);
        pane.getChildren().remove(einheiten);

        this.selbstEingabeTextfield =null;
        this.einheiten=null;
        this.menge=null;




    }


    public NutritionProduct getNutritionProduct() {
        return nutritionProduct;
    }

    public void setNutritionProduct(NutritionProduct nutritionProduct) {
        this.nutritionProduct = nutritionProduct;
    }

    public Text getSuchErgebnis() {
        return suchErgebnis;
    }
    public void setSuchErgebnis(int n, Pane pane){
        suchErgebnis=new Text();
        suchErgebnis.setLayoutX(242);
        suchErgebnis.setLayoutY(109 + (addY*n));
        pane.getChildren().add(suchErgebnis);
        setMenge(n,pane);
        setEinheiten(n,pane);
    }
    public void deleteSuchErgebnis(Pane pane){

        pane.getChildren().remove(suchErgebnis);
        pane.getChildren().remove(einheiten);
        pane.getChildren().remove(menge);

        this.einheiten=null;
        this.suchErgebnis=null;
        this.menge=null;
    }




    public MenuButton getMenusetButton() {
        return menusetButton;
    }

    public void setMenusetButton(int n, Pane pane, List<MenuItem> miList) {
        if(selbstEingabeTextfield !=null){
            pane.getChildren().remove(selbstEingabeTextfield);
            selbstEingabeTextfield =null;
        }
        MenuButton menuSet1=new MenuButton();
        menuSet1.setLayoutX(281);
        menuSet1.setLayoutY(yZeitNahrungsmittelBeschwerde + (addY*n));
        menuSet1.setText("Menu auswählen");
        menuSet1.setPrefWidth(185);
        menuSet1.setPrefHeight(25);
        pane.getChildren().add(menuSet1);
        menuSet1.getItems().addAll(miList);
        this.menusetButton =menuSet1;
    }

    public void setGescpeicherteMenusetListe(List<Menuset> gescpeicherteMenusetListe) {
        this.gescpeicherteMenusetListe = gescpeicherteMenusetListe;
    }

    public List<Menuset> getGespeicherteMenusetListe() {
        return gescpeicherteMenusetListe;
    }



    public void deleteMenuButton(Pane pane){
        pane.getChildren().remove(menusetButton);


        this.menusetButton =null;
        this.gescpeicherteMenusetListe =null;
    }



}
