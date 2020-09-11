package gmedia.net.id.OnTime.riwayat.kunjungan.hari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.rohimdev.sweetdialog.SweetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.gaji.GajiActivity;
import gmedia.net.id.OnTime.riwayat.kunjungan.KunjunganModel;
import gmedia.net.id.OnTime.riwayat.kunjungan.hari.adapter.KunjunganHariAdapter;
import gmedia.net.id.OnTime.riwayat.kunjungan.tanggal.adapter.KunjunganTglAdapter;
import gmedia.net.id.OnTime.riwayat.scanlog.ScanlogActivity;
import gmedia.net.id.OnTime.riwayat.scanlog.adapter.ScanlogAdapter;
import gmedia.net.id.OnTime.riwayat.scanlog.model.ScanlogModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

import static gmedia.net.id.OnTime.utils.Utils.customFormatTimestamp;
import static gmedia.net.id.OnTime.utils.Utils.formatTime;

public class KunjunganHariActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_kunjungan_hari)
    RecyclerView rvKunjunganHari;
    @BindView(R.id.sp_hari)
    Spinner spHari;
    @BindView(R.id.btn_proses)
    Button btnProses;

    List<KunjunganModel> kunjunganModels = new ArrayList<>();
    KunjunganHariAdapter adapter;
    SweetDialog pDialogProcess;
    LinearLayoutManager linearLayoutManager;
    Calendar calendar = Calendar.getInstance();
    private String posisiMenuHari = "";

    private String[] hari = new String[]
            {
                    "Pilih hari",
                    "Senin",
                    "Selasa",
                    "Rabu",
                    "Kamis",
                    "Jumat",
                    "Sabtu",
                    "Minggu"
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kunjungan_hari);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pDialogProcess = new SweetDialog(KunjunganHariActivity.this, SweetDialog.PROGRESS_TYPE);
        pDialogProcess.getProgressHelper().setBarColor(R.color.colorProcess);
        pDialogProcess.setCancelable(false);
        Log.d("kunjunganhari",String.valueOf(today()));
        initView();

    }

    private void initView(){

        ArrayAdapter<String> spinnerAdapterHari = new ArrayAdapter<String>(KunjunganHariActivity.this, R.layout.spinner_items, hari) {
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
        spinnerAdapterHari.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHari.setAdapter(spinnerAdapterHari);
        posisiMenuHari = String.valueOf(today());
        spHari.setSelection(today()+1);
        Log.d("kunjunganhari","posisi "+String.valueOf(Integer.parseInt(posisiMenuHari)));
        spHari.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posisiMenuHari = String.valueOf(spHari.getSelectedItemPosition()-1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnProses.setOnClickListener(v->{
            pDialogProcess.show();
            loadKunjunganHari();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadKunjunganHari();
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

    private void  loadKunjunganHari(){
        kunjunganModels = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(KunjunganHariActivity.this);
        rvKunjunganHari.setLayoutManager(layoutManager);

        JSONObject jbody = new JSONObject();
        try {
            jbody.put("hari",posisiMenuHari);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("kunjunganhari",String.valueOf(jbody));
        new ApiVolley(KunjunganHariActivity.this,jbody,"POST", ServerUrl.urlViewKunjunganHari,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        Log.d(">>>>>",response);
                        pDialogProcess.dismiss();
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i=0; i < arr.length(); i++){
                                JSONObject isi = arr.getJSONObject(i);
                                kunjunganModels.add(new KunjunganModel(
                                        isi.getString("hari"),
                                        isi.getString("lokasi"),
                                        isi.getString("latitude"),
                                        isi.getString("longitude"),
                                        isi.getString("keterangan")
                                ));
                            }
                            adapter = new KunjunganHariAdapter(KunjunganHariActivity.this,kunjunganModels);
                            rvKunjunganHari.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialogProcess.dismiss();
                        Toasty.error(KunjunganHariActivity.this,message,Toast.LENGTH_SHORT).show();
                        rvKunjunganHari.setAdapter(null);
                    }

                    @Override
                    public void onFail(String message) {
                        pDialogProcess.dismiss();
                        Toasty.error(KunjunganHariActivity.this,"Terjadi kesalahan data",Toast.LENGTH_SHORT).show();
                        rvKunjunganHari.setAdapter(null);
                    }
                })
        );
    }

    private int today(){
        int today =-1;
        String hari =  customFormatTimestamp(calendar.getTime(),"E");
        Log.d("kunjunganhari",hari);
        if(hari.equals("Sen") || hari.equals("Mon")){
            today = 0;
        }else if(hari.equals("Sel") || hari.equals("Tue")){
            today = 1;
        }else if(hari.equals("Rab") || hari.equals("Wed")){
            today = 2;
        }else if(hari.equals("Kam") || hari.equals("Thu")){
            today = 3;
        }else if(hari.equals("Jum") || hari.equals("Fri")){
            today = 4;
        }else if(hari.equals("Sab") || hari.equals("Sat")){
            today = 5;
        }else if(hari.equals("Min") || hari.equals("Sun")){
            today = 6;
        }
        return today;
    }
}