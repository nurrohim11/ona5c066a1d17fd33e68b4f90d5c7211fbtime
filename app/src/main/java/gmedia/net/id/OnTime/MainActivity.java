package gmedia.net.id.OnTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alkhattabi.bottomviewex.BottomNavViewEx;
import com.alkhattabi.kalert.KAlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.akun.AkunFragment;
import gmedia.net.id.OnTime.approval.ApprovalFragment;
import gmedia.net.id.OnTime.home.HomeFragment;
import gmedia.net.id.OnTime.pengajuan.PengajuanFragment;
import gmedia.net.id.OnTime.riwayat.RiwayatFragment;
import gmedia.net.id.OnTime.utils.RuntimePermissionsActivity;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

import static gmedia.net.id.OnTime.utils.Utils.loginActivity;

public class MainActivity extends RuntimePermissionsActivity {

    private static final int REQUEST_PERMISSIONS = 20;
    public static String latitude = "", longitude = "";
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean dialogActive = false;

    private String device_token ="";

    BottomNavViewEx bvNavigation;
    private TextView tvProfileName, tvProfileNik;

    private String[] appPermission =  {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private final int PERMIOSSION_REQUEST_CODE = 1240;
    SessionManager sessionManager;
    Button btnLogout;
    RelativeLayout rlNotif, rlSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        loadFragment(new HomeFragment());


        dialogActive = false;
        if (android.os.Build.VERSION.SDK_INT > 25) {
            statusCheck();
        }

        // Semua aplikasi
        FirebaseMessaging.getInstance().subscribeToTopic("ontime");

        // Per company
        FirebaseMessaging.getInstance().subscribeToTopic(sessionManager.getKeyIdCompany());

        device_token = FirebaseInstanceId.getInstance().getToken();
        try {
            Log.d("token", device_token);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("token", e.getMessage());
        }

//        initData();
        initView();
        initEvent();
        initPermission();
    }

    public boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void initView() {
        bvNavigation = findViewById(R.id.bv_navigation);
        tvProfileName = findViewById(R.id.tv_profile_name);
        tvProfileNik = findViewById(R.id.tv_profile_nik);
        btnLogout = findViewById(R.id.btn_logout);

        rlNotif = findViewById(R.id.rl_notif);
        rlSettings = findViewById(R.id.rl_settings);

        rlNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        if(!sessionManager.getKeyApprovalCuti().equals("1") || !sessionManager.getKeyApprovalIjin().equals("1") || !sessionManager.getKeyApprovalReimburs().equals("1")){
            bvNavigation.getMenu().removeItem(R.id.i_approval);
        }
        bvNavigation.enableItemShiftingMode(false);
        bvNavigation.enableShiftingMode(false);
        bvNavigation.enableAnimation(false);
    }

    private void initProfil(){
        JSONObject jBody = new JSONObject();
        new ApiVolley(this, jBody, "POST", ServerUrl.Profile,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d(TAG,String.valueOf(res));
                            tvProfileName.setText(res.getString("nama"));
                            tvProfileNik.setText(res.getString("nik"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onEmpty(String message) {
                        Toasty.error(MainActivity.this, message, Toast.LENGTH_SHORT, true).show();
                    }
                    @Override
                    public void onFail(String message) {
                        try {
                            sessionManager.logoutUser(loginActivity(getApplicationContext()));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                })
        );
    }

    /**
     * set listeners
     */
    private void initEvent() {
        // set listener to change the current item of view pager when click bottom nav item
        bvNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.i_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.i_riwayat:
                        fragment = new RiwayatFragment();
                        break;
                    case R.id.i_pengajuan:
                        fragment = new PengajuanFragment();
                        break;
                    case R.id.i_approval:
                        fragment = new ApprovalFragment();
                        break;
                    case R.id.i_akun:
                        fragment = new AkunFragment();
                        break;
                }
//                if(previousPosition != position) {
//                    vpNavigation.setCurrentItem(position, false);
//                    previousPosition = position;
//                    Log.i(TAG, "-----bnve-------- previous item:" + bvNavigation.getCurrentItem() + " current item:" + position + " ------------------");
//                }

                return loadFragment(fragment);
            }
        });
    }

    private void initPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED /*||
				ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED*/) {

            super.requestAppPermissions(new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.INTERNET,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.READ_PHONE_STATE
//							Manifest.permission.RECEIVE_BOOT_COMPLETED
                    },
                    R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        initProfil();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(MainActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Apakah anda yakin ingin logout")
                        .setConfirmText("Yes")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();
                                try {
                                    sessionManager.logoutUser(loginActivity(getApplicationContext()));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();

            }
        });

        if (!checkPermission()){

            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Informasi")
                    .setMessage("Aplikasi ini mengharuskan anda untuk mengijinkan akses lokasi.")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            checkPermission();
                        }
                    })
                    .show();

        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        if (!dialogActive) {
            dialogActive = true;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Mohon Hidupkan Akses Lokasi (GPS) Anda.")
                    .setCancelable(false)
                    .setPositiveButton("Hidupkan", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();

            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    dialogActive = false;
                }
            });
        }
    }

    private boolean checkPermission(){

        List<String> permissionList = new ArrayList<>();
        for (String perm : appPermission) {

            if (ContextCompat.checkSelfPermission(MainActivity.this, perm) != PackageManager.PERMISSION_GRANTED){

                permissionList.add(perm);
            }
        }

        if (!permissionList.isEmpty()) {

            ActivityCompat.requestPermissions(MainActivity.this, permissionList.toArray(new String[permissionList.size()]), PERMIOSSION_REQUEST_CODE);

            return  false;
        }

        return  true;
    }

    private String GetAppName(String ApkPackageName){

        String Name = "";

        ApplicationInfo applicationInfo;

        PackageManager packageManager = getPackageManager();

        try {

            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);

            if(applicationInfo!=null){

                Name = (String)packageManager.getApplicationLabel(applicationInfo);
            }

        }catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return Name;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}