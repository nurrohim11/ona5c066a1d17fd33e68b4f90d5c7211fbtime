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
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

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
    @BindView(R.id.tv_tgl_detail_awal)
    TextView tvTglDetailAwal;
    @BindView(R.id.tv_tgl_detail_akhir)
    TextView tvTglDetailAkhir;
    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;
    @BindView(R.id.ll_tgl)
    LinearLayout llTgl;

    String tgl_awal ="";
    String tgl_akhir ="";
    List<AbsensiModel> absensiModels;
    AbsensiAdapter adapter;

    BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_absensi);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        tvTglAwal.setText(formattedDate);
        tvTglAkhir.setText(formattedDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tgl_awal = sdf.format(c);
        tgl_akhir = sdf.format(c);

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

        behavior = BottomSheetBehavior.from(layoutBottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSheetAbsensi();
            }
        });

        llTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    private void showSheetAbsensi(){
        if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        absensiModels = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RiwayatAbsensiActivity.this);
        rvAbsensi.setLayoutManager(layoutManager);

        tvTglDetailAwal.setText(tvTglAwal.getText().toString());
        tvTglDetailAkhir.setText(tvTglAkhir.getText().toString());

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
                        try {
                            JSONArray arr = new JSONArray(response);
                            absensiModels.add(new AbsensiModel(
                                    "0","","Tanggal","",
                                    "","","",
                                    "","","Keterangan"
                            ));
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
                        Toasty.error(RiwayatAbsensiActivity.this,message, Toast.LENGTH_SHORT).show();
                        rvAbsensi.setAdapter(null);
                    }

                    @Override
                    public void onFail(String message) {
                        Toasty.error(RiwayatAbsensiActivity.this,"Terjadi kesalahan saat memuat data",Toast.LENGTH_SHORT).show();
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
}