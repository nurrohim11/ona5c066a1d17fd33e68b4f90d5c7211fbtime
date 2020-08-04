package gmedia.net.id.OnTime.riwayat.absensi.model;

public class AbsensiModel {
    String id, nama, tgl, shift, jam_masuk, jam_pulang, scan_masuk, scan_pulang, status, keterangan;

    public AbsensiModel(String id, String nama, String tgl, String shift, String jam_masuk, String jam_pulang, String scan_masuk, String scan_pulang, String status, String keterangan){
        this.id = id;
        this.nama = nama;
        this.tgl = tgl;
        this.shift = shift;
        this.jam_masuk = jam_masuk;
        this.jam_pulang = jam_pulang;
        this.scan_masuk = scan_masuk;
        this.scan_pulang = scan_pulang;
        this.status = status;
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

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getJam_masuk() {
        return jam_masuk;
    }

    public void setJam_masuk(String jam_masuk) {
        this.jam_masuk = jam_masuk;
    }

    public String getJam_pulang() {
        return jam_pulang;
    }

    public void setJam_pulang(String jam_pulang) {
        this.jam_pulang = jam_pulang;
    }

    public String getScan_masuk() {
        return scan_masuk;
    }

    public void setScan_masuk(String scan_masuk) {
        this.scan_masuk = scan_masuk;
    }

    public String getScan_pulang() {
        return scan_pulang;
    }

    public void setScan_pulang(String scan_pulang) {
        this.scan_pulang = scan_pulang;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
