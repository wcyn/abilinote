package com.learnmine.abilinote;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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

    // Search bar
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;

    private View tabContentView;
    private View searchInclude;

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

        tabContentView = findViewById(R.id.tabNContentLayout);
        searchInclude = findViewById(R.id.searchInclude);


        setSupportActionBar(mToolbar);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

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

        new DrawerBuilder().withActivity(this).build();
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.sign_in);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.settings);

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem().withName(R.string.settings)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position) {
                            case 0: // sign in
                                try
                                {
                                    Intent k = new Intent(MainActivity.this, SignInActivity.class);
                                    startActivity(k);
                                }catch(ActivityNotFoundException e){
//                                    Intent k = new Intent(BugReportActivity.class);
//                                    k.putExtra(BugReportActivity.STACKTRACE, stackTrace.toString());
//                                    myContext.startActivity(k);
                                    Toast.makeText(MainActivity.this, "Activity not Found",
                                            Toast.LENGTH_SHORT).show();
                                    Log.d(MainActivity.LOG_TAG, "Activity not found");
                                }

                        }
                        return true;
                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        Log.d(MainActivity.LOG_TAG, "Options created");

        SearchManager searchManager = (SearchManager)
                MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            Log.d(MainActivity.LOG_TAG, "Search View Not Null");
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(MainActivity.LOG_TAG, "Do nothing");
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d(MainActivity.LOG_TAG, "Search for Query");
//                searchFor(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {

                    Log.d(MainActivity.LOG_TAG, "Filter Search");
//                filterSearchFor(query);
                    return true;
                }
            });

//            searchItem.expandActionView();
            MenuItemCompat.setOnActionExpandListener(
                    searchItem, new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) {
                            tabContentView.setVisibility(View.INVISIBLE);
                            searchInclude.setVisibility(View.VISIBLE);
                            return true;
                        }
                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            searchInclude.setVisibility(View.INVISIBLE);
                            tabContentView.setVisibility(View.VISIBLE);
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent);
                            return true;
                        }
                    });

        } else {
            Log.d(MainActivity.LOG_TAG, "Search view is null");
        }
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
////            case R.id.action_settings:
////                return true;
//            case R.id.action_search:
////                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
////                startActivity(intent);
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        mSearchAction = menu.findItem(R.id.action_search);
//        return super.onPrepareOptionsMenu(menu);
//    }

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
