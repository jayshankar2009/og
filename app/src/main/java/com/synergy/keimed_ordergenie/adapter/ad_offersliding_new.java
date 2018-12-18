package com.synergy.keimed_ordergenie.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.ProductDetail;
import com.synergy.keimed_ordergenie.model.m_offerlist;
import com.synergy.keimed_ordergenie.utils.ScrollerCustomDuration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1132 on 07-09-2016.
 */
public class ad_offersliding_new extends PagerAdapter {
    Context _mContext;
    List<m_offerlist> _datalist;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat sdf = new SimpleDateFormat("LLL,dd yyyy");
    private ScrollerCustomDuration mScroller = null;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private ArrayList<Integer> IMAGES;

    public ad_offersliding_new(Context _mContext ,ArrayList<Integer> images) {
        //      this._datalist = _datalist;
        this._mContext = _mContext;
        this.IMAGES = images;
        initImageLoader();
        //  postInitViewPager();
    }

    @Override
    public int getCount() {

        //Log.d("Offers_size", String.valueOf(_datalist.size()));

        return IMAGES.size();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        LayoutInflater inflater = (LayoutInflater)_mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_ofeer_sliding_new, container, false);



        ImageView mImageView = (ImageView) view.findViewById(R.id.offer_img);
        TextView product_name = (TextView) view.findViewById(R.id.product_name);
        //TextView offerDetails = (TextView) view.findViewById(R.id.offerDetails);
       // TextView txt_validity = (TextView) view.findViewById(R.id.txt_validity);



        // m_offerlist o_offerlist=_datalist.get(position);
//         if (o_offerlist.getMainImagePath()!=null) {
//             imageLoader.displayImage(o_offerlist.getMainImagePath(), mImageView);
//         }
        mImageView.setImageResource(IMAGES.get(position));
        // product_name.setText(o_offerlist.getProduct_Desc());
        // offerDetails.setText(o_offerlist.getOfferDetails());


//        try{
//            txt_validity.setText(sdf.format(dateFormat.parse(o_offerlist.getStartDate()))+" - "+sdf.format(dateFormat.parse(o_offerlist.getEndDate())));
//
//        }catch (Exception e)
//        {
//
//        }

        //txt_validity.setText(o_offerlist.getValidity());

        // final  String pro_id=o_offerlist.getProduct_ID();
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i =new Intent(_mContext, ProductDetail.class);
//                i.putExtra("product_id",Integer.parseInt(pro_id));
//                _mContext.startActivity(i);
//            }
//        });

        container.addView(view);
        return view;
    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        //See if object from instantiateItem is related to the given view
        //required by API
        return arg0 == (View) arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
        object = null;
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                _mContext).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }

   /* private void postInitViewPager() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerCustomDuration(_mContext,
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    *//**
     * Set the factor by which the duration will change
     *//*
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }
*/

}
