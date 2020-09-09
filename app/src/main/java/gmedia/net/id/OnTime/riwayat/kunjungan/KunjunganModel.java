package gmedia.net.id.OnTime.riwayat.kunjungan;

public class KunjunganModel {

    String tgl, hari, lokasi, latitude, longitude, keterangan;

    public KunjunganModel(String tgl, String hari, String lokasi, String latitude, String longitude, String keterangan){
        this.tgl = tgl;
        this.hari = hari;
        this.lokasi = lokasi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.keterangan = keterangan;
    }

    public KunjunganModel(String hari, String lokasi, String latitude, String longitude, String keterangan){
        this.hari = hari;
        this.lokasi = lokasi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.keterangan = keterangan;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
