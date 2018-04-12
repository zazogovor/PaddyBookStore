package com.example.c12437908.fypv2.BookHelper;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.carpool.CreateCarpool;
import com.example.c12437908.fypv2.register_login.SessionManager;

/**
 * Created by cinema on 4/12/2018.
 */

public class FragmentBookDetails extends Fragment implements NumberPicker.OnValueChangeListener{

    private ImageView bookImage;
    private EditText title_et, author_et, isbn_et, price_et, stock_et;
    private RatingBar rating;
    private Button edit_btn, buy_btn, save_btn;
    private SessionManager session;

    private int quantity = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carpool_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        session = new SessionManager(getActivity().getApplicationContext());

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

        title_et.setEnabled(false);
        author_et.setEnabled(false);
        isbn_et.setEnabled(false);
        price_et.setEnabled(false);
        stock_et.setEnabled(false);


        //get passed in intent and assign values to book



        if(session.isLoggedIn()){

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
                        quantity = np.getValue();
                        d.dismiss();


                        //intent stuff

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
