package Datenhaltung;

import Fachlogik.Benutzer;
import Fachlogik.Einheit;
import Fachlogik.Menu;
import Fachlogik.Menuset;
import GUI.Menuview;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBMenuset {


    private DBConnect dbConnect;

    public DBMenuset(DBConnect dbConnect)
    {
        this.dbConnect=dbConnect;
    }




    /*Menu in Dabenbank einfugen*/
    public boolean addMenuDB(Menuset menuset)
    {
        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String addMenuset = "INSERT INTO menuset(id_benutzer, menuname) VALUES (?, ?);";
        boolean eingefuegt = false;

        String addMenu="INSERT INTO menu(id_menuset, nahrungsmittel, menge, einheit, nutritionFHIRId) VALUES (?, ?,?,?,?);";

        try{
            PreparedStatement preparedStatement = dbConnect.getCon().prepareStatement(addMenuset, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, menuset.getBenutzer().getBenutzerId());
            preparedStatement.setString(2, menuset.getMenuName());
            preparedStatement.executeUpdate();


            ResultSet rs = preparedStatement.getGeneratedKeys();

            if(rs.next())
            {
                int newID = rs.getInt(1);
                menuset.setMenuId(newID);
            }
            else
            {
                //fehler bei einfuegen
            }

            for(Menu m:menuset.getMenu()){
                PreparedStatement addmenustatement= dbConnect.getCon().prepareStatement(addMenu, Statement.RETURN_GENERATED_KEYS);
                addmenustatement.setInt(1,menuset.getMenuId());
                addmenustatement.setString(2, m.getNahrungsmittel());
                addmenustatement.setInt(3,m.getMenge());
                addmenustatement.setString(4, m.getEinheit().name());
                addmenustatement.setString(5,m.getNutritionProductId());
                addmenustatement.executeUpdate();
            }
            dbConnect.getCon().commit();



            eingefuegt = true;




        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eingefuegt;
    }

    public List<Menuset> getMenuset(Menuset menueset)
    {

        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String menuAbfrage = "SELECT * FROM menuset";
        List<Menuset> liste = new ArrayList<>();

        try{
            Statement statement = dbConnect.getCon().createStatement();
            ResultSet rs = statement.executeQuery(menuAbfrage);

            while(rs.next())
            {
                menueset = new Menuset();
                menueset.setMenuId(rs.getInt(1));
                menueset.getBenutzer().setBenutzerId(rs.getInt(2));
                menueset.setMenuName(rs.getString(3));

                liste.add(menueset);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return liste;
    }


    /*Menuset aus der Datenbank loeschen*/
    public void menudelete(Menuview menuview)
    {
        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int id = menuview.getId_menuset();
        String deleteMenu = "DELETE FROM menu WHERE id_menuset="+id+";";
        String deleteMenuset= "DELETE FROM menuset WHERE id_menuset="+id+";";


        try{
            PreparedStatement ps = dbConnect.getCon().prepareStatement(deleteMenu);
            PreparedStatement psm = dbConnect.getCon().prepareStatement(deleteMenuset);
            ps.executeUpdate();
            psm.executeUpdate();
            dbConnect.getCon().commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void menudeleteByMenuset(Benutzer benutzer){

        List<Integer> idMenuset=getMenusetId(benutzer);

        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(Integer id:idMenuset){
            String deleteMenu = "DELETE FROM menu WHERE id_menuset="+id+";";
            String deleteMenuset= "DELETE FROM menuset WHERE id_menuset="+id+";";


            try{
                PreparedStatement ps = dbConnect.getCon().prepareStatement(deleteMenu);
                PreparedStatement psm = dbConnect.getCon().prepareStatement(deleteMenuset);
                ps.executeUpdate();
                psm.executeUpdate();
                dbConnect.getCon().commit();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Integer> getMenusetId(Benutzer benutzer){
        try {
            dbConnect.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String menuAbfrage = "SELECT * FROM menuset where id_benutzer="+benutzer.getBenutzerId()+";";
        List<Integer> liste = new ArrayList<>();

        try{
            Statement statement = dbConnect.getCon().createStatement();
            ResultSet rs = statement.executeQuery(menuAbfrage);

            while(rs.next())
            {
                liste.add(rs.getInt(1));

            }

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return liste;


    }
}
