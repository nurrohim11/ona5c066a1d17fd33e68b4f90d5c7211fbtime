package gmedia.net.id.OnTime.approval.cuti.model;

public class CutiModel {
    private int id,id_karyawan;
    private String nik, nama, awal, akhir, alasan, status, keterangan, jumlah, insert_at;

    public CutiModel(int id, int id_karyawan, String nik, String nama, String awal, String akhir, String alasan, String status, String keterangan, String jumlah, String insert_at){
        this.id = id;
        this.id_karyawan= id_karyawan;
        this.nik = nik;
        this.nama = nama;
        this.awal = awal;
        this.akhir = akhir;
        this.alasan = alasan;
        this.status = status;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
        this.insert_at = insert_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_karyawan() {
        return id_karyawan;
    }

    public void setId_karyawan(int id_karyawan) {
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

    public String getAwal() {
        return awal;
    }

    public void setAwal(String awal) {
        this.awal = awal;
    }

    public String getAkhir() {
        return akhir;
    }

    public void setAkhir(String akhir) {
        this.akhir = akhir;
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

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getInsert_at() {
        return insert_at;
    }

    public void setInsert_at(String insert_at) {
        this.insert_at = insert_at;
    }
}
