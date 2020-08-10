package gmedia.net.id.coremodul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode

    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AbsenOntime";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_CHECK = "IsCheckIn";
    private static final String IS_ID_PERUSAHAAN = "HaveID";
    public static final String IS_KEY_MENU ="menu";

    // User name (make variable public to access from outside)
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_ID_KARYAWAN = "id_karyawan";
    public static final String KEY_ID_COMPANY = "id_company";
    public static final String KEY_ID_USER = "id_user";
    public static final String KEY_ID_PERUSAHAAN = "id_perusahaan";
    public static final String KEY_APPROVAL_CUTI = "apv_cuti";
    public static final String KEY_APPROVAL_IJIN = "apv_ijin";
    public static final String KEY_APPROVAL_REIMBURS = "apv_reimburs";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
//		editorIDPerusahaan = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String user_name, String token, String id_karyawan, String id_company, String id_user, String approval_cuti, String approval_ijin, String approval_reimburse) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_NAME, user_name);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_ID_KARYAWAN, id_karyawan);
        editor.putString(KEY_ID_COMPANY, id_company);
        editor.putString(KEY_ID_USER, id_user);
        editor.putString(KEY_APPROVAL_CUTI, approval_cuti);
        editor.putString(KEY_APPROVAL_IJIN, approval_ijin);
        editor.putString(KEY_APPROVAL_REIMBURS,approval_reimburse);
        // commit changes
        editor.commit();
    }

    public void createLoginSessionIDPerusahaan(String id_perusahaan) {
        editor.putBoolean(IS_ID_PERUSAHAAN, true);
        editor.putString(KEY_ID_PERUSAHAAN, id_perusahaan);
        editor.commit();
    }

    public void setMenu(boolean menu){
        editor.putBoolean(IS_KEY_MENU, menu);
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */

    public String getKeyUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    public String getKeyToken() {
        return pref.getString(KEY_TOKEN, "");
    }

    public String getKeyIdKaryawan() {
        return pref.getString(KEY_ID_KARYAWAN, "");
    }

    public String getKeyIdCompany() {
        return pref.getString(KEY_ID_COMPANY, "");
    }

    public String getKeyIdUser() {
        return pref.getString(KEY_ID_USER, "");
    }

    public String getKeyIdPerusahaan() {
        return pref.getString(KEY_ID_PERUSAHAAN, "");
    }

    public String getKeyApprovalCuti() {
        return pref.getString(KEY_APPROVAL_CUTI, "");
    }

    public String getKeyApprovalIjin() {
        return pref.getString(KEY_APPROVAL_IJIN, "");
    }

    public String getKeyApprovalReimburs() {
        return pref.getString(KEY_APPROVAL_REIMBURS, "");
    }

    /**
     * Clear session details
     */

    public void deleteIDPerusahaan() {
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_ID_PERUSAHAAN, false);
        editor.putString(KEY_ID_PERUSAHAAN, "");
        editor.commit();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean getIsLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean getIsIDPerusahaan() {
        return pref.getBoolean(IS_ID_PERUSAHAAN, false);
    }

    public boolean getIisMenu() {
        return pref.getBoolean(IS_KEY_MENU, false);
    }


    public void logoutUser(String activity) throws ClassNotFoundException {
        editor.putBoolean(IS_LOGIN, false);
        editor.putString(KEY_USER_NAME, "");
        editor.putString(KEY_TOKEN, "");
        editor.putString(KEY_ID_KARYAWAN, "");
        editor.putString(KEY_ID_COMPANY, "");
        editor.putString(KEY_ID_USER, "");
        editor.putString(KEY_APPROVAL_IJIN, "");
        editor.putString(KEY_APPROVAL_CUTI, "");
        editor.putString(KEY_APPROVAL_REIMBURS, "");
        editor.commit();
        Log.d(">>>>>",activity);
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Class.forName(activity));
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        ((Activity) _context).startActivity(i);
        ((Activity) _context).finish();
    }

}