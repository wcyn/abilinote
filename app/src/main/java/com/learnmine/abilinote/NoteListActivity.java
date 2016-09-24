package com.learnmine.abilinote;

import android.app.Activity;
import android.os.Bundle;

public class NoteListActivity extends Activity {
    public static final String NOTE_ID_EXTRA = "com.learnmine.abilinote.Identifier";
    public static final String NOTE_TITLE_EXTRA = "com.learnmine.abilinote.Title";
    public static final String NOTE_MESSAGE_EXTRA = "com.learnmine.abilinote.Message";
    public static final String NOTE_CATEGORY_EXTRA = "com.learnmine.abilinote.Category";
    public static final String NOTE_FRAGMENT_TO_LOAD_EXTRA =
            "com.learnmine.abilinote.Fragment_To_Load";
    public enum FragmentToLaunch { VIEW, EDIT, CREATE }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
    }
}
