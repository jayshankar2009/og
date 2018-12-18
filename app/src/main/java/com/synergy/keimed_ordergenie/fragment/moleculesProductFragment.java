package com.synergy.keimed_ordergenie.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.synergy.keimed_ordergenie.BR;
import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.ProductDetail;
import com.synergy.keimed_ordergenie.adapter.BindingViewHolder;
import com.synergy.keimed_ordergenie.model.m_all_full_product_chemist_search;

/**
 * Created by 1144 on 02-01-2017.
 */

public class moleculesProductFragment extends Fragment {//}  implements MakeWebRequest.OnResponseSuccess{

    private static final String ARG_POSITION = "position";
    private int position;
    private TextView emptyview;


    RecyclerView rv_product;

    private String searched_text;
    private  String OGMolecule_id ="990";

    List<m_all_full_product_chemist_search> posts=new ArrayList<m_all_full_product_chemist_search>();;

    public static moleculesProductFragment newInstance(int position) {

        moleculesProductFragment prod = new moleculesProductFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        //b.putString(INFOJSON, ja_objMedicinedetails.toString());

        prod.setArguments(b);
        return prod;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(ARG_POSITION);
        //sInfo = getArguments().getString(INFOJSON);

        View rootView ;
        rootView = inflater.inflate(R.layout.molecules_products, container, false);

        TextView  activity_main_row_header=(TextView) rootView.findViewById(R.id.activity_main_row_header);

        Bundle b =  getArguments();
        if(b != null) {
            try {
                posts = new ArrayList(b.getParcelableArrayList("product_detail"));
                getArguments().remove("product_detail");
                activity_main_row_header.setText(getArguments().getString("heading"));
                getArguments().remove("heading");

            }catch (Exception e)
            {

            }
        } else {
        }

        rv_product=(RecyclerView)rootView.findViewById(R.id.rv_product);
        emptyview=(TextView) rootView.findViewById(R.id.emptyview);
       fill_search_adapter_product(posts);

        return rootView;
    }



    public void fill_search_adapter_product( final List<m_all_full_product_chemist_search> posts_s)
    {
        posts=posts_s;
        //   txt_product_count.setText(posts_s.size()+" Results for '"+searched_text+"'");
        if(posts.isEmpty())
        {
            emptyview.setVisibility(View.VISIBLE);
            rv_product.setVisibility(View.GONE);
        }else {
            emptyview.setVisibility(View.GONE);
            rv_product.setVisibility(View.VISIBLE);
        }

        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>()
        {

            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType)
            {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, R.layout.invoice_header, parent, false);


                return new BindingViewHolder(binding.getRoot());
            }



            @Override
            public void onBindViewHolder(BindingViewHolder holder, final int position)
            {
                m_all_full_product_chemist_search order_list = posts.get(position);
                holder.getBinding().setVariable(BR.v_serchedproduct, order_list);
                holder.getBinding().executePendingBindings();
                ((TextView)holder.getBinding().getRoot().findViewById(R.id.txt_count)).setText(posts_s.get(position).getPacksize()+" "+"Tablet");

              //  ((TextView)holder.getBinding().getRoot().findViewById(R.id.txt_count)).setText(posts_s.get(position).getPacksize()+" "+posts_s.get(position).getProductFormImage().substring(0, posts_s.get(position).getProductFormImage().length()-4));


                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        Intent i =new Intent(getActivity(), ProductDetail.class);
                        i.putExtra("product_id", posts.get(position).getProduct_ID());
                        startActivity(i);

                     //   Intent i =new Intent(getActivity(), ChemistProductDetail.class);
                     //   i.putExtra("product_id",posts.get(position).getProduct_ID());
                     //   startActivity(i);

                    }
                });

            }

            @Override
            public int getItemCount() {
                return posts.size();
            }
        };

        rv_product.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_product.setAdapter(adapter);
    }

}
