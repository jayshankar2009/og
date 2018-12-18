package com.synergy.keimed_ordergenie.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.synergy.keimed_ordergenie.BuildConfig;
import com.synergy.keimed_ordergenie.Helper.SQLiteHandler;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.database.ChemistCart;
import com.synergy.keimed_ordergenie.database.ChemistCartDao;
import com.synergy.keimed_ordergenie.database.DaoSession;
import com.synergy.keimed_ordergenie.fragment.ChemistDashboardFragment;
import com.synergy.keimed_ordergenie.fragment.DashboarddetailsmenuFragment;
import com.synergy.keimed_ordergenie.fragment.DistributorDashboardFragment;
import com.synergy.keimed_ordergenie.fragment.StockistDashboardFragment;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.RefreshData;
import com.synergy.keimed_ordergenie.utils.SendingLocationOnByOrder;
import com.synergy.keimed_ordergenie.utils.SessionManager;
import com.synergy.keimed_ordergenie.utils.TypefaceUtil;
import com.synergy.keimed_ordergenie.utils.get_current_location;
import com.synergy.keimed_ordergenie.utils.get_current_location_ondashboard;
import com.synergy.keimed_ordergenie.utils.get_imie_number;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.synergy.keimed_ordergenie.app.AppConfig.GET_DASHBOARD_STASTICS_SALESMAN;
import static com.synergy.keimed_ordergenie.app.AppConfig.GET_STOCKIST_STASTICS;
import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.STOCKIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_LATITUDE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_LOGIN_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_LONGITUDE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_PROFILE_IMAGE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;


public class MainActivity extends AppCompatActivity
        implements ForceUpdateChecker.OnUpdateNeededListener, StockistDashboardFragment.OnmenuitemSelected, ChemistDashboardFragment.OnmenuitemSelected, MakeWebRequest.OnResponseSuccess, DistributorDashboardFragment.OnmenuitemSelected, DashboarddetailsmenuFragment.OnFragmentInteractionListener {
    private PopupWindow mPopupWindow;
    RelativeLayout relativeLayout;
    String currentVersion;
    String accessKey, Latitude, Longitude, CurrentLocation;
    public static String User_name;
    private String login_type, cart_flag;
    String strDescription;
    long refid;
    ArrayList<Uri> uris = new ArrayList<Uri>();
    File[] filePaths;
    RelativeLayout mRelativeLayout;
    String current_app_version = "1.0.4", available_app_version = "1.0.5";
    private Fragment o_DashboardFragment = null;
    DashboarddetailsmenuFragment o_DashboarddetailsmenuFragment;
    private ImageLoader imageLoader;
    private Date current_date = Calendar.getInstance().getTime();
    public TextView tx_customercount_value;
    public TextView tx_pendingbills_value;
    public TextView tx_orders_value;
    public TextView tx_sales_value;
    public TextView tx_returns_value;
    public TextView tx_inv_value;
    public static String text_int_total_vallue = "";
    public TextView _txt_profile_name, txt_appversion;
    public TextView _txt_profile_email;
    private ImageView iv_profile_pic;
    private static final int NOTIFICATION_ID = 147894;
    private List<Map<String, String>> list_data = new ArrayList<Map<String, String>>();
    public static final String CHEMIST_IS_LAST_10_ORDER = "chemist_is_last_10_orders";
    BroadcastReceiver onComplete;
    File backupDB, backupDB2;
    Uri uriPath, uriPath2;

    public static String last_Ten_orders;
    private ProgressDialog progressDialog;
    //Stockist Navigation
    public TextView _menu_logout, _menu_changepassword, txt_cltlegealname;

    private String User_id, Stockist_id, isDummypass, cLegalName, cLoginName;


    SharedPreferences pref;
    AppController globalVariable;

    @BindView(R.id.toolbar)
    Toolbar _toolbar;

    @BindView(R.id.appbar)
    AppBarLayout _appbar;

    @BindView(R.id.main_content)
    CoordinatorLayout _mainContent;

    @BindView(R.id.drawer)
    DrawerLayout _drawer;

    @BindView(R.id.nav_view)
    NavigationView _navView;
    /* Cart Dao */
    private DaoSession daoSession;
    private ChemistCartDao chemistCartDao;
    private List<ChemistCart> chemistCartList;
    private SessionManager session;
    /* Get Intent */
    private static final String SELECTED_CHEMIST_ID = "selected_chemist_id";
    private String chemistId;

    /* Sqlite Handler */
    private SQLiteHandler sqLiteHandler;
    boolean ischeckedInternetConn = false;
    private int postSQLiteDelivery = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Lato-Regular.ttf");
        ButterKnife.bind(this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.cstmRela);
        globalVariable = (AppController) getApplicationContext();
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        //pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        accessKey = pref.getString("key", "");
        session = new SessionManager(getApplicationContext());
        setSupportActionBar(_toolbar);
        cLoginName = pref.getString(CLIENT_LOGIN_NAME, "0");
        cLegalName = pref.getString(CLIENT_NAME, "0");
        User_id = pref.getString(USER_ID, "0");
        Stockist_id = pref.getString(CLIENT_ID, "0");
        chemistId = pref.getString(CLIENT_ID, "");
        Log.i("Image Loader---", "" + CLIENT_PROFILE_IMAGE);
        sqLiteHandler = new SQLiteHandler(this);
        /* Initialize Dao Session Class */
        daoSession = ((AppController) getApplication()).getDaoSession();
        chemistCartDao = daoSession.getChemistCartDao();
        ActionBar supportActionBar = getSupportActionBar();
        call_refresh_data();

        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        //check_app_version();
        set_navigation_drawer();
        get_client_details();
        create_dashboard();

        if (login_type.equals(CHEMIST) && (pref.getString(CLIENT_LATITUDE, "0").equals("") || pref.getString(CLIENT_LONGITUDE, "0").equals("") || pref.getString(CLIENT_LATITUDE, "0") == null || pref.getString(CLIENT_LONGITUDE, "0") == null)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getApplicationContext().getString(R.string.update_text));
            alertDialogBuilder.setMessage(getApplicationContext().getString(R.string.update_loc_text) + " " + "?");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Save Location", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    UpdateDialog();
                }
            });
            alertDialogBuilder.setNegativeButton("No,Thanks", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    dialog.cancel();
                }
            });

            alertDialogBuilder.show();
        }

    }

    void download_apk(String Url)// downloading apk from url using download service
    {
        int permission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            return;
        }

        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Url));
            request.setDescription("updating");
            request.setTitle("Downloading");
            File mFile = new File(Environment.getExternalStorageDirectory() + "/apk/app-debug.apk");
            if (mFile.exists()) {
                mFile.delete();
            }
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setDestinationInExternalPublicDir("/apk/", "app-debug.apk");
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            refid = manager.enqueue(request);
        } catch (Exception e) {
            Log.e("Exception",e.toString());

        }

    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue reposting.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        })
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();  //13 Oct 2018
    }

    private void redirectStore(String updateUrl) {
        String Url = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en";
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_notfic) {
            Intent i = new Intent(this, DistributorNotificationActivity.class);
            startActivity(i);
            return true;
        }

       /* if (id == R.id.action_export_db) {
            exportDB();
            return true;
        }
        if (id == R.id.action_logout) {
            showloginscreen();
            return true;
        } */
        else if (id == android.R.id.home) {
            _drawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (login_type.equals(STOCKIST)) {
            getMenuInflater().inflate(R.menu.menu_distributor_dashboard, menu);
            //  getMenuInflater().inflate(R.layout.menu_distributor_dashboard, menu);
            RelativeLayout rLayout = (RelativeLayout) menu.findItem(R.id.action_notfic).getActionView();

            TextView txt_cltlegealname = (TextView) rLayout.findViewById(R.id.txt_compny);
            TextView txt_username = (TextView) rLayout.findViewById(R.id.txt_username);

            txt_username.setText(cLoginName);
            txt_cltlegealname.setText(cLegalName);

        } else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        return true;
    }

    /* Dashboard APIs */
    private void create_dashboard() {
        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");
        if (login_type != null) {
            if (login_type.equals(STOCKIST)) {
                Log.i("StockList1", "---" + Stockist_id);
                MakeWebRequest.MakeWebRequest("get", AppConfig.GET_USER_OFFERS,
                        AppConfig.GET_USER_OFFERS + Stockist_id, this, true);
                o_DashboardFragment = DistributorDashboardFragment.newInstance();
            } else if (login_type.equals(CHEMIST)) {
                Log.i("StockList2", "---" + CHEMIST);
                o_DashboardFragment = ChemistDashboardFragment.newInstance();
            } else {
                Log.i("StockList3", "---" + Stockist_id);
                MakeWebRequest.MakeWebRequest("get", GET_DASHBOARD_STASTICS_SALESMAN,
                        GET_DASHBOARD_STASTICS_SALESMAN + "[" + Stockist_id + "," + User_id + "]", this, true);
                Log.d("URL13", GET_DASHBOARD_STASTICS_SALESMAN + "[" + Stockist_id + "," + User_id + "]");
                o_DashboardFragment = StockistDashboardFragment.newInstance();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_centerscreen, o_DashboardFragment);
            fragmentTransaction.commit();
        }
    }

    private void change_pass_dialog() {
        new AlertDialog.Builder(this)
                .setTitle("Change Password")
                .setMessage("Change your passowrd")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  System.exit(0);
                        dialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .show();
    }

    public void getAppVerisonFromAPI() {
        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_UPDATED_VERSION,
                AppConfig.GET_UPDATED_VERSION, this, true);
        Log.d("Version11", AppConfig.GET_UPDATED_VERSION);
    }

    public void forceUpdate(String latestVersion, final String Url) {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        if (latestVersion != null) {
            if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("New version available")
                        .setMessage("Please, update app to new version to continue reposting.")
                        .setPositiveButton("Update",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                          redirectStore("https://play.google.com/store/apps/details?id=com.synergy.keimed_ordergenie");
                                       /* progressDialog = ProgressDialog.show(MainActivity.this, "Please Wait", "Downloading.... ", true);
                                        Update(Url);*/
                                    }
                                })
                        .create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            } else {
            }
        }
    }

    public void Update(String URL) {
        //  Log.d("URL",URL);
        onComplete = new BroadcastReceiver() {

            public void onReceive(Context ctxt, Intent intent) {
                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (refid == referenceId) {
                    progressDialog.dismiss();
                    File mFile;
                    mFile = new File(Environment.getExternalStorageDirectory() + "/apk/app-debug.apk");
                    if (mFile.exists()) {
                        startInstaller(mFile);

                    } else {

                    }
                }
            }
        };

        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        download_apk(URL);
    }


    void startInstaller(File mFile) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, mFile);
            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkURI);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Uri apkUri = Uri.fromFile(mFile);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void showForceUpdateDialog(String latestVersion) {
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(this.getString(R.string.youAreNotUpdatedTitle));
        alertDialogBuilder.setMessage(this.getString(R.string.youAreNotUpdatedMessage) + " " + latestVersion + this.getString(R.string.youAreNotUpdatedMessage1));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }

    /* Navigation Drawer */
    private void set_navigation_drawer() {

        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");

        if (login_type != null) {
            if (login_type.equals(CHEMIST)) {
                LayoutInflater inflater = LayoutInflater.from(this);
                final View dialogview = inflater.inflate(R.layout.navheader_chemist, null);
                _txt_profile_name = (TextView) dialogview.findViewById(R.id.txt_profile_name_c);
                _txt_profile_email = (TextView) dialogview.findViewById(R.id.txt_profile_email_c);
                iv_profile_pic = (ImageView) dialogview.findViewById(R.id.iv_profile_pic);
                txt_appversion = (TextView) dialogview.findViewById(R.id.txt_appversion);
                Glide.with(getApplicationContext()).load(CLIENT_PROFILE_IMAGE).asBitmap().placeholder(R.drawable.genie).centerCrop().into(new BitmapImageViewTarget(iv_profile_pic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv_profile_pic.setImageDrawable(circularBitmapDrawable);
                    }
                });
                String appversion = BuildConfig.VERSION_NAME;
                txt_appversion.setText("Version: " + appversion);
                _navView.inflateMenu(R.menu.menu_nv_chemist);
                _navView.addHeaderView(dialogview);
                _navView.setBackground(getResources().getDrawable(R.drawable.white_bg_border));
                _navView.setNavigationItemSelectedListener(
                        new NavigationView.OnNavigationItemSelectedListener() {
                            // This method will trigger on item Click of navigation menu
                            @Override
                            public boolean onNavigationItemSelected(MenuItem menuItem) {
                                // Set item in checked state
                                menuItem.setChecked(true);

                                int id = menuItem.getItemId();
                                if (id == R.id.Chemist_LogOut) {
                                    cart_flag = "1";
                                    syncOrderOnLogout(cart_flag);
                                    //showloginscreen();
                                    return true;
                                }
                                if (id == R.id.Chemist_Lastorders) {
                                    showLastOrders();
                                    return true;
                                }
                                if (id == R.id.Chemist_Messages) {
                                    showMessageScreen();
                                    return true;
                                }
                                if (id == R.id.Chemist_MyProfile) {
                                    showedit();
                                    return true;
                                }
                                if (id == R.id.Chemist_location) {
                                    globalVariable.setFromMenuItemClick(true);
                                    new get_current_location(MainActivity.this);

                                    // location_alert();
                                    return true;
                                }
                                if (id == R.id.Chemist_ChangePassword) {
                                    showchangepassword();
                                    return true;
                                }
                                // TODO: handle navigation
                                // Closing drawer on item click
                                _drawer.closeDrawers();
                                return true;
                            }
                        });
                //Toast.makeText(this, "Chemist", Toast.LENGTH_SHORT).show();
            }
            //stockist login
            else if (login_type.equals(STOCKIST)) {
                LayoutInflater inflater = LayoutInflater.from(this);
                final View dialogview = inflater.inflate(R.layout.navheader_stockist, null);
                _txt_profile_name = (TextView) dialogview.findViewById(R.id.txt_profile_name_c);
                txt_appversion = (TextView) dialogview.findViewById(R.id.txt_appversion);
                _txt_profile_email = (TextView) dialogview.findViewById(R.id.txt_profile_email_c);
                iv_profile_pic = (ImageView) dialogview.findViewById(R.id.iv_profile_pic);
                Glide.with(getApplicationContext()).load(CLIENT_PROFILE_IMAGE).asBitmap().placeholder(R.drawable.genie).centerCrop().into(new BitmapImageViewTarget(iv_profile_pic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv_profile_pic.setImageDrawable(circularBitmapDrawable);
                    }
                });
                String appversion = BuildConfig.VERSION_NAME;
                txt_appversion.setText("Version: " + appversion);

                _navView.inflateMenu(R.menu.menu_nv_stockist);
                _navView.addHeaderView(dialogview);
                _navView.setNavigationItemSelectedListener(
                        new NavigationView.OnNavigationItemSelectedListener() {
                            // This method will trigger on item Click of navigation menu
                            @Override
                            public boolean onNavigationItemSelected(MenuItem menuItem) {
                                // Set item in checked state
                                menuItem.setChecked(true);
                                int id = menuItem.getItemId();
                                if (id == R.id.customers) {
                                    showDistributorcustomer();
                                    return true;
                                }
                                if (id == R.id.catalog) {
                                    showDistributorProductCatlog();
                                    return true;
                                }
                                if (id == R.id.orders) {
                                    showDistributorOrders();
                                    return true;
                                }
                                if (id == R.id.pendingbill) {
                                    showDistributorPendingBill();
                                    return true;
                                }
                                if (id == R.id.sales) {
                                    showDistributorSalesandReturn();
                                    return true;
                                }
                                if (id == R.id.users) {

                                    show_Users();

                                    return true;
                                }
                                if (id == R.id.notifications) {

                                    //Toast.makeText(MainActivity.this,"Show Notifications Here",Toast.LENGTH_SHORT).show();
                                    show_Notifications();

                                    return true;
                                }
                                if (id == R.id.callplan) {
                                    showDistributorCallPlan();
                                    return true;
                                }

                                if (id == R.id.sto_payment) {
                                    showDistributorPayments();
                                    return true;
                                }
                                if (id == R.id.track) {
                                    showDistributorTrackUser();
                                    return true;
                                }
                                if (id == R.id.notification) {
                                    showDistributorNotification();
                                    return true;
                                }
                                if (id == R.id.livechat) {
                                    // showDistributorLiveChat();
                                    return true;
                                }
                                if (id == R.id.LogOut) {
                                    showloginscreen();
                                    return true;
                                }
                                // TODO: handle navigation
                                // Closing drawer on item click
                                _drawer.closeDrawers();
                                return true;
                            }
                        });
            } else {
                LayoutInflater inflater = LayoutInflater.from(this);
                final View dialogview = inflater.inflate(R.layout.navigation_stockist, null);
                _txt_profile_name = (TextView) dialogview.findViewById(R.id.txt_profile_name);
                _txt_profile_email = (TextView) dialogview.findViewById(R.id.txt_profile_email);
                txt_appversion = (TextView) dialogview.findViewById(R.id.txt_heading);

                _menu_logout = (TextView) dialogview.findViewById(R.id.menu_logout);
                _menu_changepassword = (TextView) dialogview.findViewById(R.id.menu_changepassword);
                TextView _menu_myprofile = (TextView) dialogview.findViewById(R.id.menu_profile);
                TextView _menu_messages = (TextView) dialogview.findViewById(R.id.menu_messages);
                TextView _menu_location = (TextView) dialogview.findViewById(R.id.menu_location);
                TextView _menu_orders = (TextView) dialogview.findViewById(R.id.menu_order);
                TextView _menu_export = (TextView) dialogview.findViewById(R.id.export_db);
                TextView _menu_last10orders = (TextView) dialogview.findViewById(R.id.menu_last10_orders);
                iv_profile_pic = (ImageView) dialogview.findViewById(R.id.iv_profile_pic);
                String appversion = BuildConfig.VERSION_NAME;
                txt_appversion.setText("Version: " + appversion);
                Glide.with(getApplicationContext()).load(CLIENT_PROFILE_IMAGE).asBitmap().placeholder(R.drawable.genie).centerCrop().into(new BitmapImageViewTarget(iv_profile_pic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv_profile_pic.setImageDrawable(circularBitmapDrawable);
                    }
                });
                _navView.addView(dialogview);
                _menu_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cart_flag = "0";
                        syncOrderOnLogout(cart_flag);
                        _drawer.closeDrawers();
                    }
                });
                _menu_orders.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PendingOrderCustomerList.class);
                        startActivity(intent);
                        _drawer.closeDrawers();
                    }
                });
                _menu_export.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _drawer.closeDrawers();
                    }
                });
                _menu_myprofile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showedit();
                        _drawer.closeDrawers();
                    }
                });
                _menu_changepassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        showchangepassword();
                        // Closing drawer on item click
                        _drawer.closeDrawers();
                    }
                });
                _menu_last10orders.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showLastOrdersSalesMan();
                        // Closing drawer on item click
                        _drawer.closeDrawers();
                    }
                });

                _menu_messages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        showMessageScreen();
                        // Closing drawer on item click
                        _drawer.closeDrawers();
                    }
                });

                _menu_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        globalVariable.setFromMenuItemClick(true);
                        new get_current_location(MainActivity.this);
                        // Closing drawer on item click
                        _drawer.closeDrawers();
                    }
                });
                //Toast.makeText(this, "Saleman", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openExportDb() {
        String path = getDatabasePath("ordergenie").getPath();
        String path1 = getDatabasePath("OrderGenie").getPath();
        final String IMEI = new get_imie_number().check_imie_permission(this);
        File sd = Environment.getExternalStorageDirectory();
        //  File sd1 = Environment.getExternalStorageDirectory();
//Toast.makeText(getApplicationContext(),"Toast==="+path+ "===="+path1 ,Toast.LENGTH_SHORT).show();
        FileChannel source1 = null;
        FileChannel destination1 = null;

        String backupDBPath = IMEI + "_2";

        FileChannel source = null;
        FileChannel destination = null;


        File currentDB = new File(path);
        File targetFile = new File(sd, IMEI);

        File currentDB1 = new File(path1);
        File targetFile1 = new File(sd, backupDBPath);
        try {
            source1 = new FileInputStream(currentDB1).getChannel();

            destination1 = new FileOutputStream(targetFile1).getChannel();

            destination1.transferFrom(source1, 0, source1.size());
            Log.i("OrderGenie", "" + backupDB);
            // Toast.makeText(this, "Path111:---"+backupDB, Toast.LENGTH_LONG).show();
            source1.close();
            destination1.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            source = new FileInputStream(currentDB).getChannel();

            destination = new FileOutputStream(targetFile).getChannel();

            destination.transferFrom(source, 0, source.size());

            //  Toast.makeText(this, "Path111:---"+backupDB, Toast.LENGTH_LONG).show();
            source.close();
            destination.close();
            //    Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.custom_exportdb, null);
        Button dialogButton = (Button) customView.findViewById(R.id.btnOk);
        mPopupWindow = new PopupWindow(
                customView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        // Set an elevation value for popup window
        // Call requires API level 21
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
                File sd = Environment.getExternalStorageDirectory();
                String backupDBPath = IMEI;
                backupDB = new File(sd, backupDBPath);
                backupDB2 = new File(sd, IMEI + "_2");
                filePaths = new File[]{backupDB, backupDB2};
                for (int j = 0; j < filePaths.length; j++) {
                    uris.add(Uri.fromFile(filePaths[j]));
                }
                uriPath = Uri.parse("file://" + backupDB);
                uriPath2 = Uri.parse("file://" + backupDB2);
                // i.putExtra(Intent.EXTRA_STREAM, uriPath);
                i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

                i.setType("message/rfc822");
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL,

                        new String[]{"vishal.patil@ordergenie.co.in"});

                i.putExtra(Intent.EXTRA_SUBJECT, "OrderGenie DB File");
                try {
                    startActivityForResult(Intent.createChooser(i, "Send mail..."), 123);

                    //  finish();
                    Log.i("Finish", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }


                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(customView.findViewById(R.id.cstmRela), Gravity.CENTER, 0, 0);
    }

    private void show_Notifications() {
        //Intent intent = new Intent(MainActivity.this, SelectedpaymentList.class);
        Intent intent = new Intent(this, DistributorNotificationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            //  Toast.makeText(getApplicationContext(),"Succesfully Send",Toast.LENGTH_LONG).show();
            try {
                File file1 = new File(uriPath.getPath());
                File file2 = new File(uriPath2.getPath());
                file1.delete();
                file2.delete();
                if (file2.exists()) {
                    file2.getCanonicalFile().delete();
                    if (file2.exists()) {
                        getApplicationContext().deleteFile(file2.getName());
                    }
                }
                if (file1.exists()) {

                    file1.getCanonicalFile().delete();

                    if (file1.exists()) {
                        getApplicationContext().deleteFile(file1.getName());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void location_alert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getApplicationContext().getString(R.string.update_text));
        alertDialogBuilder.setMessage(getApplicationContext().getString(R.string.update_loc_text) + " ");
        // alertDialogBuilder.setMessage(getApplicationContext().getString(R.string.update_loc_text) + get_current_location_ondashboard(LocationAddress.getAddressFromLocation(get_current_location_ondashboard..getLatitude(), location.getLongitude())));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }


    @Override
    public void OnmenuitemSelected(int imageResId, String name, String description, String url) {
        startActivity(new Intent(MainActivity.this, CustomerlistActivityNew.class));

    }

    private void showMessageScreen() {
        // session.setLogin(false);
        Intent intent = new Intent(MainActivity.this, MessageActivity.class);
        startActivity(intent);
        // finish();
    }

    private void showloginscreen() {
        session.setLogin(false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        try {
            NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nMgr.cancel(NOTIFICATION_ID);
        } catch (Exception e) {
        }
        finish();
    }


    /* Sync Order on Logout */
    private void syncOrderOnLogout(String cart_flag) {
        String cart_flag1 = cart_flag; //0=salesman,1=chemist

        Cursor cursor = sqLiteHandler.getAllOrderByUserId(User_id);
        int cursorCount = cursor.getCount();
        int totalCarCount = chemistCartDao.getDataCount();
        if (cursorCount > 0 || totalCarCount > 0) {
            if (cart_flag1 == "1") {
                pendingOrderDialog_chemist(cursorCount, totalCarCount); //chemist
            } else {
                pendingOrderDialog(cursorCount, totalCarCount);
            }
        } else {

            showlogin_delete_saveddata();

        }
    }

    /* Pending Order Notification dialog */
    private void pendingOrderDialog(int orderCount, int totalCartCount) {
        String title = null;
        String msg = null;
        if (orderCount > 0 && totalCartCount > 0) {
            title = "Cart & Orders";
            msg = "You have " + totalCartCount + " cart items and " + orderCount + " offline order\n\nPlease connect with the internet to export Only offline order automatically";
        } else if (orderCount > 0) {
            title = "Pending Orders";
            if (orderCount == 1) {
                msg = "You have " + orderCount + " offline order. Please connect with the internet to export order automatically";
            } else {
                msg = "You have " + orderCount + " offline orders. Please connect with the internet to export order automatically";
            }
        } else if (totalCartCount > 0) {
            title = "Cart Items";
            if (totalCartCount == 1) {
                msg = "You have " + totalCartCount + " item in your cart";
            } else {
                msg = "You have " + totalCartCount + " items in your cart";
            }
        }
        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void pendingOrderDialog_chemist(int orderCount, int totalCartCount) {
        String title = null;
        String msg = null;
        if (orderCount > 0 && totalCartCount > 0) {
            title = "Cart & Orders";
            msg = "You have " + totalCartCount + " cart items and " + orderCount + " offline order\n\nPlease connect with the internet to export Only offline order automatically";
        } else if (orderCount > 0) {
            title = "Pending Orders";
            if (orderCount == 1) {
                msg = "You have " + orderCount + " offline order. Please connect with the internet to export order automatically";
            } else {
                msg = "You have " + orderCount + " offline orders. Please connect with the internet to export order automatically";
            }
        } else if (totalCartCount > 0) {
            title = "Cart Items";
            if (totalCartCount == 1) {
                msg = "You have " + totalCartCount + " item in your cart. Cart Items will get deleted on Logout.";
            } else {
                msg = "You have " + totalCartCount + " items in your cart. Cart Items will get deleted on Logout.";
            }
        }
        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        chemistCartList = chemistCartDao.getallCartItemChemist(Stockist_id, false);
                        chemistCartDao.deleteInTx(chemistCartList);
                        Snackbar mySnackbar = Snackbar.make(getWindow().getDecorView(), "Products removed from cart", Snackbar.LENGTH_SHORT);
                        mySnackbar.show();
                        finish();
                        showloginscreen();
                    }
                }).show();
        // }

    }

    private void showlogin_delete_saveddata() {
        session.setLogin(false);
        //added by apurva to clear status after logout
        sqLiteHandler.deleteRecordFromSalesmanProduct();    // Delete All Products Data
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("IS_TAKE_ORDER_ASSIGNED"); // will delete key key_name3
        editor.remove("IS_PAYMENT_COLLECTION_ASSIGNED");
        editor.remove("IS_ORDER_DELIVERY_ASSIGNED");// will delete key key_name4
        editor.clear();
        // Save the changes in SharedPreferences
        editor.commit();


        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        try {
            NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nMgr.cancel(NOTIFICATION_ID);
        } catch (Exception e) {
        }
        finish();
    }

    private void showLastOrders() {
        Intent intent = new Intent(MainActivity.this, Order_list.class);
        intent.putExtra("isLast10Order", true);
        intent.putExtra(CHEMIST_IS_LAST_10_ORDER, true);
        intent.putExtra(SELECTED_CHEMIST_ID, chemistId);
        startActivity(intent);
        // finish();
    }

    private void show_Users() {
        Intent intent = new Intent(MainActivity.this, DistributorUsers.class);
        startActivity(intent);
    }


    private void showDistributorcustomer() {
        Intent intent = new Intent(MainActivity.this, DistributorCustomerList.class);
        startActivity(intent);
    }


    private void showDistributorProductCatlog() {

        //  Intent intent = new Intent(MainActivity.this, InventorylistActivity.class);
        Intent intent = new Intent(MainActivity.this, DistributorProductcatlog.class);
        startActivity(intent);

    }


    private void showDistributorOrders() {
        Intent intent = new Intent(MainActivity.this, DistributorsOrderList.class);
        startActivity(intent);
    }


    private void showDistributorPendingBill() {
        Intent intent = new Intent(MainActivity.this, DistributorPendingBills.class);
        startActivity(intent);
    }


    private void showDistributorSalesandReturn() {
        Intent intent = new Intent(MainActivity.this, DistributorReturnsActivity.class);
        startActivity(intent);
    }


    private void showDistributorPayments() {
        Intent intent = new Intent(MainActivity.this, DistributorPayments.class);
        startActivity(intent);
    }

    private void showDistributorUsers() {

        Intent intent = new Intent(MainActivity.this, DistributorCustomerList.class);
        // intent.putExtra(CHEMIST_IS_LAST_10_ORDER, true);
        startActivity(intent);
    }


    private void showDistributorCallPlan() {
        Intent intent = new Intent(MainActivity.this, CallPlan.class);
        // intent.putExtra(CHEMIST_IS_LAST_10_ORDER, true);
        startActivity(intent);
    }


    private void showDistributorTrackUser() {

//        Intent intent = new Intent(MainActivity.this, Order_list.class);
//        intent.putExtra(CHEMIST_IS_LAST_10_ORDER, true);
//        startActivity(intent);

    }


    private void showDistributorNotification() {
        Intent intent = new Intent(MainActivity.this, DistributorNotificationActivity.class);
        startActivity(intent);
    }


    private void showDistributorLiveChat() {
        Intent intent = new Intent(MainActivity.this, StockistOrderDetails.class);
        startActivity(intent);
    }


    /* Sales Man Last 10 Orders */
    private void showLastOrdersSalesMan() {
        //Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
        Intent intent = new Intent(MainActivity.this, HistoryOrdersActivity.class);
        intent.putExtra("isLast10Order", true);
        intent.putExtra(CHEMIST_IS_LAST_10_ORDER, true);
        startActivity(intent);
    }


    private void showedit() {

        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
        // finish();
    }

    private void showchangepassword() {
        globalVariable.setFromHomeActivity(true);
        Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
        // finish();
    }

    @Override
    public void onBackPressed() {
        if (_drawer.isDrawerOpen(GravityCompat.START)) {
            _drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            exit_notification();

        }
    }

    private String get_day_number(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String daynumber = "1";

        switch (day) {

            case Calendar.MONDAY:
                daynumber = "1";
                break;
            case Calendar.TUESDAY:
                daynumber = "2";
                break;

            case Calendar.WEDNESDAY:
                daynumber = "3";
                break;
            case Calendar.THURSDAY:
                daynumber = "4";
                break;
            case Calendar.FRIDAY:
                daynumber = "5";
                break;
            case Calendar.SATURDAY:
                daynumber = "6";
                break;

            case Calendar.SUNDAY:
                daynumber = "0";
                // daynumber = "7";
                // Current day is Sunday
                break;
        }
        return daynumber;
    }

    private void exit_notification() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure want to Exit")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        //  System.exit(0);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .show();
    }

    private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String backupDBPath = "ordergenie";
        // String currentDBPath = "/data/" + "com.synergy.ordergenie" + "/databases/" + backupDBPath;
        String currentDBPath = "/data/" + "com.synergy.ordergenie" + "/databases/" + backupDBPath;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void get_client_details() {
        String name_Header = pref.getString(ConstData.user_info.CLIENT_FULL_NAME, "OrderGenie");

        // _txt_profile_name.setText(pref.getString(ConstData.user_info.CLIENT_NAME, "OrderGenie"));
        //_txt_profile_name.setText(pref.getString(ConstData.user_info.CLIENT_FULL_NAME, "OrderGenie"));


        if (name_Header.equals("null")) {
            _txt_profile_name.setText("");
        } else {
            _txt_profile_name.setText(name_Header);
        }

        _txt_profile_email.setText(pref.getString(ConstData.user_info.EMAIL_ID, "www.ordergenie.co.in"));
        User_name = pref.getString(CLIENT_NAME, "OrderGenie");
    }

    private void call_refresh_data() {

        //String passEncrypted = pref.getString(encrypt("key"), encrypt(""));
        //String key = decrypt(passEncrypted);
        String key = pref.getString("key", "");
        //String key = decrypt(passEncrypted);
        globalVariable.setToken(key);
        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, RefreshData.class);
        startService(download_intent);
    }


    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {
        if (response != null) {
//            Log.d("Version12",response.toString());
//            if (f_name.equals(AppConfig.GET_UPDATED_VERSION))
//            {
//                try {
//                    String id=response.getString("id");
//                    String appversion=response.getString("appversion");
//                    String url=response.getString("appurl");
//                    Log.d("id11",id);
//                    Log.d("id12",appversion);
//                    Log.d("id13",url);
//                    forceUpdate(appversion,url);
//
//                } catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
//
//            }
        }

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {
        if (response != null) {
            Log.d("f_name", response.toString());
            //Log.d("DETAIL_FRAG_Responce11", String.valueOf(response)+"hii");
            try {
                /* Upload SQLite Delivery Background */
                if (f_name.equals(AppConfig.SAVE_ORDER_DELIVERY)) {
                    Cursor cursor = sqLiteHandler.getSavedDelivery();
                    int cursorCount = cursor.getCount();
                    if (postSQLiteDelivery == 1) {
                        if (cursorCount > 0) {
                            sqLiteHandler.deleteSavedDelivery();
                        }
                    }
                }

                if (f_name.equals(AppConfig.GET_UPDATED_VERSION)) {

                    String id = response.getJSONObject(0).getString("id");
                    String appversion = response.getJSONObject(0).getString("appversion");
                    String url = response.getJSONObject(0).getString("appurl");
                    //   Log.d("id11",id);
                    //  Log.d("id12",appversion);
                    //  Log.d("id13",url);
                    forceUpdate(appversion, url);  //13 Oct 2018


                }

                if (f_name.equals(GET_STOCKIST_STASTICS)) {
                    JSONObject j_obj = response.getJSONObject(0);
                    Bundle b = new Bundle();
                    tx_customercount_value.setText(j_obj.getString("Total_customer_count"));
                    tx_pendingbills_value.setText(j_obj.getString("Total_pending_bills"));
                    tx_orders_value.setText(j_obj.getString("Total_orders"));
                    tx_sales_value.setText(j_obj.getString("Total_Sales"));
                    tx_returns_value.setText(j_obj.getString("Total_returns"));
                    // tx_inv_value.setText(j_obj.getString("Total_Inventory_count"));

                    text_int_total_vallue = j_obj.getString("Total_Inventory_count");
                    b.putString("Total_Inventory_count", j_obj.getString("Total_Inventory_count"));
                }
                if (f_name.equals(GET_DASHBOARD_STASTICS_SALESMAN)) {
                    //  Log.d("Response34",response.toString());
                    JSONObject j_obj = response.getJSONObject(0);
                    Bundle b = new Bundle();
                    tx_customercount_value.setText(j_obj.getString("Total_customer_count"));
                    tx_pendingbills_value.setText(j_obj.getString("Total_pending_bills"));
                    tx_orders_value.setText(j_obj.getString("Total_orders"));
                    tx_sales_value.setText(j_obj.getString("Total_Sales"));
                    tx_returns_value.setText(j_obj.getString("Total_returns"));
                    // tx_inv_value.setText(j_obj.getString("Total_Inventory_count"));
//                    text_int_total_vallue = j_obj.getString("Total_Inventory_count");
//                    b.putString("Total_Inventory_count", j_obj.getString("Total_Inventory_count"));
                }

                if (f_name.equals(AppConfig.GET_USER_OFFERS)) {
                    ((ChemistDashboardFragment) o_DashboardFragment).create_offer_sliding(response.toString());
                    response.toString();
                }

                if (f_name.equals(AppConfig.DISTRIBUTOR_DASHBOARD_DAYWISE_DATA) || f_name.equals(AppConfig.DISTRIBUTOR_DASHBOARD_WEEKWISE_DATA) || f_name.equals(AppConfig.DISTRIBUTOR_DASHBOARD_MONTHWISE_DATA)) {
                    try {
                        ((DistributorDashboardFragment) o_DashboardFragment).getRequest(response.toString(), f_name);
                        response.toString();
                    } catch (Exception e) {
                        //Log.d("Excep", e.getMessage());
                    }
                }

                if (f_name.equals(AppConfig.GET_DISTRIBUTOR_MONTH_GRAPH)) {
                    //Log.d("Graph",response.toString());
                    ((DistributorDashboardFragment) o_DashboardFragment).getRequest(response.toString(), f_name);
                    response.toString();
                }

            } catch (Exception e) {
                e.toString();
            }
        }

    }


    @Override
    public void onFragmentLoaded(TextView tx_customercount_value, TextView tx_pendingbills_value, TextView tx_orders_value,
                                 TextView tx_sales_value, TextView tx_returns_value, TextView tx_inv_value) {
        this.tx_customercount_value = tx_customercount_value;
        this.tx_pendingbills_value = tx_pendingbills_value;
        this.tx_orders_value = tx_orders_value;
        this.tx_sales_value = tx_sales_value;
        this.tx_returns_value = tx_returns_value;
        this.tx_inv_value = tx_inv_value;
        tx_customercount_value.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getApplicationContext()).load(CLIENT_PROFILE_IMAGE).asBitmap().placeholder(R.drawable.genie).centerCrop().into(new BitmapImageViewTarget(iv_profile_pic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                iv_profile_pic.setImageDrawable(circularBitmapDrawable);
            }
        });

        VersionCheck();
       // new GetVersionCode().execute();
        new checkInternetConn().execute(0);
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    public void UpdateDialog() {
        new get_current_location_ondashboard(MainActivity.this);
    }

    /* onPause */
    @Override
    protected void onPause() {
        super.onPause();
        //new checkInternetConn().execute(0);
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }


    /* onDestroy */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // new checkInternetConn().execute(0);
        SharedPreferences.Editor editor = getSharedPreferences("OrderGenieSha", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
    /* Check Internet Connection Class */
    private class checkInternetConn extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Thread.sleep(params[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (InternetConnection.checkConnection(getApplication())) {
                if (login_type != null) {
                    if (login_type.equals(CHEMIST)) {
                        Intent download_intent = new Intent(RefreshData.ACTION_CONFIRM_ORDER_CHEMIST, null, MainActivity.this, RefreshData.class);
                        startService(download_intent);
                    } else {
                        Intent download_intent = new Intent(RefreshData.ACTION_CONFIRM_ORDER, null, MainActivity.this, RefreshData.class);
                        startService(download_intent);
                        getSavedDelivery();
                    }
                }
            }
            if (!ischeckedInternetConn) {
                new checkInternetConn().execute(30000);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }
    }


    /* Check Internet Connection on Current State */
    public static class InternetConnection {
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                }
            }
            return false;
        }
    }

    /* Get SQLite Saved Delivery */
    private void getSavedDelivery() {
        Cursor cursor = sqLiteHandler.getSavedDeliveryOffline(User_id, "");
        int cursorCount = cursor.getCount();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String Delivery_docno = "";
        if (cursorCount > 0) {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        Delivery_docno = cursor.getString(2);
                        jsonObject.put("Delivery_doc_no", Delivery_docno);
                        jsonObject.put("Delivery_date", cursor.getString(7));
                        jsonObject.put("Order_Image", cursor.getString(8));
                        jsonObject.put("Sign_Image", cursor.getString(9));
                        jsonObject.put("Latitude", cursor.getString(10));
                        jsonObject.put("Longitude", cursor.getString(11));
                        jsonObject.put("Status", cursor.getString(5));
                        strDescription = cursor.getString(12);
                        jsonObject.put("Description", strDescription);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Cursor cursor1 = sqLiteHandler.getSavedDeliveryInvoicesOffline(Delivery_docno);
                    JSONObject jsonObject1 = null;
                    JSONArray jsonArray1 = null;
                    if (cursor1.getCount() > 0) {

                        jsonArray1 = new JSONArray();
                        jsonObject1 = new JSONObject();
                        if (cursor1.moveToFirst()) {
                            do {
                                try {
                                    String delivery_id = cursor1.getString(7);
                                    // String invoiceNo = cursor1.getString(4);
                                    jsonObject1.put("DeliveryId", delivery_id);
                                    jsonArray1.put(jsonObject1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } while (cursor1.moveToNext());
                        }
                    }
                    try {
                        jsonObject.put("Delivery_key", jsonArray1);
                        jsonArray.put(jsonObject);
                        onlineDeliveryAPI(jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } while (cursor.moveToNext());
            }
        }
        else{
            ischeckedInternetConn = true;
        }
    }

    /* Online Payment API */
    private void onlineDeliveryAPI(JSONArray jsonArray) {
        String url = AppConfig.SAVE_ORDER_DELIVERY_SALESMAN;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //   Utility.hideProgress();
                if (response != null) {
                    try {
                        String status = response.getJSONObject(0).getString("result");
                        if (status.equalsIgnoreCase("Failed")) {
                        } else if (status.equalsIgnoreCase("Success")) {
                            for (int i = 0; i < response.length(); i++) {
                                ischeckedInternetConn = true;
                                String Doc_ID = response.getJSONObject(i).getString("Delivery_doc_no");
                                sendDeliveryLocation(Doc_ID);
                                String InvoiceNo = response.getJSONObject(i).getString("DeliveryId");
                                String Flag2 = response.getJSONObject(i).getString("Status");
                                sqLiteHandler.updateFlagCollectInvoicesSalesman(InvoiceNo, Flag2);
                                sqLiteHandler.updateFlagDeliveryDescription(InvoiceNo, strDescription);
                                sqLiteHandler.deleteDeliverySalesmanByDocid(Doc_ID);
                                sqLiteHandler.deleteDeliverySelectedInvoiceByDocid(Doc_ID);
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Utility.hideProgress();
                //   Toast.makeText(SaveDeliveryActivity.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                //  finish();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessKey);
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    public void sendDeliveryLocation(String docid){
        Cursor cursor= sqLiteHandler.getLocationDatabyDocId(docid);
        Log.e("Ddocid",docid);
        JSONObject jsonParams = new JSONObject();


        if (cursor.getCount() > 0) {


            if (cursor.moveToFirst()) {
                do {

                    try {
                        jsonParams.put("UserID", cursor.getString(cursor.getColumnIndex(RefreshData.Key_USERID)));
                        jsonParams.put("CustID", Integer.valueOf(cursor.getString(cursor.getColumnIndex(RefreshData.Key_CUSTID))));
                        jsonParams.put("task", cursor.getString(cursor.getColumnIndex(RefreshData.Key_TASK)));
                        jsonParams.put("CurrentLocation", cursor.getString(cursor.getColumnIndex(RefreshData.Key_Address)));
                        jsonParams.put("DOC_NO", cursor.getString(cursor.getColumnIndex(RefreshData.F_KEY_CC_DOC_NO)));
                        jsonParams.put("Tran_No", cursor.getString(cursor.getColumnIndex(RefreshData.Key_TRANSACTION_ID)));
                        jsonParams.put("Latitude", cursor.getString(cursor.getColumnIndex(RefreshData.Key_LATITUDE)));
                        jsonParams.put("Longitude", cursor.getString(cursor.getColumnIndex(RefreshData.Key_LONGITUDE)));
                        jsonParams.put("unqid", cursor.getString(cursor.getColumnIndex(RefreshData.Key_UNIQ_ID)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } while (cursor.moveToNext());
            }
            Intent download_intent = new Intent(this, SendingLocationOnByOrder.class);
            download_intent.putExtra("doc_id", jsonParams.toString());
            startService(download_intent);
        }

    }
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

            progressDialog = ProgressDialog.show(MainActivity.this, "Please Wait", "Version Checking.... ", true);
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
                    // redirectStore("");
                }
            }

        }
    }

    public int VersionReturn(String version){
        int Appstoreverion=0;
        if(version!=null&&version.trim().length()>0) {


            String splitVersion = version.replace(".", "");

            Appstoreverion  = Integer.parseInt(splitVersion);
            return Appstoreverion;
        }else{
            return Appstoreverion;
        }
    }
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please, update app to new version to continue reposting.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        showDialog();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        setFinishOnTouchOutside(false);

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
    void VersionCheck(){
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

            int playStoreVersion = VersionReturn(globalVariable.getPlayStoreAppVersion());
            int myversion=VersionReturn(currentVersion);
            if(playStoreVersion!=0) {
                if (playStoreVersion > myversion) {
                    //Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
                    showUpdateDialog();
                    //finish();
                    // redirectStore("");
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
