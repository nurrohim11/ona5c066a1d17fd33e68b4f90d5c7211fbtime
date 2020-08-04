package gmedia.net.id.OnTime.home.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.MainActivity;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.utils.CustomMapsView;
import gmedia.net.id.OnTime.utils.FrontCamera;
import gmedia.net.id.OnTime.utils.GetLocation;
import gmedia.net.id.OnTime.utils.Imei;
import gmedia.net.id.OnTime.utils.Preview;
import gmedia.net.id.OnTime.utils.Utils;

import static gmedia.net.id.OnTime.utils.Utils.safeToTakePicture;

public class LockInActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private ImageView ivRefresh;
    private Preview preview;
    private Camera camera;
    private FrontCamera frontCamera;
    public static Animation animation;
    private GetLocation getLocation;
    public static String latitude = "", longitude = "";
    public static String tipe_scan = "";
    private Bitmap bitmap;
    private byte[] dataBaru;
    public static String infoSSID = "", infoBSSID = "", infoIpPublic = "", imei;
    private WifiManager manager;
    private CustomMapsView customMapsView;
    @BindView(R.id.btn_scan_lock_in)
    Button btnScanLockIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.safeToTakePicture = false;
        Utils.afterSnapCamera = false;

        //No title bar is set for the activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock_in);
        imei = Arrays.toString(Imei.getIMEI(LockInActivity.this).toArray());

        ButterKnife.bind(this);
        initUI();

        if (latitude.equals("") || longitude.equals("")) {
            getLocation = new GetLocation();
            getLocation.GetLocation(LockInActivity.this, customMapsView);
        }

        frontCamera = new FrontCamera(LockInActivity.this,"7");
        camera = frontCamera.getCameraInstance();
        preview = new Preview(LockInActivity.this, camera);
        animation = AnimationUtils.loadAnimation(LockInActivity.this, R.anim.up_from_bottom);
        frameLayout.addView(preview);

        initAction();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(LockInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void initUI() {
        frameLayout = (FrameLayout) findViewById(R.id.previewCamera);
        customMapsView = (CustomMapsView) findViewById(R.id.mapsView);
        customMapsView.onCreate(null);
        customMapsView.onResume();
        ivRefresh = (ImageView) findViewById(R.id.iv_refresh);
        getLocation = new GetLocation();
        getLocation.GetLocation(LockInActivity.this, customMapsView);
    }

    private void initAction() {
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getLocation = new GetLocation();
                getLocation.GetLocation(LockInActivity.this, customMapsView);
            }
        });

        btnScanLockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (safeToTakePicture) {
                    if (longitude.equals("") || latitude.equals("")) {
                        getLocation = new GetLocation();
                        getLocation.GetLocation(LockInActivity.this, customMapsView);
                        Toasty.error(LockInActivity.this, "Please Check Your GPS and Try Again.", Toast.LENGTH_SHORT, true).show();
                        return;
                    } else {
                        getLocation = new GetLocation();
                        getLocation.GetLocation(LockInActivity.this, customMapsView);
                        Log.d("click", "clicked");

                        try {
                            camera.takePicture(null, null, FrontCamera.mPicture);
                        }catch (Exception e){
                            Toasty.error(LockInActivity.this, "Gagal mendapatkan gambar, harap coba kembali", Toast.LENGTH_SHORT, true).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(LockInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}