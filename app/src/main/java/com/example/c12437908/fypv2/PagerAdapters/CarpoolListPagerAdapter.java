package com.example.c12437908.fypv2.PagerAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.c12437908.fypv2.carpool.CreatedCarpoolsFragment;
import com.example.c12437908.fypv2.carpool.JoinedCarpoolsFragment;

/**
 * Created by c12437908 on 07/02/2018.
 */

public class CarpoolListPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public CarpoolListPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CreatedCarpoolsFragment tab1 = new CreatedCarpoolsFragment();
                return tab1;
            case 1:
                JoinedCarpoolsFragment tab2 = new JoinedCarpoolsFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
