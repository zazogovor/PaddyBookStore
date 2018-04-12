package com.example.c12437908.fypv2.carpool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.c12437908.fypv2.Entities.Carpool;
import com.example.c12437908.fypv2.MainActivity;
import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.map_location.DataParser;
import com.example.c12437908.fypv2.map_location.FetchUrl;
import com.example.c12437908.fypv2.map_location.GetDirectionsData;
import com.example.c12437908.fypv2.map_location.MapsActivity;
import com.example.c12437908.fypv2.not_relevant.LocationDetailsActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cinema on 1/16/2018.
 */

public class CarpoolMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    private String intentOption = "Nothing";
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(CarpoolMapFragment.this);
        buildGoogleApiClient();

        mGoogleApiClient.connect();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();

//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(CarpoolMapFragment.this);
//        buildGoogleApiClient();
//
//        mGoogleApiClient.connect();

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

    public LatLng parseStringLatLng(String s){
        String[] latLng = s.split(",");
        double latitude = Double.parseDouble(latLng[0]);
        double longitude = Double.parseDouble(latLng[1]);
        LatLng location = new LatLng(latitude, longitude);

        return location;
    }

    private String getDirectionsUrl(double latitude, double longitude, double end_latitude, double end_longitude, String[] waypoints)
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        if(waypoints != null){
            googleDirectionsUrl.append("&waypoints=");
//            for(int i = 0; i < waypoints.length; i++){
//                googleDirectionsUrl.append(waypoints[i]);
//                if(i+1 < waypoints.length){
//                    googleDirectionsUrl.append("|");
//                }
//            }
            googleDirectionsUrl.append(waypoints);
        }
        googleDirectionsUrl.append("&key="+"AIzaSyCAcfy-02UHSu2F6WeQ1rhQhkCr51eBL9g");

        return googleDirectionsUrl.toString();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(MainActivity.class.getSimpleName(), "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        View v = getView();
        mMap = googleMap;
        Intent intent = getActivity().getIntent();
        String originClass = intent.getStringExtra("originClass");

        if(originClass.equals("SearchCarpools")){
            Bundle args = intent.getBundleExtra("BUNDLE");
            ArrayList<Carpool> carpoolsArray = (ArrayList<Carpool>) args.getSerializable("searchResults");

            if(carpoolsArray.size() != 0) {
                for(int i = 0; i < carpoolsArray.size(); i++){
                    final Carpool c = carpoolsArray.get(i);
                    LatLng o = getLocationFromAddress(v.getContext(), c.getOrigination());
                    LatLng d = getLocationFromAddress(v.getContext(), c.getDestination());
                    final MarkerOptions markerO = new MarkerOptions()
                    .position(o)
                    .title("Destination: " + c.getDestination());
                     final Marker mO = mMap.addMarker(markerO);

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            marker = mO;
                            Intent i = new Intent(getContext(), CarpoolDetails.class);
                            Bundle args = new Bundle();
                            args.putSerializable("carpool", (Serializable) c);
                            i.putExtra("BUNDLE", args);
                            i.putExtra("carpoolID", c.getId());
                            i.putExtra("originClass", "CarpoolMapFragment");
                            startActivity(i);

                            return false;
                        }
                    });
                }
            }
        }
        else if(originClass.equals("CarpoolMapFragment")){
            Bundle args = intent.getBundleExtra("BUNDLE");
            Carpool c = (Carpool) args.get("carpool");
            LatLng o = getLocationFromAddress(v.getContext(), c.getOrigination());
            LatLng d = getLocationFromAddress(v.getContext(), c.getDestination());

            String passengersLocations = c.getPassengerMarkers();

            final MarkerOptions markerO = new MarkerOptions()
                    .position(o)
                    .title("Origination: " + c.getOrigination());
            final Marker mO = mMap.addMarker(markerO);

            final MarkerOptions markerD = new MarkerOptions()
                    .position(d)
                    .title("Destination: " + c.getDestination());
            final Marker mD = mMap.addMarker(markerD);


            if (o != null && d != null) {
                if(!passengersLocations.equals("")){
                    if(passengersLocations.contains("|")){
                        String[] arr = passengersLocations.split("\\|");
                        for(int i = 0; i < arr.length; i++){
                            final int j = i;
                            System.out.println(passengersLocations);
                            final LatLng l = parseStringLatLng(arr[i]);
                            final MarkerOptions marker = new MarkerOptions()
                                    .position(new LatLng(l.latitude, l.longitude))
                                    .title("Passenger "+i);

                            final Marker m = mMap.addMarker(marker);
                            //mMap.addMarker(markerO);
                        }
                    }
                    else{
                        final LatLng l = parseStringLatLng(passengersLocations);
                        final MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(l.latitude, l.longitude))
                                .title("Passenger 1");

                        final Marker m = mMap.addMarker(marker);
                    }

//                    Object dataTransfer[] = new Object[3];
//                    String url = getDirectionsUrl(o.latitude, o.longitude, d.latitude, d.longitude, passengersLocations);
//                    GetDirectionsData getDirectionsData = new GetDirectionsData();
//                    dataTransfer[0] = mMap;
//                    dataTransfer[1] = url;
//                    dataTransfer[2] = d;
//                    getDirectionsData.execute(dataTransfer);

                    String url = getDirectionsUrl(o.latitude, o.longitude, d.latitude, d.longitude, passengersLocations);
                    FetchUrl FetchUrl = new FetchUrl(mMap);

                    FetchUrl.execute(url);

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(d, 8.5f));
                }
                else{
//                    Object dataTransfer[] = new Object[3];
//                    String url = getDirectionsUrl(o.latitude, o.longitude, d.latitude, d.longitude, "");
//                    GetDirectionsData getDirectionsData = new GetDirectionsData();
//                    dataTransfer[0] = mMap;
//                    dataTransfer[1] = url;
//                    dataTransfer[2] = d;
//                    getDirectionsData.execute(dataTransfer);

                    String url = getDirectionsUrl(o.latitude, o.longitude, d.latitude, d.longitude, passengersLocations);
                    FetchUrl FetchUrl = new FetchUrl(mMap);

                    FetchUrl.execute(url);

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(d, 8.5f));
                }
            }

        }

//        View v = getView();
//
//        String origination_address = getActivity().getIntent().getStringExtra("origination");
//        String destination_address = getActivity().getIntent().getStringExtra("destiantion");
//        String[] passengersLocations = getActivity().getIntent().getStringArrayExtra("passengersLocations");
//
//        LatLng origination_latlng = getLocationFromAddress(v.getContext(), origination_address);
//        final MarkerOptions markerO = new MarkerOptions()
//                .position(new LatLng(origination_latlng.latitude, origination_latlng.longitude))
//                .title("Origination");
//        final Marker mO = mMap.addMarker(markerO);
//
//        LatLng destination_latlng = getLocationFromAddress(v.getContext(), destination_address);
//        final MarkerOptions markerD = new MarkerOptions()
//                .position(new LatLng(destination_latlng.latitude, destination_latlng.longitude))
//                .title("Destination");
//        final Marker mD = mMap.addMarker(markerD);
//
//
//        if(passengersLocations != null){
//            for(int i = 0; i < passengersLocations.length; i++){
//                final int j = i;
//                final LatLng l = parseStringLatLng(passengersLocations[i]);
//                final MarkerOptions marker = new MarkerOptions()
//                        .position(new LatLng(l.latitude, l.longitude))
//                        .title("Passenger "+i);
//
//                final Marker m = mMap.addMarker(marker);
//                //mMap.addMarker(markerO);
//            }
//        }
//
//        Object dataTransfer[] = new Object[3];
//        String url = getDirectionsUrl(origination_latlng.latitude, origination_latlng.longitude, destination_latlng.latitude, destination_latlng.longitude, passengersLocations);
//        GetDirectionsData getDirectionsData = new GetDirectionsData();
//        dataTransfer[0] = mMap;
//        dataTransfer[1] = url;
//        dataTransfer[2] = origination_latlng;
//        getDirectionsData.execute(dataTransfer);
    }

    private String getDirectionsUrl(double latitude, double longitude, double end_latitude, double end_longitude, String waypoints)
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        if(!waypoints.equals("")){
            googleDirectionsUrl.append("&waypoints="+waypoints);
        }

        googleDirectionsUrl.append("&key="+"AIzaSyCAcfy-02UHSu2F6WeQ1rhQhkCr51eBL9g");

        return googleDirectionsUrl.toString();
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
