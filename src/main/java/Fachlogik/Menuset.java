package Fachlogik;

import java.util.List;

public class Menuset {

    private int menuId;
    private Benutzer benutzer;
    private String menuName;
    private List<Menu> menu;



    public Menuset()
    {

    }

    public Menuset( int menuId,Benutzer benutzer, String menuName, List<Menu> menu) {
        this.menuId=menuId;
        this.benutzer = benutzer;
        this.menuName = menuName;
        this.menu=menu;
    }

    public Menuset( Benutzer benutzer, String menuName, List<Menu> menu) {

        this.benutzer = benutzer;
        this.menuName = menuName;
        this.menu=menu;
    }
    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getMenuId() {
        return menuId;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String name) {
        this.menuName = name;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }
}
