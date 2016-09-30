package com.learnmine.abilinote;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = "MainActivity";

    public static final String MESSAGES_CHILD = "messages";
    private static final int REQUEST_INVITE = 1;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "OnCreate");

        /*
        Assigning view variables to their respective view in xml
        by findViewByID method
         */

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        /*
        Creating Adapter and setting that adapter to the viewPager
        setSupportActionBar method takes the toolbar and sets it as
        the default action bar thus making the toolbar work like a normal
        action bar.
         */
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setOnTouchListener(new TabLayout());

        /*
        TabLayout.newTab() method creates a tab view, Now a Tab view is not the view
        which is below the home_tab_cont, its the tab itself.
         */

//        final TabLayout.Tab home = tabLayout.newTab();
//        final TabLayout.Tab my_notes = tabLayout.newTab();
//        final TabLayout.Tab setting = tabLayout.newTab();


        /*
        Setting Title text for our home_tab_cont respectively
         */

//        home.setIcon(R.mipmap.ic_launcher);
//        my_notes.setText("My Notes");

        /*
        Adding the tab view to our tablayout at appropriate positions
        As I want home at first position I am passing home and 0 as argument to
        the tablayout and like wise for other home_tab_cont as well
         */
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.addTab(home, 0);
//        tabLayout.addTab(my_notes, 1);

        /*
        Adding a onPageChangeListener to the viewPager
        1st we add the PageChangeListener and pass a TabLayoutPageChangeListener so that Tabs Selection
        changes when a viewpager page changes.
         */
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setupWithViewPager(viewPager);

        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
//            startActivity(new Intent(this, SignInActivity.class));
//            finish();
//            return;
            // do nothing for now
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
