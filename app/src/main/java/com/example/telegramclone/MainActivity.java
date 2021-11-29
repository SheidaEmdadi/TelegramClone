package com.example.telegramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.telegramclone.fragments.AllUsersFragment;
import com.example.telegramclone.fragments.ContactsFragment;
import com.example.telegramclone.fragments.InviteFriendsFragment;
import com.example.telegramclone.fragments.NewGroupFragment;
import com.example.telegramclone.fragments.SavedMessagesFragment;
import com.example.telegramclone.fragments.SettingsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    TextView txtUserNameDrawer;
    String name;
//    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
//        txt = findViewById(R.id.txttes);

        View header = navigationView.getHeaderView(0);
        txtUserNameDrawer = (TextView) header.findViewById(R.id.username_drawer);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ParseUser user = new ParseUser();
        String objId = user.getObjectId();

//        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
//        query.getInBackground(objId, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                if (object != null && e == null) {
//                    name = object.get("username").toString();
////                   txt.setText(name);
//                    //todo: profile name
//                } else if (e != null) {
//                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        txtUserNameDrawer.setText(name);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_container,
                            new NewGroupFragment()
                    ).commit();
            navigationView.setCheckedItem(R.id.nav_view);
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // User chose the "Search" item, show the app settings UI...
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.chat_screen_item:

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,
                                new NewGroupFragment()
                        ).commit();
//                Intent mintent = new Intent(MainActivity.this, ChatScreenActivity.class);
//                startActivity(mintent);
                break;
            case R.id.all_users_item:

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,
                                new AllUsersFragment()
                        ).commit();
                break;
            case R.id.followed_users_item:

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,
                                new ContactsFragment()
                        ).commit();
                break;
            case R.id.invite_item:

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,
                                new InviteFriendsFragment()
                        ).commit();
                break;
            case R.id.saved_msg_item:

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,
                                new SavedMessagesFragment()
                        ).commit();
                break;
            case R.id.settings_item:

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,
                                new SettingsFragment()
                        ).commit();
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

//todo: tu safe asli vaqti back mizani az acc logout nashe
//todo 2: ax profile
//todo 3 : esm profile namayesh nemide
//todo 4 : ta yekio follow nakoni nemitoni bahash chat koni