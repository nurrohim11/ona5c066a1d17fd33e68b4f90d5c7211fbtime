package gmedia.net.id.OnTime.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.alkhattabi.kalert.KAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.Arrays;

import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.LoginActivity;
import gmedia.net.id.OnTime.MainActivity;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.home.menu.LockOutActivity;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

import static android.content.Context.WIFI_SERVICE;
import static gmedia.net.id.OnTime.utils.Utils.loginActivity;

public class Scan {
	private Context context;
	private String infoSSID = "", infoBSSID = "", infoIpPublic = "", tipe_scan, imei;
	private WifiManager manager;
	private KAlertDialog pDialogProcess;
	private KAlertDialog pDialogFailed;
	private KAlertDialog pDialogSuccess;
	private  LocationManager locationManager;
	private static final int REQUEST_LOCATION = 1;
	String latitude, longitude;

	public Scan(final Context context, String tipe_scan) {

		this.context = context;
		this.tipe_scan = tipe_scan;
		pDialogProcess = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
		pDialogProcess.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
		pDialogProcess.setCancelable(false);
		pDialogProcess.setTitleText("Processing...");
		pDialogProcess.show();

		imei = Arrays.toString(Imei.getIMEI(context).toArray());

		new ApiVolley(context, new JSONObject(), "GET", ServerUrl.urlIpPublic,  new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				Log.d(">>>>>",result);
				infoIpPublic = Jsoup.parse(result).text();
				manager = (WifiManager) context.getSystemService(WIFI_SERVICE);
				WifiInfo info = manager.getConnectionInfo();
				infoSSID = info.getSSID();
				infoBSSID = info.getBSSID();
				locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
				if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					OnGPS();
				} else {
					getLocation();
				}
				Log.d(">>>>>","Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
				initAbsen();

			}

			@Override
			public void onError(String result) {
				Toasty.error(context, result, Toast.LENGTH_SHORT, true).show();
			}
		});

	}

	private void OnGPS() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	private void getLocation() {
		if (ActivityCompat.checkSelfPermission(
				context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
				context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
		} else {
			Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (locationGPS != null) {
				double lat = locationGPS.getLatitude();
				double longi = locationGPS.getLongitude();
				latitude = String.valueOf(lat);
				longitude = String.valueOf(longi);
			} else {
				Toast.makeText(context, "Unable to find location.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void initAbsen() {
		int version = Build.VERSION.SDK_INT;
		JSONArray jsonArray = new JSONArray(Imei.getIMEI(context));
		final JSONObject jBody = new JSONObject();
		try {
			jBody.put("foto", "");
			jBody.put("tipe_scan", tipe_scan);
			jBody.put("imei", jsonArray);
			jBody.put("ip_public", infoIpPublic);
			jBody.put("mac_address", infoBSSID);
			jBody.put("latitude", latitude);
			jBody.put("longitude", longitude);
			jBody.put("api_level",String.valueOf(version));
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d("error", e.getMessage());
		}
		Log.d("ScanAbsen", String.valueOf(jBody));

		ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.ScanAbsen,  new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				Log.d(">>>>>",result);
				pDialogProcess.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						pDialogSuccess = new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE);
						pDialogSuccess.setTitleText("SUKSES!..");
						pDialogSuccess.setContentText(message);
						pDialogSuccess.setConfirmText("OK");
						pDialogSuccess.setCancelable(false);
						pDialogSuccess.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
							@Override
							public void onClick(KAlertDialog sDialog) {
								sDialog.dismiss();
								((Activity) context).finish();
								((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
							}
						});
						pDialogSuccess.show();

					} else {
						pDialogFailed = new KAlertDialog(context, KAlertDialog.ERROR_TYPE);
						pDialogFailed.setTitleText("GAGAL!..");
						pDialogFailed.setContentText(message);
						pDialogFailed.setCancelText("OK");
						pDialogFailed.setCancelable(false);
						pDialogFailed.setCancelClickListener(new KAlertDialog.KAlertClickListener() {
							@Override
							public void onClick(KAlertDialog sDialog) {
								sDialog.dismiss();
							}
						});
						pDialogFailed.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				pDialogProcess.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String message = object.getJSONObject("metadata").getString("message");
					Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
