package com.example.telegramclone;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.telegramclone.Adapters.MessageAdapter;
import com.example.telegramclone.utils.ModelClass;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TweetActivity extends AppCompatActivity implements View.OnClickListener {


    private String selectedUser;
    private ListView viewTweetsListView;
    private TextView txtTweeterName;
    private ArrayAdapter adapter;
    ImageView imgProfileTweet;





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        Toolbar toolbar = findViewById(R.id.toolbarTweet);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        selectedUser = getIntent().getStringExtra("selectedUser");

        txtTweeterName = findViewById(R.id.txtChatName);
        imgProfileTweet = findViewById(R.id.imgProfileTweetView);


        txtTweeterName.setText(selectedUser + " 's tweets");


        viewTweetsListView = findViewById(R.id.viewTweetsListView);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refreshLayoutTweet);
        TextView txtLoadingMyTweets= findViewById(R.id.txtLoadingTweet);


        final ArrayList<String> tweetList = new ArrayList<>();


//        adapter = new ArrayAdapter(this, R.layout.list1_clone2, tweetList);
        adapter = new ArrayAdapter(this, R.layout.card_tweet,
                R.id.txtCardTweet, tweetList);
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereEqualTo("user", selectedUser);
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
                            txtLoadingMyTweets.setText("This user hasn't shared any tweets yet! ");
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
                parseQuery.whereEqualTo("user", selectedUser);



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
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username", selectedUser);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
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
                                    imgProfileTweet.setImageBitmap(bitmap);



                                }
                            }
                        });

                    }
                }else if(objects.size() == 0){
                    imgProfileTweet.setImageResource(R.drawable.ic_user);
                }
                else {
                    FancyToast.makeText(TweetActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }

                dialog.dismiss();
            }
        });


    }


    @Override
    public void onClick(View v) {

//        final EditText edtMessage = findViewById(R.id.edittext_chatbox);
//
//        ParseObject chat = new ParseObject("Chats");
//        chat.put("sender", ParseUser.getCurrentUser().getUsername());
//        chat.put("recipient", selectedUser);
//        chat.put("message", edtMessage.getText().toString());
//        chat.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    String message = edtMessage.getText().toString();
//                    String sender = ParseUser.getCurrentUser().getUsername();
//
//                    ModelClass model = new ModelClass(message, sender);
//
//                    list.add(model);
//                    messageAdapter = new MessageAdapter(list, sender);
//
//                    messageAdapter.notifyDataSetChanged();
//
//                    rv.scrollToPosition(list.size() - 1);
//                    rv.setAdapter(messageAdapter);
//
//                    edtMessage.setText("");
//                }
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}



