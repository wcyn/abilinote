package com.learnmine.abilinote;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {


    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // INflate our fragment edit layout
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);

        EditText title = (EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        EditText message = (EditText) fragmentLayout.findViewById(R.id.editNoteMessage);
        ImageButton noteCatButton = (ImageButton) fragmentLayout.findViewById(R.id.editNoteButton);

        // populate widgets with note data
        Intent intent = getActivity().getIntent();
        title.setText(intent.getExtras().getString(NoteListActivity.NOTE_TITLE_EXTRA));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_edit, container, false);
    }

}
