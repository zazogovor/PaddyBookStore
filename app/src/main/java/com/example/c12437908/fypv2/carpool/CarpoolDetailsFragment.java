package com.example.c12437908.fypv2.carpool;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.example.c12437908.fypv2.MainActivity;
import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.register_login.SessionManager;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by cinema on 1/16/2018.
 */

public class CarpoolDetailsFragment extends Fragment implements DatePickerDialog.OnDateSetListener, NumberPicker.OnValueChangeListener {

    private TextView driver_tv;
    private EditText origination_et, desitnation_et, pickup_et;
    private Button join_edit_btn, date_btn, time_btn, people_btn, save_btn, cancel_btn, max_pickup_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carpool_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();

        driver_tv = (TextView) v.findViewById(R.id.driver_tv);
        origination_et = (EditText) v.findViewById(R.id.origination_tv);
        desitnation_et = (EditText) v.findViewById(R.id.destiantion_tv);
        date_btn = (Button) v.findViewById(R.id.date_tv);
        time_btn = (Button) v.findViewById(R.id.time_tv);
        people_btn = (Button) v.findViewById(R.id.max_people_tv);
        join_edit_btn = (Button) v.findViewById(R.id.join_edit_btn);
        save_btn = (Button) v.findViewById(R.id.save_btn);
        cancel_btn = (Button) v.findViewById(R.id.cancel_btn);
        pickup_et = (EditText) v.findViewById(R.id.pickup_et);
        max_pickup_btn = (Button) v.findViewById(R.id.max_pickup_btn);

        save_btn.setVisibility(View.INVISIBLE);
        cancel_btn.setVisibility(View.INVISIBLE);
        pickup_et.setVisibility(View.INVISIBLE);

        Intent i = getActivity().getIntent();
        Bundle args = i.getBundleExtra("BUNDLE");
        Carpool c = (Carpool) args.get("carpool");

        final int carpoolId = i.getIntExtra("carpoolID", 0);
//        String driverUsername = getActivity().getIntent().getStringExtra("driverUsername");
//        String origin = getActivity().getIntent().getStringExtra("origination");
//        String destin = getActivity().getIntent().getStringExtra("destination");
//        String date = getActivity().getIntent().getStringExtra("date");
//        String time = getActivity().getIntent().getStringExtra("time");
//        String max_people = getActivity().getIntent().getStringExtra("max_people");

        driver_tv.setText(c.getDriver().getUsername());
        origination_et.setText(c.getOrigination());
        desitnation_et.setText(c.getDestination());
        date_btn.setText(c.getDate());
        time_btn.setText(c.getTime());
        people_btn.setText(""+c.getMaxPeople());

        desitnation_et.setEnabled(false);
        origination_et.setEnabled(false);

        final SessionManager session = new SessionManager(getActivity().getApplicationContext());

        if(session.getUsername().equals(c.getDriver().getUsername())){
            join_edit_btn.setText("Edit");

            join_edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    join_edit_btn.setVisibility(View.INVISIBLE);
                    save_btn.setVisibility(View.VISIBLE);
                    cancel_btn.setVisibility(View.VISIBLE);

                    desitnation_et.setEnabled(true);
                    origination_et.setEnabled(true);

                    int startYear = Calendar.getInstance().get(Calendar.YEAR);
                    int starthMonth = Calendar.getInstance().get(Calendar.MONTH);
                    int startDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    final DatePickerDialog datePickerDialog = new DatePickerDialog(
                            v.getContext(), CarpoolDetailsFragment.this, startYear, starthMonth, startDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                    date_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            datePickerDialog.show();
                        }
                    });

                    people_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final Dialog d = new Dialog(v.getContext());
                            d.setTitle("NumberPicker");
                            d.setContentView(R.layout.number_dialog);
                            Button b1 = (Button) d.findViewById(R.id.button1);
                            Button b2 = (Button) d.findViewById(R.id.list_books_btn);
                            final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                            np.setMaxValue(100);
                            np.setMinValue(0);
                            np.setWrapSelectorWheel(false);
                            np.setOnValueChangedListener((NumberPicker.OnValueChangeListener) CarpoolDetailsFragment.this);
                            b1.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    people_btn.setText(String.valueOf(np.getValue()));
                                    d.dismiss();
                                }
                            });
                            b2.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    d.dismiss();
                                }
                            });
                            d.show();
                        }
                    });

                    max_pickup_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final Dialog d = new Dialog(v.getContext());
                            d.setTitle("NumberPicker");
                            d.setContentView(R.layout.number_dialog);
                            Button b1 = (Button) d.findViewById(R.id.button1);
                            Button b2 = (Button) d.findViewById(R.id.list_books_btn);
                            final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                            np.setMaxValue(100);
                            np.setMinValue(0);
                            np.setWrapSelectorWheel(false);
                            np.setOnValueChangedListener((NumberPicker.OnValueChangeListener) CarpoolDetailsFragment.this);
                            b1.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    max_pickup_btn.setText(String.valueOf(np.getValue()));
                                    d.dismiss();
                                }
                            });
                            b2.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    d.dismiss();
                                }
                            });
                            d.show();
                        }
                    });

                    time_btn.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime.get(Calendar.MINUTE);
                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    String m = "";
                                    String h = "";
                                    if(selectedMinute < 10){
                                        m = "0" + selectedMinute;
                                        if(selectedHour < 10){
                                            h = "0" + selectedHour;
                                            time_btn.setText( h + ":" + m);
                                        }
                                        else{
                                            time_btn.setText( selectedHour + ":" + m);
                                        }
                                    }
                                    else{
                                        if(selectedHour < 10){
                                            h = "0" + selectedHour;
                                            time_btn.setText( h + ":" + selectedMinute);
                                        }
                                        else{
                                            time_btn.setText( selectedHour + ":" + selectedMinute);
                                        }
                                    }
                                }
                            }, hour, minute, true);//Yes 24 hour time
                            mTimePicker.setTitle("Select Time");
                            mTimePicker.show();
                        }
                    });

                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              join_edit_btn.setVisibility(View.VISIBLE);
                              save_btn.setVisibility(View.INVISIBLE);
                              cancel_btn.setVisibility(View.INVISIBLE);

                              desitnation_et.setEnabled(false);
                              origination_et.setEnabled(false);
                          }
                    });

                    save_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            final String orig_parm = origination_et.getText().toString();
                            final String dest_parm = desitnation_et.getText().toString();
                            final String time_parm = time_btn.getText().toString();
                            final String date_parm = date_btn.getText().toString();
                            final String maxPassengers_parm = people_btn.getText().toString();
                            final String maxPickupDistance_parm = max_pickup_btn.getText().toString();

                            if(orig_parm.equals("") || dest_parm.equals("")){
                                Toast.makeText(v.getContext(), "Origination or Destination fields are empty!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                String URL = "http://10.0.2.2:8080/api/carpool/update/";
                                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String s) {
                                        if(s.equals("1")){
                                            join_edit_btn.setVisibility(View.VISIBLE);
                                            save_btn.setVisibility(View.INVISIBLE);
                                            cancel_btn.setVisibility(View.INVISIBLE);

                                            desitnation_et.setEnabled(false);
                                            origination_et.setEnabled(false);

                                            Toast.makeText(v.getContext(), "Carpool updated successfully!", Toast.LENGTH_LONG).show();

                                            getActivity().recreate();
                                        }
                                        else{
                                            Toast.makeText(v.getContext(), "Could not Login, try again.", Toast.LENGTH_LONG).show();
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
                                        parameters.put("id", "" + carpoolId);
                                        parameters.put("date", date_parm);
                                        parameters.put("time", time_parm);
                                        parameters.put("maxPassengers", maxPassengers_parm);
                                        parameters.put("origination", orig_parm);
                                        parameters.put("destination", dest_parm);
                                        parameters.put("passengersMarkers", "");
                                        parameters.put("maxPickupDistance", maxPickupDistance_parm);
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
                        }
                    });
                }
            });
        }
        else{
            Boolean alreadyJoined = false;
            for(int j = 0; j < c.getPassengers().size(); j++){
                if(c.getPassengers().get(j).getUsername().equals(session.getUsername())){
                    alreadyJoined = true;
                    j = c.getPassengers().size()+1;
                }
                else{
                    alreadyJoined = false;
                }
            }

            if(alreadyJoined == false){
                join_edit_btn.setText("Join");
                //pickup_et.setVisibility(View.VISIBLE);
                join_edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if(pickup_et.getText().toString().equals("")) {
                            //final LatLng pickup_ltlng = getLocationFromAddress(v.getContext(), pickup_et.getText().toString());

                            Intent i = getActivity().getIntent();
                            final String pickup_ltlng = i.getStringExtra("pickupLocation");

                            String URL = "http://10.0.2.2:8080/api/carpool/addPassenger/";
                            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                                @Override
                                public void onResponse(String s) {
                                    if(s.equals("1")){
                                        Toast.makeText(v.getContext(), "You have joined the carpool successfully!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(v.getContext(), MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(v.getContext(), "Could not join the carpool, try again.", Toast.LENGTH_LONG).show();
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
                                    parameters.put("id", "" + carpoolId);
                                    parameters.put("username", session.getUsername());
                                    parameters.put("passengerMarker", "" + pickup_ltlng);
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
                    }
                });
            }
            else{
                join_edit_btn.setText("Leave Carpool");

                join_edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        String URL = "http://10.0.2.2:8080/api/carpool/removePassenger/";
                        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                            @Override
                            public void onResponse(String s) {
                                if(s.equals("1")){
                                    Toast.makeText(v.getContext(), "You have left the carpool successfully!", Toast.LENGTH_LONG).show();
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
                                parameters.put("id", "" + carpoolId);
                                parameters.put("username", session.getUsername());
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

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    public com.google.android.gms.maps.model.LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        com.google.android.gms.maps.model.LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new com.google.android.gms.maps.model.LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}
