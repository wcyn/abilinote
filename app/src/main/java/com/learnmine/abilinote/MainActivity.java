package com.learnmine.abilinote;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
    public static final String LOG_TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "OnCreate");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(LOG_TAG,"Config changed!");
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(LOG_TAG, "Changed to Portrait");
        } else {
            Log.d(LOG_TAG, "Changed to Landscape");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "OnStop");
    }

    public void recordNotesHandler(View view) {
        ImageView imgView = (ImageView) view;
        int solidColor = imgView.getSolidColor();
        Log.d(MainActivity.LOG_TAG, "Recording audio!");
        Log.d(MainActivity.LOG_TAG, "Solid Color: " + solidColor);
    }

    public void goToVoiceActivity(View view) {
        Intent k = new Intent(MainActivity.this, VoiceActivity.class);
        startActivity(k);
    }

    public void goToNoteListActivity(View view) {
        Intent k = new Intent(MainActivity.this, NoteListActivity.class);
        startActivity(k);
    }

    public void goToNoteEditFragment(View view) {
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra(NoteListActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA,
                NoteListActivity.FragmentToLaunch.CREATE);
        startActivity(intent);

    }
}
