package Datenhaltung;

import Fachlogik.Benutzer;

import java.sql.*;

public class Datenbank
{
    private String url;
    private String username;
    private String password;

    private String classNameMysql = "com.mysql.cj.jdbc.Driver";
    private static Datenbank letzteSequenznummer = null;

    private Connection con;


    public static Datenbank getInstance()
    {
        if(Datenbank.letzteSequenznummer == null)
        {
            Datenbank.letzteSequenznummer = new Datenbank();
        }

        return Datenbank.letzteSequenznummer;
    }

    public Datenbank() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void connect()
    {
        try{
            Class.forName(classNameMysql);
            con = DriverManager.getConnection(url, username, password);

            final String Telematik = "CREATE DATABASE IF NOT EXISTS tuttest";

            final String Benutzer = "CREATE TABLE IF NOT EXISTS benutzer ("
                    + "id_benutzer int NOT NULL,"
                    + "nachname varchar(45)  DEFAULT NULL,"
                    + "vorname varchar(45)  DEFAULT NULL,"
                    + "gewicht double  DEFAULT NULL,"
                    + "age int DEFAULT NULL,"
                    + "geschlecht varchar(45)  DEFAULT NULL,"
                    + "PRIMARY KEY (`id_benutzer`))";

            Statement statement = con.createStatement();
            statement.execute( Telematik);
            statement.execute(Benutzer);

        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addBenutzerDB(Benutzer benutzer) throws SQLException {

        con.setAutoCommit(false);

        /*ALTER Table Anweisung, wenn man alter es als Tabellename erstellt,  muss man es in `alter` setzten sonst entsteht inkompatiblität*/
        //String einfuegen = "INSERT INTO benutzer(id_benutzer, name, vorname, gewicht, `alter`, geschlecht) VALUES (?, ?, ?, ?, ?, ?);";
        String einfuegen = "INSERT INTO benutzer(id_benutzer, nachname, vorname, gewicht, age, geschlecht) VALUES (?, ?, ?, ?, ?, ?);";
        boolean eingefuegt = false;

        try
        {
            PreparedStatement preparedStatement = con.prepareStatement(einfuegen);
            preparedStatement.setInt(1, benutzer.getBenutzerId());
            preparedStatement.setString(2, benutzer.getNachname());
            preparedStatement.setString(3,benutzer.getVorname());
            preparedStatement.setDouble(4, benutzer.getGewicht());
            preparedStatement.setInt(5, benutzer.getAlter());
            preparedStatement.setString(6, benutzer.getGeschlecht());


            preparedStatement.executeUpdate();
            con.commit();
            eingefuegt = true;

            System.out.println(benutzer.getBenutzerId() + " " +benutzer.getNachname());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eingefuegt;
    }
}

