package com.example.telegramclone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telegramclone.R;
import com.example.telegramclone.SendTweetActivity;
import com.example.telegramclone.Adapters.TabAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class TweetsFragment extends Fragment {

    private androidx.appcompat.widget.Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;

    private FloatingActionButton fabTweet;


    public TweetsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);

        fabTweet = view.findViewById(R.id.fabTweet);


//        getActivity().setTitle("Tweets");
//
//        toolbar = view.findViewById(R.id.myToolBar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        viewPager = view.findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager, false);


        fabTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SendTweetActivity.class);
                startActivity(intent);
            }
        });



        return view;


    }
}