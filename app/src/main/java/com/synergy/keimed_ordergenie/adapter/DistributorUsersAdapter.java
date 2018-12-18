package com.synergy.keimed_ordergenie.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.DistributorCallPlan;
import com.synergy.keimed_ordergenie.activity.DistributorUsersModal;

import java.util.List;

/**
 * Created by Admin on 30-01-2018.
 */

public class DistributorUsersAdapter extends RecyclerView.Adapter<DistributorUsersAdapter.MyViewHolder> {


    List<DistributorUsersModal> distributorUsersList;
    Context context;
    Boolean user_Take_Order,user_Payment,user_Delivery;
    ImageView imageView,img_takeorder,img_payment,img_Delivery;
    SharedPreferences pref;


    public DistributorUsersAdapter(List<DistributorUsersModal> distributorUsersList, Context context) {
        this.distributorUsersList = distributorUsersList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView usersname_text;
        LinearLayout linearLayout;
        public MyViewHolder(View view) {
            super(view);
            usersname_text = (TextView) view.findViewById(R.id.usersname_text);
            imageView = (ImageView) view.findViewById(R.id.img_users);
            img_takeorder = (ImageView) view.findViewById(R.id.img_takeorder);
            img_payment = (ImageView) view.findViewById(R.id.img_payment);
            img_Delivery = (ImageView) view.findViewById(R.id.img_Delivery);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context, DistributorCallPlan.class);
                    i.putExtra("user_id",distributorUsersList.get(getAdapterPosition()).get_id());
                    i.putExtra("user_task",distributorUsersList.get(getAdapterPosition()).getUserTasks());
                    context.startActivity(i);
                }
            });
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userslist_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {

//            pref = context.getSharedPreferences("DISTRIBUTOR", MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();
//            editor.remove(IS_USER_ORDER);
//            editor.remove(IS_USER_DELIVERY);
//            editor.remove(IS_USER_PAYMENT);

            DistributorUsersModal distributorUsersModal = distributorUsersList.get(position);

            String userName = distributorUsersModal.getFull_Name();

            String image = distributorUsersModal.getProfileImg();
            String userTask = distributorUsersModal.getUserTasks();





                if (userTask.contains("1")) {
                  //  editor.putBoolean(IS_USER_ORDER, true);
                    user_Take_Order=true;
                }
                else
                {
                    user_Take_Order=false;

                }


                if (userTask.contains("2")) {
                    //editor.putBoolean(IS_USER_PAYMENT, true);
                    user_Payment=true;
                }
                else
                {
                    user_Payment=false;
                }

                if (userTask.contains("3")) {
                    //editor.putBoolean(IS_USER_DELIVERY, true);
                    user_Delivery=true;

                }
                else
                {
                    user_Delivery=false;
                }
//
//            editor.commit();
            Picasso.with(context).load(image).into(imageView);

            holder.usersname_text.setText(userName);
            Resources res = context.getResources();
            //Log.d("userName",userName);



            //if(userTask != null){




                /*if(userTask.contains("1")){

                    final Drawable drawable = res.getDrawable(R.drawable.order_icon);
                    img_takeorder.setImageDrawable(drawable);

                }

                if(userTask.contains("2")){

                    final Drawable drawable = res.getDrawable(R.drawable.rupee_icon);
                    img_payment.setImageDrawable(drawable);

                }

                if(userTask.contains("3")){

                    final Drawable drawable = res.getDrawable(R.drawable.delivery_icon);
                    img_Delivery.setImageDrawable(drawable);

                }*/
            //}

           // user_Take_Order,user_Payment,user_Delivery

//            user_Take_Order = true;
//            user_Payment = true;
//            user_Delivery = true;

//            Log.d("userDeails11", String.valueOf(pref.getBoolean(IS_USER_ORDER,false)));
//            Log.d("userDeails12", String.valueOf(pref.getBoolean(IS_USER_PAYMENT,false)));
//            Log.d("userDeails13", String.valueOf(pref.getBoolean(IS_USER_DELIVERY,false)));

//            if (distributorUsersModal.getFull_Name().equals("hari"))
//            {
//                Log.d("Hari",userTask);
//                Log.d("order",String.valueOf(pref.getBoolean(IS_USER_ORDER,false)));
//                Log.d("order12",String.valueOf(pref.getBoolean(IS_USER_PAYMENT,false)));
//                Log.d("order13",String.valueOf(pref.getBoolean(IS_USER_DELIVERY,false)));
//            }
            if(user_Take_Order){

                    final Drawable drawable = res.getDrawable(R.drawable.order_icon);
                    img_takeorder.setImageDrawable(drawable);


            }else {

                final Drawable drawable = res.getDrawable(R.drawable.order_inactive);
                img_takeorder.setImageDrawable(drawable);
                //user_Take_Order = false;

            }



            //2

            if(user_Payment){

                    final Drawable drawable = res.getDrawable(R.drawable.rupee_icon);
                    img_payment.setImageDrawable(drawable);


            }else {

                final Drawable drawable = res.getDrawable(R.drawable.rupee_inactive);
                img_payment.setImageDrawable(drawable);
//                user_Payment = false;

            }

            //3


            if(user_Delivery){

                    final Drawable drawable = res.getDrawable(R.drawable.delivery_icon);
                    img_Delivery.setImageDrawable(drawable);

            }else {

                final Drawable drawable = res.getDrawable(R.drawable.delivery_inactive);
                img_Delivery.setImageDrawable(drawable);
  //              user_Delivery = false;

            }





        }catch (Exception e){


        }

    }

    @Override
    public int getItemCount() {
        return distributorUsersList.size();
    }
}
