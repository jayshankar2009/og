package com.synergy.keimed_ordergenie.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 1132 on 22-12-2016.
 */

public class OGtoast {

    public  static void  OGtoast(String Message, Context ctx)
    {

        Toast.makeText(ctx,Message,Toast.LENGTH_SHORT).show();

       // Toast toast=new Toast(ctx);
       // TextView txt= new TextView(ctx);
       // txt.setPadding(15,10,15,10);
       // txt.setBackground(ctx.getResources().getDrawable(R.drawable.curve_green_background));
       // txt.setTextColor(ctx.getResources().getColor(R.color.white));
       // txt.setText(Message);
      //  toast.setView(txt);
       // toast.(Message);
        //toast.setDuration(Toast.LENGTH_SHORT);
        //toast.show();
    }
}
