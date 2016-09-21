package com.learnmine.abilinote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by baa on 9/21/2016.
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);

        // Check if an existing View is being reused, otherwise inflate a new view from
        // custom row layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_list_item,
                    parent, false);
        }

        // Grab references of views so we can populate them with specific note row data
        TextView noteTitle = (TextView) convertView.findViewById(R.id.listItemTitle);
        TextView noteText = (TextView) convertView.findViewById(R.id.listItemText);
        ImageView noteIcon = (ImageView) convertView.findViewById(R.id.listItemNoteImg);

        // Fill each new referenced view with data associated with the note it's referencing
        noteTitle.setText(note.getTitle());
        noteText.setText(note.getMessage());
        noteIcon.setImageResource(note.getAssociateDrawable());

        // Return the modified view to be displayed
        return convertView;
    }
}
