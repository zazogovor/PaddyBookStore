package com.example.c12437908.fypv2.carpool;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.c12437908.fypv2.Entities.Carpool;
import com.example.c12437908.fypv2.Entities.User;
import com.example.c12437908.fypv2.MainActivity;
import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.register_login.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cinema on 2/7/2018.
 */

public class CarpoolPassengerListFragment extends Fragment{

    ImageView removePassengerImage;
    TextView origination_textview, username_textview, empty_list_title;
    LinearLayout passenger_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passenger_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();

        final SessionManager session = new SessionManager(getActivity().getApplicationContext());

        empty_list_title = (TextView) v.findViewById(R.id.empty_list_title);
        passenger_list = (LinearLayout) v.findViewById(R.id.passenger_list);


        Intent intent = getActivity().getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        final Carpool c = (Carpool) args.get("carpool");

        if(c.getPassengers().size() != 0){
            passenger_list.setVisibility(View.VISIBLE);
            empty_list_title.setVisibility(View.GONE);

            String[] arr = new String[0];
            String org = "";

            if(c.getPassengerMarkers().contains("|")){
                arr = c.getPassengerMarkers().split("\\|");
            }
            else if(!c.getPassengerMarkers().equals("")){
                org = c.getPassengerMarkers();
            }

            ViewGroup parent = (ViewGroup) v.findViewById(R.id.passenger_list);
            LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
            for(int i = 0; i < c.getPassengers().size(); i++){
                final User u = c.getPassengers().get(i);
                View view = inflater.inflate(R.layout.list_item_passenger, null);

                username_textview = (TextView) view.findViewById(R.id.username);
                origination_textview = (TextView) view.findViewById(R.id.origination);
                removePassengerImage = (ImageView) view.findViewById(R.id.removePassengerImage);

                username_textview.setText(u.getUsername());

                Geocoder geocoder = new Geocoder(getActivity());

                if(arr.length == 0){
                    String[] latlng = org.split(",");
                    List<Address> addresses = null;
                    try {
                        System.out.println(latlng[0]);
                        System.out.println(latlng[0]);
                        System.out.println(latlng[0]);
                        System.out.println(latlng[0]);


                        System.out.println(latlng[1]);
                        System.out.println(latlng[1]);
                        System.out.println(latlng[1]);
                        System.out.println(latlng[1]);



                        LatLng location = new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));
                        addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                        String address = addresses.get(0).getAddressLine(0);
                        origination_textview.setText(address);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    String[] latlng = arr[i].split(",");
                    List<Address> addresses = null;
                    try {
                        LatLng location = new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));
                        addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                        String address = addresses.get(0).getAddressLine(0);
                        origination_textview.setText(address);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(session.getUsername().equals(c.getDriver().getUsername())){
                    removePassengerImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            String URL = "http://10.0.2.2:8080/api/carpool/removePassenger/";
                            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                                @Override
                                public void onResponse(String s) {
                                    if(s.equals("1")){
                                        Toast.makeText(v.getContext(), "You have removed the passenger successfully!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(v.getContext(), MainActivity.class));
                                    }
                                }
                            },new Response.ErrorListener(){
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(v.getContext(), "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> parameters = new HashMap<String, String>();
                                    parameters.put("id", "" + c.getId());
                                    parameters.put("username", u.getUsername());
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

                            RequestQueue rQueue = Volley.newRequestQueue(v.getContext());
                            rQueue.add(request);
                        }
                    });
                }
                else{
                    removePassengerImage.setVisibility(View.INVISIBLE);
                }

                parent.addView(view);
            }
        }
        else {
            passenger_list.setVisibility(View.GONE);
            empty_list_title.setVisibility(View.VISIBLE);
        }
    }
}
