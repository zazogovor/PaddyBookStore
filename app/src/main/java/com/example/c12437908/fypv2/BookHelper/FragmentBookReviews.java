package com.example.c12437908.fypv2.BookHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.c12437908.fypv2.R;

/**
 * Created by cinema on 4/12/2018.
 */

public class FragmentBookReviews extends Fragment{

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_carpool_details, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View v = getView();

        }
}
