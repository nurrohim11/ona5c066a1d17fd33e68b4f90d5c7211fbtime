package gmedia.net.id.OnTime.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

import gmedia.net.id.OnTime.MainActivity;
import gmedia.net.id.OnTime.home.menu.LockInActivity;
import gmedia.net.id.OnTime.home.menu.LockOutActivity;

public class GetLocation {
	private double latitude, longitude;
	private LocationManager locationManager;
	private Criteria criteria;
	private String provider;
	private Location location;
	private final int REQUEST_PERMISSION_COARSE_LOCATION = 2;
	private final int REQUEST_PERMISSION_FINE_LOCATION = 3;
	public boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute
	private String TAG = "DetailCustomer";
	private String address0 = "";
	private FusedLocationProviderClient mFusedLocationClient;
	private LocationCallback mLocationCallback;
	private LocationRequest mLocationRequest;
	private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
			UPDATE_INTERVAL_IN_MILLISECONDS / 2;
	private LocationSettingsRequest mLocationSettingsRequest;
	private SettingsClient mSettingsClient;
	private static final int REQUEST_CHECK_SETTINGS = 0x1;
	private Boolean mRequestingLocationUpdates;
	private Location mCurrentLocation;
	public static boolean refreshMode = false;
	private GoogleMap googleMap;
	private CustomMapsView mvMap;
	private Context context;

	public void GetLocation(Context context, CustomMapsView mvMap) {
		this.context = context;
		this.mvMap = mvMap;
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
		mSettingsClient = LocationServices.getSettingsClient(context);
		mRequestingLocationUpdates = false;
		Log.d("masuk", "in");
		createLocationCallback();
		createLocationRequest();
		buildLocationSettingsRequest();
		initLocation();
	}

	private void createLocationCallback() {
		mLocationCallback = new LocationCallback() {
			@Override
			public void onLocationResult(LocationResult locationResult) {
				super.onLocationResult(locationResult);
				mCurrentLocation = locationResult.getLastLocation();
				//mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
				onLocationChanged(mCurrentLocation);
			}
		};
	}

	private void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	private void buildLocationSettingsRequest() {
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
		builder.addLocationRequest(mLocationRequest);
		mLocationSettingsRequest = builder.build();
	}

	public void onLocationChanged(Location location) {
		if (refreshMode) {
			refreshMode = false;
			this.location = location;
			this.latitude = location.getLatitude();
			this.longitude = location.getLongitude();
			LockInActivity.latitude = "" + this.latitude;
			LockInActivity.longitude = "" + this.longitude;
			LockOutActivity.latitude = "" + this.latitude;
			LockOutActivity.longitude = "" + this.longitude;
			MainActivity.latitude = "" + this.latitude;
			MainActivity.longitude = "" + this.longitude;
			setPointMap();
		}
	}

	public Double getLatitude(){
		double latitude = this.latitude;
		return latitude;
	}

	public Double getLongitude(){
		double longitude = this.longitude;
		return longitude;
	}

	private void initLocation() {

		locationManager = (LocationManager) ((Activity) context).getSystemService(Context.LOCATION_SERVICE);
		setCriteria();
		latitude = 0;
		longitude = 0;
		location = new Location("set");
		location.setLatitude(latitude);
		location.setLongitude(longitude);

		refreshMode = true;
		Bundle bundle = ((Activity) context).getIntent().getExtras();
		updateAllLocation();
	}

	public void setCriteria() {
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		provider = locationManager.getBestProvider(criteria, true);
	}

	public void updateAllLocation() {
		mRequestingLocationUpdates = true;
		startLocationUpdates();
	}

	public void startLocationUpdates() {
		// Begin by checking if the device has the necessary location settings.
		mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
				.addOnSuccessListener(((Activity) context), new OnSuccessListener<LocationSettingsResponse>() {
					@Override
					public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
						Log.i(TAG, "All location settings are satisfied.");

						//noinspection MissingPermission
						if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
								&& ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

							return;
						}
						mFusedLocationClient.requestLocationUpdates(mLocationRequest,
								mLocationCallback, Looper.myLooper());

						mFusedLocationClient.getLastLocation()
								.addOnSuccessListener(((Activity) context), new OnSuccessListener<Location>() {
									@Override
									public void onSuccess(Location clocation) {

										if (clocation != null) {
											location = clocation;
											refreshMode = true;
											onLocationChanged(location);
										} else {
											location = getLocation();
										}
									}
								});
					}
				})
				.addOnFailureListener(((Activity) context), new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						int statusCode = ((ApiException) e).getStatusCode();
						switch (statusCode) {
							case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
								Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
										"location settings ");
								try {
									// Show the dialog by calling startResolutionForResult(), and check the
									// result in onActivityResult().
									ResolvableApiException rae = (ResolvableApiException) e;
									rae.startResolutionForResult(((Activity) context), REQUEST_CHECK_SETTINGS);
								} catch (IntentSender.SendIntentException sie) {
									Log.i(TAG, "PendingIntent unable to execute request.");
								}
								break;
							case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
								String errorMessage = "Location settings are inadequate, and cannot be " +
										"fixed here. Fix in Settings.";
								Log.e(TAG, errorMessage);
								Toast.makeText(((Activity) context), errorMessage, Toast.LENGTH_LONG).show();
								mRequestingLocationUpdates = false;
								//refreshMode = false;
						}

						//get Location
						location = getLocation();
					}
				});
	}

	public Location getLocation() {

		try {

			locationManager = (LocationManager) ((Activity) context).getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			Log.v("isGPSEnabled", "=" + isGPSEnabled);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

			if (isGPSEnabled == false && isNetworkEnabled == false) {
				// no network provider is enabled
				Toast.makeText(context, "Cannot identify the location.\nPlease turn on GPS or turn on your MenuData.",
						Toast.LENGTH_LONG).show();
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					location = null;

					// Granted the permission first
					if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity) context),
								Manifest.permission.ACCESS_COARSE_LOCATION)) {
							showExplanation("Permission Needed", "Rationale", Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_PERMISSION_COARSE_LOCATION);
						} else {
							requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_PERMISSION_COARSE_LOCATION);
						}

						if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity) context),
								Manifest.permission.ACCESS_FINE_LOCATION)) {
							showExplanation("Permission Needed", "Rationale", Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_FINE_LOCATION);
						} else {
							requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_FINE_LOCATION);
						}
						return null;
					}

					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
					Log.d("Network", "Network");

					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							//onLocationChanged(location);
						}
					}
				}

				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {

					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
					Log.d("GPS Enabled", "GPS Enabled");

					if (locationManager != null) {
						Location bufferLocation = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (bufferLocation != null) {

							location = bufferLocation;
						}
					}
				} else {
					//Toast.makeText(context, "Turn on your GPS for better accuracy", Toast.LENGTH_SHORT).show();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (location != null && location.getLongitude() != 0 && location.getLatitude() != 0) {
			onLocationChanged(location);
		}

		return location;
	}

	private void showExplanation(String title,
								 String message,
								 final String permission,
								 final int permissionRequestCode) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
				.setMessage(message)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						requestPermission(permission, permissionRequestCode);
					}
				});
		builder.create().show();
	}

	private void requestPermission(String permissionName, int permissionRequestCode) {
		ActivityCompat.requestPermissions(((Activity) context),
				new String[]{permissionName}, permissionRequestCode);
	}

	public void setPointMap() {

		mvMap.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap mMap) {

				googleMap = mMap;
				googleMap.clear();
				googleMap.addMarker(new MarkerOptions()
						.anchor(0.0f, 1.0f)
						.draggable(false)
						.position(new LatLng(latitude, longitude)));

				if (ActivityCompat.checkSelfPermission(((Activity) context), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(context, "Please allow location access from y   our app permission", Toast.LENGTH_SHORT).show();
					return;
				}

				//googleMap.setMyLocationEnabled(true);
				googleMap.getUiSettings().setZoomControlsEnabled(true);
				MapsInitializer.initialize(context);
				LatLng position = new LatLng(latitude, longitude);
				// For zooming automatically to the location of the marker
				CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(15).build();
				googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

				updateKeterangan(position);

				googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
					@Override
					public void onMarkerDragStart(Marker marker) {

					}

					@Override
					public void onMarkerDrag(Marker marker) {

					}

					@Override
					public void onMarkerDragEnd(Marker marker) {

						LatLng position = marker.getPosition();
						updateKeterangan(position);
						Log.d(TAG, "onMarkerDragEnd: " + position.latitude + " " + position.longitude);
					}
				});
			}
		});
	}

	private void updateKeterangan(LatLng position) {

		latitude = position.latitude;
		longitude = position.longitude;

	}

	private String getAddress(Location location) {
		List<Address> addresses;
		try {
			addresses = new Geocoder(context, Locale.getDefault()).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			return findAddress(addresses);
		} catch (Exception e) {

			return "";

		}
	}

	private String findAddress(List<Address> addresses) {
		String address = "";
		if (addresses != null) {
			for (int i = 0; i < addresses.size(); i++) {

				Address addre = addresses.get(i);
				String street = addre.getAddressLine(0);
				if (null == street)
					street = "";

				String city = addre.getLocality();
				if (city == null) city = "";

				String state = addre.getAdminArea();
				if (state == null) state = "";

				String country = addre.getCountryName();
				if (country == null) country = "";

				address = street + ", " + city + ", " + state + ", " + country;
			}
			return address;
		}
		return address;
	}
}
