package gmedia.net.id.OnTime.approval.ijin.model;

public class IjinModel {
    String id, id_karyawan, nik, nama, tgl, jam, alasan, insert_at;

    public IjinModel(String id, String id_karyawan, String nik, String nama, String tgl, String jam, String alasan, String insert_at){
        this.id = id;
        this.id_karyawan = id_karyawan;
        this.nik = nik;
        this.nama = nama;
        this.tgl = tgl;
        this.jam = jam;
        this.alasan = alasan;
        this.insert_at = insert_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_karyawan() {
        return id_karyawan;
    }

    public void setId_karyawan(String id_karyawan) {
        this.id_karyawan = id_karyawan;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
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

    public String getInsert_at() {
        return insert_at;
    }

    public void setInsert_at(String insert_at) {
        this.insert_at = insert_at;
    }
}
