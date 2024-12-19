package Fachlogik;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Tagsuebersicht {

    private int tagebuchId;
    private Benutzer benutzer;
    private LocalDate datum;
    private String nahrungsmittel;
    private LocalTime zeit;
    private int menge;
    private Einheit einheit;
    private String beschwerde;
    private String fhirID;



    private String nutritionFHIRId;


    public Tagsuebersicht()
    {

    }

    public Tagsuebersicht(LocalDate datum, Benutzer benutzer, LocalTime zeit, String nahrungsmittel,int menge, Einheit einheit, String beschwerde) {

        this.nahrungsmittel=nahrungsmittel;
        this.benutzer = benutzer;
        this.datum = datum;
        this.zeit=zeit;
        this.menge=menge;
        this.einheit=einheit;
        this.beschwerde=beschwerde;
    }


    public Tagsuebersicht(int id, LocalDate datum, Benutzer benutzer, LocalTime zeit, String nahrungsmittel,int menge, Einheit einheit, String beschwerde, String nutritionFHIRId) {
        this.tagebuchId=id;
        this.benutzer = benutzer;
        this.datum = datum;
        this.zeit=zeit;
        this.menge=menge;
        this.einheit=einheit;
        this.beschwerde=beschwerde;
        this.nahrungsmittel=nahrungsmittel;
        this.nutritionFHIRId=nutritionFHIRId;
    }


    public String getFhirID() {
        return fhirID;
    }

    public void setFhirID(String fhirID) {
        this.fhirID = fhirID;
    }

    public int getTagebuchId() {
        return tagebuchId;
    }

    public void setTagebuchId(int tagebuchId) {
        this.tagebuchId = tagebuchId;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public String getNahrungsmittel() {
        return nahrungsmittel;
    }

    public void setNahrungsmittel(String nahrungsmittel) {
        this.nahrungsmittel = nahrungsmittel;
    }

    public LocalTime getZeit() {
        return zeit;
    }

    public void setZeit(LocalTime zeit) {
        this.zeit = zeit;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }

    public Einheit getEinheit() {
        return einheit;
    }

    public void setEinheit(Einheit einheit) {
        this.einheit = einheit;
    }

    public String getBeschwerde() {
        return beschwerde;
    }

    public void setBeschwerde(String beschwerde) {
        this.beschwerde = beschwerde;
    }

    public String getNutritionFHIRId() {
        return nutritionFHIRId;
    }

    public void setNutritionFHIRId(String nutritionFHIRId) {
        this.nutritionFHIRId = nutritionFHIRId;
    }

    @Override
    public String toString() {
        return "Tagebuch{" +
                "tagebuchId=" + tagebuchId +
                ", benutzer=" + benutzer +
                ", datum=" + datum +
                ", zeit=" + zeit +
                ", nahrungsmittel=" + nahrungsmittel +
                ", menge=" + menge +
                ", einheit=" + einheit +
                ", beschwerde=" + beschwerde+
                '}';
    }
}
