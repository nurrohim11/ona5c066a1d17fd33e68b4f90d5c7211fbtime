package gmedia.net.id.OnTime.riwayat.absensi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alkhattabi.sweetdialog.SweetDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.absensi.adapter.AbsensiAdapter;
import gmedia.net.id.OnTime.riwayat.absensi.model.AbsensiModel;
import gmedia.net.id.OnTime.riwayat.gaji.GajiActivity;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

import static gmedia.net.id.OnTime.utils.Utils.formatDate;

public class RiwayatAbsensiActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_tgl_awal)
    RelativeLayout rlTglAwal;
    @BindView(R.id.tv_tgl_awal)
    TextView tvTglAwal;
    @BindView(R.id.rl_tgl_akhir)
    RelativeLayout rlTglAkhir;
    @BindView(R.id.tv_tgl_akhir)
    TextView tvTglAkhir;
    @BindView(R.id.btn_proses)
    Button btnProses;
    @BindView(R.id.rv_absensi)
    RecyclerView rvAbsensi;
    @BindView(R.id.ll_absensi)
    LinearLayout llAbsensi;
    @BindView(R.id.ll_not_found)
    LinearLayout llNotFound;

    String tgl_awal ="";
    String tgl_akhir ="";
    List<AbsensiModel> absensiModels;
    AbsensiAdapter adapter;
    SweetDialog pDialogProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_absensi);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialogProcess = new SweetDialog(RiwayatAbsensiActivity.this,SweetDialog.PROGRESS_TYPE);
        pDialogProcess.setCancelable(false);
        init();
    }

    private void init(){
        tvTglAwal.setText("Tanggal Awal");
        tvTglAkhir.setText("Tanggal Akhir");

        rlTglAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final java.util.Calendar customDate = java.util.Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        customDate.set(java.util.Calendar.YEAR, year);
                        customDate.set(java.util.Calendar.MONTH, month);
                        customDate.set(java.util.Calendar.DATE, dayOfMonth);
                        SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                        tvTglAwal.setText(sdFormat.format(customDate.getTime()));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        tgl_awal = sdf.format(customDate.getTime());
                    }
                };
                new DatePickerDialog(RiwayatAbsensiActivity.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
            }
        });

        rlTglAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final java.util.Calendar customDate = java.util.Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        customDate.set(java.util.Calendar.YEAR, year);
                        customDate.set(java.util.Calendar.MONTH, month);
                        customDate.set(java.util.Calendar.DATE, dayOfMonth);
                        SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                        tvTglAkhir.setText(sdFormat.format(customDate.getTime()));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        tgl_akhir = sdf.format(customDate.getTime());
                    }
                };
                new DatePickerDialog(RiwayatAbsensiActivity.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
            }
        });

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()) {
                    pDialogProcess.show();
                    loadAbsensi();
                }
            }
        });

    }

    private void loadAbsensi(){
        absensiModels = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RiwayatAbsensiActivity.this);
        rvAbsensi.setLayoutManager(layoutManager);

        JSONObject jbody = new JSONObject();
        try {
            jbody.put("datestart",tgl_awal);
            jbody.put("dateend",tgl_akhir);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ApiVolley(RiwayatAbsensiActivity.this,jbody,"POST", ServerUrl.viewAbsensi,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogProcess.dismiss();
                        llAbsensi.setVisibility(View.VISIBLE);
                        llNotFound.setVisibility(View.GONE);
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i=0; i < arr.length(); i++){
                                JSONObject isi = arr.getJSONObject(i);
                                absensiModels.add(new AbsensiModel(
                                        isi.getString("id"),
                                        isi.getString("nama"),
                                        formatDate("yyyy-MM-dd","dd/MM/yyyy",isi.getString("tgl")),
                                        isi.getString("shift"),
                                        isi.getString("jam_masuk"),
                                        isi.getString("jam_pulang"),
                                        isi.getString("scan_masuk"),
                                        isi.getString("scan_pulang"),
                                        isi.getString("status"),
                                        isi.getString("keterangan")
                                ));
                            }
                            adapter = new AbsensiAdapter(RiwayatAbsensiActivity.this,absensiModels);
                            rvAbsensi.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialogProcess.dismiss();
                        llAbsensi.setVisibility(View.GONE);
                        llNotFound.setVisibility(View.VISIBLE);
                        Toasty.error(RiwayatAbsensiActivity.this,message, Toast.LENGTH_SHORT).show();
                        rvAbsensi.setAdapter(null);
                    }

                    @Override
                    public void onFail(String message) {
                        pDialogProcess.dismiss();
                        if(message.equals("Unauthorized")){
                            try {
                                SessionManager session = new SessionManager(RiwayatAbsensiActivity.this);
                                session.logoutUser(Utils.loginActivity(RiwayatAbsensiActivity.this));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toasty.error(RiwayatAbsensiActivity.this,"Terjadi kesalahan saat memuat data",Toast.LENGTH_SHORT).show();
                        }
                        rvAbsensi.setAdapter(null);
                    }
                })
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean check(){
        if(tgl_awal.equals("")){
            Toasty.error(this,"Masukkan tanggal awal",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(tgl_akhir.equals("")){
            Toasty.error(this,"Masukkan tanggal akhir",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}