package com.example.telegramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.telegramclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView chatListView;
    private ArrayList<String> chatsList;
    private ArrayAdapter adapter;
    private String selectedUser;
    private LinearLayout chatBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        selectedUser = getIntent().getStringExtra("selectedUser");

        findViewById(R.id.button_chatbox_send).setOnClickListener(this);
        chatsList = new ArrayList();

        chatBox = findViewById(R.id.layout_chatbox);


        chatListView = findViewById(R.id.chatListView);

        adapter = new ArrayAdapter(this, R.layout.list1_clone2, chatsList);
        chatListView.setAdapter(adapter);

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


                            if (chatObject.get("sender").equals(ParseUser.getCurrentUser().getUsername())) {
                                message = ParseUser.getCurrentUser().getUsername() + ": " + message;
//                                messageText.setText(message);
//                                nameText.setText(ParseUser.getCurrentUser().getUsername());

                            }
                            if (chatObject.get("sender").equals(selectedUser)) {
                                message = selectedUser + ": " + message;
//                                messageText.setText(message);
//                                nameText.setText(ParseUser.getCurrentUser().getUsername());

                            }

                            chatsList.add(message);
                        }
                        adapter.notifyDataSetChanged();

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
                    chatsList.add(ParseUser.getCurrentUser().getUsername()+": "+ edtMessage.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtMessage.setText("");
                }
            }
        });
    }




}



