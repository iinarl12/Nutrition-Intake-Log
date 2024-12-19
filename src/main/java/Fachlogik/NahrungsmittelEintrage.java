package Fachlogik;

import java.time.LocalTime;


public class NahrungsmittelEintrage extends Nahrungsmittel{

    String beschwerde;
    LocalTime zeit;

    public NahrungsmittelEintrage(String nahrungsmittel, int menge, Einheit einheit, String beschwerde, LocalTime zeit) {
        super(nahrungsmittel,menge,einheit);
        this.beschwerde = beschwerde;
        this.zeit = zeit;
    }



    public String getBeschwerde() {
        return beschwerde;
    }

    public void setBeschwerde(String beschwerde) {
        this.beschwerde = beschwerde;
    }

    public LocalTime getZeit() {
        return zeit;
    }

    public void setZeit(LocalTime zeit) {
        this.zeit = zeit;
    }
}
