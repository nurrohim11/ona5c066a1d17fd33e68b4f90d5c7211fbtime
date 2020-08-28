package gmedia.net.id.OnTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alkhattabi.sweetbottomnavbar.SweetBottomNavbar;
import com.alkhattabi.sweetdialog.SweetDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.akun.AkunFragment;
import gmedia.net.id.OnTime.approval.ApprovalFragment;
import gmedia.net.id.OnTime.home.HomeFragment;
import gmedia.net.id.OnTime.pengajuan.PengajuanFragment;
import gmedia.net.id.OnTime.riwayat.RiwayatFragment;
import gmedia.net.id.OnTime.utils.RuntimePermissionsActivity;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
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
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    SweetBottomNavbar bvNavigation;
    private TextView tvProfileName, tvProfileNik;


    private String[] appPermission =  {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private final int PERMIOSSION_REQUEST_CODE = 1240;

    SessionManager sessionManager;
    Button btnLogout;
    //    RelativeLayout rlNotif, rlSettings;
    RelativeLayout rlSettings;
    EditText passLama;
    EditText passBaru;
    EditText rePassBaru;
    CircleImageView ivProfile;
    private Boolean klikToVisiblePassLama = true;
    private boolean klikToVisiblePassBaru = true;
    private boolean klikToVisiblePassRe = true;

    Dialog dialog;

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
        initPermission();

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

        initView();
        initEvent();
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
        ivProfile = findViewById(R.id.img_profil);

//        rlNotif = findViewById(R.id.rl_notif);
        rlSettings = findViewById(R.id.rl_settings);

        if(!sessionManager.getKeyApprovalCuti().equals("1") && !sessionManager.getKeyApprovalIjin().equals("1") && !sessionManager.getKeyApprovalReimburs().equals("1")){
            bvNavigation.getMenu().removeItem(R.id.i_approval);
        }

        bvNavigation.enableItemShiftingMode(false);
        bvNavigation.enableShiftingMode(false);
        bvNavigation.enableAnimation(false);

//        rlNotif.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        rlSettings.setOnClickListener(view ->{

            dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.popup_ganti_password);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final ImageView visiblePassLama = dialog.findViewById(R.id.visiblePassLama);
            final ImageView visiblePassBaru = dialog.findViewById(R.id.visiblePassBaru);
            final ImageView visibleRePassBaru = dialog.findViewById(R.id.visibleRePassBaru);
            passLama = dialog.findViewById(R.id.passLama);
            passBaru = dialog.findViewById(R.id.passBaru);
            rePassBaru = dialog.findViewById(R.id.reTypePassBaru);
            visiblePassLama.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (klikToVisiblePassLama) {
                        visiblePassLama.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                        passLama.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        klikToVisiblePassLama = false;
                    } else {
                        visiblePassLama.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                        passLama.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        klikToVisiblePassLama = true;
                    }
                }
            });

            visiblePassBaru.setOnClickListener(v->{
                if (klikToVisiblePassBaru) {
                    visiblePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    passBaru.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    klikToVisiblePassBaru = false;
                } else {
                    visiblePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                    passBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    klikToVisiblePassBaru = true;
                }
            });

            visibleRePassBaru.setOnClickListener(v->{
                if (klikToVisiblePassRe) {
                    visibleRePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    rePassBaru.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    klikToVisiblePassRe = false;
                } else {
                    visibleRePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                    rePassBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    klikToVisiblePassRe = true;
                }
            });

            RelativeLayout OK = dialog.findViewById(R.id.tombolOKgantiPassword);
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // validasi
                    if (passLama.getText().toString().isEmpty()) {
                        passLama.setError("Password lama harap diisi");
                        passLama.requestFocus();
                        return;
                    } else {
                        passLama.setError(null);
                    }

                    if (passBaru.getText().toString().isEmpty()) {
                        passBaru.setError("Password baru harap diisi");
                        passBaru.requestFocus();
                        return;
                    } else {
                        passBaru.setError(null);
                    }

                    if (rePassBaru.getText().toString().isEmpty()) {
                        rePassBaru.setError("Password baru ulang harap diisi");
                        rePassBaru.requestFocus();
                        return;
                    } else {
                        rePassBaru.setError(null);
                    }
                    if (!rePassBaru.getText().toString().equals(passBaru.getText().toString())) {
                        rePassBaru.setError("Password ulang tidak sama");
                        rePassBaru.requestFocus();
                        return;
                    } else {
                        rePassBaru.setError(null);
                    }
                    doChangePassword();
                }
            });
            RelativeLayout cancel = dialog.findViewById(R.id.tombolcancelGantiPassword);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetDialog(MainActivity.this, SweetDialog.WARNING_TYPE)
                        .setTitleText("Are you sure ?")
                        .setContentText("Apakah anda yakin ingin logout ?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetDialog.SweetClickListener() {
                            @Override
                            public void onClick(SweetDialog sDialog) {
                                sDialog.dismiss();
                                try {
                                    sessionManager.logoutUser(loginActivity(getApplicationContext()));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
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

    private void doChangePassword(){
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("password_lama",passLama.getText().toString());
            jBody.put("password_baru",passBaru.getText().toString());
            jBody.put("re_password",rePassBaru.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiVolley(MainActivity.this,jBody, "POST",ServerUrl.gantiPassword,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        Toasty.success(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },500);
                    }

                    @Override
                    public void onEmpty(String message) {
                        Toasty.error(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String message) {
                        Toasty.error(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                })
        );
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
                            Glide.with(MainActivity.this)
                                    .load(res.getString("foto"))
                                    .placeholder(R.drawable.computer_user)
                                    .into(ivProfile);
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
                        Log.d(MainActivity.class.getSimpleName(),"onfail "+message);
                        if(message.equals("Unauthorized")){
                            try {
                                SessionManager session = new SessionManager(MainActivity.this);
                                session.logoutUser(Utils.loginActivity(MainActivity.this));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
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
        if (checkPermission()){

            // diijinkan
            // updating location service, disable for some reason
			/*if(!isServiceRunning(MainActivityBaru.this, UpdateLocationService.class)){
				startService(new Intent(MainActivityBaru.this, UpdateLocationService.class));
			}*/
        }else{

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
//        exit();
    }

    private void exit(){
        SweetDialog pDialogWarning = new SweetDialog(MainActivity.this, SweetDialog.WARNING_TYPE);
        pDialogWarning.setTitleText("Are you sure ?");
        pDialogWarning.setContentText("Apakah anda yakin untuk keluar ?");
        pDialogWarning.setCancelable(true);
        pDialogWarning.setConfirmText("Ya"); //Do not call this if you don't want to show confirm button
        pDialogWarning.setCancelText("Tidak");//Do not call this if you don't want to show cancel button
        pDialogWarning.setConfirmClickListener(new SweetDialog.SweetClickListener() {
            @Override
            public void onClick(SweetDialog sweetDialog) {
                finish();
            }
        });
        pDialogWarning.setCancelClickListener(new SweetDialog.SweetClickListener() {
            @Override
            public void onClick(SweetDialog sweetDialog) {
                pDialogWarning.dismiss();
            }
        });
        pDialogWarning.show();
    }

}