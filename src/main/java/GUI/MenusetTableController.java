package GUI;

import Datenhaltung.DBConnect;
import Datenhaltung.DBMenuset;
import Fachlogik.Benutzer;
import Fachlogik.Einheit;
import Fachlogik.Menuset;
import Fachlogik.Tagsuebersicht;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.hl7.fhir.r5.model.NutritionProduct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenusetTableController {

    @FXML
    private TableView <Menuview>table;

    @FXML
    private TableColumn<Menuview, String> name;
    @FXML
    private TableColumn<Menuview, String> nahrungsmittel;
    @FXML
    private TableColumn<Menuview, Integer> menge;
    @FXML
    private TableColumn<Menuview, Einheit> einheit;
    @FXML
    private Button delete;
    @FXML
    private Pane menuset;
    @FXML
    private TextField suchfeld;

    @FXML
    private Text benutzer;
    private List<Menuset> menusetList=new ArrayList<>();
    private Benutzer selectedBenutzer;

    private ObservableList<Menuset> msList = FXCollections.observableArrayList();

    private ObservableList<Menuview> menuviews = FXCollections.observableArrayList();

    private String query = null;

    private BenutzerController benutzerController;

    private DBConnect dbConnect=new DBConnect();;

    DBMenuset dbMenuset = new DBMenuset(dbConnect);



    @FXML
    public void initialize() {


        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        populateTable();

    }

    public void getBenutzerController(BenutzerController benutzerController) {
        this.benutzerController = benutzerController;
    }

    public void setBenutzerTitle(Benutzer benutzer) {
        this.benutzer.setText(benutzer.getVorname());
    }

    public void setBenutzer(Benutzer selectedBenutzer) {
        this.selectedBenutzer = selectedBenutzer;
    }



    private void refreshTable()
    {
        if(selectedBenutzer != null)
        {
            try {

                msList.clear();

                query = "SELECT * FROM `menuset` where id_benutzer=" + selectedBenutzer.getBenutzerId() + ";";
                Statement statement = null;
                statement = DBConnect.getInstance().getCon().createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while(resultSet.next())
                {
                    ObservableList<Fachlogik.Menu> menuList = FXCollections.observableArrayList();
                    String getmenu= "SELECT * FROM `menu` where id_menuset=" + resultSet.getInt(1) + ";";
                    Statement statementmenu = null;
                    statementmenu = DBConnect.getInstance().getCon().createStatement();
                    ResultSet resultmenu = statementmenu.executeQuery(getmenu);

                    while(resultmenu.next()){
                        int id = resultmenu.getInt(1);
                        int idmenuset= resultmenu.getInt(2);
                        String nah= resultmenu.getString(3);
                        int me=resultmenu.getInt(4);

                        Einheit einheit;

                        if (resultmenu.getString(5).equals("gramm")) {
                            einheit = Einheit.gramm;
                        } else if(resultmenu.getString(5).equals("milliliter"))
                        {
                            einheit = Einheit.milliliter;
                        }
                        else einheit=Einheit.stueck;
                        String nutritionProduct=resultmenu.getString(6);

                        Fachlogik.Menu menu=new Fachlogik.Menu(nah,me,einheit,nutritionProduct);
                        menuList.add(menu);
                    }


                    msList.add(new Menuset(
                            resultSet.getInt(1),
                            selectedBenutzer,
                            resultSet.getString(3),
                            menuList));
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void populateTable()
    {
        menuviews.clear();

        if(selectedBenutzer != null)
        {
            refreshTable();
            for(Menuset mset:msList){
                for(Fachlogik.Menu menu1:mset.getMenu()){
                    Menuview menuview= new Menuview(mset.getMenuId(),mset.getMenuName(),menu1.getNahrungsmittel(),menu1.getMenge(),menu1.getEinheit(), menu1.getNutritionProductId());
                    menuviews.add(menuview);
                }
            }

            name.setCellValueFactory(na->new SimpleStringProperty(na.getValue().getName()));
            nahrungsmittel.setCellValueFactory(nahm-> new SimpleStringProperty(nahm.getValue().getNahrungsmittel()));
            menge.setCellValueFactory(me->new SimpleObjectProperty<>(me.getValue().getMenge()));
            einheit.setCellValueFactory(ei -> new SimpleObjectProperty(ei.getValue().getEinheit()));

            table.setItems(menuviews);
        }
    }



    @FXML
    public void delete(ActionEvent actionEvent) {
        for(Menuview mv:table.getSelectionModel().getSelectedItems()){
            String n=mv.getName();

            dbMenuset.menudelete(mv);
            Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,
                    "Menuset "+ n+" würde erfolgreich gelöscht",ButtonType.OK);
            a1.show();

        }
        table.getItems().removeAll(table.getSelectionModel().getSelectedItems());

        populateTable();
    }

    @FXML
    public void suchen(Event event) {
        if(suchfeld.getText().replaceAll(" ","").equals("")){
            table.setItems(menuviews);
        }
        ObservableList<Menuview> menuviews1 = FXCollections.observableArrayList();
        int i=0;
        while(i<menuviews.size()){
            if((menuviews.get(i).getName()).replaceAll(" ","").toLowerCase().contains(suchfeld.getText().replaceAll(" ","").toLowerCase())){
                menuviews1.add(menuviews.get(i));
            }
            i++;
        }
        table.setItems(menuviews1);
    }

}

