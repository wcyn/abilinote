package com.learnmine.abilinote;


import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends ListFragment {
    private ArrayList<Note> notes;
    private NoteAdapter noteAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        String[] values = new String[]{"Blue", "Red", "Yellow", "Green", "Purple", "Orange"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_list_item_1, values);
//        setListAdapter(adapter);
        notes = new ArrayList<Note>();
        notes.add(new Note("New Note Title", "Body of our Note", Note.Category.FINANCE));
        notes.add(new Note("Personal Note", "Remeber to remember stuff, okay?", Note.Category.PERSONAL));
        notes.add(new Note("Another Quote Note", "Yes, that rhymes, just like extraordinary quotes should", Note.Category.QUOTE));
        notes.add(new Note("Finance Note", "The Body of a note on Finance", Note.Category.FINANCE));
        notes.add(new Note("Technical Note", "An example of something pretty technical", Note.Category.TECHNICAL));
        notes.add(new Note("My Favorite Quote", "My Favorite Quote has to be the Quote by Abraham Lincoln", Note.Category.QUOTE));notes.add(new Note("Another Quote Note", "Yes, that rhymes, just like extraordinary quotes should", Note.Category.QUOTE));
        notes.add(new Note("Finance Note", "The Body of a note on Finance", Note.Category.FINANCE));
        notes.add(new Note("Technical Note", "An example of something pretty technical", Note.Category.TECHNICAL));
        notes.add(new Note("My Favorite Quote", "My Favorite Quote has to be the Quote by Abraham Lincoln", Note.Category.QUOTE));

        noteAdapter = new NoteAdapter(getActivity(), notes);
        setListAdapter(noteAdapter);

        getListView().setDivider(ContextCompat.getDrawable(getActivity(),
                android.R.color.darker_gray));
        getListView().setDividerHeight(1);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }
}
