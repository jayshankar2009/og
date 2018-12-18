package com.synergy.keimed_ordergenie.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.synergy.keimed_ordergenie.BuildConfig;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.utils.CheckVersionControll;
import com.synergy.keimed_ordergenie.utils.SessionManager;

import org.jsoup.Jsoup;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_SETTINGS;
import static android.Manifest.permission_group.CAMERA;

public class SplashScreenActivity extends AppCompatActivity {

	private static final String TAG = "SplashScreenActivity";
	private final int SPLASH_DISPLAY_LENGTH = 3000;
	public static final int requestPermissionCode = 7;
	private SessionManager session;
	boolean isChecked= true;
	private ProgressDialog progressDialog;
	AlertDialog.Builder builder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.splash_screen);
		//ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
		builder = new AlertDialog.Builder(this);
		session = new SessionManager(getApplicationContext());

		//if (isIMEIPermissionGranted() == true) {

	/*	new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (session.isLoggedIn()) {
					showmainscreen();
				}else {
					showloginscreen();
				}
			}
		}, SPLASH_DISPLAY_LENGTH);*/
	/*	if(CheckingPermissionIsEnabledOrNot())
		{*/

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (session.isLoggedIn()) {
					new CheckVersionControll(SplashScreenActivity.this);
					showmainscreen();

				}else {
					new CheckVersionControll(SplashScreenActivity.this);
					showloginscreen();

				}

			}
		}, SPLASH_DISPLAY_LENGTH);


		/*}
		else
		{RequestMultiplePermission();}*/
	}

	private void showmainscreen() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void showloginscreen() {
		Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private void RequestMultiplePermission() {
		Toast.makeText(getApplicationContext(),"Toast1",Toast.LENGTH_LONG).show();
		// Creating String Array with Permissions.
		ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]
				{
						CAMERA,
						RECORD_AUDIO,
						READ_PHONE_STATE,
						WRITE_EXTERNAL_STORAGE,
						READ_EXTERNAL_STORAGE,
						ACCESS_COARSE_LOCATION,
						CALL_PHONE,
						WRITE_SETTINGS
				}, requestPermissionCode);

	}
	/*@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case requestPermissionCode:
				if (grantResults.length > 0) {
					boolean RecordAudioPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
					boolean ReadPhoneState = grantResults[1] == PackageManager.PERMISSION_GRANTED;
					boolean WriteExternalStorage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
					boolean ReadExternalStorage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
					boolean AccessCoarseLoaction = grantResults[4] == PackageManager.PERMISSION_GRANTED;
					boolean CallPhone = grantResults[5] == PackageManager.PERMISSION_GRANTED;
					if (RecordAudioPermission && ReadPhoneState
							&& WriteExternalStorage && ReadExternalStorage&& AccessCoarseLoaction &&
							CallPhone) {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if (session.isLoggedIn()) {
									isChecked=true;
									showmainscreen();
								}else {
									isChecked=true;
									showloginscreen();
								}
							}
						}, SPLASH_DISPLAY_LENGTH);
					}
					else {
						isChecked=false;
RequestMultiplePermission();
					}
				}else {
				*//*	builder.setMessage("Do you want to close this application ?")
							.setCancelable(false)
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									finish();
									Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
											Toast.LENGTH_SHORT).show();
								}
							})
							.setNegativeButton("No", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									//  Action for 'NO' Button
									dialog.cancel();
									Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
											Toast.LENGTH_SHORT).show();
								}
							});
					//Creating dialog box
					AlertDialog alert = builder.create();
					//Setting the title manually
					alert.setTitle("AlertDialogExample");
					alert.show();*//*
				}
				break;
		}
	}
*/

	private class GetVersionCode extends AsyncTask<Void, String, String> {
		String currentVersion;

		{
			try {
				currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = ProgressDialog.show(SplashScreenActivity.this, "Please Wait", "Processing... ", true);
		}

		@Override
		protected String doInBackground(Void... voids) {

			String newVersion = null;
			try {
				newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")
						.timeout(30000)
						.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
						.referrer("http://www.google.com")
						.get()
						.select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
						.first()
						.ownText();
				return newVersion;
			} catch (Exception e) {
				return newVersion;
			}
		}

		@Override
		protected void onPostExecute(String onlineVersion) {
			super.onPostExecute(onlineVersion);
			progressDialog.dismiss();
			if (onlineVersion != null &&onlineVersion.length()>0) {

				int playStoreVersion = VersionReturn(onlineVersion);
				int myVersion = VersionReturn(currentVersion);

				if (playStoreVersion>myVersion){
					//Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
					showUpdateDialog();
					//finish();
					// redirectStore("");
				}else{
					if (session.isLoggedIn()) {
						showmainscreen();
					}else {
						showloginscreen();
					}
				}
			}

		}
	}

	public void showUpdateDialog(){
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("New version available")
				.setMessage("Please, update app to new version to continue reposting.")
				.setPositiveButton("Update",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								redirectStore("");
								dialog.dismiss();
							}
						})
				.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public int VersionReturn(String version){
		String splitVersion = version.replace(".","");

		int Appstoreverion =Integer.parseInt(splitVersion);
		return Appstoreverion;
	}

	private void redirectStore(String updateUrl) {

		String Url = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en";
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
	public boolean CheckingPermissionIsEnabledOrNot() {
		int RecordAudioPermission = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
		int ReadPhoneState = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
		int WriteExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
		int ReadExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
		int AccessCoarseLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
		int CallPhone = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);

		return  RecordAudioPermission == PackageManager.PERMISSION_GRANTED &&
				ReadPhoneState == PackageManager.PERMISSION_GRANTED &&
				WriteExternalStorage == PackageManager.PERMISSION_GRANTED&&
				ReadExternalStorage == PackageManager.PERMISSION_GRANTED&&
				AccessCoarseLocation == PackageManager.PERMISSION_GRANTED&&
				CallPhone == PackageManager.PERMISSION_GRANTED;
	}
}