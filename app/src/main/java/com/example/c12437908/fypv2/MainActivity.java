package com.example.c12437908.fypv2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.example.c12437908.fypv2.BookActivities.Basket;
import com.example.c12437908.fypv2.BookActivities.BooksMenu;
import com.example.c12437908.fypv2.register_login.LoginActivity;
import com.example.c12437908.fypv2.register_login.RegisterActivity;
import com.example.c12437908.fypv2.register_login.SessionManager;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private ActionBar actionBar;
    RelativeLayout register_btn, login_btn, carpool_btn, home_btn, logout_btn, books_btn, basket_btn, account_btn;

    SessionManager session;


    @Override
    protected void onRestart() {
        super.onRestart();
        setButtonListeners();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return hideKeyboard();
            }
        });

        setButtonListeners();
    }

    private void setButtonListeners() {
        session = new SessionManager(getApplicationContext());
        Boolean loginState = session.isLoggedIn();
        session.saveBasket();

        register_btn = (RelativeLayout) findViewById(R.id.register_btn);
        login_btn = (RelativeLayout) findViewById(R.id.login_btn);
        home_btn = (RelativeLayout) findViewById(R.id.home_btn);
        logout_btn = (RelativeLayout) findViewById(R.id.logout_btn);
        books_btn = (RelativeLayout) findViewById(R.id.books_btn);
        basket_btn = (RelativeLayout) findViewById(R.id.basket_btn);
        account_btn = (RelativeLayout) findViewById(R.id.account_btn);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        if(loginState){
            register_btn.setVisibility(View.GONE);
            login_btn.setVisibility(View.GONE);
            account_btn.setVisibility(View.VISIBLE);

            account_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, UserDetails.class));
                }
            });
        }
        else
        {
            register_btn.setVisibility(View.VISIBLE);
            login_btn.setVisibility(View.VISIBLE);
            account_btn.setVisibility(View.GONE);

            register_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                }
            });

            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
        }

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        books_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BooksMenu.class));
            }
        });

        basket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Basket.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_websearch).setVisible(false);
        return super.onCreateOptionsMenu(menu);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        hideKeyboard();
        return super.onPrepareOptionsMenu(menu);
    }

    private boolean hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
