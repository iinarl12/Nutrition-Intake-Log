package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hl7.fhir.r5.model.NutritionProduct;

import java.io.IOException;
import java.util.List;

public class MenusetInput {

    private TextField nahrungsmittel;
    private MenuButton einheiten;
    private TextField menge;
    private Button delete;
    private MenuButton nahrungsmittelAuswahlen;
    private MenuItem selberEintragen;
    private MenuItem suchen;
    Text stammdaten= new Text();

    private NutritionProduct nutritionProduct;

    public NutritionProduct getNutritionProduct() {
        return nutritionProduct;
    }

    public void setNutritionProduct(NutritionProduct nutritionProduct) {
        this.nutritionProduct = nutritionProduct;
    }

    public MenusetInput() {
    }

    public MenuItem getSelberEintragen() {
        return selberEintragen;
    }

    public MenuItem getSuchen() {
        return suchen;
    }



    public TextField getNahrungsmittel() {
        return nahrungsmittel;
    }

    public void setNahrungsmittelAuswahl(int n, Pane pane){
        nahrungsmittelAuswahlen=new MenuButton();
        nahrungsmittelAuswahlen.setLayoutX(50);
        nahrungsmittelAuswahlen.setLayoutY(98 + (50*n));
        nahrungsmittelAuswahlen.setText("Nahrungsmittel auswahlen");

        selberEintragen=new MenuItem();
        selberEintragen.setText("neu eintragen");
        nahrungsmittelAuswahlen.getItems().add(selberEintragen);

        suchen=new MenuItem();
        suchen.setText("suchen");
        nahrungsmittelAuswahlen.getItems().add(suchen);

        pane.getChildren().add(nahrungsmittelAuswahlen);
    }

    public void setNahrungsmittel(int n, Pane pane) {
        nahrungsmittel=new TextField();
        nahrungsmittel.setLayoutX(248);
        nahrungsmittel.setLayoutY(98 + (50*n));
        nahrungsmittel.setPromptText("Nahrungsmittel");
        nahrungsmittel.setPrefWidth(179);
        nahrungsmittel.setPrefHeight(25);
        pane.getChildren().add(nahrungsmittel);
    }

    public MenuButton getEinheiten() {
        return einheiten;
    }

    public void setEinheiten(int n, Pane pane) {
        MenuButton einheit= new MenuButton();
        einheit.setLayoutX(560);
        einheit.setLayoutY(98 + (50*n));
        einheit.setText("Einheiten");
        einheit.setPrefWidth(81);
        einheit.setPrefHeight(25);
        pane.getChildren().add(einheit);
        this.einheiten=einheit;
        MenuItem gramm= new MenuItem();
        gramm.setText("Gramm");
        einheit.getItems().add(gramm);
        gramm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                einheit.setText("Gramm");
            }
        });
        MenuItem mililiter= new MenuItem();
        mililiter.setText("Milliliter");
        einheit.getItems().add(mililiter);
        mililiter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                einheit.setText("Milliliter");
            }
        });

        MenuItem stuck= new MenuItem();
        stuck.setText("Stück");
        einheit.getItems().add(stuck);
        stuck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                einheit.setText("Stück");
            }
        });
    }

    public Text getStammdaten(){
        return  stammdaten;
    }

    public MenuButton getNahrungsmittelAuswahlen(){
        return nahrungsmittelAuswahlen;
    }

    public TextField getMenge() {
        return menge;
    }

    public void setMenge(int n, Pane pane) {
        TextField menge=new TextField();
        menge.setLayoutX(448);
        menge.setLayoutY(98 + (50*n));
        menge.setPromptText("Menge");
        menge.setPrefWidth(94);
        menge.setPrefHeight(25);
        menge.setTextFormatter(new TextFormatter<>(change -> {
            if(change.getText().matches("[0-9]*")) return change;
            return null;

        }));;
        pane.getChildren().add(menge);
        this.menge = menge;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(int n, Pane pane) {
        Button deleteB = new Button();
        deleteB.setLayoutX(696);
        deleteB.setLayoutY(98 + (50*n));
        deleteB.setText("Löschen");
        deleteB.setTextFill(Paint.valueOf("RED"));
        pane.getChildren().add(deleteB);
        this.delete=deleteB;
    }

    public void setYaxis(int n,Pane pane){
        nahrungsmittelAuswahlen.setLayoutY(98 + (50*n));
        if(stammdaten!=null)stammdaten.setLayoutY(115 + (50*n));
        if(nahrungsmittel!=null)nahrungsmittel.setLayoutY(98 + (50*n));
        einheiten.setLayoutY(98 + (50*n));
        menge.setLayoutY(98 + (50*n));
        delete.setLayoutY(98 + (50*n));
    }
}
