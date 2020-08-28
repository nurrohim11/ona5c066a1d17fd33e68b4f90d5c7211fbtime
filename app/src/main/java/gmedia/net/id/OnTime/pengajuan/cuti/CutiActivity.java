package gmedia.net.id.OnTime.pengajuan.cuti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alkhattabi.sweetdialog.SweetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.MainActivity;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

import static gmedia.net.id.OnTime.utils.Utils.loginActivity;

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

    SweetDialog pDialogProses;
    SweetDialog pSweetDialogWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuti);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialogProses = new SweetDialog(this, SweetDialog.PROGRESS_TYPE);
        pDialogProses.getProgressHelper().setBarColor(R.color.colorProcess);
        pDialogProses.setTitleText("Sedang memproses..");
        pDialogProses.setCancelable(false);


        init();
    }

    private void init(){
        loadTotalCuti();
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
                if(check()){
                    pSweetDialogWarning = new SweetDialog(CutiActivity.this, SweetDialog.WARNING_TYPE);
                    pSweetDialogWarning.setTitleText("Are you sure?");
                    pSweetDialogWarning.setContentText("Apakah anda yakin akan mengajukan cuti ?");
                    pSweetDialogWarning.setConfirmText("Ya");
                    pSweetDialogWarning.setCancelText("Tidak");
                    pSweetDialogWarning.setConfirmClickListener(new SweetDialog.SweetClickListener() {
                        @Override
                        public void onClick(SweetDialog sDialog) {
                            pSweetDialogWarning.dismiss();
                            prosesCuti();
                        }
                    });
                    pSweetDialogWarning.setCancelClickListener(new SweetDialog.SweetClickListener() {
                        @Override
                        public void onClick(SweetDialog sDialog) {
                            pSweetDialogWarning.dismiss();
                        }
                    });
                    pSweetDialogWarning.show();

                }
            }
        });
    }



    private void initDefault(){
        tvTglAwal.setText("Masukkan tanggal awal cuti");
        tvTglAkhir.setText("Masukkan tanggal akhir cuti");
        tgl_awal = "";
        tgl_akhir = "";
        edtKeterangan.setText("");

    }

    private void loadTotalCuti(){
        new ApiVolley(CutiActivity.this,new JSONObject(),"GET",ServerUrl.sisaCuti,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @SuppressLint("SetTextI18n")
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
                        if(message.equals("Unauthorized")){
                            try {
                                SessionManager session = new SessionManager(CutiActivity.this);
                                session.logoutUser(Utils.loginActivity(CutiActivity.this));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toasty.error(CutiActivity.this,"Terjadi kesalahan saat memuat data",Toast.LENGTH_SHORT).show();
                        }
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
            Toasty.error(this,"Masukkan tanggal awal cuti",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(tgl_akhir.equals("")){
            Toasty.error(this,"Masukkan tanggal akhir cuti",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(edtKeterangan.equals("")){
            Toasty.error(this,"Masukkan alasan cuti",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!tgl_awal.equals("") && !tgl_akhir.equals("")){
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(c);

            if(today.compareTo(tgl_awal) > 0 || today.compareTo(tgl_awal) == 0){
                Toasty.error(CutiActivity.this,"Pengajuan cuti tidak boleh hari ini atau hari kemaren",Toast.LENGTH_SHORT).show();
                return false;
            }

            if(tgl_awal.compareTo(tgl_akhir) > 0){
                Toasty.error(CutiActivity.this,"Tanggal cuti tidak sesuai",Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }
}