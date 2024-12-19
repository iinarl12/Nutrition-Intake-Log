package Fachlogik;

public class Menu {

    private String nahrungsmittel;
    private int menge;
    private Einheit einheit;
    private String nutritionProductId;

    public Menu(String nahrungsmittel, int menge, Einheit einheit, String nutritionProductId) {
        this.nahrungsmittel = nahrungsmittel;
        this.menge = menge;
        this.einheit = einheit;
        this.nutritionProductId=nutritionProductId;
    }

    public String getNutritionProductId() {
        return nutritionProductId;
    }

    public void setNutritionProductId(String nutritionProductId) {
        this.nutritionProductId = nutritionProductId;
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
}
