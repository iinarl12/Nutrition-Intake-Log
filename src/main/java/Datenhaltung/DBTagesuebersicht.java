package Datenhaltung;

import Fachlogik.Benutzer;
import Fachlogik.Tagsuebersicht;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBTagesuebersicht {

    private DBConnect dbConnect;

    public DBTagesuebersicht(DBConnect dbConnect)
    {
        this.dbConnect = dbConnect;
    }

    /*In Datenbank Tagesuber einfuegen*/
    public boolean addTagsuebersicht(Tagsuebersicht tagsuebersicht)
    {
        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String tageeintragung = "INSERT INTO tagsuebersicht(datum,  id_benutzer, zeit, nahrungsmittel, menge, einheit, beschwerde, nutritionFHIRId) VALUES (?, ?, ?, ?, ?, ?, ?,?);";
        boolean eingefuegt = false;

        try{
            PreparedStatement preparedStatement = dbConnect.getCon().prepareStatement(tageeintragung, Statement.RETURN_GENERATED_KEYS );
            preparedStatement.setDate(1, Date.valueOf(tagsuebersicht.getDatum()));
            preparedStatement.setLong(2, tagsuebersicht.getBenutzer().getBenutzerId());
            preparedStatement.setTime(3, Time.valueOf(tagsuebersicht.getZeit()));
            preparedStatement.setString(4, tagsuebersicht.getNahrungsmittel());
            preparedStatement.setInt(5, tagsuebersicht.getMenge());
            preparedStatement.setString(6, tagsuebersicht.getEinheit().name());
            preparedStatement.setString(7, tagsuebersicht.getBeschwerde());
            preparedStatement.setString(8, tagsuebersicht.getNutritionFHIRId());


            //preparedStatement.setInt(1, buch.getTagebuchId());

            //
            preparedStatement.executeUpdate();
            dbConnect.getCon().commit();
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if(rs.next())
            {
                int newID = rs.getInt(1);
                tagsuebersicht.setTagebuchId(newID);
                System.out.println(newID);
            }
            else
            {
                //fehler bei einfuegen
            }


            eingefuegt = true;

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return eingefuegt;
    }

    /*Die Dateneintraege in Tagesubersicht in Datenbanken abfragen*/
    public List<Tagsuebersicht> getTagebuchListBe(Tagsuebersicht tagsuebersicht) throws SQLException {

        dbConnect.getCon().setAutoCommit(false);

        long id = tagsuebersicht.getTagebuchId();

       // String abfrageTagebuch = "SELECT * FROM `tagesuebersicht` where id_benutzer=" + benutzer.getBenutzerId() + " and datum=\"" + tagsuebersicht.getDatum() + "\"";
        String abfrageTagebuch = "SELECT * FROM `tagsuebersicht` where datum=" + tagsuebersicht.getDatum() + "\"";

        List<Tagsuebersicht> liste = new ArrayList<>();
        ResultSet rs = null;
        try{
            Statement statement = dbConnect.getCon().createStatement();
           // rs = statement.executeQuery(abfrageTagebuch);

            while(rs.next())
            {

                tagsuebersicht = new Tagsuebersicht();
                tagsuebersicht.setTagebuchId(rs.getInt(1));
                tagsuebersicht.setDatum(rs.getDate(2).toLocalDate());
                tagsuebersicht.getBenutzer().setBenutzerId(rs.getInt(3));

                liste.add(tagsuebersicht);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Alle ID Tagesubersicht " + rs.getInt(1));
        return liste;
    }


    /*Von Datenbank ein Eintrag aus der Tagsuebersicht loeschen*/
    public void deleteTagebuch(Tagsuebersicht tagsuebersicht)
    {
        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int id = tagsuebersicht.getTagebuchId();
        String tagebuchloeschen = "DELETE FROM tagsuebersicht WHERE id_tagebuch="+id+";";

        try{
            PreparedStatement ps = dbConnect.getCon().prepareStatement(tagebuchloeschen);
            ps.executeUpdate();
            dbConnect.getCon().commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void deleteTagebuchByBenutzer(Benutzer benutzer){
        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String tagebuchloeschen = "DELETE FROM tagsuebersicht WHERE id_benutzer="+benutzer.getBenutzerId()+";";

        try{
            PreparedStatement ps = dbConnect.getCon().prepareStatement(tagebuchloeschen);
            ps.executeUpdate();
            dbConnect.getCon().commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateTagebuch(Tagsuebersicht tagsuebersicht){
        String update = "UPDATE tagsuebersicht set datum=?, id_benutzer=?, zeit=?, nahrungsmittel=?, menge=?, einheit=?, beschwerde=?, nutritionFHIRId=? where id_tagebuch="+tagsuebersicht.getTagebuchId()+";";
        boolean updated = false;
        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try
        {
            PreparedStatement preparedStatement = DBConnect.getInstance().getCon().prepareStatement(update, Statement.RETURN_GENERATED_KEYS);


            preparedStatement.setDate(1, Date.valueOf(tagsuebersicht.getDatum()));
            preparedStatement.setLong(2, tagsuebersicht.getBenutzer().getBenutzerId());
            preparedStatement.setTime(3, Time.valueOf(tagsuebersicht.getZeit()));
            preparedStatement.setString(4, tagsuebersicht.getNahrungsmittel());
            preparedStatement.setInt(5, tagsuebersicht.getMenge());
            preparedStatement.setString(6, tagsuebersicht.getEinheit().name());
            preparedStatement.setString(7, tagsuebersicht.getBeschwerde());
            preparedStatement.setString(8, tagsuebersicht.getNutritionFHIRId());
            preparedStatement.executeUpdate();

            dbConnect.getCon().commit();


            updated = true;


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return updated;
    }

}
