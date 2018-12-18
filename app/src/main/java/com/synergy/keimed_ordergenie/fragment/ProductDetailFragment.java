package com.synergy.keimed_ordergenie.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONObject;

import com.synergy.keimed_ordergenie.R;

public class ProductDetailFragment extends Fragment {


    private static final String MEMBERID = "";
    private static final String ARG_POSITION = "position";
    private ProgressDialog pDialog;
    private  JSONObject j_data;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;

    public static ProductDetailFragment newInstance(int position) {

        ProductDetailFragment f = new ProductDetailFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView ;
        rootView = inflater.inflate(R.layout.fragment_product_page_detail, container, false);
       // initImageLoader();
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        initImageLoader();
        try {
            String json_data = getArguments().getString("j_obj");
            getArguments().remove("j_obj");
            if(json_data!=null) {
                j_data = new JSONObject(json_data);
            }



        final TextView txt_company_name  = (TextView) rootView.findViewById(R.id.txt_company_name);
        final TextView ProductName  = (TextView) rootView.findViewById(R.id.ProductName);
        final TextView txt_price   = (TextView) rootView.findViewById(R.id.txt_price);
        final TextView txt_form          = (TextView) rootView.findViewById(R.id.txt_form);
        final TextView txt_pack_size    = (TextView) rootView.findViewById(R.id.txt_pack_size);
        final ImageView thumbnail          =(ImageView)rootView.findViewById(R.id.thumbnail);


            ProductName.setText(j_data.getString("Itemname"));
            txt_form.setText(j_data.getString("DoseForm"));
            txt_company_name.setText(j_data.getString("MfgName"));

            imageLoader.displayImage(j_data.getString("ImagePath"), thumbnail);
            txt_price.setText(j_data.getString("Rate"));
            txt_pack_size.setText(j_data.getString("Packsize"));
        thumbnail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
             image_zoom();
            }
        });
        }catch (Exception e)
        {
           e.toString();
        }

        return rootView;
    }



    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                getActivity()).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }



    private  void image_zoom()
    {
        //Intent Intenet_imageZoom = new Intent(getActivity(), ImageZoomProcduct.class);
        //Intenet_imageZoom.putExtra("imagepath", imgpath);

       // startActivity(Intenet_imageZoom);
    }


}