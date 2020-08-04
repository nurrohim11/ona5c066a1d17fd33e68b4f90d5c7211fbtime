package gmedia.net.id.OnTime.pengajuan.cuti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alkhattabi.kalert.KAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.scanlog.ScanlogActivity;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

public class CutiActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_jml_sisa_cuti)
    TextView tvJmlSisaCuti;
    @BindView(R.id.rl_tgl_awal)
    RelativeLayout rlTglAwal;
    @BindView(R.id.tv_tgl_awal)
    TextView tvTglAwal;
    @BindView(R.id.rl_tgl_akhir)
    RelativeLayout rlTglAkhir;
    @BindView(R.id.tv_tgl_akhir)
    TextView tvTglAkhir;
    @BindView(R.id.edt_keterangan)
    EditText edtKeterangan;
    @BindView(R.id.btn_proses)
    Button btnProses;

    String tgl_awal;
    String tgl_akhir;

    KAlertDialog pDialogProses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuti);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialogProses = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialogProses.getProgressHelper().setBarColor(R.color.colorProcess);
        pDialogProses.setTitleText("Sedang memproses..");
        pDialogProses.setCancelable(false);

        init();
    }

    private void init(){
        loadSisaCuti();
        initDefault();
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
                new DatePickerDialog(CutiActivity.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
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
                new DatePickerDialog(CutiActivity.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
            }
        });

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesCuti();
            }
        });
    }

    private void initDefault(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        tvTglAwal.setText(formattedDate);
        tvTglAkhir.setText(formattedDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tgl_awal = sdf.format(c);
        tgl_akhir = sdf.format(c);
        edtKeterangan.setText("");
    }

    private void loadSisaCuti(){
        new ApiVolley(CutiActivity.this,new JSONObject(),"GET",ServerUrl.sisaCuti,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        try {
                            JSONObject res = new JSONObject(response);
                            tvJmlSisaCuti.setText(res.getString("jumlah")+" hari");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onEmpty(String message) {
                    }

                    @Override
                    public void onFail(String message) {
                    }
                })
        );
    }


    private void prosesCuti(){
        pDialogProses.show();
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("datestart", tgl_awal);
            jBody.put("dateend", tgl_akhir);
            jBody.put("alasan", edtKeterangan.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiVolley(CutiActivity.this,jBody,"POST",ServerUrl.addCuti,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogProses.dismiss();
                        Toasty.success(CutiActivity.this,message,Toast.LENGTH_SHORT).show();
                        initDefault();
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialogProses.dismiss();
                        Toasty.error(CutiActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String message) {
                        pDialogProses.dismiss();
                        Toasty.error(CutiActivity.this,"Terjadi kesalahan saat memuat data",Toast.LENGTH_SHORT).show();
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