package GUI;

import FHIR.Nahrungsmittelstammdaten;
import Fachlogik.Einheit;
import Fachlogik.Tagsuebersicht;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.hl7.fhir.r5.model.NutritionProduct;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class TagebuchNaehrwerteController {


    @javafx.fxml.FXML
    private Text kalorienValue;
    @javafx.fxml.FXML
    private Text zuckerValue;
    @javafx.fxml.FXML
    private Text fettValue;
    @javafx.fxml.FXML
    private Text proteinValue;
    @javafx.fxml.FXML
    private Text kohlenhydrateValue;
    @javafx.fxml.FXML
    private Text naehrwerte;

    private Tagsuebersicht tagsuebersicht;

    private TagebuchTableController tagebuchTableController;
    Nahrungsmittelstammdaten nahrungsmittelstammdaten= new Nahrungsmittelstammdaten();
    double menge=0;
    double denominator;


    @FXML
    public void initialize() throws IOException {

    }
    public void setNaehrwerte(){
        naehrwerte.setText(tagsuebersicht.getNahrungsmittel());
        String kalorie="";
        String kohlenhydrate="";
        String zucker="";
        String ballaststoff="";
        String fett="";
        String protein="";
        String alkohol="";
        if(tagsuebersicht.getNutritionFHIRId()!=null){
            NutritionProduct nutritionProduct= nahrungsmittelstammdaten.getNutritionProduct(tagsuebersicht.getNutritionFHIRId());
            denominator=Double.valueOf(nutritionProduct.getNutrient().get(0).getAmount().get(0).getDenominator().getValue().doubleValue());
            menge=tagsuebersicht.getMenge();

            String pattern="###,###.00";
            DecimalFormat df=new DecimalFormat(pattern);


            if(tagsuebersicht.getEinheit().equals(Einheit.gramm)||tagsuebersicht.getEinheit().equals(Einheit.milliliter)){
                kalorie =  df.format(menge/denominator*nutritionProduct.getNutrient().get(0).getAmount().get(0).getNumerator().getValue().doubleValue());
                kohlenhydrate=df.format(menge/denominator*nutritionProduct.getNutrient().get(1).getAmount().get(0).getNumerator().getValue().doubleValue());
                zucker=df.format(menge/denominator*nutritionProduct.getNutrient().get(2).getAmount().get(0).getNumerator().getValue().doubleValue());
                fett=df.format(menge/denominator*nutritionProduct.getNutrient().get(3).getAmount().get(0).getNumerator().getValue().doubleValue());
                protein=df.format(menge/denominator*nutritionProduct.getNutrient().get(4).getAmount().get(0).getNumerator().getValue().doubleValue());
                /*ballaststoff=df.format(menge/denominator*nutritionProduct.getNutrient().get(3).getAmount().get(0).getNumerator().getValue().doubleValue());
                alkohol=df.format(menge/denominator*nutritionProduct.getNutrient().get(6).getAmount().get(0).getNumerator().getValue().doubleValue());*/
            }
            else if (tagsuebersicht.getEinheit().equals(Einheit.stueck)){
                kalorie =df.format(menge*nutritionProduct.getNutrient().get(0).getAmount().get(0).getNumerator().getValue().doubleValue());
                kohlenhydrate=df.format(menge*nutritionProduct.getNutrient().get(1).getAmount().get(0).getNumerator().getValue().doubleValue());
                zucker=df.format(menge*nutritionProduct.getNutrient().get(2).getAmount().get(0).getNumerator().getValue().doubleValue());
                fett=df.format(menge*nutritionProduct.getNutrient().get(3).getAmount().get(0).getNumerator().getValue().doubleValue());
                protein=df.format(menge*nutritionProduct.getNutrient().get(4).getAmount().get(0).getNumerator().getValue().doubleValue());
                /*ballaststoff=df.format(menge*nutritionProduct.getNutrient().get(3).getAmount().get(0).getNumerator().getValue().doubleValue());
                alkohol=df.format(menge*nutritionProduct.getNutrient().get(6).getAmount().get(0).getNumerator().getValue().doubleValue());*/
            }

            kalorienValue.setText(kalorie+"");
            kohlenhydrateValue.setText(kohlenhydrate+"");
            zuckerValue.setText(zucker+"");
            fettValue.setText(fett+"");
            proteinValue.setText(protein+"");
/*
            ballaststoffValue.setText(ballaststoff+"");
            alkoholValue.setText(alkohol+"");*/
        }
        else{
            kalorienValue.setText("N/A");
            kohlenhydrateValue.setText("N/A");
            zuckerValue.setText("N/A");
            fettValue.setText("N/A");
            proteinValue.setText("N/A");
            /*ballaststoffValue.setText("N/A");
            alkoholValue.setText("N/A");*/

        }
    }

    public void setTagsuebersicht(Tagsuebersicht tagsuebersicht){
        this.tagsuebersicht=tagsuebersicht;
    }






}
