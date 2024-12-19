package GUI;

import Fachlogik.Einheit;

public class Menuview {

    private String name;
    private String nahrungsmittel;
    private int menge;
    private Einheit einheit;
    private String nutritionProductID;

    private int id_menuset;

    public Menuview(int id_menuset,String name, String nahrungsmittel, int menge, Einheit einheit, String nutritionProductID) {
        this.id_menuset=id_menuset;
        this.name = name;
        this.nahrungsmittel = nahrungsmittel;
        this.menge = menge;
        this.einheit = einheit;
        this.nutritionProductID=nutritionProductID;
    }

    public String getNutritionProductID() {
        return nutritionProductID;
    }

    public void setNutritionProductID(String nutritionProductID) {
        this.nutritionProductID = nutritionProductID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNahrungsmittel() {
        return nahrungsmittel;
    }

    public void setNahrungsmittel(String nahrungsmittel) {
        this.nahrungsmittel = nahrungsmittel;
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

    public int getId_menuset() {
        return id_menuset;
    }

    public void setId_menuset(int id_menuset) {
        this.id_menuset = id_menuset;
    }
}
