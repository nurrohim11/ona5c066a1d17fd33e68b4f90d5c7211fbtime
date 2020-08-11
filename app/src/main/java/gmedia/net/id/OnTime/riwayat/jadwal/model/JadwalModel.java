package gmedia.net.id.OnTime.riwayat.jadwal.model;

public class JadwalModel {
    private String tgl, hari, shift, jam_mulai, jam_selesai, keterangan;

    public JadwalModel(String tgl, String hari, String shift, String jam_mulai, String jam_selesai, String keterangan){
        this.tgl = tgl;
        this.hari = hari;
        this.shift = shift;
        this.jam_mulai= jam_mulai;
        this.jam_selesai = jam_selesai;
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

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getJam_mulai() {
        return jam_mulai;
    }

    public void setJam_mulai(String jam_mulai) {
        this.jam_mulai = jam_mulai;
    }

    public String getJam_selesai() {
        return jam_selesai;
    }

    public void setJam_selesai(String jam_selesai) {
        this.jam_selesai = jam_selesai;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
