package gmedia.net.id.OnTime.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alkhattabi.kalert.KAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.home.menu.LockInActivity;
import gmedia.net.id.OnTime.home.menu.LockOutActivity;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

import static gmedia.net.id.OnTime.utils.Utils.afterSnapCamera;


public class FrontCamera {
	private static Context context;

	public static byte[] dataBaru;
	private static File saveDirectory;
	private static String folderName = "MyCameraApp";
	private static String filePathURI;
	private static String tipe_scan;

	public FrontCamera(Context context,String tipe_scan) {
		this.context = context;
		this.tipe_scan = tipe_scan;
	}

	public Camera getCameraInstance() {
		Camera c = null;
		try {
			c = openFrontFacingCamera();
		} catch (Exception e) {
		}
		return c;
	}

	public static Camera openFrontFacingCamera() {
		int cameraCount = 0;
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					cam = Camera.open(camIdx);
				} catch (RuntimeException e) {
					Log.e("error camera", "Camera failed to open: " + e.getLocalizedMessage());
				}
			}
		}
		return cam;
	}

	public static Camera.PictureCallback mPicture = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			int rotasi = 0;

			if (Preview.rotasiLayar == 0) {
				rotasi = 270;
			} else if (Preview.rotasiLayar == 1) {
				rotasi = 0;
			} else if (Preview.rotasiLayar == 3) {
				rotasi = 180;
			}
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//bitmap asli
			Matrix matrix = new Matrix();
			matrix.postRotate(rotasi);
			Long tsLong = System.currentTimeMillis()/1000;
			String ts = tsLong.toString();
			Uri uri = getImageUri(context, bitmap);
			String namaFile = ts+".JPG";

			String pathfile = copyFileFromUri(context, uri, namaFile, matrix);
			try {
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				Double widthBaru = (double) 0;
				Double heightBaru = (double) 0;
				if (width > height) {

					heightBaru = (double) 640 / width * height;
					widthBaru = (double) 640;
				} else if (width < height) {
					widthBaru = (double) 640 / height * width;
					heightBaru = (double) 640;
				} else if (width == height) {
					widthBaru = (double) 640;
					heightBaru = (double) 640;
				}
				Log.d("WidthHeight", String.valueOf(widthBaru) + " " + String.valueOf(heightBaru));
				bitmap = Bitmap.createScaledBitmap(bitmap, Integer.valueOf(widthBaru.intValue()), Integer.valueOf(heightBaru.intValue()), true);//resize bitmap
				int widthWatermark = bitmap.getWidth();
				int tambahanWidthWatermark = (widthWatermark / 6) - 30;
				int heightWatermark = bitmap.getHeight();
				Log.d("width & height", "" + widthWatermark + " " + heightWatermark);
				Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

				Canvas canvas = new Canvas(mutableBitmap);
				canvas.drawBitmap(bitmap, 0, 0, null);
				Paint paint = new Paint();
				paint.setColor(Color.parseColor("#FFFFFF"));
				paint.setStyle(Paint.Style.FILL);

				if (Preview.rotasiLayar == 0) {
					paint.setTextSize(16);
				} else {
					paint.setTextSize(20);
				}

				SimpleDateFormat sdf = new SimpleDateFormat("dd / MM / yyyy - HH:mm:ss");
				String currentDateandTime = sdf.format(new Date());
				canvas.drawText(currentDateandTime, (widthWatermark / 2) + tambahanWidthWatermark, heightWatermark - 20, paint);
				ByteArrayOutputStream blob = new ByteArrayOutputStream();
				mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, blob);
				dataBaru = blob.toByteArray();
				mutableBitmap.recycle();
				try {
					FileOutputStream fos = new FileOutputStream(new File(pathfile));
					fos.write(dataBaru);
					fos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					Log.d("kamera", e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
					Log.d("kamera", e.getMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("kamera", e.getMessage());
			}
			Utils.afterSnapCamera = true;
			if (Utils.afterSnapCamera) {
				Absen();
			}
			Log.d("TEST", "File created");
			Utils.safeToTakePicture = true;

		}
	};

	public static void Absen() {

		KAlertDialog pDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
		pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
		pDialog.setTitleText("Processing...");
		pDialog.setCancelable(false);
		pDialog.show();

		KAlertDialog pDialogSuccess = new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE);
		pDialogSuccess.setTitleText("SUKSES!..");
		pDialogSuccess.setConfirmText("OK");
		pDialogSuccess.setCancelable(false);

		KAlertDialog pDialogFailed = new KAlertDialog(context, KAlertDialog.ERROR_TYPE);
		pDialogFailed.setTitleText("GAGAL!..");
		pDialogFailed.setCancelText("OK");
		pDialogFailed.setCancelable(false);

		Bitmap bitmapBaru;
		bitmapBaru = BitmapFactory.decodeByteArray(FrontCamera.dataBaru, 0, FrontCamera.dataBaru.length);
		JSONArray jsonArray = new JSONArray(Imei.getIMEI(context));
		final JSONObject jBody = new JSONObject();
		try {
			if(tipe_scan.equals("7")){
				jBody.put("foto", EncodeBitmapToString.convert(bitmapBaru));
				jBody.put("latitude", LockInActivity.latitude);
				jBody.put("longitude", LockInActivity.longitude);
				jBody.put("tipe_scan", tipe_scan);
				jBody.put("imei", jsonArray);
				jBody.put("ip_public", LockInActivity.infoIpPublic);
				jBody.put("mac_address", LockInActivity.infoBSSID);
			}else{
				jBody.put("foto", EncodeBitmapToString.convert(bitmapBaru));
				jBody.put("latitude", LockOutActivity.latitude);
				jBody.put("longitude", LockOutActivity.longitude);
				jBody.put("tipe_scan", tipe_scan);
				jBody.put("imei", jsonArray);
				jBody.put("ip_public", LockOutActivity.infoIpPublic);
				jBody.put("mac_address", LockOutActivity.infoBSSID);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d("error", e.getMessage());
		}

		new ApiVolley(context,jBody,"POST",ServerUrl.ScanAbsen, new AppRequestCallback(new AppRequestCallback.ResponseListener() {
			@Override
			public void onSuccess(String response, String message) {
				pDialog.dismiss();
				pDialogSuccess.setContentText(message);
				pDialogSuccess.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
					@Override
					public void onClick(KAlertDialog sDialog) {
						sDialog.dismiss();
						((Activity) context).finish();
						((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					}
				});
				pDialogSuccess.show();
			}

			@Override
			public void onEmpty(String message) {
				pDialog.dismiss();
				pDialogFailed.setContentText(message);
				pDialogFailed.setCancelClickListener(new KAlertDialog.KAlertClickListener() {
					@Override
					public void onClick(KAlertDialog sDialog) {
						sDialog.dismiss();
					}
				});
				pDialogFailed.show();
			}

			@Override
			public void onFail(String message) {
				pDialog.dismiss();
				Toasty.error(context, "Terjadi kesalahan data.", Toast.LENGTH_SHORT, true).show();
			}
		}));
	}

	public static byte[] dataNew(byte[] data) {
		return dataBaru = data;
	}

	public static File getOutputMediaFile() {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

		return mediaFile;
	}

	private static String copyFileFromUri(Context context, Uri fileUri, String namaFile, Matrix matrix) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String extension = namaFile.substring(namaFile.lastIndexOf("."));
		FileOutputStream out = null;
		String hasil = "";

		try {
			ContentResolver content = context.getContentResolver();
			inputStream = content.openInputStream(fileUri);
			File root = Environment.getExternalStorageDirectory();
			if (root == null) {
				//Log.d(TAG, "Failed to get root");
			}
			// create a directory
			saveDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator);
			// create direcotory if it doesn't exists
			saveDirectory.mkdirs();
			final int time = (int) (new Date().getTime() / 1000);
			extension = extension.toLowerCase();
			Bitmap bm2 = null;
			if (extension.equals(".jpeg") || extension.equals(".jpg") || extension.equals(".png") || extension.equals(".bmp")) {

				outputStream = new FileOutputStream(saveDirectory.getAbsoluteFile() + File.separator + time + namaFile); // filename.png, .mp3, .mp4 ...
				bm2 = BitmapFactory.decodeStream(inputStream);
				int scale = 80;

				int imageHeight = bm2.getHeight();
				int imageWidth = bm2.getWidth();

				int newWidth = 0;
				int newHeight = 0;

				if (imageHeight > imageWidth) {

					newWidth = 640;
					newHeight = newWidth * imageHeight / imageWidth;
				} else {

					newHeight = 640;
					newWidth = newHeight * imageWidth / imageHeight;
				}

				bm2 = Bitmap.createScaledBitmap(bm2, newWidth, newHeight, false);

				if (matrix != null) {

					bm2 = Bitmap.createBitmap(bm2, 0, 0, bm2.getWidth(), bm2.getHeight(), matrix, true);
				}

				bm2.compress(Bitmap.CompressFormat.JPEG, scale, outputStream);

				File file = new File(saveDirectory, time + namaFile);
				//Log.i(TAG, "" + file);
				if (file.exists())
					file.delete();
				try {
					FileOutputStream outstreamBitmap = new FileOutputStream(file);
					bm2.compress(Bitmap.CompressFormat.JPEG, scale, outstreamBitmap);
					outstreamBitmap.flush();
					outstreamBitmap.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {

				outputStream = new FileOutputStream(saveDirectory.getAbsoluteFile() + File.separator + time + namaFile); // filename.png, .mp3, .mp4 ...
				if (outputStream != null) {
					Log.e("kamera", "Output Stream Opened successfully");
				}

				byte[] buffer = new byte[1000];
				int bytesRead = 0;
				while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) >= 0) {
					outputStream.write(buffer, 0, buffer.length);
				}
			}

			filePathURI = Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator + time + namaFile;
			hasil = filePathURI;
			/*if (bm2 != null) {

				listPhoto.add(new PhotoModel(filePathURI, "", ImageUtils.convert(bm2)));
				adapterPhoto.notifyDataSetChanged();
			}*/

			//new UploadFileToServer().execute();
		} catch (Exception e) {
			Log.e("kamera", "Exception occurred " + e.getMessage());
		} finally {

		}
		return hasil;
	}

	public static Bitmap rotateImage(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
				matrix, true);
	}

	public static Uri getImageUri(Context context, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		Long tsLong = System.currentTimeMillis()/1000;
		String ts = tsLong.toString();
		String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, ts, null);
		return Uri.parse(path);
	}
}
