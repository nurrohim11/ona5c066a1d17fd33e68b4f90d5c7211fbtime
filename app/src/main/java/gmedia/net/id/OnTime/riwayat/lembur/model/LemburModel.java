package gmedia.net.id.OnTime.riwayat.lembur.model;

public class LemburModel {
    String id, nama, nominal, keterangan, tgl_mulai, tgl_selesai;

    public LemburModel(String id, String nama, String nominal, String keterangan, String tgl_mulai, String tgl_selesai){
        this.id = id;
        this.nama = nama;
        this.nominal= nominal;
        this.keterangan = keterangan;
        this.tgl_mulai = tgl_mulai;
        this.tgl_selesai = tgl_selesai;
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

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTgl_mulai() {
        return tgl_mulai;
    }

    public void setTgl_mulai(String tgl_mulai) {
        this.tgl_mulai = tgl_mulai;
    }

    public String getTgl_selesai() {
        return tgl_selesai;
    }

    public void setTgl_selesai(String tgl_selesai) {
        this.tgl_selesai = tgl_selesai;
    }
}
