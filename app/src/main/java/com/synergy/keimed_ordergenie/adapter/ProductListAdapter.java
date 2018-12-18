package com.synergy.keimed_ordergenie.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.synergy.keimed_ordergenie.model.ChemistInventorymodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 9/10/2018.
 */

/*public class ProductListAdapter {
}*/
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<ChemistInventorymodel> contactList;
    private List<ChemistInventorymodel> contactListFiltered;
    private ContactsAdapterListener listener;
    String constraint1,constraint2;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView P_itemname, tx_psizee,tx_pmanufaturename,tx_prate,tx_ptrr,txt_mrp;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            P_itemname = (TextView) view.findViewById(R.id.tx_productitemname);
            tx_psizee = (TextView) view.findViewById(R.id.tx_psize);
            tx_pmanufaturename = (TextView) view.findViewById(R.id.tx_manufacturname);
            tx_prate = (TextView) view.findViewById(R.id.tx_rate);
            tx_ptrr = (TextView) view.findViewById(R.id.tx_ptr);
            txt_mrp = (TextView) view.findViewById(R.id.tx_mrp);
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


    public ProductListAdapter(Context context, List<ChemistInventorymodel> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_catlog, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ChemistInventorymodel contact = contactListFiltered.get(position);
       // final ChemistInventorymodel contact = contactListFiltered.get(position);
       // ChemistInventorymodel order_list = postss.get(position);

      /*  TextView customer_name = (TextView)holder.findViewById(R.id.btm_text_cust_name);
        TextView p_Itemname = (TextView) holder.getBinding().getRoot().findViewById(R.id.tx_productitemname);
        TextView tx_psizee = (TextView) holder.getBinding().getRoot().findViewById(R.id.tx_psize);
        TextView tx_pmanufaturename = (TextView) holder.getBinding().getRoot().findViewById(R.id.tx_manufacturname);
        TextView tx_prate = (TextView) holder.getBinding().getRoot().findViewById(R.id.tx_rate);
        */
if(contact.get_in()== null){
    holder.P_itemname.setText("NA");
}else {
    holder.P_itemname.setText(""+contact.get_in());
}
        //holder.P_itemname.setText((contact.get_lm() == null) ? "NA" : contact.get_lm());
        holder.tx_psizee.setText((contact.get_pz() == null) ? "NA" : contact.get_pz());
        holder.tx_pmanufaturename.setText((contact.get_mn() == null) ? "NA" : contact.get_mn());
       // holder.tx_prate.setText((contact.get_ptr() == null) ? "NA" : contact.getRate());
        if (contact.get_ptr()== null) {
            holder.tx_prate.setText("PTR: NA");
        }else {
         holder.tx_prate.setText("PTR: Rs. "+contact.get_ptr());
        }
        if (contact.get_mrp()== null) {
            holder.txt_mrp.setText("NA");
        }else {
            holder.txt_mrp.setText("MRP: "+contact.get_mrp());
        }
     //   holder.tx_ptrr.setText((contact.get_ptr() == null) ? "NA" : contact.get_ptr());
      //  holder.txt_mrp.setText((contact.getMrp() == null) ? "NA" : contact.getMrp());
        //  holder.name.setText(contact.getCustomerName());
      /*  holder.name.setText((contact.getCustomerName() == null) ? "NA" : contact.getCustomerName());
        holder.amount.setText((contact.getOutstanding_Bill() == null) ? "Rs.0" : contact.getOutstanding_Bill());
        holder.custcode.setText((contact.getCust_Code() == null) ? "NA" : contact.getCust_Code());
*/

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
                        List<ChemistInventorymodel> filteredList = new ArrayList<>();
                        for (ChemistInventorymodel row : contactList)
                        {


                            if ((row.get_in() != null && row.get_in().replace("-", "").replace(" ", "").replace("(", "").replace(")", "")
                                    .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "").replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase()
                                    // .matches(constraint.toString().replace(" ","").replace("-","")
                                    .startsWith(constraint1.replace(" ", "").replace("-", "")
                                            //.contains(constraint.toString().replace(" ","").replace("-","")
                                            .replace("(", "").replace(")", "")
                                            .replace("/", "").replace("*", "").replace(":", "").replace("_", "").replace(".", "").replace("'", "").replace("`", "").replace("~", "").replace("^", "")
                                            .replace("+", "").replace(",", "").replace("<","").replace(">","").toLowerCase())
                            )&&(row.get_in() != null && row.get_in().replace("-", "").replace(" ", "").replace("(", "").replace(")", "")
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
                    contactListFiltered = (List<ChemistInventorymodel>) filterResults.values;
                    notifyDataSetChanged();
                }
            };

    }

    public interface ContactsAdapterListener {
        void onContactSelected(ChemistInventorymodel contact, View v, int position);
    }

//    private  void openBottomSheet(View v) {
//        //View view = activity.getLayoutInflater ().inflate (R.layout.bottom_sheet, null);
//        // View view = inflater.inflate( R.layout.bottom_sheet, null );
//        Context context=v.getContext();
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//        View view = inflater.inflate (R.layout.activity_app_bar_customerlist_new, null);
//        TextView customer_name = (TextView)view.findViewById(R.id.btm_text_cust_name);
//        LinearLayout view_profile = (LinearLayout)view.findViewById(R.id.lnr_view_profile);
//        LinearLayout pending_bills = (LinearLayout)view.findViewById(R.id.lnr_view_pending_bills);
//        LinearLayout view_orders = (LinearLayout)view.findViewById( R.id.lnr_view_all_orders);
//        LinearLayout view_salesreturn = (LinearLayout)view.findViewById( R.id.lnr_view_sales_return);
//        LinearLayout view_location = (LinearLayout)view.findViewById( R.id.lnr_view_up_location);
//
//        //  final Dialog mBottomSheetDialog = new Dialog (context, R.style.MaterialDialogSheet);
//        final Dialog mBottomSheetDialog = new Dialog (context, R.style.AppTheme);
//        mBottomSheetDialog.setContentView (view);
//        mBottomSheetDialog.setCancelable (true);
//        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
//        mBottomSheetDialog.show ();
//
//
//        view_orders.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),"view_orders",Toast.LENGTH_SHORT).show();
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//        view_profile.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),"view_profile",Toast.LENGTH_SHORT).show();
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//        pending_bills.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),"pending_bills",Toast.LENGTH_SHORT).show();
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//        customer_name.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),"customer_name",Toast.LENGTH_SHORT).show();
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//
//        view_salesreturn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),"view_salesreturn",Toast.LENGTH_SHORT).show();
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//        view_location.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),"view_salesreturn",Toast.LENGTH_SHORT).show();
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//    }
}
