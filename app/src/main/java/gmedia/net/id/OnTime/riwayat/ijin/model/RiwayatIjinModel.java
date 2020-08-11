package gmedia.net.id.OnTime.riwayat.ijin.model;

public class RiwayatIjinModel {
    private String tanggal, jam, alasan, status;

    public RiwayatIjinModel(String tgl, String jam, String alasan, String keterangan) {
        this.tanggal = tgl;
        this.jam = jam;
        this.alasan = alasan;
        this.status = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
