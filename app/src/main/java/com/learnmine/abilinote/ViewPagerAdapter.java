package com.learnmine.abilinote;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by baa on 9/28/2016.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(MainActivity.LOG_TAG, "Position: " + position);
        switch (position) {
            case 0:
                return new HomeTabFragment();
            case 1:
                return new NotesTabFragment();

        }
        return new HomeTabFragment();
    }

    @Override
    public int getCount() {
        Log.d(MainActivity.LOG_TAG, "Getting count!");
        return 2;           // As there are only 2 Tabs
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Home";
            case 1:
                return "My Notes";
        }
        return super.getPageTitle(position);
    }

}