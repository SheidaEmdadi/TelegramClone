package com.example.telegramclone.fragments;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.telegramclone.ChatActivity;
import com.example.telegramclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class ChatScreenFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
//    private String followedUser;

    public ChatScreenFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_screen, container, false);


        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refreshLayout);

        listView = view.findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), R.layout.list1_clone2, arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        TextView txtLoadingUsers = view.findViewById(R.id.txtLoadingUsers);

        listView.setOnItemClickListener(ChatScreenFragment.this);
        listView.setOnItemLongClickListener(ChatScreenFragment.this);
        try {

            ParseQuery<ParseObject> query = new ParseQuery("User");
            query.setLimit(1000);
            query.whereEqualTo("username", ParseUser.getCurrentUser());
            ParseQuery<ParseObject> parseInnerQuery = new ParseQuery<>("Chats");



            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereContainedIn("username", ParseUser.getCurrentUser().getList("chatList"));

            parseQuery.findInBackground(new FindCallback<ParseUser>() {
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


            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(getContext(), "saved", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                    } else {
                        FancyToast.makeText(getContext(), e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                    }
                }
            });
            //todo inam dorost kon
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            ParseQuery<ParseUser> parseQuery1 = ParseUser.getQuery();
                            parseQuery1.whereContainedIn("username", ParseUser.getCurrentUser().getList("following"));
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


        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseQuery<ParseUser> parseQuery1 = ParseUser.getQuery();
        parseQuery1.whereContainedIn("username", ParseUser.getCurrentUser().getList("following"));
        parseQuery1.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseQuery1.whereNotContainedIn("username", arrayList);
        parseQuery1.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects.size() > 0) {
                    if (e == null) {
                        for (ParseUser user : objects) {
                            arrayList.add(user.getUsername());
//                            ParseUser.getCurrentUser().getList("chatList").remove(arrayList.get(position));
                            List currentUserChattingList = ParseUser.getCurrentUser().getList("chatList");
                            ParseUser.getCurrentUser().remove("chatList");
                            ParseUser.getCurrentUser().put("chatList", currentUserChattingList);
                        }
                        arrayAdapter.notifyDataSetChanged();

                    }
                } else {
                    ParseUser.getCurrentUser().add("chatList", arrayList.get(position));

                }
            }
        });
//        if (checkedTextView.isChecked()) {
//            FancyToast.makeText(getContext(), arrayList.get(position) + " is followed", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
//            ParseUser.getCurrentUser().add("chatList", arrayList.get(position));
//        } else {
////            FancyToast.makeText(getContext(), arrayList.get(position) + " is unfollowed", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
//            ParseUser.getCurrentUser().getList("chatList").remove(arrayList.get(position));
//            List currentUserChattingList = ParseUser.getCurrentUser().getList("chatList");
//            ParseUser.getCurrentUser().remove("chatList");
//            ParseUser.getCurrentUser().put("chatList", currentUserChattingList);
//        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                } else {
                    FancyToast.makeText(getContext(), e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                }
            }
        });
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("selectedUser",arrayList.get(position));
        startActivity(intent);
        return true;
    }

//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

//        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
//        parseQuery.whereEqualTo("username", arrayList.get(position));
//        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if (user != null && e == null) {
//
//                    final FlatDialog flatDialog = new FlatDialog(getActivity());
//                    flatDialog.setTitle(user.getUsername())
//                            .setSubtitle("Bio: " + user.get("profileBio") + "\n" +
//                                    "Profession: " + user.get("profileProfession") + "\n" +
//                                    "Hobbies: " + user.get("profileHobbies") + "\n" +
//                                    "Favourite Sports: " + user.get("profileFavSport"))
//                            .setIcon(R.drawable.person)
//                            .setFirstButtonText("Posts")
//                            .setSecondButtonText("CANCEL")
//                            .withFirstButtonListner(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(getContext(), UsersPostsActivity.class);
//                                    intent.putExtra("username", arrayList.get(position));
//                                    startActivity(intent);
//                                }
//                            })
//                            .withSecondButtonListner(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    flatDialog.dismiss();
//                                }
//                            })
//                            .show();
//
//
//                }
//            }
//        });

//        return true;
//    }
}
