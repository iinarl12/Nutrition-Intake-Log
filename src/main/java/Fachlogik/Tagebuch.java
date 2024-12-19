package Fachlogik;

import java.util.Date;
import java.util.List;

public class Tagebuch {
    Benutzer benutzer;
    Date datum;
    List<NahrungsmittelEintrage> nahrungsmittelEintrage;

    public Tagebuch(Date datum, List<NahrungsmittelEintrage> nahrungsmittelEintrage) {
        this.datum = datum;
        this.nahrungsmittelEintrage = nahrungsmittelEintrage;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public List<NahrungsmittelEintrage> getNahrungsmittelEintrage() {
        return nahrungsmittelEintrage;
    }

    public void setNahrungsmittelEintrage(List<NahrungsmittelEintrage> nahrungsmittelEintrage) {
        this.nahrungsmittelEintrage = nahrungsmittelEintrage;
    }
}
