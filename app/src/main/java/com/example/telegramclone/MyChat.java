//package com.example.telegramclone;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.parse.FindCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//import com.parse.ParseUser;
//import com.parse.SaveCallback;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MyChat extends AppCompatActivity {
//
//    private ImageView imgBack;
//    private TextView txtChat;
//    private EditText edtMessage;
//    private FloatingActionButton fab;
//    private RecyclerView rvChat;
//    String username, otherName;
//    private String selectedUser;
//
//    MessageAdapter adapter;
//    ArrayList<ModelClass> list;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_chat);
//
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        selectedUser = getIntent().getStringExtra("selectedUser");
//
//
//        imgBack = findViewById(R.id.imageViewBack);
//        txtChat = findViewById(R.id.textViewChat);
//        edtMessage = findViewById(R.id.edtMessage);
//        fab = findViewById(R.id.fab);
//        rvChat = findViewById(R.id.rvChat);
//
//        rvChat.setLayoutManager(new LinearLayoutManager(this));
//        list = new ArrayList();
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final EditText edtMessage = findViewById(R.id.edittext_chatbox);
//
//                ParseObject chat = new ParseObject("Chats");
//                chat.put("sender", ParseUser.getCurrentUser().getUsername());
//                chat.put("recipient", selectedUser);
//                chat.put("message", edtMessage.getText().toString());
//                chat.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            list.add( edtMessage.getText().toString() ,  ParseUser.getCurrentUser().getUsername()+": ");
//                            adapter.notifyDataSetChanged();
//                            edtMessage.setText("");
//                        }
//                    }
//                });
//            }
//        });
//        getMessage();
//
//
//
////        rvChat.setLayoutManager(new LinearLayoutManager(this));
////        list = new ArrayList<>();
//
//        try {
//
//            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chats");
//            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chats");
//
//            firstUserChatQuery.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
//            firstUserChatQuery.whereEqualTo("recipient", selectedUser);
//
//            secondUserChatQuery.whereEqualTo("sender", selectedUser);
//            secondUserChatQuery.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());
//
//            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
//            allQueries.add(firstUserChatQuery);
//            allQueries.add(secondUserChatQuery);
//
//            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
//            myQuery.orderByAscending("createdAt");
//
//            myQuery.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> objects, ParseException e) {
//
//                    if (objects.size() > 0 && e == null) {
//                        for (ParseObject chatObject : objects) {
//                            String message = chatObject.get("message") + "";
//
//
//                            if (chatObject.get("sender").equals(ParseUser.getCurrentUser().getUsername())) {
//                                message = ParseUser.getCurrentUser().getUsername() + ": " + message;
////                                messageText.setText(message);
////                                nameText.setText(ParseUser.getCurrentUser().getUsername());
//
//                            }
//                            if (chatObject.get("sender").equals(selectedUser)) {
//                                message = selectedUser + ": " + message;
////                                messageText.setText(message);
////                                nameText.setText(ParseUser.getCurrentUser().getUsername());
//
//                            }
//
//                            list.add(message);
//                        }
//                        adapter.notifyDataSetChanged();
//
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    public void getMessage() {
//
//    }
//}