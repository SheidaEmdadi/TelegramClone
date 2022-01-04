package com.example.telegramclone.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.telegramclone.ChatActivity;
import com.example.telegramclone.MainActivity;
import com.example.telegramclone.R;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class FollowingFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    ImageView imgProfileCard;

    public FollowingFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following, container, false);


        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refreshLayout);

//        imgProfileCard =view.findViewById(R.id.imgProfileCard);

        View v = getLayoutInflater().inflate(R.layout.card_following, container);
        imgProfileCard = (ImageView)v.findViewById(R.id.imgProfileCard);

        listView = view.findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), R.layout.card_following,
                R.id.txtCardFollowing, arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        TextView txtLoadingUsers = view.findViewById(R.id.txtLoadingUsers);

        listView.setOnItemClickListener(FollowingFragment.this);
        listView.setOnItemLongClickListener(FollowingFragment.this);
        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereContainedIn("username", ParseUser.getCurrentUser().getList("following"));

//            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
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

                            }
                            else{
                                txtLoadingUsers.setText("you should follow users to chat with them and see their tweets! " +
                                        "\n" +
                                        "to follow others, visit \" All users \" tab and click on their name.");
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

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

                }
            });



            ParseQuery<ParseObject> parseQuery1 = new ParseQuery<ParseObject>("Photo");
            parseQuery1.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setMessage("Loading...");
            dialog.show();

            parseQuery1.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {

                        for (ParseObject post : objects) {

                            ParseFile postPicture = (ParseFile) post.get("picture");
                            postPicture.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if (data != null && e == null) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        imageViewParams.setMargins(5, 5, 5, 5);
                                        imgProfileCard.setImageBitmap(bitmap);



                                    }
                                }
                            });

                        }
                    }else if(objects.size() == 0){
                        imgProfileCard.setImageResource(R.drawable.ic_user);
                    }
                    else {
                        FancyToast.makeText(getContext(), e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                    }

                    dialog.dismiss();
                }
            });
//todo:

        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("selectedUser",arrayList.get(position));
        startActivity(intent);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
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
