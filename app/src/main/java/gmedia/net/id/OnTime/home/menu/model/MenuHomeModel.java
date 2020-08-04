package gmedia.net.id.OnTime.home.menu.model;

public class MenuHomeModel {
    String kode, nameIcon, linkMenu;
    int imgIcon, bgMenu;

    public MenuHomeModel(String kode, String nameIcon, String linkMenu, int imgIcon, int bgMenu){
        this.kode = kode;
        this.nameIcon = nameIcon;
        this.linkMenu = linkMenu;
        this.imgIcon = imgIcon;
        this.bgMenu = bgMenu;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNameIcon() {
        return nameIcon;
    }

    public void setNameIcon(String nameIcon) {
        this.nameIcon = nameIcon;
    }

    public String getLinkMenu() {
        return linkMenu;
    }

    public void setLinkMenu(String linkMenu) {
        this.linkMenu = linkMenu;
    }

    public int getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(int imgIcon) {
        this.imgIcon = imgIcon;
    }

    public int getBgMenu() {
        return bgMenu;
    }

    public void setBgMenu(int bgMenu) {
        this.bgMenu = bgMenu;
    }

}