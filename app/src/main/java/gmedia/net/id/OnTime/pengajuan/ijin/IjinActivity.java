package gmedia.net.id.OnTime.pengajuan.ijin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alkhattabi.kalert.KAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.pengajuan.cuti.CutiActivity;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

import static gmedia.net.id.coremodul.FormatItem.formatDate;


public class IjinActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_tgl)
    RelativeLayout rlTgl;
    @BindView(R.id.tv_tgl)
    TextView tvTgl;
    @BindView(R.id.rl_jam)
    RelativeLayout rlJam;
    @BindView(R.id.tv_jam)
    TextView tvJam;
    @BindView(R.id.edt_keterangan)
    EditText edtKeterangan;
    @BindView(R.id.btn_proses)
    Button btnProses;

    String tgl="";
    String jam="";

    KAlertDialog pDialogProses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijin);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialogProses = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialogProses.getProgressHelper().setBarColor(R.color.colorProcess);
        pDialogProses.setTitleText("Sedang memproses..");
        pDialogProses.setCancelable(false);

        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");
        tvTgl.setText(sdFormat.format(date.getTime()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tgl = sdf.format(date.getTime());

        SimpleDateFormat fTime = new SimpleDateFormat("HH:mm");
        tvJam.setText(fTime.format(date.getTime()));
        jam = fTime.format(date.getTime());

        initAction();
    }

    private void  initAction(){
        rlTgl.setOnClickListener(new View.OnClickListener() {
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
                        tvTgl.setText(sdFormat.format(customDate.getTime()));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        tgl = sdf.format(customDate.getTime());
                    }
                };
                new DatePickerDialog(IjinActivity.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
            }
        });

        rlJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar mcurrentTime = java.util.Calendar.getInstance();
                int hour = mcurrentTime.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(java.util.Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(IjinActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String jamx = String.valueOf(selectedHour);
                        String menitx = String.valueOf(selectedMinute);
                        jamx = jamx.length() < 2 ? "0" + jamx : jamx;
                        menitx = menitx.length() < 2 ? "0" + menitx : menitx;
                        tvJam.setText(jamx + ":" + menitx);
                        jam = jamx + ":" + menitx;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doIjin();
            }
        });
    }

    private void doIjin(){
        pDialogProses.show();
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("tgl", tgl);
            jBody.put("jam", jam);
            jBody.put("alasan", edtKeterangan.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("IjinActivity",String.valueOf(jBody));

        new ApiVolley(IjinActivity.this,jBody,"POST", ServerUrl.addIjin,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogProses.dismiss();
                        Toasty.success(IjinActivity.this,message, Toast.LENGTH_SHORT).show();
                        initDefault();
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialogProses.dismiss();
                        Toasty.error(IjinActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String message) {
                        pDialogProses.dismiss();
                        Toasty.error(IjinActivity.this,"Terjadi kesalahan saat memuat data",Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    private void initDefault(){
        tvTgl.setText("Masukkan tanggal");
        tvJam.setText("Masukkan jam");
        edtKeterangan.setText("");
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