package com.example.telegramclone.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.telegramclone.fragments.ContactsTweetsTab;
import com.example.telegramclone.fragments.MyTweetsTab;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int tabPosition) {
        switch (tabPosition) {
            case 0:
                ContactsTweetsTab contactsTweetsTab = new ContactsTweetsTab();
                return contactsTweetsTab;

            case 1:
                MyTweetsTab MyTweetsTab = new MyTweetsTab();
            return MyTweetsTab;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Contacts tweets";

            case 1:
                return "My tweets";


            default:
                return null;
        }
    }
}
