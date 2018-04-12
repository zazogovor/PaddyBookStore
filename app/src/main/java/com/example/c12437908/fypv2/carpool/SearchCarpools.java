package com.example.c12437908.fypv2.carpool;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.c12437908.fypv2.BusAPI.BusSavedData;
import com.example.c12437908.fypv2.BusAPI.BusStop;
import com.example.c12437908.fypv2.BusAPI.BusStopResults;
import com.example.c12437908.fypv2.BusAPI.Route;
import com.example.c12437908.fypv2.BusAPI.TimeTableForADay;
import com.example.c12437908.fypv2.BusAPI.TimeTableInformation;
import com.example.c12437908.fypv2.Entities.Carpool;
import com.example.c12437908.fypv2.HelperMethods;
import com.example.c12437908.fypv2.MainActivity;
import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.register_login.SessionManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cinema on 12/18/2017.
 */

public class SearchCarpools  extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NumberPicker.OnValueChangeListener {

    private EditText origin_et, destin_et;
    private Button search_btn, date_btn, time_btn, min_max_btn;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private ActionBar actionBar;
    private String date_entered;
    private Date time_entered;
    private String dayOfTheWeek;
    private ArrayList<Route> availibleRoutes = new ArrayList<Route>();
    private ArrayList<TimeTableInformation> routeTimeTables = new ArrayList<>();
    private ArrayList<TimeTableForADay> tempList = new ArrayList<>();

    RelativeLayout register_btn, login_btn, carpool_btn, home_btn, logout_btn;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_search);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerTitle = getString(R.string.drawer_open);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.app_name
        ) {
            public void onDrawerClosed(View view) {
                actionBar.setTitle(getTitle());
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                actionBar.setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setButtonListeners();
    }

    private void setButtonListeners() {
        session = new SessionManager(getApplicationContext());

        register_btn = (RelativeLayout) findViewById(R.id.register_btn);
        login_btn = (RelativeLayout) findViewById(R.id.login_btn);
        carpool_btn = (RelativeLayout) findViewById(R.id.carpool_btn);
        home_btn = (RelativeLayout) findViewById(R.id.home_btn);
        origin_et = (EditText) findViewById(R.id.origin_et);
        search_btn = (Button) findViewById(R.id.search_btn);
        logout_btn = (RelativeLayout) findViewById(R.id.logout_btn);
        destin_et = (EditText) findViewById(R.id.destin_et);
        date_btn = (Button) findViewById(R.id.date_btn);
        time_btn = (Button) findViewById(R.id.time_btn);
        min_max_btn = (Button) findViewById(R.id.min_max_btn);

        register_btn.setVisibility(View.GONE);
        login_btn.setVisibility(View.GONE);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchCarpools.this, MainActivity.class));
            }
        });

        carpool_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchCarpools.this, CarpoolMenu.class));
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                startActivity(new Intent(SearchCarpools.this, MainActivity.class));
            }
        });

        int startYear = Calendar.getInstance().get(Calendar.YEAR);
        int starthMonth = Calendar.getInstance().get(Calendar.MONTH);
        int startDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                SearchCarpools.this, SearchCarpools.this, startYear, starthMonth, startDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        date_btn.setText(startDay + "/" + (starthMonth+1) + "/" + startYear);
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        time_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SearchCarpools.this, new TimePickerDialog.OnTimeSetListener() {
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

        min_max_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(SearchCarpools.this);
                d.setTitle("NumberPicker");
                d.setContentView(R.layout.number_dialog);
                Button b1 = (Button) d.findViewById(R.id.button1);
                Button b2 = (Button) d.findViewById(R.id.list_books_btn);
                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                np.setMaxValue(100);
                np.setMinValue(0);
                np.setWrapSelectorWheel(false);
                np.setOnValueChangedListener(SearchCarpools.this);
                b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        min_max_btn.setText(String.valueOf(np.getValue()));
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

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = "http://10.0.2.2:8080/api/carpool/carpools";
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        final LatLng pickup_ltlng = getLocationFromAddress(SearchCarpools.this, origin_et.getText().toString());
                        Double l1 = pickup_ltlng.latitude;
                        Double l2 = pickup_ltlng.longitude;
                        String coordl1 = l1.toString();
                        String coordl2 = l2.toString();
                        String pickup_string = coordl1 + "," + coordl2;

                        final List<Carpool> searchResults = new ArrayList<Carpool>();
                        Gson gson = new GsonBuilder().create();
                        Carpool[] carpoolsArray = gson.fromJson(s, Carpool[].class);

                        //add carpools to the result list
                        if (carpoolsArray.length != 0) {
                            try {
                                Intent intent = new Intent(SearchCarpools.this, CarpoolSearchResults.class);
                                Date time_selected = new SimpleDateFormat("HH:mm").parse(time_btn.getText().toString());
                                Date time_before;
                                Date time_after;

                                if(min_max_btn.getText().toString().equals("0 min")){
                                    time_before = new Date(time_selected.getTime() + (5 * 60000));
                                    time_after = new Date(time_selected.getTime() - (5 * 60000));
                                }
                                else{
                                    time_before = new Date(time_selected.getTime() + (Integer.parseInt(min_max_btn.getText().toString()) * 60000));
                                    time_after = new Date(time_selected.getTime() - (Integer.parseInt(min_max_btn.getText().toString()) * 60000));
                                }

                                if (!destin_et.getText().toString().equals("")) {
                                    final LatLng destination_ltlng = getLocationFromAddress(SearchCarpools.this, destin_et.getText().toString());

                                    Double l12 = destination_ltlng.latitude;
                                    Double l22 = destination_ltlng.longitude;
                                    String coordl12 = l12.toString();
                                    String coordl22 = l22.toString();
                                    String destination_string = coordl12 + "," + coordl22;

                                    intent.putExtra("destinationLocation", destination_string);

                                    for (int i = 0; i < carpoolsArray.length; i++) {
                                        final Carpool c = carpoolsArray[i];
                                        LatLng carpoolO = getLocationFromAddress(SearchCarpools.this, c.getOrigination());
                                        LatLng carpoolD = getLocationFromAddress(SearchCarpools.this, c.getDestination());

                                        Date carpool_time = new SimpleDateFormat("HH:mm").parse(c.getTime());

                                        if (c.getDate().equals(date_btn.getText().toString())) {
                                            if (carpool_time.after(time_after) && carpool_time.before(time_before)) {
                                                if(c.getPassengers().size() < c.getMaxPeople()){
                                                    if (SphericalUtil.computeDistanceBetween(pickup_ltlng, carpoolO) <= (c.getMaxPickupDistance() * 1000)) {
                                                        if (!destin_et.getText().toString().equals("")) {
                                                            if (SphericalUtil.computeDistanceBetween(destination_ltlng, carpoolD) <= 5000) {
                                                                searchResults.add(c);
                                                            }
                                                        } else {
                                                            searchResults.add(c);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < carpoolsArray.length; i++) {
                                        final Carpool c = carpoolsArray[i];
                                        LatLng carpoolO = getLocationFromAddress(SearchCarpools.this, c.getOrigination());
                                        LatLng carpoolD = getLocationFromAddress(SearchCarpools.this, c.getDestination());

                                        Date carpool_time = new SimpleDateFormat("HH:mm").parse(c.getTime());

                                        if (c.getDate().equals(date_btn.getText().toString())) {
                                            if (carpool_time.after(time_after) && carpool_time.before(time_before)) {
                                                if (c.getDate().equals(date_btn.getText().toString())) {
                                                    if (SphericalUtil.computeDistanceBetween(pickup_ltlng, carpoolO) <= (c.getMaxPickupDistance() * 1000)) {
                                                        searchResults.add(c);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Bundle args = new Bundle();
                                args.putSerializable("searchResults", (Serializable) searchResults);
                                intent.putExtra("BUNDLE", args);
                                intent.putExtra("pickupLocation", pickup_string);
                                intent.putExtra("originClass", "SearchCarpools");
                                intent.putExtra("time", time_btn.getText().toString());
                                intent.putExtra("date", date_btn.getText().toString());
                                getTimeTables(intent, pickup_string);
                                //startActivity(intent);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SearchCarpools.this, "Could not find Carpools in your area.", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SearchCarpools.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
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
                RequestQueue rQueue = Volley.newRequestQueue(SearchCarpools.this);
                rQueue.add(request);
            }

        });
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date_btn = (Button) findViewById(R.id.date_btn);
        date_btn.setText(dayOfMonth + "/" + (month+1) + "/" + year);
    }

    private void getTimeTables(final Intent intent, String pickup_string){
        try {
            date_entered = date_btn.getText().toString();
            SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
            Date dt1=format1.parse(date_entered);
            DateFormat format2=new SimpleDateFormat("EEEE");
            dayOfTheWeek = format2.format(dt1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            time_entered = new SimpleDateFormat("HH:mm").parse(time_btn.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LatLng loc = HelperMethods.getLocationFromAddress(SearchCarpools.this, pickup_string);

        BusStopResults bsr = BusSavedData.busStopResults;
        ArrayList<BusStop> results = new ArrayList<BusStop>();

        BusStop bs = new BusStop();
        BusStop bs2 = new BusStop();

        Double distance = 0.0;
        int pos = 0;

        for(int i = 0; i < bsr.getResults().size(); i++){
            LatLng bll = new LatLng(Double.parseDouble(BusSavedData.busStopResults.getResults().get(i).getLatitude()), Double.parseDouble(BusSavedData.busStopResults.getResults().get(i).getLongitude()));

            Double tempd = SphericalUtil.computeDistanceBetween(loc, bll);
            if(i == 0){
                pos = i;
                distance = tempd;
                bs = BusSavedData.busStopResults.getResults().get(i);
            }
            else{
                if(distance > tempd){
                    pos = i;
                    distance = tempd;
                    bs2 = bs;
                    bs = BusSavedData.busStopResults.getResults().get(i);
                }
            }
        }

        for(int i = 0; i < bsr.getResults().size(); i++){
            LatLng bll = new LatLng(Double.parseDouble(BusSavedData.busStopResults.getResults().get(i).getLatitude()), Double.parseDouble(BusSavedData.busStopResults.getResults().get(i).getLongitude()));

            Double tempd = SphericalUtil.computeDistanceBetween(loc, bll);
            if(i == 0){
                distance = tempd;
                bs2 = BusSavedData.busStopResults.getResults().get(i);
            }
            else{
                if(distance > tempd && BusSavedData.busStopResults.getResults().get(i) != bs){
                    distance = tempd;
                    bs2 = BusSavedData.busStopResults.getResults().get(i);
                }
            }
        }

        final BusStop busStop1 = bs;
        final BusStop busStop2 = bs2;

        int m = distance.intValue();
        System.out.println(m + "meters");
        System.out.println(bs.getStopid());
        System.out.println(bs.getFullname());

        System.out.println(bs2.getStopid());
        System.out.println(bs2.getFullname());

//        fetchBusInfromation(bs, 0, intent);

        String URL = "http://data.dublinked.ie/cgi-bin/rtpi/timetableinformation?type=week&stopid=" + busStop1.getStopid() + "&routeid=" + busStop1.getOperators().get(0).getRoutes().get(0) + "&format=json";
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new GsonBuilder().create();
                final TimeTableInformation tti = gson.fromJson(s, TimeTableInformation.class);

                String URL = "http://data.dublinked.ie/cgi-bin/rtpi/timetableinformation?type=week&stopid=" + busStop2.getStopid() + "&routeid=" + busStop2.getOperators().get(0).getRoutes().get(0) + "&format=json";
                StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new GsonBuilder().create();
                        final TimeTableInformation tti2 = gson.fromJson(s, TimeTableInformation.class);
                        String dest = "";
                        String dest2 = "";
                        LatLng timetableDestinationLatLng = new LatLng(0, 0);
                        LatLng timetableDestinationLatLng2 = new LatLng(0, 0);

                        //Converts the destination of a TimeTable to a LatLng
                        for(int i = 0; i < tti.getResults().size(); i++){
                            if(!tti.getResults().get(i).getDestination().equals("")){
                                dest = tti.getResults().get(i).getDestination();
                                String[] t = dest.split("[^A-Za-z0-9]");
                                dest = t[0] + ", Ireland";
                                System.out.println("Destination 1: " + dest);
                                timetableDestinationLatLng = getLocationFromAddress(SearchCarpools.this, dest);
                                 i = tti.getResults().size();
                            }
                        }

                        for(int i = 0; i < tti2.getResults().size(); i++){
                            if(!tti2.getResults().get(i).getDestination().equals("")){
                                dest2 = tti2.getResults().get(i).getDestination();
                                String[] t2 = dest2.split("[^A-Za-z0-9]");
                                dest2 = t2[0] + ", Ireland";
                                System.out.println("Destination 2: " + dest2);
                                timetableDestinationLatLng2 = getLocationFromAddress(SearchCarpools.this, dest2);
                                i = tti2.getResults().size();
                            }
                        }

//                        String dest = tti.getResults().get(0).getDestination() + ", Ireland";
//                        String[] t = dest.split("[\\\\s@&.?$+-]+");
//                        dest = t[0] + ", Ireland";

//                        String dest2 = tti2.getResults().get(0).getDestination() + ", Ireland";
//                        String[] t2 = dest2.split("[\\\\s@&.?$+-]+");
//                        dest2 = t2[0] + ", Ireland";
//
//                        LatLng timetableDestinationLatLng = getLocationFromAddress(SearchCarpools.this, dest);
//                        LatLng timetableDestinationLatLng2 = getLocationFromAddress(SearchCarpools.this, dest2);
                        LatLng destination_ltlng = getLocationFromAddress(SearchCarpools.this, destin_et.getText().toString());

                        if (SphericalUtil.computeDistanceBetween(destination_ltlng, timetableDestinationLatLng) < SphericalUtil.computeDistanceBetween(destination_ltlng, timetableDestinationLatLng2)) {
                            fetchBusInfromation(busStop1, 0, intent);
                            System.out.println("Destination: " + dest);
                        }
                        else{
                            fetchBusInfromation(busStop2, 0, intent);
                            System.out.println("Destination: " + dest2);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SearchCarpools.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
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

                RequestQueue rQueue = Volley.newRequestQueue(SearchCarpools.this);
                rQueue.add(request);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SearchCarpools.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
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
        RequestQueue rQueue = Volley.newRequestQueue(SearchCarpools.this);
        rQueue.add(request);
    }


    private void fetchBusInfromation(final BusStop busstop, final int counter, final Intent intent){
        String URL = "http://data.dublinked.ie/cgi-bin/rtpi/timetableinformation?type=week&stopid=" + busstop.getStopid() + "&routeid=" + busstop.getOperators().get(0).getRoutes().get(counter) + "&format=json";
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new GsonBuilder().create();
                TimeTableInformation tti = gson.fromJson(s, TimeTableInformation.class);
                //routeTimeTables.add(tti);
                //System.out.println(routeTimeTables.size());

                System.out.println("Route 1no.: " + tti.getRoute());
                String local_route = tti.getRoute();
                System.out.println("Route R.: " + local_route);

                for(int k = 0; k < tti.getResults().size(); k++){
                    if(tti.getResults().get(k).getStartdayofweek().equals(dayOfTheWeek) && tti.getResults().get(k).getEnddayofweek().equals(dayOfTheWeek)) {
                        //loop through the TimeTables and find the depoarture for the time entered
                        for(int m = 0; m < tti.getResults().get(k).getDepartures().size(); m++){
                            try {
                                Date current_timetable = new SimpleDateFormat("HH:mm:ss").parse(tti.getResults().get(k).getDepartures().get(m));
//                                if(m + 1 < tti.getResults().get(k).getDepartures().size()){
//                                    Date next_timetable = new SimpleDateFormat("HH:mm:ss").parse(tti.getResults().get(k).getDepartures().get(m+1));
//
//                                    if(time_entered.before(current_timetable)){
//                                        TimeTableForADay t = new TimeTableForADay(tti.getRoute(), tti.getResults().get(k).getDepartures().get(m), tti.getResults().get(k).getDestination(), tti.getResults().get(
//                                                k).getDestinationlocalized());
//                                        tempList.add(t);
//                                        m = tti.getResults().get(k).getDepartures().size()+1;
//                                    }
//                                    else if (time_entered.after(current_timetable) && time_entered.before(next_timetable)) {
//                                        System.out.println("Route 2no.: " + tti.getRoute());
//                                        TimeTableForADay t = new TimeTableForADay(tti.getRoute(), tti.getResults().get(k).getDepartures().get(m), tti.getResults().get(k).getDestination(), tti.getResults().get(
//                                                k).getDestinationlocalized());
//                                        tempList.add(t);
//                                        m = tti.getResults().get(k).getDepartures().size()+1;
//                                    }
//                                }
//                                else if(time_entered.before(current_timetable)){
//                                    System.out.println("derp");
//                                    TimeTableForADay t = new TimeTableForADay(tti.getRoute(), tti.getResults().get(k).getDepartures().get(m), tti.getResults().get(k).getDestination(), tti.getResults().get(
//                                            k).getDestinationlocalized());
//                                    tempList.add(t);
//                                    m = tti.getResults().get(k).getDepartures().size();
//                                }
                                if(time_entered.before(current_timetable) || (time_entered.getTime() == current_timetable.getTime())){
                                    System.out.println("derp");
                                    TimeTableForADay t = new TimeTableForADay(tti.getRoute(), tti.getResults().get(k).getDepartures().get(m), tti.getResults().get(k).getDestination(), tti.getResults().get(
                                            k).getDestinationlocalized());
                                    tempList.add(t);
                                    m = tti.getResults().get(k).getDepartures().size();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        k = tti.getResults().size();
                    }
                }

                if(counter+1 < busstop.getOperators().get(0).getRoutes().size()){
                    fetchBusInfromation(busstop, counter+1, intent);
                }
                else{
                    Bundle args = new Bundle();
                    args.putSerializable("timetables", (Serializable) tempList);
                    intent.putExtra("timetables_bundle", args);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SearchCarpools.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
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

        RequestQueue rQueue = Volley.newRequestQueue(SearchCarpools.this);
        rQueue.add(request);
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
    }
}
