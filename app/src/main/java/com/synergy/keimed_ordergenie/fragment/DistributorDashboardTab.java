package com.synergy.keimed_ordergenie.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.synergy.keimed_ordergenie.R;

/**
 * Created by admin on 11/30/2017.
 */

public class DistributorDashboardTab extends Fragment implements TabHost.OnTabChangeListener {

    private static final String TAG = "FragmentTabs";
    public static final String TAB_WORDS = "words";
    public static final String TAB_NUMBERS = "numbers";
    private ViewPager viewPager;
    private View mRoot;
    private FragmentTabHost mTabHost;
    private int mCurrentTab;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.activity_dashboard_tab, null);
        mTabHost = (FragmentTabHost) mRoot.findViewById(android.R.id.tabhost);
        setupTabs();
        return mRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        mTabHost.setOnTabChangedListener(this);
        mTabHost.setCurrentTab(mCurrentTab);
        // manually start loading stuff in the first tab
       // updateTab(TAB_WORDS, R.id.tab_1);
    }

    private void setupTabs() {
        mTabHost.setup(); // you must call this before adding your tabs!
      //  mTabHost.addTab(newTab(TAB_WORDS, R.string.tab_words, R.id.tab_1));
     //   mTabHost.addTab(newTab(TAB_NUMBERS, R.string.tab_numbers, R.id.tab_2));
        mTabHost = (FragmentTabHost)mRoot.findViewById(android.R.id.tabs);
       // mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Tab1"),
                DistributorDashboardFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Tab2"),
                ChemistDashboardFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("Tab3"),
//                Tab3Fragment.class, null);

    }

    private void getSupportFragmentManager() {
    }

    private TabHost.TabSpec newTab(String tag, int labelId, int tabContentId) {
        Log.d(TAG, "buildTab(): tag=" + tag);

        View indicator = LayoutInflater.from(getActivity()).inflate(
                R.layout.activity_dashboard_tab,
                (ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
        ((TextView) indicator.findViewById(R.id.text)).setText(labelId);

        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
        tabSpec.setIndicator(indicator);
        tabSpec.setContent(tabContentId);
        return tabSpec;
    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d(TAG, "onTabChanged(): tabId=" + tabId);
        if (TAB_WORDS.equals(tabId)) {
//            updateTab(tabId, R.id.tab_1);
//            mCurrentTab = 0;
//            return;
        }
        if (TAB_NUMBERS.equals(tabId)) {
//            updateTab(tabId, R.id.tab_2);
//            mCurrentTab = 1;
//            return;
        }
    }

    public static DistributorDashboardTab newInstance() {
        DistributorDashboardTab fragment = new DistributorDashboardTab();
        return fragment;

    }

//    private void updateTab(String tabId, int placeholder) {
//        FragmentManager fm = getFragmentManager();
//        if (fm.findFragmentByTag(tabId) == null) {
//            fm.beginTransaction()
//                    .replace(placeholder, new DistributorDashboardFragment()
//                    .commit();
//        }
//    }
//
//    public static Fragment newInstance() {
//    }
}