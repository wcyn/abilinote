package com.learnmine.abilinote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by baa on 9/28/2016.
 */


public class HomeTabFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(MainActivity.LOG_TAG, "Inflating!");
        return inflater.inflate(R.layout.home_tab_cont, container, false);
    }
}