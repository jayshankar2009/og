package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1132 on 27-07-2016.
 */
public class m_inventory_items extends BaseObservable {


    String Product_Code;
   // String Packsize;
    Float Mrp;
    Float Rate;
    //Double Mrp;
    //Double Rate;
    String MfgCode;
    String MfgName;
    String Image_path;
    String HalfScheme;
    String PercentScheme;




    public m_inventory_items(Float Mrp,
                             Float Rate,
                             String MfgCode,
                             String MfgName, String HalfScheme, String PercentScheme) {
      //  this.Product_Code = Product_Code;
     //  this.Packsize = Packsize;
        this.Mrp = Mrp;
        this.Rate = Rate;
        this.MfgCode = MfgCode;
        this.MfgName = MfgName;
       // this.Image_path = Image_path;
        this.HalfScheme=HalfScheme;
        this.PercentScheme=PercentScheme;
    }

    @Bindable
    public String getProduct_Code() {
        return Product_Code;
    }

    @Bindable
    public String getImage_path() {
        return Image_path;
    }

//    @Bindable
//    public String getPacksize() {
//        return Packsize;
//    }

    @Bindable
    public Float getMrp() {
        return Mrp;
    }

    @Bindable
    public Float getRate() {
        return Rate;
    }

    @Bindable
    public String getMfgCode() {
        return MfgCode;
    }

    @Bindable
    public String getMfgName() {
        return MfgName;
    }

    @Bindable
    public String getHalfScheme() {
        return HalfScheme;
    }

    @Bindable
    public String getPercentScheme() {
        return PercentScheme;
    }
}
