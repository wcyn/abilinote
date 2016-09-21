package com.learnmine.abilinote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Note> {

    /**
     * Created by baa on 9/21/2016.
     */
    public static class ViewHolder {
        TextView noteTitle;
        TextView noteText;
        ImageView noteIcon;
    }

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);

        // create new view holder
        ViewHolder viewHolder;

        // Check if an existing View is being reused, otherwise inflate a new view from
        // custom row layout
        if (convertView == null) {

            // If we don't have a view that is being used, create one, and ensure you create a
            // view holder along with it to save our view references to
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_list_item,
                    parent, false);
            viewHolder.noteTitle = (TextView) convertView.findViewById(R.id.listItemTitle);
            viewHolder.noteText = (TextView) convertView.findViewById(R.id.listItemText);
            viewHolder.noteIcon = (ImageView) convertView.findViewById(R.id.listItemNoteImg);

            convertView.setTag(viewHolder);
        } else {
            // We already have a view so just go to our viewHolder and grab the widgets from it
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Fill each new referenced view with data associated with the note it's referencing
        viewHolder.noteTitle.setText(note.getTitle());
        viewHolder.noteText.setText(note.getMessage());
        viewHolder.noteIcon.setImageResource(note.getAssociateDrawable());

        // Return the modified view to be displayed
        return convertView;
    }
}
