package com.example.c12437908.fypv2.carpool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.c12437908.fypv2.Entities.Carpool;
import com.example.c12437908.fypv2.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by c12437908 on 07/02/2018.
 */

public class CreatedCarpoolsFragment  extends Fragment {

    TextView driver_textview, origination_textview, destination_textview, date_textview, time_textview, empty_list_title;
    RelativeLayout carpool_info_btn;
    LinearLayout carpool_layout;
    ArrayList<Carpool> carpoolsArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carpool_search_results, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();
        empty_list_title = (TextView) v.findViewById(R.id.empty_list_title);

        final Intent intent = getActivity().getIntent();
        Bundle args1 = intent.getBundleExtra("BUNDLE_CREATED");

        carpoolsArray = (ArrayList<Carpool>) args1.getSerializable("createdCarpools");

        if(carpoolsArray.size() != 0){
            empty_list_title.setVisibility(View.GONE);
//            carpool_layout = (LinearLayout) v.findViewById(R.id.carpool_list);
//            carpool_layout.setVisibility(View.VISIBLE);

            ViewGroup parent = (ViewGroup) v.findViewById(R.id.carpool_list);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            for(int i = 0; i < carpoolsArray.size(); i++){
                final Carpool c = carpoolsArray.get(i);
                View view = inflater.inflate(R.layout.list_item_carpool, null);

                driver_textview = (TextView) view.findViewById(R.id.driver);
                origination_textview = (TextView) view.findViewById(R.id.origination);
                destination_textview = (TextView) view.findViewById(R.id.destination);
                date_textview = (TextView) view.findViewById(R.id.date);
                time_textview = (TextView) view.findViewById(R.id.time);
                carpool_info_btn = (RelativeLayout) view.findViewById(R.id.carpool_info_btn);

                driver_textview.setText(c.getDriver().getUsername());
                origination_textview.setText(c.getOrigination());
                destination_textview.setText(c.getDestination());
                date_textview.setText(c.getDate());
                time_textview.setText(c.getTime());

                carpool_info_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pickup_location = intent.getStringExtra("pickupLocation");
                        Intent i = new Intent(getContext(), CarpoolDetails.class);
                        Bundle args = new Bundle();
                        args.putSerializable("carpool", (Serializable) c);
                        i.putExtra("BUNDLE", args);
                        i.putExtra("carpoolID", c.getId());
                        i.putExtra("pickupLocation", pickup_location);
                        i.putExtra("originClass", "CarpoolMapFragment");
                        startActivity(i);
                    }
                });
                parent.addView(view);
            }
        }
        else {
            empty_list_title.setVisibility(View.VISIBLE);
        }

    }
}
