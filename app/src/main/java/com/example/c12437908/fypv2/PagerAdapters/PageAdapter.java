package com.example.c12437908.fypv2.PagerAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.c12437908.fypv2.BookHelper.FragmentBookDetails;
import com.example.c12437908.fypv2.BookHelper.FragmentBookReviews;

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
                FragmentBookDetails tab1 = new FragmentBookDetails();
                return tab1;
            case 1:
                FragmentBookReviews tab2 = new FragmentBookReviews();
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