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

public class AbsenPulangActivity extends AppCompatActivity {

    TextView tvJam, tvMenit;
    Button btnScanPulang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_pulang);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initUi();
    }

    private void initUi(){
        tvJam = findViewById(R.id.tv_jam);
        tvMenit = findViewById(R.id.tv_menit);
        btnScanPulang = findViewById(R.id.btn_scan_pulang);
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                long date = System.currentTimeMillis();
                SimpleDateFormat formatTgl = new SimpleDateFormat("dd MMMM yyyy");
                SimpleDateFormat formatJam = new SimpleDateFormat("HH");
                SimpleDateFormat formatMenit = new SimpleDateFormat("mm");
                tvJam.setText(formatJam.format(date));
                tvMenit.setText(formatMenit.format(date));
                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(r, 100);

        btnScanPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Scan(AbsenPulangActivity.this, "2");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}