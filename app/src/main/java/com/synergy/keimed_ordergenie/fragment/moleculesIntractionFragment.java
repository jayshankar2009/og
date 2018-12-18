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
import com.synergy.keimed_ordergenie.model.m_drug_intraction;

/**
 * Created by 1144 on 02-01-2017.
 */

public class moleculesIntractionFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private int position;

    RecyclerView rv_drugIntraction;
    RecyclerView rv_drugMoleculeIntraction;
    private String searched_text;
    private  String OGMolecule_id ="990";
    private TextView emptyview;

    List<m_drug_intraction> posts_drug_interaction=new ArrayList<m_drug_intraction>();;

    List<m_drug_intraction> posts_drug_molecule=new ArrayList<m_drug_intraction>();;

    public static moleculesIntractionFragment newInstance(int position) {

        moleculesIntractionFragment Intrac = new moleculesIntractionFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        //b.putString(INFOJSON, ja_objMedicinedetails.toString());

        Intrac.setArguments(b);
        return Intrac;
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
        rootView = inflater.inflate(R.layout.molecules_intraction, container, false);




        rv_drugIntraction =(RecyclerView)rootView.findViewById(R.id.rv_drugIntraction);
        rv_drugMoleculeIntraction =(RecyclerView)rootView.findViewById(R.id.rv_drugMoleculeIntraction);
        emptyview =(TextView) rootView.findViewById(R.id.emptyview);
        Bundle b =  getArguments();
        if(b != null) {
          try{
              if(b.getParcelableArrayList("mol_mol_interaction")!=null) {
                  posts_drug_molecule = new ArrayList(b.getParcelableArrayList("mol_mol_interaction"));
              }
              getArguments().remove("mol_mol_interaction");
              if(b.getParcelableArrayList("drug_interaction")!=null) {
                  posts_drug_interaction = new ArrayList(b.getParcelableArrayList("drug_interaction"));
              }
              getArguments().remove("drug_interaction");
        }catch (Exception e)
        {

        }

        } else {
        }


        fill_search_adapter_drugMoleculeIntraction(posts_drug_molecule);
        fill_search_adapter_drugIntraction(posts_drug_interaction);
        return rootView;
    }


    public void fill_search_adapter_drugIntraction( final List<m_drug_intraction> posts_s)
    {
        posts_drug_interaction =posts_s;
        //   txt_product_count.setText(posts_s.size()+" Results for '"+searched_text+"'");
        if(posts_drug_interaction.isEmpty())
        {
             emptyview.setVisibility(View.VISIBLE);
            rv_drugIntraction.setVisibility(View.GONE);
        }else {
             emptyview.setVisibility(View.GONE);
            rv_drugIntraction.setVisibility(View.VISIBLE);
        }

        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>()
        {

            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType)
            {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, R.layout.drug_intraction_detail, parent, false);


                return new BindingViewHolder(binding.getRoot());
            }



            @Override
            public void onBindViewHolder(BindingViewHolder holder, final int position)
            {
                m_drug_intraction order_list = posts_drug_interaction.get(position);
                holder.getBinding().setVariable(BR.v_drug_interaction, order_list);
                holder.getBinding().executePendingBindings();

                //  ((TextView)holder.getBinding().getRoot().findViewById(R.id.txt_count)).setText(posts_s.get(position).getPacksize()+" "+posts_s.get(position).getProductFormImage().substring(0, posts_s.get(position).getProductFormImage().length()-4));


                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        // commneted to disp information commented date 25 April 2017 line above code running

                       // Intent i =new Intent(getActivity(), ProductDetail.class);
                        //i.putExtra("product_id", posts_drug_interaction.get(position).getMolecule_Id());
                        //startActivity(i);

                        //----------------------------------------------------------------------

                      //  Intent i =new Intent(getActivity(), ChemistProductDetail.class);
                      //  i.putExtra("product_id", posts_drug_interaction.get(position).getMolecule_Id());
                     //   startActivity(i);

                    }
                });

            }

            @Override
            public int getItemCount() {
                return posts_drug_interaction.size();
            }
        };

        rv_drugIntraction.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_drugIntraction.setAdapter(adapter);
    }


    public void fill_search_adapter_drugMoleculeIntraction( final List<m_drug_intraction> posts_s)
    {
        posts_drug_molecule=posts_s;
        //   txt_product_count.setText(posts_s.size()+" Results for '"+searched_text+"'");
        if(posts_drug_molecule.isEmpty())
        {
            emptyview.setVisibility(View.VISIBLE);
            rv_drugMoleculeIntraction.setVisibility(View.GONE);
        }else {
            emptyview.setVisibility(View.GONE);
            rv_drugMoleculeIntraction.setVisibility(View.VISIBLE);
        }

        final RecyclerView.Adapter<BindingViewHolder> adapter = new RecyclerView.Adapter<BindingViewHolder>()
        {

            @Override
            public BindingViewHolder onCreateViewHolder(ViewGroup parent, final int viewType)
            {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, R.layout.drug_molecule_intraction_detail, parent, false);


                return new BindingViewHolder(binding.getRoot());
            }



            @Override
            public void onBindViewHolder(BindingViewHolder holder, final int position)
            {
                m_drug_intraction order_list = posts_drug_molecule.get(position);
                holder.getBinding().setVariable(BR.v_drug_molecule_interaction, order_list);
                holder.getBinding().executePendingBindings();

                //  ((TextView)holder.getBinding().getRoot().findViewById(R.id.txt_count)).setText(posts_s.get(position).getPacksize()+" "+posts_s.get(position).getProductFormImage().substring(0, posts_s.get(position).getProductFormImage().length()-4));


                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        Intent i =new Intent(getActivity(), ProductDetail.class);
                        i.putExtra("product_id", posts_drug_interaction.get(position).getMolecule_Id());
                        startActivity(i);

                     //   Intent i =new Intent(getActivity(), ChemistProductDetail.class);
                    //    i.putExtra("product_id",posts_drug_molecule.get(position).getMolecule_Id());
                     //   startActivity(i);

                    }
                });

            }

            @Override
            public int getItemCount() {
                return posts_drug_molecule.size();
            }
        };

        rv_drugMoleculeIntraction.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_drugMoleculeIntraction.setAdapter(adapter);
    }
}
