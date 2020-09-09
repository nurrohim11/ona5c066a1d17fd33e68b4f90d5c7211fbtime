package gmedia.net.id.OnTime.riwayat.kunjungan.tanggal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alkhattabi.sweetdialog.SweetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.jadwal.JadwalActivity;
import gmedia.net.id.OnTime.riwayat.jadwal.adapter.JadwalAdapter;
import gmedia.net.id.OnTime.riwayat.jadwal.model.JadwalModel;
import gmedia.net.id.OnTime.riwayat.kunjungan.KunjunganModel;
import gmedia.net.id.OnTime.riwayat.kunjungan.tanggal.adapter.KunjunganTglAdapter;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

public class KunjunganTglActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_kunjungan_tgl)
    RecyclerView rvRiwayatJadwal;
    @BindView(R.id.rl_tgl_awal)
    RelativeLayout rlTglAwal;
    @BindView(R.id.rl_tgl_akhir)
    RelativeLayout rlTglAkhir;
    @BindView(R.id.tv_tgl_awal)
    TextView tvTglAwal;
    @BindView(R.id.tv_tgl_akhir)
    TextView tvTglAkhir;
    @BindView(R.id.btn_proses)
    Button btnProses;

    int start =0, count=10;
    String tgl_awal="",tgl_akhir="";
    List<KunjunganModel> jadwalModels = new ArrayList<>();
    KunjunganTglAdapter adapter;
    Calendar calendar = Calendar.getInstance();
    SweetDialog pDialogProses;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kunjungan_tgl);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialogProses = new SweetDialog(KunjunganTglActivity.this, SweetDialog.PROGRESS_TYPE);
        pDialogProses.setCancelable(false);
        pDialogProses.getProgressHelper().setBarColor(R.color.colorProcess);

        start =0;count=10;
        linearLayoutManager = new LinearLayoutManager(this);
        setupListKunjunganTgl();
        setupListScrollListenerKunjunganTgl();
        initView();
    }


    private void initView(){
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
                new DatePickerDialog(KunjunganTglActivity.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
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
                new DatePickerDialog(KunjunganTglActivity.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
            }
        });

        btnProses.setOnClickListener(v -> {
            if(check()){
                start =0;
                count=10;
                adapter.notifyDataSetChanged();
                pDialogProses.show();
                loadKunjunganTgl("");
            }
        });
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

    private void setupListScrollListenerKunjunganTgl(){
        rvRiwayatJadwal.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (! recyclerView.canScrollVertically(1)){
                    pDialogProses.dismiss();
                    start += count;
                    loadKunjunganTgl("1");
                }
            }
        });
    }

    private void setupListKunjunganTgl(){
        adapter = new KunjunganTglAdapter(KunjunganTglActivity.this, jadwalModels);
        rvRiwayatJadwal.setLayoutManager(linearLayoutManager);
        rvRiwayatJadwal.setAdapter(adapter);
    }

    private void loadKunjunganTgl(String flag){
        if (start ==0){
            jadwalModels.clear();
        }
        JSONObject params = new JSONObject();
        try {
            params.put("datestart",tgl_awal);
            params.put("dateend",tgl_akhir);
            params.put("start",start);
            params.put("count",count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("kunjungan tgl", "jadwal "+params);

        new ApiVolley(KunjunganTglActivity.this,params,"POST", ServerUrl.urlViewKunjunganTgl,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogProses.dismiss();
                        Log.d("kunjungan tgl", "response "+response);

                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject isi = arr.getJSONObject(i);
                                jadwalModels.add(new KunjunganModel(
                                        isi.getString("tgl"),
                                        "",
                                        isi.getString("lokasi"),
                                        isi.getString("latitude"),
                                        isi.getString("longitude"),
                                        isi.getString("keterangan")
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialogProses.dismiss();
                        if(flag.equals("")){
                            jadwalModels.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFail(String message) {
                        pDialogProses.dismiss();
                        if(message.equals("Unauthorized")){
                            try {
                                SessionManager session = new SessionManager(KunjunganTglActivity.this);
                                session.logoutUser(Utils.loginActivity(KunjunganTglActivity.this));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toasty.error(KunjunganTglActivity.this,message, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        jadwalModels.clear();
        start =0;
        count =10;
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