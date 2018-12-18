package com.synergy.keimed_ordergenie.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.m_customerlist_new;
import com.synergy.keimed_ordergenie.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 8/22/2018.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<m_customerlist_new> contactList;
    private List<m_customerlist_new> contactListFiltered;
    private ContactsAdapterListener listener;
    String constraint1,constraint2;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, amount,custcode,drugExpiry;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tx_customername);
            amount = (TextView) view.findViewById(R.id.tx_outstanding);
            custcode = (TextView) view.findViewById(R.id.tx_customercode);
          //  drugExpiry = (TextView) view.findViewById(R.id.tx_drug_expiry);
           // thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback

                    if (contactListFiltered!=null) {

                        listener.onContactSelected(contactListFiltered.get(getAdapterPosition()), view, getAdapterPosition());
                       // Toast.makeText(context, "Clikced on" + contactListFiltered.get(getAdapterPosition()).getCustomerName(), Toast.LENGTH_SHORT).show();
                    }
                   // show_bottom_sheet(view, contactListFiltered.get(getAdapterPosition()).getCustomerName());
                   // openBottomSheet(view);
                }
            });
        }
    }


    public CustomerListAdapter(Context context, List<m_customerlist_new> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragement_customerlist_items_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final m_customerlist_new contact = contactListFiltered.get(position);
      //  holder.name.setText(contact.getCustomerName());

        String dlEx=String.valueOf(contact.getDLExpInd());
       // String strReplace = dlEx.substring(0, dlEx.indexOf("T"));

        holder.name.setText((contact.getCustomerName() == null) ? "NA" : contact.getCustomerName());
        holder.amount.setText((contact.getOutstanding_Bill() == null) ? "Rs.0" : "Rs."+contact.getOutstanding_Bill());
        holder.custcode.setText((contact.getCust_Code() == null) ? "NA" : contact.getCust_Code());
        // holder.drugExpiry.setText((contact.getDLExpiry() == null) ? "Drug License Expiry Date:NA" : "Drug License Expiry Date:" + contact.getDLExpiry());
      //  holder.drugExpiry.setText("Drug License Expiry Date:"+contact.getDLExpiry());

        //
      //  Log.i("Chemist List date",""+contact.getDLExpiry().equals(null));
       // if(contact.getDLExpiry().equals("NA") || contact.getDLExpiry().equals("")) {
              // } else {
         //   String strReplace = contact.getDLExpiry().substring(0, contact.getDLExpiry().indexOf("T"));

       // }
        Log.i("Chemist List",""+dlEx);

//        Glide.with(context)
//                .load(contact.getImage())
//                .apply(RequestOptions.circleCropTransform())
//                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {

      //  return contactListFiltered.size();
        return (contactListFiltered == null) ? 0 : contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charString = charSequence.toString();
              //  Log.d("charString",charString);
                if(charString.contains(" ")){
                    String [] arrOfStr = charString.split(" ");
                    constraint1= arrOfStr[0];
                    constraint2=arrOfStr[1];
                }
                else{
                    constraint1= charString;
                    constraint2=" ";
                }

                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<m_customerlist_new> filteredList = new ArrayList<>();
                    for (m_customerlist_new row : contactList)
                    {
                       // Log.d("customer11",row.getCustomerName());
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
//                        if (row.getCustomerName()!=null) {
//                            if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase())) {
//                                Log.d("customer12", row.getCustomerName());
//                                filteredList.add(row);
//                            }
//                        }

                        if ((row.getCustomerName() != null && row.getCustomerName().replace("-", "").replace(" ", "").replace("(", "").replace(")", "")
                                .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "").replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase()
                                // .matches(constraint.toString().replace(" ","").replace("-","")
                                .contains(constraint1.replace(" ", "").replace("-", "")
                                        //.contains(constraint.toString().replace(" ","").replace("-","")
                                        .replace("(", "").replace(")", "")
                                        .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "")
                                        .replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase())
                        )&&(row.getCustomerName() != null && row.getCustomerName().replace("-", "").replace(" ", "").replace("(", "").replace(")", "")
                                .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "").replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase()
                                .contains(constraint2.replace(" ","").replace("-","")
                                        .replace("(", "").replace(")", "")
                                        .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "")
                                        .replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase())
                        ))


                        {
                            synchronized (this) {
                                filteredList.add(row);

                            }
                        }

                    }
                   contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
               // contactListFiltered = (ArrayList<m_customerlist_new>) filterResults.values;
               // contactListFiltered = (ArrayList<m_customerlist_new>) filterResults.values;
                contactListFiltered = (List<m_customerlist_new>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(m_customerlist_new contact, View v,int position);
    }

    private  void openBottomSheet(View v) {
        //View view = activity.getLayoutInflater ().inflate (R.layout.bottom_sheet, null);
        // View view = inflater.inflate( R.layout.bottom_sheet, null );
        Context context=v.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate (R.layout.activity_app_bar_customerlist_new, null);
        TextView customer_name = (TextView)view.findViewById(R.id.btm_text_cust_name);
        LinearLayout view_profile = (LinearLayout)view.findViewById(R.id.lnr_view_profile);
        LinearLayout pending_bills = (LinearLayout)view.findViewById(R.id.lnr_view_pending_bills);
        LinearLayout view_orders = (LinearLayout)view.findViewById( R.id.lnr_view_all_orders);
        LinearLayout view_salesreturn = (LinearLayout)view.findViewById( R.id.lnr_view_sales_return);
        LinearLayout view_location = (LinearLayout)view.findViewById( R.id.lnr_view_up_location);

      //  final Dialog mBottomSheetDialog = new Dialog (context, R.style.MaterialDialogSheet);
        final Dialog mBottomSheetDialog = new Dialog (context, R.style.AppTheme);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.show ();


        view_orders.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"view_orders",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });

        view_profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"view_profile",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });

        pending_bills.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"pending_bills",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });

        customer_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"customer_name",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });


        view_salesreturn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"view_salesreturn",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });

        view_location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"view_salesreturn",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });

    }
}
