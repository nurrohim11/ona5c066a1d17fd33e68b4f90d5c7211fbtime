package gmedia.net.id.OnTime.riwayat.scanlog.model;

public class ScanlogModel {

    String id, nama, scan_date, scan_time, keterangan;
    double latitude, longitude;

    public ScanlogModel(String id, String nama, String scan_date, String scan_time, String keterangan, double latitude, double longitude){
        this.id = id;
        this.nama = nama;
        this.scan_date = scan_date;
        this.scan_time= scan_time;
        this.keterangan = keterangan;
        this.latitude = latitude;
        this.longitude=  longitude;
    }
    public ScanlogModel(String id, String nama, String scan_date, String scan_time, String keterangan){
        this.id = id;
        this.nama = nama;
        this.scan_date = scan_date;
        this.scan_time= scan_time;
        this.keterangan = keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getScan_date() {
        return scan_date;
    }

    public void setScan_date(String scan_date) {
        this.scan_date = scan_date;
    }

    public String getScan_time() {
        return scan_time;
    }

    public void setScan_time(String scan_time) {
        this.scan_time = scan_time;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
