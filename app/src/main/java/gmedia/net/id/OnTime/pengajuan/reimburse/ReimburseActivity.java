package gmedia.net.id.OnTime.pengajuan.reimburse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.rohimdev.sweetdialog.SweetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.pengajuan.lembur.LemburActivity;
import gmedia.net.id.OnTime.utils.Converter;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.ImageUtils;
import gmedia.net.id.coremodul.SessionManager;
import gun0912.tedbottompicker.TedBottomPicker;

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
    SweetDialog pDialogProses;

    String tgl_bayar= "";
    private Uri selectedUri;
    private RequestManager requestManager;
    private String current = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimburse);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialogProses = new SweetDialog(this, SweetDialog.PROGRESS_TYPE);
        pDialogProses.getProgressHelper().setBarColor(R.color.colorProcess);
        pDialogProses.setTitleText("Sedang memproses..");
        pDialogProses.setCancelable(false);

        initDefault();
        requestManager = Glide.with(this);
        initAction();
    }

    private void initDefault(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        tvTglBayar.setText(formattedDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tgl_bayar = sdf.format(c);
        edtKeterangan.setText("");

        edtNominal.setText("");
        ivBukti.setImageResource(0);

        edtNominal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(current)){
                    edtNominal.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.\\s]", "");
                    String formatted;
                    if(!cleanString.isEmpty()){
                        double parsed = Double.parseDouble(cleanString);
                        formatted = Converter.doubleToRupiah(parsed);
                    }
                    else{
                        formatted = "";
                    }

                    current = formatted;
                    edtNominal.setText(formatted);
                    edtNominal.setSelection(formatted.length());

                    edtNominal.addTextChangedListener(this);
                }
            }
        });

    }

    private void initAction(){
        ivSearchImage.setOnClickListener(view -> {
            TedBottomPicker.with(ReimburseActivity.this)
                    //.setPeekHeight(getResources().getDisplayMetrics().heightPixels/2)
                    .setSelectedUri(selectedUri)
                    //.showVideoMedia()
                    .setPeekHeight(1600)
                    .show(uri -> {
                        selectedUri = uri;
                        requestManager
                                .load(uri)
                                .into(ivBukti);
                    });
        });

        rlTgl.setOnClickListener(view ->{
            final java.util.Calendar customDate = java.util.Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    customDate.set(java.util.Calendar.YEAR, year);
                    customDate.set(java.util.Calendar.MONTH, month);
                    customDate.set(java.util.Calendar.DATE, dayOfMonth);
                    SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    tvTglBayar.setText(sdFormat.format(customDate.getTime()));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    tgl_bayar = sdf.format(customDate.getTime());
                }
            };
            new DatePickerDialog(ReimburseActivity.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
        });

        btnProses.setOnClickListener(view ->{
            if(validasiFormReimburse()){
                new SweetDialog(ReimburseActivity.this, SweetDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Apakah anda yakin ingin mengajukan reimburse ?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetDialog.SweetClickListener() {
                            @Override
                            public void onClick(SweetDialog sDialog) {
                                sDialog.dismiss();
                                doAddReimburse();
                            }
                        })
                        .setCancelClickListener(new SweetDialog.SweetClickListener() {
                            @Override
                            public void onClick(SweetDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    private Bitmap convertBitmap(ImageView view){
        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        return bitmap;
    }

    private Boolean validasiFormReimburse(){
        if(tgl_bayar.equals("")){
            Toasty.error(ReimburseActivity.this, "Masukkan tanggal pembayaran.", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (edtNominal.getText().toString().matches("")){
            Toasty.error(ReimburseActivity.this, "Masukkan nominal pembayaran.", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(ivBukti.getDrawable() == null){
            Toasty.error(ReimburseActivity.this, "Masukkan foto bukti pembayaran.", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (edtKeterangan.getText().toString().matches("")){
            Toasty.error(ReimburseActivity.this, "Masukkan keterangan.", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    private void doAddReimburse(){
        pDialogProses.show();
        Bitmap b = convertBitmap(ivBukti);
        String image= "";
        if(ivBukti.getDrawable() == null){
            image="";
        }else{
            image = ImageUtils.convert(b);
        }

        JSONObject jBody = new JSONObject();
        try {
            jBody.put("tgl_pembayaran", tgl_bayar);
            jBody.put("foto_pembayaran",image);
            jBody.put("nominal",edtNominal.getText().toString().replaceAll("[Rp,.\\s]", ""));
            jBody.put("ket",edtKeterangan.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Reimburse",String.valueOf(jBody));

        new ApiVolley(ReimburseActivity.this,jBody,"POST", ServerUrl.urlPostReimburse,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogProses.dismiss();
                        Toasty.success(ReimburseActivity.this,message,Toast.LENGTH_SHORT).show();
                        initDefault();
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialogProses.dismiss();
                        Toasty.error(ReimburseActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String message) {
                        pDialogProses.dismiss();
                        if(message.equals("Unauthorized")){
                            try {
                                SessionManager session = new SessionManager(ReimburseActivity.this);
                                session.logoutUser(Utils.loginActivity(ReimburseActivity.this));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toasty.error(ReimburseActivity.this,"Terjadi kesalahan saat memuat data",Toast.LENGTH_SHORT).show();
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