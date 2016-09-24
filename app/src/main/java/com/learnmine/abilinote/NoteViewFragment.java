package com.learnmine.abilinote;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteViewFragment extends Fragment {


    public NoteViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_view, container, false);
        TextView title = (TextView) fragmentLayout.findViewById(R.id.viewNoteTitle);
        TextView message = (TextView) fragmentLayout.findViewById(R.id.viewNoteMessage);
        ImageView icon = (ImageView) fragmentLayout.findViewById(R.id.viewNoteIcon);

        Intent intent = getActivity().getIntent();
        title.setText(intent.getExtras().getString(NoteListActivity.NOTE_TITLE_EXTRA));
        message.setText(intent.getExtras().getString(NoteListActivity.NOTE_MESSAGE_EXTRA));

        Note.Category noteCat = (Note.Category) intent.getSerializableExtra(
                NoteListActivity.NOTE_CATEGORY_EXTRA);
        NoteListActivity.FragmentToLaunch fragmentToLaunch = (NoteListActivity.FragmentToLaunch)
                intent.getSerializableExtra(NoteListActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA);

        if (fragmentToLaunch != NoteListActivity.FragmentToLaunch.CREATE) {
            icon.setImageResource(Note.categoryToDrawable(noteCat));
        }
        // Return the modified fragment layout
        return fragmentLayout;
    }

}
