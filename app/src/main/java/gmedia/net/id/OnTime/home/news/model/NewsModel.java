package gmedia.net.id.OnTime.home.news.model;

public class NewsModel {
    private Boolean baru;
    private String id, tanggal, judul, deskripsi;

    public NewsModel(String id, String tgl, String judul, String deskripsi) {
        this.id = id;
        this.tanggal = tgl;
        this.judul = judul;
        this.deskripsi = deskripsi;
    }

    public NewsModel(String id, String tgl, String judul) {
        this.id = id;
        this.tanggal = tgl;
        this.judul = judul;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getBaru() {
        return baru;
    }

    public void setBaru(Boolean baru) {
        this.baru = baru;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
