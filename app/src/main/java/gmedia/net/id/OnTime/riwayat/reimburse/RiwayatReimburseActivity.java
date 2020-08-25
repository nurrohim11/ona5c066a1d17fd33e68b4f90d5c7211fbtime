package gmedia.net.id.OnTime.riwayat.reimburse;

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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.reimburse.adapter.ReimburseAdapter;
import gmedia.net.id.OnTime.riwayat.reimburse.model.ReimburseModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

public class RiwayatReimburseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_riwayat_reimburse)
    RecyclerView rvRiwayatReimburse;
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
    List<ReimburseModel> reimburseModelList = new ArrayList<>();
    ReimburseAdapter reimburseAdapter;
    Calendar calendar = Calendar.getInstance();
    SweetDialog pDialogProses;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_reimburse);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialogProses = new SweetDialog(RiwayatReimburseActivity.this, SweetDialog.PROGRESS_TYPE);
        pDialogProses.setCancelable(false);
        pDialogProses.getProgressHelper().setBarColor(R.color.colorProcess);

        start =0;count=10;
        linearLayoutManager = new LinearLayoutManager(this);
        setupListRiwayatReimburse();
        setupListScrollListenerRiwayatReimburse();
        initView();
    }

    private void initView(){
        Date c = calendar.getTime();
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
                new DatePickerDialog(RiwayatReimburseActivity.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
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
                new DatePickerDialog(RiwayatReimburseActivity.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
            }
        });

        btnProses.setOnClickListener(v -> {
            reimburseAdapter.notifyDataSetChanged();
            pDialogProses.show();
            loadRiwayatReimburse("");
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

    private void setupListScrollListenerRiwayatReimburse(){
        rvRiwayatReimburse.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (! recyclerView.canScrollVertically(1)){
                    pDialogProses.dismiss();
                    start += count;
                    loadRiwayatReimburse("1");
                }
            }
        });
    }

    private void setupListRiwayatReimburse(){
        reimburseAdapter = new ReimburseAdapter(RiwayatReimburseActivity.this, reimburseModelList);
        rvRiwayatReimburse.setLayoutManager(linearLayoutManager);
        rvRiwayatReimburse.setAdapter(reimburseAdapter);
    }

    private void loadRiwayatReimburse(String flag){
        if(start == 0){
//            pDialogProses.dismiss();
            reimburseModelList.clear();
        }
        JSONObject params = new JSONObject();
        try {
            params.put("datestart",tgl_awal);
            params.put("dateend",tgl_akhir);
            params.put("start", String.valueOf(start));
            params.put("count", String.valueOf(count));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("RiwayatReimburse", "params "+ String.valueOf(params));

        new ApiVolley(RiwayatReimburseActivity.this,params,"POST", ServerUrl.urlRiwayatReimburse,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogProses.dismiss();
                        Log.d("RiwayatReimburse", "response "+response);

                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject isi = arr.getJSONObject(i);
                                reimburseModelList.add(new ReimburseModel(
                                        isi.getString("id"),
                                        isi.getString("nik"),
                                        isi.getString("nama"),
                                        isi.getString("tgl_pembayaran"),
                                        isi.getString("foto_pembayaran"),
                                        isi.getString("nominal"),
                                        isi.getString("ket"),
                                        isi.getString("approval"),
                                        isi.getString("insert_at"),
                                        isi.getString("status")
                                ));
                            }
                            reimburseAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialogProses.dismiss();
                        if(flag.equals("")){
                            reimburseModelList.clear();
                            reimburseAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFail(String message) {
                        pDialogProses.dismiss();
                        Toasty.error(RiwayatReimburseActivity.this,message, Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        reimburseModelList.clear();
        start =0;
        count =10;
//        loadRiwayatReimburse("");
    }

}