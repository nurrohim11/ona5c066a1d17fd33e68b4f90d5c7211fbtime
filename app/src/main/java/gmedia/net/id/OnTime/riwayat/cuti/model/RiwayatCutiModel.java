package gmedia.net.id.OnTime.riwayat.cuti.model;

/**
 * Created by Bayu on 08/12/2017.
 */

public class RiwayatCutiModel {

    private String tglAwal, tglAkhir, alasan, status;

    public RiwayatCutiModel(String awal, String akhir, String alasan, String keterangan) {
        this.tglAwal = awal;
        this.tglAkhir = akhir;
        this.alasan = alasan;
        this.status = keterangan;
    }

    public String getTglAwal() {
        return tglAwal;
    }

    public void setTglAwal(String tglAwal) {
        this.tglAwal = tglAwal;
    }

    public String getTglAkhir() {
        return tglAkhir;
    }

    public void setTglAkhir(String tglAkhir) {
        this.tglAkhir = tglAkhir;
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
