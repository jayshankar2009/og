package com.synergy.keimed_ordergenie.utils;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.synergy.keimed_ordergenie.fragment.moleculesInfoFragment;
import com.synergy.keimed_ordergenie.fragment.moleculesIntractionFragment;
import com.synergy.keimed_ordergenie.fragment.moleculesProductFragment;


/**
 * Created by 1144 on 02-01-2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT =3;
    private String titles[] ;
    private String iProductId, sMemberId ;



    public ViewPagerAdapter(FragmentManager fm, String[] titles2, String ProductId, String MemberId) {
        super(fm);

        iProductId  = ProductId ;
        sMemberId  = MemberId ;

        titles=titles2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // Open FragmentTab1.java
            case 0:
                return moleculesProductFragment.newInstance(position);
            case 1:
                return moleculesInfoFragment.newInstance(position);

            case 2:
                return moleculesIntractionFragment.newInstance(position) ;


        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }


}
