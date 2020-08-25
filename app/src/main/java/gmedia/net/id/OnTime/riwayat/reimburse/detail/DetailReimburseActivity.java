package gmedia.net.id.OnTime.riwayat.reimburse.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.reimburse.model.ReimburseModel;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.SessionManager;

public class DetailReimburseActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    public static final String REIMBURSE_ITEM = "reimburse_item";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_nik)
    TextView tvNik;
    @BindView(R.id.tv_nama)
    TextView tvNama;
    @BindView(R.id.tv_tgl_bayar)
    TextView tvTglBayarl;
    @BindView(R.id.tv_nominal)
    TextView tvNominal;
    @BindView(R.id.tv_ket)
    TextView tvKet;
    @BindView(R.id.img_bukti)
    ImageView imgBukti;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reimburse);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUi();
    }

    private void initUi(){
        String reimburse_item = getIntent().getStringExtra(REIMBURSE_ITEM);
        final ReimburseModel item = gson.fromJson(reimburse_item, ReimburseModel.class);

        tvNik.setText(item.getNik());
        tvNama.setText(item.getNama());
        tvTglBayarl.setText(Utils.formatDate("yyyy-MM-dd", "dd MMMM yyyy", item.getTgl_pembayaran()));
        tvKet.setText(item.getKet());

        Integer nominal = Integer.parseInt(item.getNominal());
        tvNominal.setText(String.format("Rp %,d", nominal));

        Glide
                .with(DetailReimburseActivity.this)
                .load(item.getFoto())
                .placeholder(R.drawable.computer_user)
                .into(imgBukti);

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