package com.synergy.keimed_ordergenie.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.app.AppController;
import com.synergy.keimed_ordergenie.fragment.ProfileShowFragment;
import com.synergy.keimed_ordergenie.fragment.profileEditFragment;
import com.synergy.keimed_ordergenie.utils.CompatibilityUtil;
import com.synergy.keimed_ordergenie.utils.ConstData;
import com.synergy.keimed_ordergenie.utils.ImageUtils;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;
import com.synergy.keimed_ordergenie.utils.get_imie_number;

import static com.synergy.keimed_ordergenie.utils.ConstData.Login_type.CHEMIST;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ADRESS;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_CITY_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_MOBILE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_NAME;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_PROFILE_IMAGE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ROLE;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ROLE_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.EMAIL_ID;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_ORDER_DELIVERY_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_PAYMENT_COLLECTION_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.IS_TAKE_ORDER_ASSIGNED;
import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.USER_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class ProfileActivity extends AppCompatActivity implements MakeWebRequest.OnResponseSuccess {

    AppController globalVariable;
    MenuItem menu_item_save;
    SharedPreferences pref;
    private TransitionManager mTransitionManager;
    private ImageLoader imageLoader;
    DisplayImageOptions options;

    private String encoded_image;

    @BindView(R.id.toolbar)
    Toolbar _toolbar;


    @BindView(R.id.prof_img)
    ImageView prof_img;


    Fragment page;
    String login_type;
    private Scene mScene1;
    private Scene mScene2;
    File destination;
    ViewGroup container;
    public static final int MY_PERMISSIONS_REQUEST_PHONE_STATE = 125;
    private String IMEI = "";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 300;

    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    int PERMISSION_ALL = 1;

    @BindView(R.id.main_heading_txt)

    TextView main_heading_txt;


    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout _toolbar_layout;

    @BindView(R.id.fab)
    FloatingActionButton _fab;


    @OnClick(R.id.fab)
    void onfabclick() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            show_profile_pic_change_dialog();
        }


    }

    @OnClick(R.id.fab_bills)
    void show_pendingbills() {

        if (login_type.equals(CHEMIST)) {

            Intent intent = new Intent(ProfileActivity.this, AllPendingBills.class);
            startActivity(intent);
        } else {

            Intent intent = new Intent(ProfileActivity.this, IndividualPendingBillsActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.fab_order)
    void show_order() {
        if (login_type.equals(CHEMIST)) {

            Intent intent = new Intent(ProfileActivity.this, Order_list.class);
            startActivity(intent);
        } else {


            Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.fab_sales)
    void show_sales() {
        Intent intent = new Intent(ProfileActivity.this, SalesReturnActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this ,R.color.colorPrimaryDark));
        }

        globalVariable = (AppController) getApplicationContext();
        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        setSupportActionBar(_toolbar);

        String nameCheck = getIntent().getStringExtra("sendName");


        initImageLoader();
        String name = "Og_profile";
        destination = new File(Environment
                .getExternalStorageDirectory(), name + ".jpg");
        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(1000)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        IMEI = new get_imie_number().check_imie_permission(this);
        login_type = pref.getString(ConstData.user_info.CLIENT_ROLE, "");


        container = (ViewGroup) findViewById(R.id.main_centerscreen);
        // TransitionInflater transitionInflater = TransitionInflater.from(this);
        //  mTransitionManager = transitionInflater.inflateTransitionManager(R.transition.transition_manager, container);
        // mScene1 = Scene.getSceneForLayout(container, R.layout.fragment_profile_show, this);
        // mScene2 = Scene.getSceneForLayout(container, R.layout.fragment_profile_edit, this);
        // set_data();

        //_toolbar_layout.setTitle(pref.getString(ConstData.user_info.CLIENT_NAME, ""));
        _toolbar_layout.setTitle(pref.getString(ConstData.user_info.CLIENT_FULL_NAME, ""));
        main_heading_txt.setText(pref.getString(ConstData.user_info.CLIENT_ROLE, ""));
        ProfileShowFragment fragment = new ProfileShowFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_centerscreen, fragment).commit();

        imageLoader.displayImage(pref.getString(CLIENT_PROFILE_IMAGE, ""), prof_img, options);
        if (!login_type.equals(CHEMIST)) {

            stockist.setVisibility(View.GONE);

        }
    }

    @BindView(R.id.fab_stockist_list)
    com.github.clans.fab.FloatingActionButton stockist;

    @OnClick(R.id.fab_stockist_list)
    void show_Stockist() {
        Intent intent = new Intent(ProfileActivity.this, StockistList.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);

        menu_item_save = menu.findItem(R.id.action_edit);

        if (globalVariable.getCall_plan_Started()) {
            Log.d("CALLPLAN11", "YES CALPLAN");
            menu_item_save.setVisible(false);
        } else {
            Log.d("CALLPLAN12", "NO CALPLAN");
            menu_item_save.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_edit:

                if (menu_item_save.getTitle().toString().equals("Edit")) {
                    profileEditFragment fragment = new profileEditFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_centerscreen, fragment, "editfragment");
                    // transaction.addToBackStack("showfragment");
                    transaction.commit();

                    //  goToScene2(findViewById(R.id.action_edit));

                    menu_item_save.setTitle("Save");
                    _fab.setVisibility(View.VISIBLE);

                } else {

                    page = getSupportFragmentManager().findFragmentByTag("editfragment");


                    if (page != null) {
                        try {
                            if (encoded_image != null) {
                                JSONObject j_obj = new JSONObject();
                                //JSONObject j_obj = new JSONObject();
                                j_obj.put("data", encoded_image);
                                Log.d("IMAGEENCODE11", j_obj.toString());
                           /* MakeWebRequest.MakeWebRequest("Post", AppConfig.SAVE_IMAGE_PROFILE, AppConfig.SAVE_IMAGE_PROFILE,
                                    new JSONArray().put(j_obj), this,false,"");*/
                                MakeWebRequest.MakeWebRequest("Post", AppConfig.SAVE_IMAGE_PROFILE, AppConfig.SAVE_IMAGE_PROFILE, j_obj, this, true);
                            } else {
                                ((profileEditFragment) page).save_profile("");
                                ProfileShowFragment fragment = new ProfileShowFragment();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.main_centerscreen, fragment, "showfragment");
                                //    transaction.addToBackStack("showfragment");
                                transaction.commit();

                                //     goToScene1(findViewById(R.id.action_edit));

                                menu_item_save.setTitle("Edit");


                                _fab.setVisibility(View.INVISIBLE);
                            }


                        } catch (Exception e) {

                        }

                    }


                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public Intent getSupportParentActivityIntent() {

        Intent newIntent = null;
        try {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                if (menu_item_save != null) {
                    menu_item_save.setTitle("Edit");
                }
                _fab.setVisibility(View.INVISIBLE);

            } else {
                newIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(newIntent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newIntent;
    }

    @Override
    public void onBackPressed() {
        //
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            if (menu_item_save != null) {
                menu_item_save.setTitle("Edit");
            }
            _fab.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }


    }

    private void show_profile_pic_change_dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_profile_photo, null);
        dialogBuilder.setView(dialogView);

        ImageButton img_gallery = (ImageButton) dialogView.findViewById(R.id.img_gallery);
        ImageButton img_camera = (ImageButton) dialogView.findViewById(R.id.img_camera);


        final AlertDialog alertDialog = dialogBuilder.create();


        img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browse_intenet = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(browse_intenet, GALLERY_IMAGE_REQUEST_CODE);
                alertDialog.dismiss();


            }
        });

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(destination));
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                alertDialog.dismiss();

            }
        });


        //alertDialog.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,100));
        // alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.height = 350;
        ///lp.x=-170;
        //lp.y=100;
        alertDialog.getWindow().setAttributes(lp);

    }

  /*  void set_data()
    {
        EditText  input_name = (EditText) container.findViewById(R.id.input_name);
      EditText input_email_id = (EditText) container.findViewById(R.id.input_email_id);
      EditText input_shop_name = (EditText) container.findViewById(R.id.input_shop_name);
      EditText input_adress = (EditText) container.findViewById(R.id.input_adress);
      EditText input_phone = (EditText) container.findViewById(R.id.input_phone);
      EditText input_city = (EditText)  container.findViewById(R.id.input_city);
      EditText input_state = (EditText)  container.findViewById(R.id.input_state);


        input_name.setText(pref.getString(CLIENT_NAME, ""));
        input_email_id.setText(pref.getString(EMAIL_ID, ""));
        input_shop_name.setText(pref.getString(CLIENT_NAME, ""));
        input_adress.setText(pref.getString(CLIENT_ADRESS, ""));
        input_phone.setText(pref.getString(CLIENT_MOBILE, ""));
    }*/

    public void onSuccess_json_object(String f_name, JSONObject response) {

        Log.d("RESPPROFILE11", String.valueOf(response));

        if (response != null) {

            try {

                if (f_name.equals(AppConfig.SAVE_IMAGE_PROFILE)) {

                    if (page != null) {

                        Log.d("RESPPROFILE12", String.valueOf(response));

                        ((profileEditFragment) page).save_profile(response.getString("file"));


                    }
                }
                if (f_name.equals(AppConfig.EDIT_PROFILE)) {

                    if (page != null) {

                        Log.d("RESPPROFILE13", String.valueOf(response));

                        ((profileEditFragment) page).save_profile(response.getString("file"));


                    }
                }


                if (f_name.equals("profile")) {


                    ProfileShowFragment fragment = new ProfileShowFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_centerscreen, fragment, "showfragment");
                    //    transaction.addToBackStack("showfragment");
                    transaction.commit();

                    //     goToScene1(findViewById(R.id.action_edit));

                    menu_item_save.setTitle("Edit");


                    _fab.setVisibility(View.INVISIBLE);


                    String s = "{\"imei\":" + IMEI + "}";

                    //globalVariable.setToken(null);


                    MakeWebRequest.MakeWebRequest("out_array", "GetMe", AppConfig.GET_ME,
                            null, this, true, s);


                    if (response.getBoolean("updated") == true) {
                        new AlertDialog.Builder(this)
                                .setTitle("Profile")
                                .setMessage("Profile updated succesfully.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Profile")
                                .setMessage("Some thing went wrong . Please try again.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    }
                }


            } catch (Exception e) {
                e.toString();
            }
        }


    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        Log.d("RESPPROFILE12", String.valueOf(response));

        if (response != null) {
            try {


                if (f_name.equals("GetMe")) {
                    showmainscreen(response);
                }

            } catch (Exception e) {
                e.toString();
            }
        }


    }

    private void showmainscreen(JSONArray response) {


        try {

            SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

            SharedPreferences.Editor editor = pref.edit();

            editor.putString(CLIENT_ID, response.getJSONObject(0).getString("ClientID"));
            editor.putString(USER_ID, response.getJSONObject(0).getString("_id"));
            editor.putString(CLIENT_NAME, response.getJSONObject(0).getString("Client_LegalName"));
            editor.putString(EMAIL_ID, response.getJSONObject(0).getString("email"));
            editor.putString(CLIENT_ROLE, response.getJSONObject(0).getString("role"));
            editor.putString(CLIENT_MOBILE, response.getJSONObject(0).getString("Mobile_No"));
           // editor.putString(CLIENT_MOBILE, response.getJSONObject(0).getString("Client_Mobile"));
            editor.putString(CLIENT_PROFILE_IMAGE, response.getJSONObject(0).getString("ProfileImage"));
            editor.putString(CLIENT_CITY_ID, response.getJSONObject(0).getString("CityID"));
            editor.putString(CLIENT_ADRESS, response.getJSONObject(0).getString("Location"));
            editor.putString(CLIENT_ROLE_ID, response.getJSONObject(0).getString("Role_ID"));


            if (response.getJSONObject(0).getString("UserTasks").contains("1")) {
                editor.putBoolean(IS_TAKE_ORDER_ASSIGNED, true);
            }
            if (response.getJSONObject(0).getString("UserTasks").contains("2")) {
                editor.putBoolean(IS_PAYMENT_COLLECTION_ASSIGNED, true);
            }
            if (response.getJSONObject(0).getString("UserTasks").contains("3")) {
                editor.putBoolean(IS_ORDER_DELIVERY_ASSIGNED, true);
            }

            //editor.commit();

            editor.apply();


            profileEditFragment fragment = new profileEditFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_centerscreen, fragment, "editfragment");
            // transaction.addToBackStack("showfragment");
            transaction.commit();


        } catch (Exception e) {

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    IMEI = CompatibilityUtil.get_iemi(ProfileActivity.this);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    new get_imie_number().check_imie_permission(this);

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Unnati need  permissions for uploading profile photo!", Toast.LENGTH_LONG)
                                .show();

                    } else {
                        show_profile_pic_change_dialog();
                    }
                }
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    //destination
                    compres_image(destination.getAbsolutePath());
                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    Uri uri = imageReturnedIntent.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    compres_image(picturePath);
                    break;

                }


        }
    }

    private void compres_image(String Imgpath) {
        byte[] ImagBytes = ImageUtils.compressImage(Imgpath);


        try {


            if (destination.exists()) {
                destination.delete();
            }


            FileOutputStream fos = new FileOutputStream(destination);
            fos.write(ImagBytes);
            fos.flush();
            fos.close();

            try {


                encoded_image = Base64.encodeToString(ImagBytes, Base64.DEFAULT);

                imageLoader.clearDiskCache();
                imageLoader.clearMemoryCache();
                imageLoader.clearDiscCache();
                imageLoader.displayImage("file://" + destination, prof_img, options);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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


}
