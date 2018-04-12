package com.example.c12437908.fypv2.BusAPI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.c12437908.fypv2.Entities.Carpool;
import com.example.c12437908.fypv2.HelperMethods;
import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.carpool.CarpoolDetails;
import com.example.c12437908.fypv2.carpool.CarpoolSearchResults;
import com.example.c12437908.fypv2.carpool.SearchCarpools;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.SphericalUtil;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by cinema on 2/20/2018.
 */

public class BusStopFragment extends Fragment{


    private String date_entered;
    private String time_entered;
    private String dayOfTheWeek;
    private ArrayList<Route> availibleRoutes = new ArrayList<Route>();
    private ArrayList<TimeTableInformation> routeTimeTables = new ArrayList<>();
    private ArrayList<TimeTableForADay> tempList = new ArrayList<>();


    private LinearLayout bus_list;
    private TextView empty_list_title, route_no, destination, localized, departure_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buses, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        empty_list_title = (TextView) v.findViewById(R.id.empty_list_title);

        Intent intent = getActivity().getIntent();


        Bundle args = intent.getBundleExtra("timetables_bundle");
        tempList = (ArrayList<TimeTableForADay>) args.getSerializable("timetables");
        System.out.println("list size:" + tempList.size());

//        String origin = intent.getStringExtra("pickupLocation");
//
//        try {
//            date_entered = intent.getStringExtra("date");
//            SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
//            Date dt1=format1.parse(date_entered);
//            DateFormat format2=new SimpleDateFormat("EEEE");
//            dayOfTheWeek = format2.format(dt1);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        time_entered = intent.getStringExtra("time");
//
//        LatLng loc = HelperMethods.getLocationFromAddress(getContext(), origin);
//
//        BusStopResults bsr = BusSavedData.busStopResults;
//        ArrayList<BusStop> results = new ArrayList<BusStop>();
//        BusStop bs = new BusStop();
//        Double distance = 0.0;
//        int pos = 0;
//
//        for(int i = 0; i < bsr.getResults().size(); i++){
//            LatLng bll = new LatLng(Double.parseDouble(BusSavedData.busStopResults.getResults().get(i).getLatitude()), Double.parseDouble(BusSavedData.busStopResults.getResults().get(i).getLongitude()));
//
//            Double tempd = SphericalUtil.computeDistanceBetween(loc, bll);
//            if(tempd < 100){
//                results.add(BusSavedData.busStopResults.getResults().get(i));
//            }
//            if(i == 0){
//                pos = i;
//                distance = tempd;
//                bs = BusSavedData.busStopResults.getResults().get(i);
//            }
//            else{
//                if(distance > tempd){
//                    pos = i;
//                    distance = tempd;
//                    bs = BusSavedData.busStopResults.getResults().get(i);
//                }
//            }
//        }
//
//        int m = distance.intValue();
//        System.out.println(m + "meters");
//        System.out.println(bs.getStopid());
//        System.out.println(bs.getFullname());
//
//        for(int i = 1; i < results.size(); i++){
//            System.out.println(results.get(i).getStopid());
//            System.out.println(results.get(i).getFullname());
//        }
//
//        fetchInfromation(bs, 0);

//        for(int i = 0; i < routeTimeTables.size(); i++){
//            for(int k = 0; k < routeTimeTables.get(i).getResults().size(); k++){
//                if(routeTimeTables.get(i).getResults().get(k).getStartdayofweek().equals(dayOfTheWeek)) {
//                    System.out.println(k);
//                    System.out.println(routeTimeTables.get(i).getResults().get(k).getStartdayofweek());
//                    k = routeTimeTables.get(i).getResults().size();
//                }
//            }
//        }

        if(tempList.size() != 0){
            empty_list_title.setVisibility(View.GONE);

            ViewGroup parent = (ViewGroup) v.findViewById(R.id.bus_list);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            for(int i = 0; i < tempList.size(); i++){
                //final Route r = availibleRoutes.get(i);
                View view = inflater.inflate(R.layout.list_item_route, null);

                route_no = view.findViewById(R.id.route_no);
                destination = view.findViewById(R.id.destination);
                localized = view.findViewById(R.id.destination_localized);
                departure_time = view.findViewById(R.id.departure_time);

                route_no.setText(tempList.get(i).getRoute());
                destination.setText(tempList.get(i).getDestination());
                localized.setText(tempList.get(i).getDestination_localized());
                departure_time.setText(tempList.get(i).getDeparture_time());


                parent.addView(view);
            }
        }
        else {
            empty_list_title.setVisibility(View.VISIBLE);
        }
    }

    private void fetchInfromation(final BusStop busstop, final int counter){
        String URL = "http://data.dublinked.ie/cgi-bin/rtpi/timetableinformation?type=week&stopid=" + busstop.getStopid() + "&routeid=" + busstop.getOperators().get(0).getRoutes().get(counter) + "&format=json";
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new GsonBuilder().create();
                TimeTableInformation tti = gson.fromJson(s, TimeTableInformation.class);
                //routeTimeTables.add(tti);
                //System.out.println(routeTimeTables.size());

                System.out.println(tti.getRoute());

                for(int k = 0; k < tti.getResults().size(); k++){
                    if(tti.getResults().get(k).getStartdayofweek().equals(dayOfTheWeek)) {
                        System.out.println(k);
                        System.out.println(tti.getResults().get(k).getStartdayofweek());
                        //k = tti.getResults().size();
                        routeTimeTables.add(tti);
                        System.out.println(routeTimeTables.size());
                        System.out.println(tti.getResults().size());

                        TimeTableForADay t = new TimeTableForADay(tti.getRoute(), tti.getResults().get(k).getDepartures().get(1), tti.getResults().get(k).getDestination(), tti.getResults().get(
                                k).getDestinationlocalized());
                        tempList.add(t);
                        System.out.println("templistsize = " + tempList.size());
                    }
                }

                if(counter+1 < busstop.getOperators().get(0).getRoutes().size()){
                    fetchInfromation(busstop, counter+1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
            }
        });

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);

    }
}
