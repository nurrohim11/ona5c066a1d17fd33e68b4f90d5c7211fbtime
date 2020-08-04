package gmedia.net.id.OnTime.akun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.AlarmClock;
import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.MainActivity;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

import static gmedia.net.id.OnTime.utils.Utils.formatTgl;
import static gmedia.net.id.OnTime.utils.Utils.loginActivity;

public class AkunFragment extends Fragment {
    View view;
    TextView tvNik, tvDivisi, tvJabatan, tvPosisi, tvTglMasuk, tvNoEktp, tvNama, tvNoTelepon, tvEmail, tvAlamat, tvTempatLahir;
    TextView tvTglLahir, tvJenisKelamin, tvAgama, tvStatusNikah, tvGolonganDarah, tvPendidikanTerakhir;
    SessionManager sessionManager;

    public AkunFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_akun, container, false);
        sessionManager = new SessionManager(getContext());
        initUi();
        return view;
    }

    private void initUi(){
        // data pekerjaan
        tvNik = view.findViewById(R.id.tv_nik);
        tvDivisi = view.findViewById(R.id.tv_divisi);
        tvJabatan = view.findViewById(R.id.tv_jabatan);
        tvPosisi = view.findViewById(R.id.tv_posisi);
        tvTglMasuk = view.findViewById(R.id.tv_tgl_masuk);

        // data biodata diri
        tvNoEktp = view.findViewById(R.id.tv_no_ektp);
        tvNama = view.findViewById(R.id.tv_nama);
        tvNoTelepon = view.findViewById(R.id.tv_no_telepon);
        tvEmail = view.findViewById(R.id.tv_email);
        tvAlamat = view.findViewById(R.id.tv_alamat);
        tvTempatLahir = view.findViewById(R.id.tv_tempat_lahir);
        tvTglLahir = view.findViewById(R.id.tv_tgl_lahir);
        tvJenisKelamin = view.findViewById(R.id.tv_jenis_kelamin);
        tvAgama = view.findViewById(R.id.tv_agama);
        tvStatusNikah = view.findViewById(R.id.tv_status_nikah);
        tvGolonganDarah = view.findViewById(R.id.tv_golongan_darah);

        // data pendidikan terakhir
        tvPendidikanTerakhir = view.findViewById(R.id.tv_pendidikan_terakhir);
    }

    @Override
    public void onResume() {
        super.onResume();
        initProfile();
    }

    private void initProfile(){

        JSONObject jBody = new JSONObject();
        new ApiVolley(getContext(), jBody, "POST", ServerUrl.Profile,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Akun fragment",String.valueOf(res));

                            tvNik.setText(res.getString("nik"));
                            tvDivisi.setText(res.getString("divisi"));
                            tvJabatan.setText(res.getString("jabatan"));
                            tvPosisi.setText(res.getString("posisi"));
                            tvTglMasuk.setText(formatTgl(res.getString("tgl_masuk")));

                            tvNoEktp.setText(res.getString("ktp"));
                            tvNama.setText(res.getString("nama"));
                            tvNoTelepon.setText(res.getString("telp"));
                            tvEmail.setText(res.getString("email"));
                            tvAlamat.setText(res.getString("alamat"));
                            tvTempatLahir.setText(res.getString("tempat_lahir"));
                            tvTglLahir.setText(formatTgl(res.getString("tgl_lahir")));
                            tvJenisKelamin.setText(res.getString("jenis_kelamin"));
                            tvAgama.setText(res.getString("agama"));
                            tvStatusNikah.setText(res.getString("status_nikah"));
                            tvGolonganDarah.setText(res.getString("golongan_darah"));

                            tvPendidikanTerakhir.setText(res.getString("pendidikan"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onEmpty(String message) {
                        Toasty.error(getContext(), message, Toast.LENGTH_SHORT, true).show();
                    }
                    @Override
                    public void onFail(String message) {
                        try {
                            sessionManager.logoutUser(loginActivity(getContext()));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                })
        );
    }
}