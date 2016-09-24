package com.learnmine.abilinote;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class NoteDetailActivity extends Activity {

    private NoteListActivity.FragmentToLaunch fragmentToLaunch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        createAndAddFragment();
        Intent intent = getIntent();
        fragmentToLaunch = (NoteListActivity.FragmentToLaunch)
                intent.getSerializableExtra(NoteListActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA);

        if (fragmentToLaunch == NoteListActivity.FragmentToLaunch.CREATE) {
            Log.d(MainActivity.LOG_TAG,"Yep, = to CREATE");
            changeToAddFragment();
        }
//        else {
//            Log.d(MainActivity.LOG_TAG,"Nope, != to VIEW");
//            createAndAddFragment(NoteListActivity.FragmentToLaunch.EDIT);
//        }
    }

    public void changeToEditFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NoteEditFragment noteEditFragment = new NoteEditFragment();
        fragmentTransaction.replace(R.id.note_container, noteEditFragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }

    public void changeToAddFragment() {
        setTitle("Create Note");
        Log.d(MainActivity.LOG_TAG, "Change to Add");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NoteEditFragment noteCreateFragment = new NoteEditFragment();
        fragmentTransaction.replace(R.id.note_container, noteCreateFragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        Log.d(MainActivity.LOG_TAG, "Comitting at to Add");
        fragmentTransaction.commit();
    }

    public void editNoteHandler(View view) {
        changeToEditFragment();

//        createAndAddFragment(NoteListActivity.FragmentToLaunch.EDIT);
    }

    public void addNoteHandler(View view) {
        changeToAddFragment();
    }

    private void createAndAddFragment () {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        Log.d(MainActivity.LOG_TAG, "View Switch");
        NoteViewFragment noteViewFragment = new NoteViewFragment();
        setTitle(R.string.view_note);
        fragmentTransaction.add(R.id.note_container, noteViewFragment, "NOTE_VIEW_FRAGMENT");

        fragmentTransaction.commit();

    }

    public void saveNoteHandler(View view) {
        NoteEditFragment fragment = (NoteEditFragment) getFragmentManager().findFragmentById(R.id.note_container);
        fragment.saveNote();
    }
}
