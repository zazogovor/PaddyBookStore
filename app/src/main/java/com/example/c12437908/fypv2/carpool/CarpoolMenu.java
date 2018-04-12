package com.example.c12437908.fypv2.carpool;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.example.c12437908.fypv2.BusAPI.BusStop;
import com.example.c12437908.fypv2.BusAPI.BusStopResults;
import com.example.c12437908.fypv2.register_login.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cinema on 12/18/2017.
 */

public class CarpoolMenu extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private ActionBar actionBar;
    RelativeLayout register_btn, login_btn, carpool_btn, home_btn, list_btn, search_btn, add_btn, logout_btn;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_menu);

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
        list_btn = (RelativeLayout) findViewById(R.id.my_carpools_btn);
        search_btn = (RelativeLayout) findViewById(R.id.search_carpool_btn);
        add_btn = (RelativeLayout) findViewById(R.id.create_carpool_btn);
        logout_btn = (RelativeLayout) findViewById(R.id.logout_btn);

        register_btn.setVisibility(View.GONE);
        login_btn.setVisibility(View.GONE);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarpoolMenu.this, MainActivity.class));
            }
        });

        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //empty_list_title = (TextView) findViewById(R.id.empty_list_title);
                //volley call to get carpools
                final List<Carpool> createdCarpools = new ArrayList<Carpool>();
                final List<Carpool> joinedCarpools = new ArrayList<Carpool>();
                String URL = "http://10.0.2.2:8080/api/carpool/carpools";

                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(CarpoolMenu.this, "Your Carpools fetched successfully!", Toast.LENGTH_LONG).show();

                        //String s is a JSON string now
                        Gson gson = new GsonBuilder().create();
                        Carpool[] carpoolsArray = gson.fromJson(s, Carpool[].class);
                        System.out.println(carpoolsArray.length);

                        for(int i = 0; i < carpoolsArray.length; i++){
                            if(carpoolsArray[i].getDriver().getUsername().equals(session.getUsername())){
                                createdCarpools.add(carpoolsArray[i]);
                            }
                            else{
                                if(carpoolsArray[i].getPassengers().size() > 0){
                                    for(int j = 0; j < carpoolsArray[i].getPassengers().size(); j++){
                                        if(carpoolsArray[i].getPassengers().get(j).getUsername().equals(session.getUsername())){
                                            joinedCarpools.add(carpoolsArray[i]);
                                        }
                                    }
                                }

                            }
                        }
                        Intent intent = new Intent(CarpoolMenu.this, ListCarpools.class);

                        Bundle args = new Bundle();
                        args.putSerializable("createdCarpools", (Serializable) createdCarpools);
                        intent.putExtra("BUNDLE_CREATED", args);

                        Bundle args1 = new Bundle();
                        args1.putSerializable("joinedCarpools", (Serializable) joinedCarpools);
                        intent.putExtra("BUNDLE_JOINED", args1);

                        startActivity(intent);
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(CarpoolMenu.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
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

                RequestQueue rQueue = Volley.newRequestQueue(CarpoolMenu.this);
                rQueue.add(request);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarpoolMenu.this, SearchCarpools.class));
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarpoolMenu.this, CreateCarpool.class));
            }
        });

        carpool_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarpoolMenu.this, CarpoolMenu.class));
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                startActivity(new Intent(CarpoolMenu.this, MainActivity.class));
            }
        });

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
}
