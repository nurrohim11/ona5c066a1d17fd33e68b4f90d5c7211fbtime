package gmedia.net.id.OnTime.riwayat.gaji;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alkhattabi.kalert.KAlertDialog;
import com.bumptech.glide.util.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.absensi.RiwayatAbsensiActivity;
import gmedia.net.id.OnTime.riwayat.absensi.adapter.AbsensiAdapter;
import gmedia.net.id.OnTime.riwayat.absensi.model.AbsensiModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

import static gmedia.net.id.OnTime.utils.Utils.formatDate;

public class GajiActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sp_bulan)
    Spinner spBulan;
    @BindView(R.id.edt_tahun)
    EditText edtTahun;
    @BindView(R.id.ll_header_gaji)
    LinearLayout llHeaderGaji;
    @BindView(R.id.ll_gaji_empty)
    LinearLayout llGajiNotFound;
    @BindView(R.id.ll_gaji)
    LinearLayout llGajiFound;
    @BindView(R.id.btn_proses)
    Button btnProses;
    @BindView(R.id.tv_total_gaji)
    TextView tvTotalGaji;
    @BindView(R.id.tv_jml_gaji)
    TextView tvJmlGaji;
    private String posisiMenuBulan = "", posisiMenuTahun = "";
    private String[] bulan = new String[]
            {
                    "Pilih Bulan",
                    "Januari",
                    "Februari",
                    "Maret",
                    "April",
                    "Mei",
                    "Juni",
                    "Juli",
                    "Agustus",
                    "September",
                    "Oktober",
                    "November",
                    "Desember"
            };

    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;

    BottomSheetBehavior behavior;
    KAlertDialog pDialogProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaji);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pDialogProcess = new KAlertDialog(GajiActivity.this,KAlertDialog.PROGRESS_TYPE);
        pDialogProcess.getProgressHelper().setBarColor(R.color.colorProcess);
        pDialogProcess.setCancelable(false);
        initView();
    }

    private void initView(){
        Calendar calendar = Calendar.getInstance();
        edtTahun.setText(Utils.customFormatTimestamp(calendar.getTime(),"yyyy"));

        ArrayAdapter<String> spinnerAdapterBulan = new ArrayAdapter<String>(GajiActivity.this, R.layout.spinner_items, bulan) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.WHITE);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        spinnerAdapterBulan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBulan.setAdapter(spinnerAdapterBulan);
        spBulan.setSelection(Integer.parseInt(Utils.customFormatTimestamp(calendar.getTime(),"MM")));
        spBulan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posisiMenuBulan = String.valueOf(spBulan.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        llHeaderGaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        btnProses.setOnClickListener(v->{

            if (posisiMenuBulan.equals("0")) {
                Toasty.error(GajiActivity.this, "Silahkan Pilih Bulan Terlebih Dahulu", Toast.LENGTH_LONG).show();
                return;
            }else if(TextUtils.isEmpty(edtTahun.getText().toString())){
                Toasty.error(GajiActivity.this, "Masukkan tahun Terlebih Dahulu", Toast.LENGTH_LONG).show();
            } else {
                pDialogProcess.show();
                showSheetGaji();
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

    private void showSheetGaji(){
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("tgl", "");
            jBody.put("bln", posisiMenuBulan);
            jBody.put("thn", edtTahun.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("GajiActivity","params"+jBody);

        new ApiVolley(GajiActivity.this,jBody,"POST",ServerUrl.infoGaji,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogProcess.dismiss();
                        if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                        Log.d("GajiActivity",response);
                        llGajiFound.setVisibility(View.VISIBLE);
                        try {
                            JSONObject res = new JSONObject(response);
                            tvJmlGaji.setText(Utils.formatRupiah(res.getString("nominal")));
                            tvTotalGaji.setText(Utils.formatRupiah(res.getString("nominal")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialogProcess.dismiss();
                        if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                        llGajiNotFound.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFail(String message) {
                        pDialogProcess.dismiss();
                        if(message.equals("Unauthorized")){
                            try {
                                SessionManager session = new SessionManager(GajiActivity.this);
                                session.logoutUser(Utils.loginActivity(GajiActivity.this));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toasty.error(GajiActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );

    }

}