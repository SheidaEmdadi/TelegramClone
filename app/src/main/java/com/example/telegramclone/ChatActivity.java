package com.example.telegramclone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.telegramclone.Adapters.MessageAdapter;
import com.example.telegramclone.utils.ModelClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

//    private ListView chatListView;
    //    private ArrayList<String> chatsList;
//    private ArrayAdapter adapter;
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



        selectedUser = getIntent().getStringExtra("selectedUser");


        findViewById(R.id.fab).setOnClickListener(this);
//        chatsList = new ArrayList();


        rv = findViewById(R.id.rvChat);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();


//        chatListView = findViewById(R.id.chatListView);
        txtChatName = findViewById(R.id.txtChatName);

        txtChatName.setText(selectedUser);


        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


//        adapter = new ArrayAdapter(this, R.layout.card_received,
//                R.id.textViewReceived, chatsList);
//        chatListView.setAdapter(adapter);
//
//
//        adapter = new ArrayAdapter(this, R.layout.list1_clone2, chatsList);


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


//                            chatListView.setAdapter(messageAdapter);


//                            String message = chatObject.get("message") + "";
//
//
//                            if (chatObject.get("sender").equals(ParseUser.getCurrentUser().getUsername())) {
//
//                                message = ParseUser.getCurrentUser().getUsername() + ": " + message;
////                                messageText.setText(message);
//
//                            }
//                            if (chatObject.get("sender").equals(selectedUser)) {
//
//                                message = selectedUser + ": " + message;
////                                messageText.setText(message);
//
//                            }


//                            chatsList.add(message);
                        }


//                        adapter.notifyDataSetChanged();

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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



