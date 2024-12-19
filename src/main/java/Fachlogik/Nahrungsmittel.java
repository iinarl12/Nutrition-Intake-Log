package Fachlogik;

enum Einheit{
    Gramm,Mililiter
}

public class Nahrungsmittel {
    private String nahrungsmittel;
    private int menge;
    private Einheit einheit;

    public Nahrungsmittel(String nahrungsmittel, int menge, Einheit einheit) {
        this.nahrungsmittel = nahrungsmittel;
        this.menge = menge;
        this.einheit = einheit;
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
