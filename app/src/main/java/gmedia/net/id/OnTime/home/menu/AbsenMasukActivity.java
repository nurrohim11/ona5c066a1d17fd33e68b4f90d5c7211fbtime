package gmedia.net.id.OnTime.home.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.utils.Scan;

import static gmedia.net.id.OnTime.utils.ServerUrl.ScanAbsen;

public class AbsenMasukActivity extends AppCompatActivity {
    TextView tvJam, tvMenit;
    Button btnScanMasuk;
    private String tipe_scan = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_masuk);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initUi();
    }

    private void initUi(){
        tvJam = findViewById(R.id.tv_jam);
        tvMenit = findViewById(R.id.tv_menit);
        btnScanMasuk = findViewById(R.id.btn_scan_masuk);
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                long date = System.currentTimeMillis();
                SimpleDateFormat formatJam = new SimpleDateFormat("HH");
                SimpleDateFormat formatMenit = new SimpleDateFormat("mm");
                tvJam.setText(formatJam.format(date));
                tvMenit.setText(formatMenit.format(date));
                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(r, 100);

        btnScanMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Scan(AbsenMasukActivity.this, tipe_scan);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}