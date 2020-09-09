package gmedia.net.id.OnTime.riwayat.scanlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.home.news.NewsActivity;
import gmedia.net.id.OnTime.riwayat.scanlog.adapter.ScanlogAdapter;
import gmedia.net.id.OnTime.riwayat.scanlog.model.ScanlogModel;
import gmedia.net.id.OnTime.utils.GridSpacingItemDecoration;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

import static gmedia.net.id.OnTime.utils.Utils.formatTime;

public class ScanlogActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_tgl)
    RelativeLayout rlTgl;
    @BindView(R.id.tv_tgl)
    TextView tvTgl;
    @BindView(R.id.btn_proses)
    Button btnProses;
    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;
    @BindView(R.id.tv_tgl_detail)
    TextView tvTglDetail;
    @BindView(R.id.rv_scanlog)
    RecyclerView rvScanlog;

    String tgl ="";
    List<ScanlogModel> scanlogModels;
    ScanlogAdapter adapter;

    BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanlog);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initAction();
    }

    private void initAction(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        tvTgl.setText(formattedDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tgl = sdf.format(c);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
//                    case BottomSheetBehavior.STATE_HIDDEN:{}
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED: {}
//                    break;
//                    case BottomSheetBehavior.STATE_COLLAPSED: {}
//                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:{}
//                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

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
                new DatePickerDialog(ScanlogActivity.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
            }
        });

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailScanlog();
            }
        });

        tvTglDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    private void showDetailScanlog() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ScanlogActivity.this);
        rvScanlog.setLayoutManager(layoutManager);
        scanlogModels = new ArrayList<>();

        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        tvTglDetail.setText(tvTgl.getText().toString());

        JSONObject jbody = new JSONObject();
        try {
            jbody.put("date",tgl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ApiVolley(ScanlogActivity.this,jbody,"POST", ServerUrl.viewScanlog,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
            @Override
            public void onSuccess(String response, String message) {
                Log.d(">>>>>",response);
                try {
                    JSONArray arr = new JSONArray(response);
                    scanlogModels.add(new ScanlogModel(
                            "0","","","Waktu","Keterangan",0,0
                    ));
                    for (int i=0; i < arr.length(); i++){
                        JSONObject isi = arr.getJSONObject(i);
                        scanlogModels.add(new ScanlogModel(
                                isi.getString("id"),
                                isi.getString("nama"),
                                isi.getString("scan_date"),
                                formatTime(isi.getString("scan_time")),
                                isi.getString("keterangan")
                        ));
                    }
                    adapter = new ScanlogAdapter(ScanlogActivity.this,scanlogModels);
                    rvScanlog.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEmpty(String message) {
                Toasty.error(ScanlogActivity.this,message,Toast.LENGTH_SHORT).show();
                rvScanlog.setAdapter(null);
            }

            @Override
            public void onFail(String message) {
                Toasty.error(ScanlogActivity.this,"Terjadi kesalahan data",Toast.LENGTH_SHORT).show();
                rvScanlog.setAdapter(null);
            }
        }));
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