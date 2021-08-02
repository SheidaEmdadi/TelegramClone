package com.example.telegramclone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.telegramclone.fragments.CallsFragment;
import com.example.telegramclone.fragments.ContactsFragment;
import com.example.telegramclone.fragments.InviteFriendsFragment;
import com.example.telegramclone.fragments.NewGroupFragment;
import com.example.telegramclone.fragments.SavedMessagesFragment;
import com.example.telegramclone.fragments.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            case R.id.group_item:

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,
                                new NewGroupFragment()
                        ).commit();
                break;
            case R.id.call_item:

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,
                                new CallsFragment()
                        ).commit();
                break;
            case R.id.contacts_item:

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


        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}