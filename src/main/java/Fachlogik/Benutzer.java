package Fachlogik;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Benutzer{

    private int benutzerId;

    private String nachname;
    private String vorname;
    private double gewicht;

    private String fhirId;

    private LocalDate geburtsdatum;

    private Geschlecht geschlecht;

    public Benutzer()
    {

    }

    public Benutzer(String nachname,String vorname, double gewicht, LocalDate geburtsdatum,Geschlecht geschlecht){

        this.nachname = nachname;
        this.vorname = vorname;
        this.gewicht = gewicht;

        this.geburtsdatum = geburtsdatum;
        this.geschlecht= geschlecht;

    }

    public Benutzer(int benutzerId,String nachname,String vorname, double gewicht, LocalDate geburtsdatum,Geschlecht geschlecht){

        this.benutzerId=benutzerId;

        this.nachname = nachname;
        this.vorname = vorname;
        this.gewicht = gewicht;

        this.geburtsdatum = geburtsdatum;
        this.geschlecht= geschlecht;
    }
    public Benutzer(int benutzerId,String nachname,String vorname, double gewicht, LocalDate geburtsdatum,Geschlecht geschlecht, String fhirId){

        this.benutzerId=benutzerId;

        this.nachname = nachname;
        this.vorname = vorname;
        this.gewicht = gewicht;

        this.geburtsdatum = geburtsdatum;
        this.geschlecht= geschlecht;
        this.fhirId=fhirId;

    }
    public int getBenutzerId() {
        return benutzerId;
    }

    public void setBenutzerId(int benutzerId) {
        this.benutzerId = benutzerId;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String name) {
        this.nachname = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public double getGewicht() {
        return gewicht;
    }

    public void setGewicht(double gewicht) {
        this.gewicht = gewicht;
    }

    public LocalDate getGeburtsdatum() {

        return geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {

        this.geburtsdatum = geburtsdatum;
    }

    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(Geschlecht geschlecht) {
        this.geschlecht = geschlecht;
    }

    public String getFhirId() {
        return fhirId;
    }

    public void setFhirId(String fhirId) {
        this.fhirId = fhirId;
    }


    @Override
    public String toString() {
        return " "+benutzerId + " " + vorname +" "+ nachname+" "+fhirId;
    }


}
