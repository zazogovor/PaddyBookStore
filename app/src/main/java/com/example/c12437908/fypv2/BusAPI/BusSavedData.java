package com.example.c12437908.fypv2.BusAPI;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.c12437908.fypv2.HelperMethods;
import com.example.c12437908.fypv2.carpool.CarpoolSearchResults;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.SphericalUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cinema on 2/20/2018.
 */

public class BusSavedData {

    public static BusStopResults busStopResults;

    public BusStopResults getBusStopResults(){
        return this.busStopResults;
    }

    public void setBusStopResults(BusStopResults busStopResults) {
        this.busStopResults = busStopResults;
    }

    public static void getBusStops(Context context){
        //Request all busstops and check which one is the closest to you
        String URL = "http://data.dublinked.ie/cgi-bin/rtpi/busstopinformation";
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
//                //String s is a JSON string now
//                String lat = "52.6757355";
//                String log = "-6.2943022";
//                LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(log));

                Gson gson = new GsonBuilder().create();
                busStopResults = gson.fromJson(s, BusStopResults.class);
                System.out.println(busStopResults.getResults().size());
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                return parameters;
            }
        };

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
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }
}
