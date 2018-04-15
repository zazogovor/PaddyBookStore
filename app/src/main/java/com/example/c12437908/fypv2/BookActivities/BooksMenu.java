package com.example.c12437908.fypv2.BookActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.c12437908.fypv2.MainActivity;
import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.register_login.LoginActivity;
import com.example.c12437908.fypv2.register_login.RegisterActivity;
import com.example.c12437908.fypv2.register_login.SessionManager;

/**
 * Created by c12437908 on 11/04/2018.
 */

public class BooksMenu extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private ActionBar actionBar;
    RelativeLayout register_btn, login_btn, home_btn, logout_btn, books_btn, basket_btn;
    Button list_books_btn, search_books_btn, add_book_btn;

    SessionManager session;

    @Override
    protected void onRestart() {
        super.onRestart();
        setButtonListeners();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_menu);

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
        Boolean loginState = session.isLoggedIn();

        register_btn = (RelativeLayout) findViewById(R.id.register_btn);
        login_btn = (RelativeLayout) findViewById(R.id.login_btn);
        home_btn = (RelativeLayout) findViewById(R.id.home_btn);
        logout_btn = (RelativeLayout) findViewById(R.id.logout_btn);
        books_btn = (RelativeLayout) findViewById(R.id.books_btn);
        basket_btn = (RelativeLayout) findViewById(R.id.basket_btn);

        list_books_btn = (Button) findViewById(R.id.list_books_btn);
        search_books_btn = (Button) findViewById(R.id.search_books_btn);
        add_book_btn = (Button) findViewById(R.id.add_book_btn);


        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BooksMenu.this, MainActivity.class));
            }
        });

        if(loginState){
            register_btn.setVisibility(View.GONE);
            login_btn.setVisibility(View.GONE);
        }
        else
        {
            register_btn.setVisibility(View.VISIBLE);
            login_btn.setVisibility(View.VISIBLE);

            register_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BooksMenu.this, RegisterActivity.class));
                }
            });

            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BooksMenu.this, LoginActivity.class));
                }
            });
        }

        if(session.getUser().getType().equals("ADMIN")){
            add_book_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BooksMenu.this, CreateBook.class));
                }
            });
        }
        else{
            add_book_btn.setVisibility(View.GONE);
        }

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                startActivity(new Intent(BooksMenu.this, MainActivity.class));
            }
        });

        books_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BooksMenu.this, BooksMenu.class));
            }
        });

        basket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BooksMenu.this, Basket.class));
            }
        });

        list_books_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BooksMenu.this, ListBooks.class));
            }
        });

        search_books_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BooksMenu.this, SearchBooks.class));
            }
        });
    }
}
