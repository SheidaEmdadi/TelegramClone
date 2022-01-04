package com.example.telegramclone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.telegramclone.R;
import com.example.telegramclone.SendTweetActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyTweetsTab extends Fragment {

    private ListView viewTweetsListView;
    private ArrayAdapter adapter;

    public MyTweetsTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tweets_tab, container, false);

        viewTweetsListView = view.findViewById(R.id.viewMyTweetsListView);
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refreshLayoutMyT);
        TextView txtLoadingMyTweets= view.findViewById(R.id.txtLoadingMyTweets);

        viewTweetsListView = view.findViewById(R.id.viewMyTweetsListView);


        final ArrayList<String> tweetList = new ArrayList<>();


        adapter = new ArrayAdapter(getContext(), R.layout.card_my_tweet,
                R.id.txtCardMyTweet, tweetList);

        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
            parseQuery.orderByDescending("createdAt");
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if ( e == null) {
                        if(objects.size() > 0) {
                            for (ParseObject tweetObject : objects) {
                                tweetList.add( tweetObject.getString("tweet"));


                            }

                            viewTweetsListView.setAdapter(adapter);
                            txtLoadingMyTweets.animate().alpha(0).setDuration(2000);
                            viewTweetsListView.setVisibility(View.VISIBLE);

                        }else{
                            txtLoadingMyTweets.setText("you haven't shared any tweets yet! " +
                                    "\n" +
                                    "to share a tweet, use the button below the screen!");
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
                parseQuery.orderByDescending("createdAt");
                parseQuery.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());



// I just needed to add this line of code below,
//so the list wouldn't be duplicated on each refresh

                tweetList.clear();

                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects.size() > 0) {
                            if (e == null) {
                                for (ParseObject tweetObject : objects) {
                                    tweetList.add( tweetObject.getString("tweet"));


                                }
                                adapter.notifyDataSetChanged();
                                if (swipeRefreshLayout.isRefreshing()) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }

                                viewTweetsListView.setAdapter(adapter);
                            }
                        }

                    }
                });

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });



        return view;
    }
}