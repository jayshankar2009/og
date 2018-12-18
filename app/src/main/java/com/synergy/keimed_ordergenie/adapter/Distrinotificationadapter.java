package com.synergy.keimed_ordergenie.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.Distributornotificationmodal;

import java.util.List;
import java.util.Random;

/**
 * Created by Admin on 05-02-2018.
 */

public class Distrinotificationadapter extends RecyclerView.Adapter<Distrinotificationadapter.MyViewHolder> {

    private List<Distributornotificationmodal> notificationList;
    char part1,part2;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textview_notificationname, textview_notificationtime, textview_notificationprize,distribtcircle_textview;

        public MyViewHolder(View view) {
            super(view);


            textview_notificationname = (TextView) view.findViewById(R.id.textview_notificationname);
            textview_notificationtime = (TextView) view.findViewById(R.id.textview_notificationtime);
            textview_notificationprize = (TextView) view.findViewById(R.id.textview_notificationprize);
            distribtcircle_textview = (TextView) view.findViewById(R.id.distribtcircle_textview);

        }
    }


    public Distrinotificationadapter(List<Distributornotificationmodal> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.distributornotificationadapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Distributornotificationmodal distributornotificationmodal = notificationList.get(position);
        holder.textview_notificationname.setText(distributornotificationmodal.getChemistName());
        holder.textview_notificationtime.setText(distributornotificationmodal.getNotificationTiming());
        holder.textview_notificationprize.setText(distributornotificationmodal.getGetNotificationPrize());
//distribtcircle_textview

        String notificationName = distributornotificationmodal.getChemistName() ;

       String cName[] = notificationName.split("");


//        if (cName.length == 1) {
//            part1 = cName[0].charAt(0);
//            holder.distribtcircle_textview.setText(String.valueOf(part1));
//        } else {
//            if (cName.length > 1) {
//                part1 = cName[0].charAt(0);
//                part2 = cName[1].charAt(0);
//                holder.distribtcircle_textview.setText(String.valueOf(part1) + String.valueOf(part2));
//            }
//        }

      //  Log.d("notificationName", String.valueOf(cName));


        Random r = new Random();
        int red = r.nextInt(255 - 0 + 1) + 0;
        int green = r.nextInt(255 - 0 + 1) + 0;
        int blue = r.nextInt(255 - 0 + 1) + 0;
        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.OVAL);
        draw.setColor(Color.rgb(red, green, blue));
        holder.distribtcircle_textview.setBackground(draw);

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
