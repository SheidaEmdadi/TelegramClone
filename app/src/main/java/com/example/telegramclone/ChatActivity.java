package com.example.telegramclone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.telegramclone.Adapters.MessageAdapter;
import com.example.telegramclone.utils.ModelClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.List;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgProfileChat;

    private String selectedUser;
    private TextView txtChatName;

    MessageAdapter messageAdapter;
    List<ModelClass> list;
    RecyclerView rv;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        imgProfileChat = findViewById(R.id.imgProfileChat);
        selectedUser = getIntent().getStringExtra("selectedUser");


        findViewById(R.id.fab).setOnClickListener(this);


        rv = findViewById(R.id.rvChat);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();


        txtChatName = findViewById(R.id.txtChatName);

        txtChatName.setText(selectedUser);


        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayShowTitleEnabled(false);





        try {

            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chats");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chats");

            firstUserChatQuery.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("recipient", selectedUser);

            secondUserChatQuery.whereEqualTo("sender", selectedUser);
            secondUserChatQuery.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());


            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {

                            String message = chatObject.get("message") + "";
                            String sender = chatObject.get("sender") + "";

                            ModelClass model = new ModelClass(message, sender);

                            list.add(model);
                            messageAdapter = new MessageAdapter(list, sender);

                            messageAdapter.notifyDataSetChanged();

                            rv.scrollToPosition(list.size() - 1);
                            rv.setAdapter(messageAdapter);

                           }


                        }



                    }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                                    imgProfileChat.setImageBitmap(bitmap);



                                }
                            }
                        });

                    }
                }else if(objects.size() == 0){
                    imgProfileChat.setImageResource(R.drawable.ic_user);
                }
                else {
                    FancyToast.makeText(ChatActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }

                dialog.dismiss();
            }
        });



    }


    @Override
    public void onClick(View v) {

        final EditText edtMessage = findViewById(R.id.edittext_chatbox);

        ParseObject chat = new ParseObject("Chats");
        chat.put("sender", ParseUser.getCurrentUser().getUsername());
        chat.put("recipient", selectedUser);
        chat.put("message", edtMessage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    String message = edtMessage.getText().toString();
                    String sender = ParseUser.getCurrentUser().getUsername();

                    ModelClass model = new ModelClass(message, sender);

                    list.add(model);
                    messageAdapter = new MessageAdapter(list, sender);

                    messageAdapter.notifyDataSetChanged();

                    rv.scrollToPosition(list.size() - 1);
                    rv.setAdapter(messageAdapter);

                    edtMessage.setText("");
                }
            }
        });
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



