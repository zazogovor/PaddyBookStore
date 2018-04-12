package com.example.c12437908.fypv2.carpool;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.c12437908.fypv2.PagerAdapters.CarpoolListPagerAdapter;
import com.example.c12437908.fypv2.Entities.Carpool;
import com.example.c12437908.fypv2.MainActivity;
import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.register_login.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cinema on 12/18/2017.
 */

public class ListCarpools extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private ActionBar actionBar;
    TextView driver_textview, origination_textview, destination_textview, date_textview, time_textview, empty_list_title;
    RelativeLayout register_btn, login_btn, carpool_btn, home_btn, carpool_info_btn, logout_btn;
    LinearLayout carpool_layout;
    SessionManager session;

    List<Carpool> createdCarpools = new ArrayList<Carpool>();
    List<Carpool> joinedCarpools = new ArrayList<Carpool>();

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchCarpools();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_carpools_list);

        session = new SessionManager(getApplicationContext());

        final Intent intent = getIntent();
        carpool_layout = (LinearLayout) findViewById(R.id.carpool_list);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("My Carpools"));
        tabLayout.addTab(tabLayout.newTab().setText("Joined Carpools"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final CarpoolListPagerAdapter adapter = new CarpoolListPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
        register_btn = (RelativeLayout) findViewById(R.id.register_btn);
        login_btn = (RelativeLayout) findViewById(R.id.login_btn);
        carpool_btn = (RelativeLayout) findViewById(R.id.carpool_btn);
        home_btn = (RelativeLayout) findViewById(R.id.home_btn);
        logout_btn = (RelativeLayout) findViewById(R.id.logout_btn);

        register_btn.setVisibility(View.GONE);
        login_btn.setVisibility(View.GONE);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListCarpools.this, MainActivity.class));
            }
        });

        carpool_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListCarpools.this, CarpoolMenu.class));
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                startActivity(new Intent(ListCarpools.this, MainActivity.class));
            }
        });
    }

    private void fetchCarpools(){
        //empty_list_title = (TextView) findViewById(R.id.empty_list_title);
        //volley call to get carpools
        String URL = "http://10.0.2.2:8080/api/carpool/carpools";

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Toast.makeText(ListCarpools.this, "Your Carpools fetched successfully!", Toast.LENGTH_LONG).show();

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
                            for(int j = 0; j < carpoolsArray[i].getPassengers().size(); i++){
                                if(carpoolsArray[i].getPassengers().get(j).getUsername().equals(session.getUsername())){
                                    joinedCarpools.add(carpoolsArray[i]);
                                }
                            }
                        }

                    }
                }


                //add carpools to the view
//                if(carpoolsArray.length != 0){
//                    carpool_layout.setVisibility(View.VISIBLE);
//                    empty_list_title.setVisibility(View.GONE);
//
//                    ViewGroup parent = (ViewGroup) findViewById(R.id.carpool_list);
//                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    for(int i = 0; i < carpoolsArray.length; i++){
//                        final Carpool c = carpoolsArray[i];
//                        View view = inflater.inflate(R.layout.list_item_carpool, null);
//
//                        driver_textview = (TextView) view.findViewById(R.id.driver);
//                        origination_textview = (TextView) view.findViewById(R.id.origination);
//                        destination_textview = (TextView) view.findViewById(R.id.destination);
//                        date_textview = (TextView) view.findViewById(R.id.date);
//                        time_textview = (TextView) view.findViewById(R.id.time);
//                        carpool_info_btn = (RelativeLayout) view.findViewById(R.id.carpool_info_btn);
//
//                        driver_textview.setText(c.getDriver().getUsername());
//                        origination_textview.setText(c.getOrigination());
//                        destination_textview.setText(c.getDestination());
//                        date_textview.setText(c.getDate());
//                        time_textview.setText(c.getTime());
//
//                        carpool_info_btn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent i = new Intent(ListCarpools.this, CarpoolDetails.class);
//                                i.putExtra("carpoolID", c.getId());
//                                startActivity(i);
//                            }
//                        });
//                        parent.addView(view);
//                    }
//                }
//                else {
//                    carpool_layout.setVisibility(View.GONE);
//                    empty_list_title.setVisibility(View.VISIBLE);
//                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ListCarpools.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
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

        RequestQueue rQueue = Volley.newRequestQueue(ListCarpools.this);
        rQueue.add(request);
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
