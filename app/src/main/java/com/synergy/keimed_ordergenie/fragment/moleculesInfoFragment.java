package com.synergy.keimed_ordergenie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.activity.ExpandableListAdapter;
import com.synergy.keimed_ordergenie.model.m_General_Info;
import com.synergy.keimed_ordergenie.model.m_molecule_interaction;

/**
 * Created by 1144 on 02-01-2017.
 */

public class moleculesInfoFragment extends Fragment{

    private static final String ARG_POSITION = "position";
    private int position;
    private TextView emptyview;
    //RecyclerView rv_info;

    ExpandableListView rv_info;
    ExpandableListAdapter listAdapter;
    HashMap<String, List<String>> listDataChild;


    private List<m_General_Info> posts=new ArrayList<m_General_Info>();

    List<String> headers_Questionslist = new ArrayList<String>();
    List<String> chile_Answerslist;

    List<m_molecule_interaction> posts_drug_molecule_intraction=new ArrayList<m_molecule_interaction>();



    public static moleculesInfoFragment newInstance(int position) { //},JSONArray ja_objMedicinedetails) {

        moleculesInfoFragment Info = new moleculesInfoFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);

        //b.putString(INFOJSON, ja_objMedicinedetails.toString());

        Info.setArguments(b);
        return Info;
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
        rootView = inflater.inflate(R.layout.molecules_info, container, false);

       // rv_info=(RecyclerView)rootView.findViewById(R.id.rv_info);
        rv_info=(ExpandableListView) rootView.findViewById(R.id.rv_info);

        emptyview=(TextView) rootView.findViewById(R.id.emptyview);

        Bundle b =  getArguments();
        if(b != null) {
            try{

                if(b.getParcelableArrayList("info")!=null)
                {
                    posts =new ArrayList(b.getParcelableArrayList("info"));

                    listDataChild = new HashMap<String, List<String>>();
                    for(int i = 0;i<posts.size();i++)
                    {
                        String questions = posts.get(i).getPharmacology_Option();
                        String answers = posts.get(i).getPharmacology_Desc();
                        headers_Questionslist.add(questions);
                        chile_Answerslist = new ArrayList<String>();
                        chile_Answerslist.add(answers);
                        listDataChild.put(headers_Questionslist.get(i),chile_Answerslist);
                    }

                }

                if(b.getParcelableArrayList("mol_mol_interaction")!=null)
                {
                    posts_drug_molecule_intraction=new ArrayList(b.getParcelableArrayList("mol_mol_interaction"));
                }

                getArguments().remove("info");
                getArguments().remove("mol_mol_interaction");
            }catch (Exception e)
            {
                e.toString();
            }
        } else {
        }


        fill_search_adapter_info(posts);
        return rootView;
    }


    public void fill_search_adapter_info( final List<m_General_Info> posts_s)
    {
        posts=posts_s;
        //   txt_product_count.setText(posts_s.size()+" Results for '"+searched_text+"'");
        if(posts.isEmpty())
        {
            emptyview.setVisibility(View.VISIBLE);
            rv_info.setVisibility(View.GONE);
        }else {
            emptyview.setVisibility(View.GONE);
            rv_info.setVisibility(View.VISIBLE);
        }

        listAdapter = new ExpandableListAdapter(getContext(),headers_Questionslist,listDataChild);
        // setting list adapter
        rv_info.setAdapter(listAdapter);

      //  rv_info.setLayoutManager(new LinearLayoutManager(getActivity()));
       // rv_info.setAdapter(adapter);
    }
}
