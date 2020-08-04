package gmedia.net.id.OnTime.home.menu;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gmedia.net.id.OnTime.MainActivity;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.utils.CustomMapsView;
import gmedia.net.id.OnTime.utils.FrontCamera;
import gmedia.net.id.OnTime.utils.GetLocation;
import gmedia.net.id.OnTime.utils.Imei;
import gmedia.net.id.OnTime.utils.Preview;
import gmedia.net.id.OnTime.utils.Utils;

import static gmedia.net.id.OnTime.utils.Utils.safeToTakePicture;
import static gmedia.net.id.OnTime.utils.Utils.afterSnapCamera;

public class LockOutActivity extends AppCompatActivity {

    private Preview preview;
    private Camera camera;
    private FrontCamera frontCamera;
    public static Animation animation;
    private GetLocation getLocation;
    public static String latitude = "", longitude = "";
    public static String infoSSID = "", infoBSSID = "", infoIpPublic = "", imei;

    @BindView(R.id.previewCamera)
    FrameLayout frameLayout;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;
    @BindView(R.id.mapsView)
    CustomMapsView customMapsView;
    @BindView(R.id.btn_scan_lock_out)
    Button btnScanLockOut;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        safeToTakePicture = false;
        afterSnapCamera = false;

        //No title bar is set for the activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock_out);
        imei = Arrays.toString(Imei.getIMEI(LockOutActivity.this).toArray());

        ButterKnife.bind(this);
        unbinder = ButterKnife.bind(this);

        initUI();

        if (latitude.equals("") || longitude.equals("")) {
            getLocation = new GetLocation();
            getLocation.GetLocation(LockOutActivity.this, customMapsView);
        }

        frontCamera = new FrontCamera(LockOutActivity.this,"8");
        camera = frontCamera.getCameraInstance();
        preview = new Preview(LockOutActivity.this, camera);
        animation = AnimationUtils.loadAnimation(LockOutActivity.this, R.anim.up_from_bottom);
        frameLayout.addView(preview);

        initAction();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(LockOutActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void initUI() {
        customMapsView.onCreate(null);
        customMapsView.onResume();
        getLocation = new GetLocation();
        getLocation.GetLocation(LockOutActivity.this, customMapsView);

        btnScanLockOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (safeToTakePicture) {
                    if (longitude.equals("") || latitude.equals("")) {
                        getLocation = new GetLocation();
                        getLocation.GetLocation(LockOutActivity.this, customMapsView);
                        Toast.makeText(LockOutActivity.this, "Please Check Your GPS and Try Again", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        getLocation = new GetLocation();
                        getLocation.GetLocation(LockOutActivity.this, customMapsView);
                        Log.d("click", "clicked");

                        try {
                            camera.takePicture(null, null, FrontCamera.mPicture);
                        }catch (Exception e){
                            Toast.makeText(LockOutActivity.this, "Gagal mendapatkan gambar, harap coba kembali", Toast.LENGTH_LONG);
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void initAction() {
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getLocation = new GetLocation();
                getLocation.GetLocation(LockOutActivity.this, customMapsView);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(LockOutActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}