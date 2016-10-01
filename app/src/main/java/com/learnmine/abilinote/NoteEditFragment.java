package com.learnmine.abilinote;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    private EditText title;
    private EditText message;
    private ImageButton noteCatButton;
    private long noteId = 0;
    private NoteListActivity.FragmentToLaunch fragmentToLaunch;
//    Note.Category noteCat;

    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate our fragment edit layout
        Log.d(MainActivity.LOG_TAG, "Started Editing Fragment!");
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);


        title = (EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        message = (EditText) fragmentLayout.findViewById(R.id.editNoteMessage);
        noteCatButton = (ImageButton) fragmentLayout.findViewById(R.id.editNoteButton);

        // populate widgets with note data
        Intent intent = getActivity().getIntent();
        title.setText(intent.getExtras().getString(NoteListActivity.NOTE_TITLE_EXTRA));
        message.setText(intent.getExtras().getString(NoteListActivity.NOTE_MESSAGE_EXTRA));
        fragmentToLaunch = (NoteListActivity.FragmentToLaunch)
                intent.getSerializableExtra(NoteListActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA);

//        if (fragmentToLaunch != NoteListActivity.FragmentToLaunch.CREATE) {
//            Log.d(MainActivity.LOG_TAG, "Not CREATE");
//            noteCat = (Note.Category) intent.getSerializableExtra(
//                    NoteListActivity.NOTE_CATEGORY_EXTRA);
//            noteCatButton.setImageResource(Note.categoryToDrawable(noteCat));
//        } else {
//            noteCat = Note.Category.PERSONAL;
//            noteCatButton.setImageResource(Note.categoryToDrawable(noteCat));
//        }
//        noteId = intent.getExtras().getLong(NoteListActivity.NOTE_ID_EXTRA, 0);

        // return our modified fragment layout
        return fragmentLayout;
    }

    public void saveNote() {

//        AbilinoteDbAdapter dbAdapter = new AbilinoteDbAdapter(getActivity().getBaseContext());
//        dbAdapter.open();
//
//        if (fragmentToLaunch == NoteListActivity.FragmentToLaunch.CREATE) {
//            Log.d(MainActivity.LOG_TAG, "CReating MOde");
//            dbAdapter.createNote(title.getText() + "", message.getText() + "", noteCat);
//        } else {
//            // update instead of creating new
//            dbAdapter.updateNote(noteId, title.getText() + "", message.getText() + "", noteCat);
//        }
//        dbAdapter.close();
        Intent intent = new Intent(getActivity(), NoteListActivity.class);
        startActivity(intent);
    }
}
