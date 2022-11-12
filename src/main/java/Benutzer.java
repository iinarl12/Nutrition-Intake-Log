public class Benutzer{

    private int benutzerId;
    private String name;
    private String vorname;
    private double gewicht;
    private int alter;
    private String geschlecht;
    private static int counter = 0;



    public Benutzer(String name,String vorname, double gewicht, int alter,String geschlecht){

        this.benutzerId = counter;
        counter++;
        this.name = name;
        this.vorname = vorname;
        this.gewicht = gewicht;
        this.alter = alter;
        this.geschlecht= geschlecht;

    }
    public int getPatientId() {
        return benutzerId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setGewicht(String strasse) {
        this.gewicht = gewicht;
    }

    public int getAlter() {
        return alter;
    }

    public void setAlter(int alter) {
        this.alter = alter;
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(String geschlecht) {
        this.geschlecht = geschlecht;
    }



    @Override
    public String toString() {
        return " "+benutzerId + "              " + vorname +" "+ name;
    }


}
