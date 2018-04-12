package com.example.c12437908.fypv2.carpool;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
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
import com.example.c12437908.fypv2.MainActivity;
import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.map_location.DataParser;
import com.example.c12437908.fypv2.map_location.FetchUrl;
import com.example.c12437908.fypv2.not_relevant.CreateLocationActivity;
import com.example.c12437908.fypv2.not_relevant.DBManager;
import com.example.c12437908.fypv2.not_relevant.LocationDetailsActivity;
import com.example.c12437908.fypv2.register_login.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cinema on 12/18/2017.
 */

public class CreateCarpool extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NumberPicker.OnValueChangeListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Button date_btn, people_btn, create_btn, time_btn, max_pickup_btn;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private ActionBar actionBar;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private EditText origination, destination;
    private RelativeLayout register_btn, login_btn, carpool_btn, home_btn, logout_btn;
    private Circle circle;
    SessionManager session;

    private GoogleMap mMap;
    private String intentOption = "Nothing";
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LatLng destination_latlng, origination_latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_carpool);

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(CreateCarpool.this);
        buildGoogleApiClient();
    }

    private void setButtonListeners() {
        session = new SessionManager(getApplicationContext());

        register_btn = (RelativeLayout) findViewById(R.id.register_btn);
        login_btn = (RelativeLayout) findViewById(R.id.login_btn);
        carpool_btn = (RelativeLayout) findViewById(R.id.carpool_btn);
        home_btn = (RelativeLayout) findViewById(R.id.home_btn);
        logout_btn = (RelativeLayout) findViewById(R.id.logout_btn);

        date_btn = (Button) findViewById(R.id.date_btn);
        people_btn = (Button) findViewById(R.id.people_btn);
        create_btn = (Button) findViewById(R.id.create_btn);
        time_btn = (Button) findViewById(R.id.time_btn);
        create_btn = (Button) findViewById(R.id.create_btn);
        max_pickup_btn = (Button) findViewById(R.id.max_pickup_btn);

        origination = (EditText) findViewById(R.id.origination_edittext);
        destination = (EditText) findViewById(R.id.destination_edittext);

        register_btn.setVisibility(View.GONE);
        login_btn.setVisibility(View.GONE);

        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return hideKeyboard();
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateCarpool.this, MainActivity.class));
            }
        });

        carpool_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateCarpool.this, CarpoolMenu.class));
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                startActivity(new Intent(CreateCarpool.this, MainActivity.class));
            }
        });

        int startYear = Calendar.getInstance().get(Calendar.YEAR);
        int starthMonth = Calendar.getInstance().get(Calendar.MONTH);
        int startDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                CreateCarpool.this, CreateCarpool.this, startYear, starthMonth, startDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        date_btn.setText(startDay + "/" + (starthMonth+1) + "/" + startYear);
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        people_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog d = new Dialog(CreateCarpool.this);
                d.setTitle("NumberPicker");
                d.setContentView(R.layout.number_dialog);
                Button b1 = (Button) d.findViewById(R.id.button1);
                Button b2 = (Button) d.findViewById(R.id.list_books_btn);
                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                np.setMaxValue(100);
                np.setMinValue(0);
                np.setWrapSelectorWheel(false);
                np.setOnValueChangedListener(CreateCarpool.this);
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

                final Dialog d = new Dialog(CreateCarpool.this);
                d.setTitle("NumberPicker");
                d.setContentView(R.layout.number_dialog);
                Button b1 = (Button) d.findViewById(R.id.button1);
                Button b2 = (Button) d.findViewById(R.id.list_books_btn);
                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                np.setMaxValue(100);
                np.setMinValue(0);
                np.setWrapSelectorWheel(false);
                np.setOnValueChangedListener(CreateCarpool.this);
                b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        max_pickup_btn.setText(String.valueOf(np.getValue()));

                        if(origination_latlng != null){
                            circle = mMap.addCircle(new CircleOptions()
                                    .center(origination_latlng)
                                    .radius(np.getValue()*1000)
                                    .strokeColor(Color.RED)
                                    .strokeWidth(5));
                        }


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
                mTimePicker = new TimePickerDialog(CreateCarpool.this, new TimePickerDialog.OnTimeSetListener() {
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

        origination.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!origination.getText().toString().equals("")) {
                        origination_latlng = getLocationFromAddress(CreateCarpool.this, origination.getText().toString());
                        final MarkerOptions markerO = new MarkerOptions()
                                .position(origination_latlng)
                                .title("Origin");

                        final Marker m = mMap.addMarker(markerO);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origination_latlng, 10.0f));

                        if (origination_latlng != null && destination_latlng != null) {
//                            Object dataTransfer[] = new Object[3];
//                            String url = getDirectionsUrl(origination_latlng.latitude, origination_latlng.longitude, destination_latlng.latitude, destination_latlng.longitude);
//                            GetDirectionsData getDirectionsData = new GetDirectionsData();
//                            dataTransfer[0] = mMap;
//                            dataTransfer[1] = url;
//                            dataTransfer[2] = origination_latlng;
//                            getDirectionsData.execute(dataTransfer);

                            String url = getDirectionsUrl(origination_latlng.latitude, origination_latlng.longitude, destination_latlng.latitude, destination_latlng.longitude);
                            FetchUrl FetchUrl = new FetchUrl(mMap);

                            FetchUrl.execute(url);
                        }
                    }
                }
            }
        });

        destination.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!destination.getText().toString().equals("")) {
                        destination_latlng = getLocationFromAddress(CreateCarpool.this, destination.getText().toString());
                        final MarkerOptions markerO = new MarkerOptions()
                                .position(destination_latlng)
                                .title("Destination");

                        final Marker m = mMap.addMarker(markerO);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination_latlng, 10.0f));

                        if (destination_latlng != null && origination_latlng != null) {
//                            Object dataTransfer[] = new Object[3];
//                            String url = getDirectionsUrl(origination_latlng.latitude, origination_latlng.longitude, destination_latlng.latitude, destination_latlng.longitude);
//                            GetDirectionsData getDirectionsData = new GetDirectionsData();
//                            dataTransfer[0] = mMap;
//                            dataTransfer[1] = url;
//                            dataTransfer[2] = destination_latlng;
//                            getDirectionsData.execute(dataTransfer);

                            String url = getDirectionsUrl(origination_latlng.latitude, origination_latlng.longitude, destination_latlng.latitude, destination_latlng.longitude);
                            FetchUrl FetchUrl = new FetchUrl(mMap);

                            FetchUrl.execute(url);
                        }
                    }
                }
            }
        });

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String date = date_btn.getText().toString();
                final String time = time_btn.getText().toString();
                final String driver_username = session.getUsername();
                final String orig = origination.getText().toString();
                final String dest = destination.getText().toString();
                final String maxPeople = people_btn.getText().toString();
                final String maxPickupDistance = max_pickup_btn.getText().toString();

                String URL = "http://10.0.2.2:8080/api/carpool/add";
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("1")){
                            startActivity(new Intent(CreateCarpool.this, MainActivity.class));
                            Toast.makeText(CreateCarpool.this, "Carpool successfully created!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(CreateCarpool.this, "Could not create the Carpool, try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(CreateCarpool.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("date", date);
                        parameters.put("time", time);
                        parameters.put("driver", driver_username);
                        parameters.put("maxPassengers", maxPeople);
                        parameters.put("origination", orig);
                        parameters.put("destination", dest);
                        parameters.put("maxPickupDistance", maxPickupDistance);

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

                RequestQueue rQueue = Volley.newRequestQueue(CreateCarpool.this);
                rQueue.add(request);
            }
        });
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
//    Is used with GetDirectionsData, DataParser and commented out code in currentLocation() method
//    to draw a path/direction on the map activity
    private String getDirectionsUrl(double latitude, double longitude, double end_latitude, double end_longitude)
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&key="+"AIzaSyCAcfy-02UHSu2F6WeQ1rhQhkCr51eBL9g");

        return googleDirectionsUrl.toString();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date_btn = (Button) findViewById(R.id.date_btn);
        date_btn.setText(dayOfMonth + "/" + (month+1) + "/" + year);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //get intent and pass in the location to zoom to
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringExtra("Option") != null)
                intentOption = intent.getStringExtra("Option");
        }

        if(intentOption.equals("NearbyLocations")){
            LatLng local = new LatLng(intent.getDoubleExtra("Lat", 0), intent.getDoubleExtra("Lon", 0));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, 15.0f));
        }
        else if(intentOption.equals("Directions")){
        }


//        for(int i = 0; i < db.getAllLocals().size(); i++){
//            final int j = i;
//            final MarkerOptions markerO = new MarkerOptions()
//                    .position(new LatLng(Double.parseDouble(db.getAllLocals().get(i).getLat()), Double.parseDouble(db.getAllLocals().get(i).getLon())))
//                    .title(db.getAllLocals().get(i).getDescription());
//
//            final Marker m = mMap.addMarker(markerO);
//            //mMap.addMarker(markerO);
//            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    marker = m;
//                    Intent intent = new Intent(CreateCarpool.this, LocationDetailsActivity.class);
//                    intent.putExtra("LOCATION_ID", db.getAllLocals().get(j).getId());
//                    startActivity(intent);
//
//                    return false;
//                }
//            });
//            System.out.println(db.getAllLocals().get(i).getLat() + "---" + db.getAllLocals().get(i).getLon());
//        }

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng point) {
//                MainActivity.incrementCounter();
//
//                MarkerOptions marker = new MarkerOptions()
//                        .position(new LatLng(point.latitude, point.longitude))
//                        .title("Location no." + MainActivity.getCounter());
//                mMap.addMarker(marker);
//
//                Intent intent = new Intent(CreateCarpool.this, CreateLocationActivity.class);
//                intent.putExtra("lat", point.latitude);
//                intent.putExtra("lon", point.longitude);
//                startActivity(intent);
//            }
//        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(MainActivity.class.getSimpleName(), "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private boolean hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
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








    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }
}
