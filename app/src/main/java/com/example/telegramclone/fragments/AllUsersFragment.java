package com.example.telegramclone.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.example.telegramclone.ChatActivity;
import com.example.telegramclone.R;
import com.example.telegramclone.TweetActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class AllUsersFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
//    private String followedUser;

    public AllUsersFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_users, container, false);

        FancyToast.makeText(getContext(), "long press on user to see profile", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();


        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refreshLayout);

        listView = view.findViewById(R.id.listView);
        arrayList = new ArrayList();

        arrayAdapter = new ArrayAdapter(getContext(), R.layout.list1_clone, arrayList);
//        arrayAdapter = new ArrayAdapter(getContext(), R.layout.list1_clone3,
//                R.id.checked_text3, arrayList);
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

    // for adding *following* attribute you need to change the type of list to simple.list.item.checked(line 49 , 50),
//    then you need to do this lines below ( 82 - 107)
    @Override
    public void onItemClick (AdapterView < ? > parent, View view,int position, long id){


//        CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.checked_text3);
        CheckedTextView checkedTextView = (CheckedTextView) view;

        if (checkedTextView.isChecked()) {
            FancyToast.makeText(getContext(), arrayList.get(position) + " is followed", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
            ParseUser.getCurrentUser().add("following", arrayList.get(position));
        } else {
            FancyToast.makeText(getContext(), arrayList.get(position) + " is unfollowed", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
            ParseUser.getCurrentUser().getList("following").remove(arrayList.get(position));
            List currentUserFollowingList = ParseUser.getCurrentUser().getList("following");
            ParseUser.getCurrentUser().remove("following");
            ParseUser.getCurrentUser().put("following", currentUserFollowingList);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
//                    FancyToast.makeText(getContext(), "saved", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                } else {
                    FancyToast.makeText(getContext(), e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                }
            }
        });
    }

//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        return false;
//    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", arrayList.get(position));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null) {

                    final FlatDialog flatDialog = new FlatDialog(getActivity());
                    flatDialog.setTitle(user.getUsername())
                            .setSubtitle("Bio: " + user.get("profileBio")  + "\n")
                            .setIcon(R.drawable.ic_userr)
                            .setBackgroundColor(R.color.fd_bg)
                            .setFirstButtonColor(R.color.blue)
                            .setSecondButtonColor(R.color.fd_sc)
                            .setFirstButtonText("Tweets")
                            .setSecondButtonText("Cancel")
                            .withFirstButtonListner(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), TweetActivity.class);
                                    intent.putExtra("selectedUser",arrayList.get(position));
                                    startActivity(intent);
                                }
                            })
                            .withSecondButtonListner(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    flatDialog.dismiss();
                                }
                            })
                            .show();


                }
            }
        });

        return true;
    }
}
