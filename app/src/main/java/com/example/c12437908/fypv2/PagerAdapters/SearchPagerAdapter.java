package com.example.c12437908.fypv2.PagerAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.c12437908.fypv2.BusAPI.BusStopFragment;
import com.example.c12437908.fypv2.carpool.CarpoolMapFragment;
import com.example.c12437908.fypv2.carpool.CarpoolSearchResultsListFragment;

/**
 * Created by cinema on 1/22/2018.
 */

public class SearchPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public SearchPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                BusStopFragment tab1 = new BusStopFragment();
                return tab1;
            case 1:
                CarpoolSearchResultsListFragment tab2 = new CarpoolSearchResultsListFragment();
                return tab2;
            case 2:
                CarpoolMapFragment tab3 = new CarpoolMapFragment();
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
