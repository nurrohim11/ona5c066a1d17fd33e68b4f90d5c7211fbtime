package gmedia.net.id.OnTime.pengajuan.reimburse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import gmedia.net.id.OnTime.R;

public class ReimburseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_tgl)
    RelativeLayout rlTgl;
    @BindView(R.id.tv_tgl_bayar)
    TextView tvTglBayar;
    @BindView(R.id.rl_nominal)
    RelativeLayout rlNominal;
    @BindView(R.id.edt_nomimal)
    EditText edtNominal;
    @BindView(R.id.iv_search_image)
    ImageView ivSearchImage;
    @BindView(R.id.iv_bukti)
    ImageView ivBukti;
    @BindView(R.id.edt_keterangan)
    EditText edtKeterangan;
    @BindView(R.id.btn_proses)
    Button btnProses;

    String tgl_bayar= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimburse);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initAction();
    }

    private void initAction(){
        ivSearchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}