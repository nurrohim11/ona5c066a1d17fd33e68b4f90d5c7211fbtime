package gmedia.net.id.OnTime.pengajuan.lembur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.bumptech.glide.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.pengajuan.ijin.IjinActivity;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.FormatItem;

public class LemburActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_mulai_lembur)
    RelativeLayout rlMulaiLembur;
    @BindView(R.id.tv_mulai_lembur)
    TextView tvMulaiLembur;
    @BindView(R.id.rl_selesai_lembur)
    RelativeLayout rlSelesaiLembur;
    @BindView(R.id.tv_selesai_lembur)
    TextView tvSelesaiLembur;
    @BindView(R.id.edt_keterangan)
    EditText edtKeterangan;
    @BindView(R.id.btn_proses)
    Button btnProses;

    String tgl_mulai_lembur = "";
    String time_mulai_lembur = "";
    String tgl_selesai_lembur ="";
    String time_selesai_lembur ="";
    private KAlertDialog pDialogProses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembur);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialogProses = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialogProses.getProgressHelper().setBarColor(R.color.colorProcess);
        pDialogProses.setTitleText("Sedang memproses..");
        pDialogProses.setCancelable(false);
        initAction();
    }

    @SuppressLint("SetTextI18n")
    private void initAction(){
        initDefault();
        rlMulaiLembur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Date[] value = {new Date()};
                final Calendar cal = Calendar.getInstance();
                cal.setTime(value[0]);
                new DatePickerDialog(LemburActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override public void onDateSet(DatePicker view,
                                                            int y, int m, int d) {
                                cal.set(Calendar.YEAR, y);
                                cal.set(Calendar.MONTH, m);
                                cal.set(Calendar.DAY_OF_MONTH, d);
                                tgl_mulai_lembur = Utils.customFormatTimestamp(cal.getTime(),FormatItem.formatDate);
                                tvMulaiLembur.setText(Utils.customFormatTimestamp(cal.getTime(),FormatItem.formatDate)+" "+time_mulai_lembur);

                                // now show the time picker
                                new TimePickerDialog(LemburActivity.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override public void onTimeSet(TimePicker view,
                                                                            int h, int min) {
                                                cal.set(Calendar.HOUR_OF_DAY, h);
                                                cal.set(Calendar.MINUTE, min);
                                                value[0] = cal.getTime();
                                                time_mulai_lembur = Utils.customFormatTimestamp(cal.getTime(),FormatItem.formatTime2);
                                                tvMulaiLembur.setText(Utils.customFormatTimestamp(cal.getTime(),FormatItem.formatDate)+" "+time_mulai_lembur);
                                            }
                                        }, cal.get(Calendar.HOUR_OF_DAY),
                                        cal.get(Calendar.MINUTE), true).show();
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        rlSelesaiLembur.setOnClickListener(v->{
            final Date[] value = {new Date()};
            final Calendar cal = Calendar.getInstance();
            cal.setTime(value[0]);
            new DatePickerDialog(LemburActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override public void onDateSet(DatePicker view,
                                                        int y, int m, int d) {
                            cal.set(Calendar.YEAR, y);
                            cal.set(Calendar.MONTH, m);
                            cal.set(Calendar.DAY_OF_MONTH, d);
                            tgl_selesai_lembur = Utils.customFormatTimestamp(cal.getTime(),FormatItem.formatDate);
                            tvSelesaiLembur.setText(Utils.customFormatTimestamp(cal.getTime(),FormatItem.formatDate)+" "+time_selesai_lembur);

                            // now show the time picker
                            new TimePickerDialog(LemburActivity.this,
                                    new TimePickerDialog.OnTimeSetListener() {
                                        @Override public void onTimeSet(TimePicker view,
                                                                        int h, int min) {
                                            cal.set(Calendar.HOUR_OF_DAY, h);
                                            cal.set(Calendar.MINUTE, min);
                                            value[0] = cal.getTime();
                                            time_selesai_lembur = Utils.customFormatTimestamp(cal.getTime(),FormatItem.formatTime2);
                                            tvSelesaiLembur.setText(Utils.customFormatTimestamp(cal.getTime(),FormatItem.formatDate)+" "+time_selesai_lembur);
                                        }
                                    }, cal.get(Calendar.HOUR_OF_DAY),
                                    cal.get(Calendar.MINUTE), true).show();
                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();

        });

        btnProses.setOnClickListener(v->{
            submitLembur();
        });
    }

    private void initDefault(){
        Calendar calendar = Calendar.getInstance();
        Log.d("LemburActivity", String.valueOf(calendar.getTime()));
        tgl_mulai_lembur = Utils.customFormatTimestamp(calendar.getTime(),FormatItem.formatDate);
        time_mulai_lembur = Utils.customFormatTimestamp(calendar.getTime(),FormatItem.formatTime2);
        tvMulaiLembur.setText(Utils.customFormatTimestamp(calendar.getTime(),FormatItem.formatDateDisplay)+" "+time_mulai_lembur);

        tgl_selesai_lembur = Utils.customFormatTimestamp(calendar.getTime(),FormatItem.formatDate);
        time_selesai_lembur = Utils.customFormatTimestamp(calendar.getTime(),FormatItem.formatTime2);
        tvSelesaiLembur.setText(Utils.customFormatTimestamp(calendar.getTime(),FormatItem.formatDateDisplay)+" "+time_selesai_lembur);

        edtKeterangan.setText("");
    }

    @SuppressLint("SimpleDateFormat")
    private void submitLembur(){
        pDialogProses.show();
        String timestamp_mulai_lembur = tgl_mulai_lembur+" "+time_mulai_lembur;
        String timestamp_selesai_lembur = tgl_selesai_lembur+" "+time_selesai_lembur;

        JSONObject jBody = new JSONObject();
        try {
            jBody.put("datetimestart",timestamp_mulai_lembur);
            jBody.put("datetimeend",timestamp_selesai_lembur);
            jBody.put("keterangan",edtKeterangan.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ApiVolley(LemburActivity.this,jBody, "POST", ServerUrl.addLembur,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogProses.dismiss();
                        Toasty.success(LemburActivity.this,message,Toast.LENGTH_SHORT).show();
                        initDefault();
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialogProses.dismiss();
                        Toasty.error(LemburActivity.this,message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String message) {
                        pDialogProses.dismiss();
                        Toasty.error(LemburActivity.this,message, Toast.LENGTH_SHORT).show();
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