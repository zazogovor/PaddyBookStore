package com.example.c12437908.fypv2.PagerAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.c12437908.fypv2.carpool.CarpoolDetailsFragment;
import com.example.c12437908.fypv2.carpool.CarpoolMapFragment;
import com.example.c12437908.fypv2.carpool.CarpoolPassengerListFragment;

public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CarpoolDetailsFragment tab1 = new CarpoolDetailsFragment();
                return tab1;
            case 1:
                CarpoolMapFragment tab2 = new CarpoolMapFragment();
                return tab2;
            case 2:
                CarpoolPassengerListFragment tab3 = new CarpoolPassengerListFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}