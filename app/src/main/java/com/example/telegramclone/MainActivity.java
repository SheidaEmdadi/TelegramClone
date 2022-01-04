package com.example.telegramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.telegramclone.fragments.AllUsersFragment;
import com.example.telegramclone.fragments.FollowingFragment;
import com.example.telegramclone.fragments.TweetsFragment;
import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    NavigationView navigationView;
    TextView txtUserNameDrawer;
    ImageView profileImageDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
//        profileImageDrawer = findViewById(R.id.profile_image_drawer);



        View header = navigationView.getHeaderView(0);
        txtUserNameDrawer = (TextView) header.findViewById(R.id.username_drawer);
        profileImageDrawer = (ImageView) header.findViewById(R.id.profile_image_drawer);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ParseUser parseUser = ParseUser.getCurrentUser();

        if (parseUser.get("username") == null) {
            txtUserNameDrawer.setText("");
        } else {
            txtUserNameDrawer.setText(parseUser.get("username").toString());
        }

        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username", parseUser.getUsername());

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
                                    profileImageDrawer.setImageBitmap(bitmap);



                                }
                            }
                        });

                    }
                }else if(objects.size() == 0){
                    profileImageDrawer.setImageResource(R.drawable.ic_user);
                }
                else {
                    FancyToast.makeText(MainActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }

                dialog.dismiss();
            }
        });






        profileImageDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(i);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();




    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.all_users_item:

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,
                                new AllUsersFragment()
                        ).commit();


                break;
            case R.id.followed_users_item:

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,
                                new FollowingFragment()
                        ).commit();
                break;
            case R.id.tweets:

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,
                                new TweetsFragment()
                        ).commit();
                break;

            case R.id.profile_item:

                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

                break;

            case R.id.logout_item:

                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;


        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                drawer.closeDrawer(GravityCompat.START);
            }
        }, 300);
        return true;
    }


}



