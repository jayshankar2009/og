package com.synergy.keimed_ordergenie.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.app.AppConfig;
import com.synergy.keimed_ordergenie.utils.MakeWebRequest;

import static com.synergy.keimed_ordergenie.utils.ConstData.user_info.CLIENT_ID;
import static com.synergy.keimed_ordergenie.utils.SessionManager.PREF_NAME;

public class ChemistProductDetail extends AppCompatActivity implements  MakeWebRequest.OnResponseSuccess {

    private String product_id;
    private String Chemist_id;
    private SharedPreferences pref;

    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;

    @BindView(R.id.txt_mfg)
    TextView txt_mfg;

    @BindView(R.id.txt_dosage)
    TextView txt_dosage;

    @BindView(R.id.txt_packsize)
    TextView txt_packsize;

    @BindView(R.id.txt_ptr)
    TextView txt_ptr;

    @BindView(R.id.txt_mrp)
    TextView txt_mrp;

    @BindView(R.id.txt_medicine_name)
    TextView txt_medicine_name;

    @BindView(R.id.med_image)
    ImageView med_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemist_product_detail);
        ButterKnife.bind(this);
        initImageLoader();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        Chemist_id=pref.getString(CLIENT_ID,"");

        product_id=String.valueOf(getIntent().getIntExtra("product_id",0));

        MakeWebRequest.MakeWebRequest("get", AppConfig.GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DETAIL,
                AppConfig.GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DETAIL + "["+product_id+","+Chemist_id+"]", this, true);

    }

    @Override
    public void onSuccess_json_object(String f_name, JSONObject response) {

    }

    @Override
    public void onSuccess_json_array(String f_name, JSONArray response) {

        if(response!=null)
        {
            try {


                if (f_name.equals(AppConfig.GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DETAIL)) {

                    getSupportActionBar().setTitle(response.getJSONObject(0).getString("Itemname"));

                    txt_mfg.setText(response.getJSONObject(0).getString("MfgName"));
                    txt_dosage.setText(response.getJSONObject(0).getString("DoseForm"));
                    txt_packsize.setText(response.getJSONObject(0).getString("Packsize"));
                    txt_ptr.setText(response.getJSONObject(0).getString("Rate"));
                    txt_mrp.setText(response.getJSONObject(0).getString("MRP"));

                    txt_medicine_name.setText(response.getJSONObject(0).getString("Itemname"));

                    imageLoader.displayImage(response.getJSONObject(0).getString("ImagePath"), med_image);
                    }


            }catch (Exception e)
            {

            }
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
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }

}
