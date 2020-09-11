package gmedia.net.id.OnTime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rohimdev.sweetdialog.SweetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.ItemValidation;
import gmedia.net.id.coremodul.SessionManager;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    SessionManager sessionManager;
    EditText edtUsername, edtPassword, edtKodePerusahaan;
    ImageView imgVisiblePass;
    SweetDialog pDialog, pSuccess, pError;
    ItemValidation iv = new ItemValidation();
    private boolean klikToVisiblePass = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
//        if (sessionManager.getIsLoggedIn()){
//            startActivity(new Intent(LoginActivity.this, MainActivity.class)
//                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//            finish();
//        }
        sessionManager.checkLogin(Utils.MainActivity(this));

        pDialog = new SweetDialog(LoginActivity.this, SweetDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
        pDialog.setCancelable(false);
        
        initUi();
    }

    private void initUi(){
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtKodePerusahaan = findViewById(R.id.edt_kode_perusahaan);
        imgVisiblePass = findViewById(R.id.visiblePass);
        imgVisiblePass.setOnClickListener(v->{
            if(klikToVisiblePass){
                imgVisiblePass.setImageDrawable(getResources().getDrawable(R.drawable.visible));;
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                klikToVisiblePass = false;
            }else{
                imgVisiblePass.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                klikToVisiblePass = true;
            }
        });

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtUsername.getText().toString().length() == 0){
                    edtUsername.setError("Username harap diisi");
                    edtUsername.requestFocus();
                    return;

                }else{
                    edtUsername.setError(null);
                }

                if(edtPassword.getText().toString().length() == 0){

                    edtPassword.setError("Password harap diisi");
                    edtPassword.requestFocus();
                    return;

                }else{
                    edtPassword.setError(null);
                }

                if(edtKodePerusahaan.getText().toString().length() == 0){

                    edtKodePerusahaan.setError("Kode Perusahaan harap diisi");
                    edtKodePerusahaan.requestFocus();
                    return;

                }else{
                    edtPassword.setError(null);
                }

                doLogin();
            }
        });
    }
    private void doLogin(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        pDialog.show();
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("username", edtUsername.getText().toString());
            jBody.put("password", edtPassword.getText().toString());
            jBody.put("kode",edtKodePerusahaan.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiVolley(LoginActivity.this, jBody, "POST", ServerUrl.UrlLogin, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                pDialog.dismiss();
                try {
                    Log.d("LoginActivity","onsuccess "+result);
                    JSONObject response = new JSONObject(result);
                    String status = response.getJSONObject("metadata").getString("status");
                    String message = response.getJSONObject("metadata").getString("message");

                    if(iv.parseNullInteger(status) == 200){
                        JSONObject jo = response.getJSONObject("response");
                        sessionManager.createLoginSession(
                                jo.getString("username"),
                                jo.getString("token"),
                                jo.getString("id_karyawan"),
                                jo.getString("id_company"),
                                jo.getString("id_user"),
                                jo.getString("cuti"),
                                jo.getString("ijin"),
                                jo.getString("reimburs")
                        );
                        pSuccess = new SweetDialog(LoginActivity.this, SweetDialog.SUCCESS_TYPE);
                        pSuccess.setTitleText(message);
                        pSuccess.setContentText("Please wait");
                        pSuccess.setCancelable(false);
                        pSuccess.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pSuccess.dismiss();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            }
                        }, 1200);
                    }else{
                        Toasty.error(LoginActivity.this, message, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(LoginActivity.this, "Terjadi kesalahan data", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onError(String result) {
                pDialog.dismiss();
                Toasty.error(LoginActivity.this, result, Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}