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


public class ContactsTweetsTab extends Fragment {

    private ListView viewTweetsListView;
    private ArrayAdapter adapter;

    public ContactsTweetsTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_tweets_tab, container, false);

        viewTweetsListView = view.findViewById(R.id.viewContactsTweetsListView);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refreshLayoutCT);
        TextView txtLoadingTweets= view.findViewById(R.id.txtLoadingContactsTweets);

        viewTweetsListView = view.findViewById(R.id.viewContactsTweetsListView);


        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();

//        adapter = new ArrayAdapter(getContext(), R.layout.card_contacts_tweet,
//                R.id.txtCardContactsTweetName, R.id.txtCardContactsTweetValue, tweetList);
        final SimpleAdapter adapter = new SimpleAdapter(getContext(), tweetList, R.layout.list2_clone, new String[]{"tweetUserName", "tweetValue"}, new int[]{android.R.id.text1, android.R.id.text2});
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("following"));
            parseQuery.orderByDescending("createdAt");
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if ( e == null) {
                        if(objects.size() > 0) {
                            for (ParseObject tweetObject : objects) {
                                HashMap<String, String> userTweet = new HashMap<>();
                                userTweet.put("tweetUserName", tweetObject.getString("user"));
                                userTweet.put("tweetValue", tweetObject.getString("tweet"));
                                tweetList.add(userTweet);

                            }

                            viewTweetsListView.setAdapter(adapter);
                            txtLoadingTweets.animate().alpha(0).setDuration(2000);
                            viewTweetsListView.setVisibility(View.VISIBLE);

                        }else{
                            txtLoadingTweets.setText("There's no tweets yet! " +
                                    "\n" +
                                    "If you haven't followed anyone, you should follow first!");
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
                parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("following"));
                parseQuery.orderByDescending("createdAt");



// I just needed to add this line of code below,
//so the list wouldn't be duplicated on each refresh

                tweetList.clear();

                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects.size() > 0) {
                            if (e == null) {
                                for (ParseObject tweetObject : objects) {
                                    HashMap<String, String> userTweet = new HashMap<>();
                                    userTweet.put("tweetUserName", tweetObject.getString("user"));
                                    userTweet.put("tweetValue", tweetObject.getString("tweet"));
                                    tweetList.add(userTweet);

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