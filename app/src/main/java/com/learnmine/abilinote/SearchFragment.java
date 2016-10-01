package com.learnmine.abilinote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by baa on 9/28/2016.
 */


public class SearchFragment extends ListFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(MainActivity.LOG_TAG, "Inflating Search tab!");
        super.onActivityCreated(savedInstanceState);
        String[] values = new String[]{"Blue", "Red", "Yellow", "Green", "Purple", "Orange"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        getListView().setDivider(ContextCompat.getDrawable(getActivity(),
                android.R.color.darker_gray));
        getListView().setDividerHeight(1);
    }



}