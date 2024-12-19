package Datenhaltung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {

    private static String url="jdbc:mysql://localhost:3306/?serverTimezone=UTC" ;
    private static String databaseName="TelematikTelemedizin";
    private static String username= "root";
    private static String password= "1234";

    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static DBConnect inst = null;

    private static Connection con;

    public static DBConnect getInstance()
    {
        if(DBConnect.inst == null)
        {
            DBConnect.inst = new DBConnect();
        }

        return DBConnect.inst;
    }

    public DBConnect() {


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

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }


    public Connection connect()
    {
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(this.getUrl(), this.getUsername(), this.getPassword());

            final String Telematik = "CREATE DATABASE IF NOT EXISTS TelematikTelemedizin";
            Statement statement = con.createStatement();
            statement.executeUpdate( Telematik);
            con.setCatalog(databaseName);
            statement = con.createStatement();

            final String Benutzer = "CREATE TABLE IF NOT EXISTS benutzer ("
                    + "id_benutzer int NOT NULL AUTO_INCREMENT,"
                    + "nachname varchar(200)  DEFAULT NULL,"
                    + "vorname varchar(200)  DEFAULT NULL,"
                    + "gewicht double  DEFAULT NULL,"
                    + "geburstdatum date  DEFAULT NULL,"
                    + "geschlecht enum('weiblich', 'maennlich', 'divers')  DEFAULT NULL,"
                    + "fhirId varchar(30) DEFAULT NULL,"
                    + "PRIMARY KEY (`id_benutzer`))";


            final String Menuset = "CREATE TABLE IF NOT EXISTS menuset ("
                    + "id_menuset int NOT NULL AUTO_INCREMENT,"
                    + "id_benutzer int NOT NULL,"
                    + "menuname varchar(200) NOT NULL,"
                    + "PRIMARY KEY (`id_menuset`),"
                    + "foreign key (`id_benutzer`) references benutzer(`id_benutzer`))";

            final String Menu = "CREATE TABLE IF NOT EXISTS menu ("
                    + "id_menu int NOT NULL AUTO_INCREMENT,"
                    + "id_menuset int NOT NULL,"
                    + "nahrungsmittel varchar(200) NOT NULL,"
                    + "menge int  NOT NULL,"
                    + "einheit enum('gramm', 'milliliter', 'stueck')  NOT NULL,"
                    + "nutritionFHIRId varchar(30) DEFAULT NULL,"
                    + "PRIMARY KEY (`id_menu`),"
                    + "foreign key (`id_menuset`) references menuset(`id_menuset`))";


            final String Tagsuebersicht = "CREATE TABLE IF NOT EXISTS tagsuebersicht ("
                    + "id_tagebuch int NOT NULL AUTO_INCREMENT,"
                    + "datum date  NOT NULL,"
                    + "id_benutzer int,"
                    + "zeit TIME NOT NULL,"
                    + "nahrungsmittel varchar(200) NOT NULL,"
                    + "menge int  NOT NULL,"
                    + "einheit enum('gramm', 'milliliter', 'stueck')  NOT NULL,"
                    + "beschwerde varchar(400)  NOT NULL,"
                    + "nutritionFHIRId varchar(30) DEFAULT NULL,"
                    + "PRIMARY KEY(`id_tagebuch`),"
                    + "foreign key (`id_benutzer`) references benutzer(`id_benutzer`))";



            statement.executeUpdate(Benutzer);
            statement.executeUpdate(Menuset);
            statement.executeUpdate(Menu);
            statement.executeUpdate(Tagsuebersicht);

            System.out.println("Database created successfully...");

        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return con;
    }

    public Connection getCon() {
        return con;
    }
}
