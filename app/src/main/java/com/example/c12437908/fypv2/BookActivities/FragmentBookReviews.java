package com.example.c12437908.fypv2.BookActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.c12437908.fypv2.Entities.Book;
import com.example.c12437908.fypv2.Entities.Review;
import com.example.c12437908.fypv2.R;
import com.example.c12437908.fypv2.register_login.LoginActivity;
import com.example.c12437908.fypv2.register_login.SessionManager;

/**
 * Created by cinema on 4/12/2018.
 */

public class FragmentBookReviews extends Fragment{


    private SessionManager session;
    TextView postee_tv, comment_tv;
    RatingBar rating;
    Button createReview_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_reviews, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();

        session = new SessionManager(getActivity().getApplicationContext());
        final Intent intent = getActivity().getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        final Book b = (Book) args.get("book");


        createReview_btn = (Button) v.findViewById(R.id.createReview_btn);
        createReview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.isLoggedIn()){
                    Intent i = new Intent(getContext(), CreateReview.class);
                    Bundle args = new Bundle();
                    args.putSerializable("book", b);
                    i.putExtra("BUNDLE", args);
                    startActivity(i);
                }
                else{
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });


        if(b.getReviews().size() != 0){
            ViewGroup parent = (ViewGroup) v.findViewById(R.id.review_list);
            LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
            for(int i =0; i < b.getReviews().size(); i++){
                final Review r = b.getReviews().get(i);
                View view = inflater.inflate(R.layout.list_item_review, null);

                postee_tv = (TextView) view.findViewById(R.id.postee_tv);
                comment_tv = (TextView) view.findViewById(R.id.comment_tv);
                rating = (RatingBar) view.findViewById(R.id.rating);

                postee_tv.setText(b.getReviews().get(i).getPostee().getUsername());
                comment_tv.setText(b.getReviews().get(i).getComment());
                rating.setRating(b.getReviews().get(i).getRating());

                parent.addView(view);
            }
        }

    }
}
