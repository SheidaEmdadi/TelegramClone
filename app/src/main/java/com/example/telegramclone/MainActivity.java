package com.example.telegramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;


import com.example.telegramclone.Adapters.TabAdapter;
import com.example.telegramclone.fragments.AllUsersFragment;
import com.example.telegramclone.fragments.FollowingFragment;
import com.example.telegramclone.fragments.TweetsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    NavigationView navigationView;
    TextView txtUserNameDrawer;
    ImageView profileImageView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();



//        FloatingActionButton fab = findViewById(R.id.fabChat);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, Users.class);
//                startActivity(intent);
//            }
//        });


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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                // User chose the "Search" item, show the app settings UI...
//                return true;
//
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

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

//                getSupportFragmentManager().beginTransaction().
//                        replace(R.id.fragment_container,
//                                new ProfileFragment()
//                        ).commit();

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
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


}



//todo 2: ax profile
//todo 4 : ta yekio follow nakoni nemitoni bahash chat koni