package gmedia.net.id.OnTime.home.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.utils.Scan;

public class MulaiLemburActivity extends AppCompatActivity {

    @BindView(R.id.tv_jam)
    TextView tvJam;
    @BindView(R.id.tv_menit)
    TextView tvMenit;
    @BindView(R.id.btn_scan_mulai_lembur)
    Button btnScanMulaiLembur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulai_lembur);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi(){
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

        btnScanMulaiLembur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Scan(MulaiLemburActivity.this, "2");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}