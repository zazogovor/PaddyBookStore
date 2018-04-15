package com.example.c12437908.fypv2.BookActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.c12437908.fypv2.Entities.BasketEntity;
import com.example.c12437908.fypv2.Entities.Book;
import com.example.c12437908.fypv2.MainActivity;
import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.register_login.LoginActivity;
import com.example.c12437908.fypv2.register_login.RegisterActivity;
import com.example.c12437908.fypv2.register_login.SessionManager;

/**
 * Created by c12437908 on 10/04/2018.
 */

public class Basket  extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private ActionBar actionBar;
    private RelativeLayout register_btn, login_btn, home_btn, logout_btn, books_btn, basket_btn, book_info_btn;
    private TextView title_tv, author_tv, isbn_tv, price_tv, stock_tv, total_tv;
    private Button purchase_btn;

    SessionManager session;

    @Override
    protected void onRestart() {
        super.onRestart();
        setButtonListeners();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

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
        BasketEntity basket = BasketEntity.getINSTANCE();
        Boolean loginState = session.isLoggedIn();

        register_btn = (RelativeLayout) findViewById(R.id.register_btn);
        login_btn = (RelativeLayout) findViewById(R.id.login_btn);
        home_btn = (RelativeLayout) findViewById(R.id.home_btn);
        logout_btn = (RelativeLayout) findViewById(R.id.logout_btn);
        books_btn = (RelativeLayout) findViewById(R.id.books_btn);
        basket_btn = (RelativeLayout) findViewById(R.id.basket_btn);
        purchase_btn = (Button) findViewById(R.id.purchase_btn);
        total_tv = (TextView) findViewById(R.id.total_tv);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Basket.this, MainActivity.class));
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
                    startActivity(new Intent(Basket.this, RegisterActivity.class));
                }
            });

            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Basket.this, LoginActivity.class));
                }
            });
        }

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                startActivity(new Intent(Basket.this, MainActivity.class));
            }
        });

        books_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Basket.this, BooksMenu.class));
            }
        });

        basket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Basket.this, Basket.class));
            }
        });

        purchase_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Basket.this, Payment.class));
            }
        });

        if(basket.getBooks().size() != 0){
            Double total = 0.00;
            ViewGroup parent = (ViewGroup) findViewById(R.id.book_list);
            LayoutInflater inflater = (LayoutInflater) this.getLayoutInflater();
            for(int i = 0; i < basket.getBooks().size(); i++){
                final Book book = basket.getBooks().get(i).getBook();
                View view = inflater.inflate(R.layout.list_item_book, null);

                Double items_price = book.getPrice() * basket.getBooks().get(i).getAmount();
                total += items_price;

                title_tv = (TextView) view.findViewById(R.id.title_tv);
                author_tv = (TextView) view.findViewById(R.id.author_tv);
                isbn_tv = (TextView) view.findViewById(R.id.isbn_tv);
                price_tv = (TextView) view.findViewById(R.id.price_tv);
                stock_tv = (TextView) view.findViewById(R.id.stock_tv);
                book_info_btn = (RelativeLayout) view.findViewById(R.id.book_info_btn);

                title_tv.setText(book.getTitle());
                author_tv.setText(book.getAuthor());
                isbn_tv.setText(book.getISBN());
                price_tv.setText("" + book.getPrice());
                stock_tv.setText("" + basket.getBooks().get(i).getAmount());

                parent.addView(view);
            }
            total_tv.setText("" + total);
        }
    }
}
