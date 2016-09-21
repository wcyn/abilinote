package com.learnmine.abilinote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoteListActivity extends AppCompatActivity {
    public static final String NOTE_ID_EXTRA = "com.learnmine.abilinote.Note Identifier";
    public static final String NOTE_TITLE_EXTRA = "com.learnmine.abilinote.Note Title";
    public static final String NOTE_MESSAGE_EXTRA = "com.learnmine.abilinote.Note Message";
    public static final String NOTE_CATEGORY_EXTRA = "com.learnmine.abilinote.Note Category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
    }
}
