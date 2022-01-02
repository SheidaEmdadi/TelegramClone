package com.example.telegramclone.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.telegramclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class AllUsersTest extends Fragment {



    public AllUsersTest() {
        // Required empty public constructor
    }


    RecyclerView rv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_users_test2, container, false);

         rv = view.findViewById(R.id.rvUsers);
         rv.setLayoutManager(new LinearLayoutManager(getContext()));


        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refreshLayout);

        listView = view.findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), R.layout.list1_clone, arrayList);
//        arrayAdapter = new ArrayAdapter(getContext(), R.layout.card_all_users,
//                R.id.txtCardAllUsers, arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        TextView txtLoadingUsers = view.findViewById(R.id.txtLoadingUsers);

        listView.setOnItemClickListener(AllUsersFragment.this);
        listView.setOnItemLongClickListener(AllUsersFragment.this);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                try {
                    if (e == null) {
                        if (users.size() > 0) {
                            for (ParseUser user : users) {
                                arrayList.add(user.getUsername());
                            }
                            listView.setAdapter(arrayAdapter);
                            txtLoadingUsers.animate().alpha(0).setDuration(2000);
                            listView.setVisibility(View.VISIBLE);

                            for (String user : arrayList) {
                                if (ParseUser.getCurrentUser().getList("following") != null) {
                                    if (ParseUser.getCurrentUser().getList("following").contains(user)) {

//                                    followedUser = user + ",";

                                        listView.setItemChecked(arrayList.indexOf(user), true);

//                                    FancyToast.makeText(getContext(), ParseUser.getCurrentUser().getUsername() + " is following " + followedUser, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ParseQuery<ParseUser> parseQuery1 = ParseUser.getQuery();
                        parseQuery1.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

                        parseQuery1.whereNotContainedIn("username", arrayList);
                        parseQuery1.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {

                                if (objects.size() > 0) {
                                    if (e == null) {
                                        for (ParseUser user : objects) {
                                            arrayList.add(user.getUsername());
                                        }
                                        arrayAdapter.notifyDataSetChanged();
                                        if (swipeRefreshLayout.isRefreshing()) {
                                            swipeRefreshLayout.setRefreshing(false);
                                        }
                                    }
                                } else {
                                    if (swipeRefreshLayout.isRefreshing()) {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }

                            }
                        });
                    }
                });

            }
        });






        return view;







    }
}