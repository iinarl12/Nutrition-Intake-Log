package Datenhaltung;

import Fachlogik.Benutzer;
import Fachlogik.Geschlecht;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBBenutzer {


    private DBConnect dbConnect;


    public DBBenutzer(DBConnect dbConnect)
    {
        this.dbConnect = dbConnect;
    }

        /*Benuzter in die Datenbank einfuegen*/
    public boolean addBenutzerDB(Benutzer benutzer)
    {
        /*ALTER Table Anweisung, wenn man alter es als Tabellename erstellt,  muss man es in `alter` setzten sonst entsteht inkompatiblität*/
        //String einfuegen = "INSERT INTO benutzer(id_benutzer, name, vorname, gewicht, `alter`, geschlecht) VALUES (?, ?, ?, ?, ?, ?);";
        String einfuegen = "INSERT INTO benutzer(nachname, vorname, gewicht, geburstdatum, geschlecht, fhirId ) VALUES ( ?, ?, ?, ? , ?,?);";
        boolean eingefuegt = false;
        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try
        {
            PreparedStatement preparedStatement = DBConnect.getInstance().getCon().prepareStatement(einfuegen, Statement.RETURN_GENERATED_KEYS);


            preparedStatement.setString(1, benutzer.getNachname());
            preparedStatement.setString(2,benutzer.getVorname());
            preparedStatement.setDouble(3, benutzer.getGewicht());
            preparedStatement.setDate(4, java.sql.Date.valueOf( benutzer.getGeburtsdatum()));
            preparedStatement.setString(5, benutzer.getGeschlecht().name());
            preparedStatement.setString(6,benutzer.getFhirId());
            preparedStatement.executeUpdate();

            dbConnect.getCon().commit();

            ResultSet rs = preparedStatement.getGeneratedKeys();

            if(rs.next())
            {
                int newID = rs.getInt(1);
                benutzer.setBenutzerId(newID);
            }
            else
            {
                //fehler bei einfuegen
            }

            eingefuegt = true;


        } catch (SQLException e) {
            e.printStackTrace();
        }



        return eingefuegt;
    }

    /*Selelct Abfrage der Benutzer*/
    public List<Benutzer> getBenutzerliste()
    {
        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String abfrageBenutzer = "SELECT * FROM benutzer";
        List<Benutzer> benutzerList = new ArrayList<>();

        try{
            Statement statement = DBConnect.getInstance().getCon().createStatement();
            ResultSet resultSet = statement.executeQuery(abfrageBenutzer);

            while(resultSet.next())
            {
               Benutzer benutzer = new Benutzer();
                benutzer.setBenutzerId(resultSet.getInt(1));
                benutzer.setNachname(resultSet.getString(2));
                benutzer.setVorname(resultSet.getString(3));
                benutzer.setGewicht(resultSet.getDouble(4));
                benutzer.setGeburtsdatum(resultSet.getDate(5).toLocalDate());
                benutzer.setGeschlecht(Geschlecht.valueOf(resultSet.getString(6)));
                benutzerList.add(benutzer);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return benutzerList;

    }


    /*Benutzer aus der Datenbank loeschen*/
    public Boolean benutzerdelete(Benutzer benutzer)
    {
        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int id = benutzer.getBenutzerId();
        DBMenuset dbMenuset=new DBMenuset(dbConnect);
        dbMenuset.menudeleteByMenuset(benutzer);
        DBTagesuebersicht dbTagesuebersicht=new DBTagesuebersicht(dbConnect);
        dbTagesuebersicht.deleteTagebuchByBenutzer(benutzer);

        String deleteBenutzer = "DELETE FROM benutzer WHERE id_benutzer =" + id+";";

        PreparedStatement ps = null;

        try{
            ps = DBConnect.getInstance().getCon().prepareStatement(deleteBenutzer);
            ps.executeUpdate();
            dbConnect.getCon().commit();
            return true;

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBenutzer(Benutzer benutzer){
        String update = "UPDATE benutzer set nachname=?, vorname=?, gewicht=?, geburstdatum=?, geschlecht=? where id_benutzer="+benutzer.getBenutzerId()+";";
        boolean updated = false;
        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try
        {
            PreparedStatement preparedStatement = DBConnect.getInstance().getCon().prepareStatement(update, Statement.RETURN_GENERATED_KEYS);


            preparedStatement.setString(1, benutzer.getNachname());
            preparedStatement.setString(2,benutzer.getVorname());
            preparedStatement.setDouble(3, benutzer.getGewicht());
            preparedStatement.setDate(4, java.sql.Date.valueOf( benutzer.getGeburtsdatum()));
            preparedStatement.setString(5, benutzer.getGeschlecht().name());
            preparedStatement.executeUpdate();

            dbConnect.getCon().commit();


            updated = true;


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return updated;

    }
}
