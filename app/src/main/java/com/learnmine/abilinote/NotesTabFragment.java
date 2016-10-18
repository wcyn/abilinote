package com.learnmine.abilinote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by baa on 9/28/2016.
 */


public class NotesTabFragment extends Fragment {
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public static final String LOG_TAG = "NoteTabFrag";
        public static final String S_BLOCKS_CHILD = "sentence_blocks";
        public static final String NOTES_CHILD = MainActivity.NOTES_CHILD;

        public TextView messageTextView;
        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.sentenceBlockTextView);
//            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
//            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(MainActivity.LOG_TAG, "Inflating!");
        return inflater.inflate(R.layout.fragment_notes_tab, container, false);
    }
}