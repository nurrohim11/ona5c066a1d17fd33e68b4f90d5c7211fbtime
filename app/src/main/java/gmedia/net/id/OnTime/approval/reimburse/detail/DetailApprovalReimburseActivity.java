package gmedia.net.id.OnTime.approval.reimburse.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alkhattabi.sweetdialog.SweetDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.approval.reimburse.model.ReimburseModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

public class DetailApprovalReimburseActivity extends AppCompatActivity {

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
    @BindView(R.id.btn_terima)
    Button btnTerima;
    @BindView(R.id.btn_tolak)
    Button btnTolak;
    @BindView(R.id.rl_button)
    RelativeLayout rlButton;
    SessionManager sessionManager;
    SweetDialog pDialog;
    SweetDialog pDialogApprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_approval_reimburse);
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
                .with(DetailApprovalReimburseActivity.this)
                .load(item.getFoto())
                .placeholder(R.drawable.computer_user)
                .into(imgBukti);
        if(sessionManager.getKeyApprovalReimburs().equals("1")){
            if(item.getStatus().equals("0")){
                btnTerima = findViewById(R.id.btn_terima);
                btnTolak = findViewById(R.id.btn_tolak);

                btnTerima.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pDialogApprove = new SweetDialog(DetailApprovalReimburseActivity.this, SweetDialog.CUSTOM_IMAGE_TYPE);
                        pDialogApprove.setTitleText("Are you sure ?");
                        pDialogApprove.setContentText("Apakah anda yakin untuk menyutujui pengajuan ini ?");
                        pDialogApprove.setCustomImage(R.drawable.gambaraproval);
                        pDialogApprove.setCancelable(true);
                        pDialogApprove.setCloseDialog(true);
                        pDialogApprove.setConfirmText("Setujui"); //Do not call this if you don't want to show confirm button
                        pDialogApprove.setCancelText("Batal");//Do not call this if you don't want to show cancel button
                        pDialogApprove.setConfirmClickListener(new SweetDialog.KAlertClickListener() {
                            @Override
                            public void onClick(SweetDialog sweetDialog) {
                                pDialog = new SweetDialog(DetailApprovalReimburseActivity.this, SweetDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
                                pDialog.setCancelable(false);
                                pDialog.show();
                                approveReimburse(item.getId(),"1");
                            }
                        });
                        pDialogApprove.setCancelClickListener(new SweetDialog.KAlertClickListener() {
                            @Override
                            public void onClick(SweetDialog sweetDialog) {
                                pDialogApprove.dismiss();
                            }
                        });
                        pDialogApprove.show();
                    }
                });
                btnTolak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pDialogApprove = new SweetDialog(DetailApprovalReimburseActivity.this, SweetDialog.CUSTOM_IMAGE_TYPE);
                        pDialogApprove.setTitleText("Are you sure ?");
                        pDialogApprove.setContentText("Apakah anda yakin untuk menolak pengajuan ini ?");
                        pDialogApprove.setCustomImage(R.drawable.gambaraproval);
                        pDialogApprove.setCancelable(true);
                        pDialogApprove.setCloseDialog(true);
                        pDialogApprove.setConfirmText("Tolak"); //Do not call this if you don't want to show confirm button
                        pDialogApprove.setCancelText("Batal");//Do not call this if you don't want to show cancel button
                        pDialogApprove.setConfirmClickListener(new SweetDialog.KAlertClickListener() {
                            @Override
                            public void onClick(SweetDialog sweetDialog) {
                                pDialog = new SweetDialog(DetailApprovalReimburseActivity.this, SweetDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
                                pDialog.setCancelable(false);
                                pDialog.show();
                                approveReimburse(item.getId(),"2");
                            }
                        });
                        pDialogApprove.setCancelClickListener(new SweetDialog.KAlertClickListener() {
                            @Override
                            public void onClick(SweetDialog sweetDialog) {
                                pDialogApprove.dismiss();
                            }
                        });
                        pDialogApprove.show();
                    }
                });
            }else{
                rlButton.setVisibility(View.GONE);
            }
        }else{
            rlButton.setVisibility(View.GONE);
        }
    }

    private void approveReimburse(String id_reimburse,String status){
        JSONObject params = new JSONObject();
        try {
            params.put("id_reimburse",id_reimburse);
            params.put("status_apv",status);
        }catch (Exception e){
            e.printStackTrace();
        }
        new ApiVolley(DetailApprovalReimburseActivity.this, params,"POST", ServerUrl.urlPostApproval,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogApprove.dismiss();
                        pDialog.dismiss();
                        Toasty.success(DetailApprovalReimburseActivity.this,message, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pDialogApprove.dismiss();
                                finish();
                            }
                        },1000);
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialog.dismiss();
                        Toasty.error(DetailApprovalReimburseActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String message) {
                        pDialog.dismiss();
                        if(message.equals("Unauthorized")){
                            try {
                                SessionManager session = new SessionManager(DetailApprovalReimburseActivity.this);
                                session.logoutUser(Utils.loginActivity(DetailApprovalReimburseActivity.this));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toasty.error(DetailApprovalReimburseActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
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