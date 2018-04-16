package com.example.c12437908.fypv2.BookActivities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.c12437908.fypv2.Entities.BasketEntity;
import com.example.c12437908.fypv2.Entities.Book;
import com.example.c12437908.fypv2.Entities.User;
import com.example.c12437908.fypv2.MainActivity;
import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.register_login.LoginActivity;
import com.example.c12437908.fypv2.register_login.SessionManager;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cinema on 4/12/2018.
 */

public class FragmentBookDetails extends Fragment implements NumberPicker.OnValueChangeListener{

    private ImageView bookImage;
    private EditText title_et, author_et, isbn_et, price_et, stock_et;
    private RatingBar rating;
    private Button edit_btn, buy_btn, save_btn;
    private SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        session = new SessionManager(getActivity().getApplicationContext());
        final Intent intent = getActivity().getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        final Book b = (Book) args.get("book");

        bookImage = (ImageView) v.findViewById(R.id.book_img);
        title_et = (EditText) v.findViewById(R.id.title_et);
        author_et = (EditText) v.findViewById(R.id.author_et);
        isbn_et = (EditText) v.findViewById(R.id.isbn_et);
        price_et = (EditText) v.findViewById(R.id.price_et);
        stock_et = (EditText) v.findViewById(R.id.stock_et);
        rating = (RatingBar) v.findViewById(R.id.ratingBar);
        edit_btn = (Button) v.findViewById(R.id.edit_btn);
        buy_btn = (Button) v.findViewById(R.id.buy_btn);
        save_btn = (Button) v.findViewById(R.id.save_btn);

        save_btn.setVisibility(View.INVISIBLE);

        title_et.setText(b.getTitle());
        author_et.setText(b.getAuthor());
        isbn_et.setText(b.getISBN());
        price_et.setText("" + b.getPrice());
        stock_et.setText("" + b.getQuantity());
        rating.setNumStars(b.getRating());

        title_et.setEnabled(false);
        author_et.setEnabled(false);
        isbn_et.setEnabled(false);
        price_et.setEnabled(false);
        stock_et.setEnabled(false);

        if(session.isLoggedIn()){
            if(session.getUser().getType().equals("ADMIN")){
                edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        save_btn.setVisibility(View.VISIBLE);

                        title_et.setEnabled(true);
                        author_et.setEnabled(true);
                        isbn_et.setEnabled(true);
                        price_et.setEnabled(true);
                        stock_et.setEnabled(true);

                        save_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String URL = "http://10.0.2.2:8080/api/book/edit";
                                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String s) {
                                        Toast.makeText(getContext(), "Book successfully modified. ", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getContext(), ListBooks.class));
                                    }
                                },new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(getContext(), "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> parameters = new HashMap<String, String>();
                                        parameters.put("id", "" + b.getId());
                                        parameters.put("title", title_et.getText().toString());
                                        parameters.put("author", author_et.getText().toString());
                                        parameters.put("ISBN", isbn_et.getText().toString());
                                        parameters.put("price", price_et.getText().toString());
                                        parameters.put("stock", stock_et.getText().toString());
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

                                RequestQueue rQueue = Volley.newRequestQueue(getContext());
                                rQueue.add(request);
                            }
                        });
                    }
                });

            }
            else{
                edit_btn.setVisibility(View.GONE);
                save_btn.setVisibility(View.GONE);
            }
        }
        else{
            edit_btn.setVisibility(View.GONE);
            save_btn.setVisibility(View.GONE);
        }

        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog d = new Dialog(v.getContext());
                d.setTitle("NumberPicker");
                d.setContentView(R.layout.number_dialog);
                Button b1 = (Button) d.findViewById(R.id.button1);
                Button b2 = (Button) d.findViewById(R.id.button2);
                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                np.setMaxValue(100);
                np.setMinValue(0);
                np.setWrapSelectorWheel(false);
                np.setOnValueChangedListener(FragmentBookDetails.this);
                b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        if(np.getValue() > b.getQuantity()){
                            Toast.makeText(getContext(), "Amount selected exceeds current stock!", Toast.LENGTH_LONG).show();
                            d.dismiss();
                        }
                        else{
                            BasketEntity bb = BasketEntity.getINSTANCE();
                            bb.addBookToBasket(b, np.getValue());

    //                      Trying to save the singleton BasketEntity isntance to SharedPreferences so its state is saved between sessions
    //                      BasketEntity be = session.getBasket();
    //                      be.addBookToBasket(b, np.getValue());
    //                      session.saveBasket();

                            d.dismiss();
                            startActivity(new Intent(getContext(), ListBooks.class));
                        }
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
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
    }
}
