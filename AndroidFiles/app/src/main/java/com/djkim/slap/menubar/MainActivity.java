package com.djkim.slap.menubar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.djkim.slap.R;
import com.djkim.slap.createGroup.CreateGroupActivity;
import com.djkim.slap.dispatch.DispatchActivity;
import com.djkim.slap.group.AdminGroupDetailsFragment;
import com.djkim.slap.group.GroupDetailsFragment;
import com.djkim.slap.home.GroupListFragment;
import com.djkim.slap.match.MatchGroupActivity;
import com.djkim.slap.match.MatchGroupListFragment;
import com.djkim.slap.messenger.MessageService;
import com.djkim.slap.profile.MyProfileFragment;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class MainActivity extends ActionBarActivity {
    private static final int MATCH_REQUEST_CODE = 0;
    private static final int CREATE_REQUEST_CODE = 1;

    public static final String sBackStackTag = "main_activity_back_stack";

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        addDrawerItems();

        setupDrawer();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Fragment fragment = new GroupListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.main_layout, fragment)
                .addToBackStack(sBackStackTag)
                .commit();

        //Set up Sinch service. Might want to put this in a better place.
        // ParseInstallation required for push notifications
        final Intent serviceIntent = new Intent(getApplicationContext(), MessageService.class);
        startService(serviceIntent);
        ParseUser currentUser = ParseUser.getCurrentUser();
        String currentUserName = ParseUser.getCurrentUser().getUsername();
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", currentUser);
        installation.put("username", currentUserName);
        installation.saveInBackground();
    }

    private void addDrawerItems() {
        final String[] osArray = {"My Profile", "My Groups", "Create a Group", "Find Matches", "Log Out"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = null;

                switch (position) {
                    case 0:     // Profile
                        fragment = new MyProfileFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, fragment)
                                .addToBackStack(sBackStackTag)
                                .commit();
                        break;
                    case 1:     // My Groups
                        fragment = new GroupListFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, fragment)
                                .addToBackStack(sBackStackTag)
                                .commit();
                        break;
                    case 2:     // Create a Group
                        Intent createGroupIntent = new Intent(MainActivity.this, CreateGroupActivity.class);
                        startActivityForResult(createGroupIntent, CREATE_REQUEST_CODE);
                        break;
                    case 3:     // Find Matches
                        Intent matchGroupIntent = new Intent(MainActivity.this, MatchGroupActivity.class);
                        startActivityForResult(matchGroupIntent, MATCH_REQUEST_CODE);
                        break;
                    case 4:     // Logout
                        // TODO: replace this with Utils method
                        ParseUser.getCurrentUser().logOut();    // preferably use logOutInBackground...
                        startActivity(new Intent(MainActivity.this, DispatchActivity.class));
                        break;
                }
                //Highlight the selected item, update the title, and close the drawer
                mDrawerList.setItemChecked(position, true);
                setTitle(osArray[position]);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_actions, menu);
        menuInflater.inflate(R.menu.searchview, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    //React to specific icons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Bundle bundle = new Bundle();
            bundle.putString("name", query);
            Fragment fragment = new StringSearchGroupListFragment();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_layout, fragment)
                    .addToBackStack(sBackStackTag)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            FragmentManager fragmentManager = getFragmentManager();
            if (requestCode == MATCH_REQUEST_CODE) {
                Fragment fragment = new MatchGroupListFragment();
                fragment.setArguments(data.getExtras());
                fragmentManager.beginTransaction()
                        .replace(R.id.main_layout, fragment)
                        .addToBackStack(sBackStackTag)
                        .commit();
            } else if (requestCode == CREATE_REQUEST_CODE) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(GroupDetailsFragment.sGroupArgumentKey,
                        data.getExtras().getSerializable(CreateGroupActivity.CREATE_GROUP_EXTRA));

                Fragment fragment = new AdminGroupDetailsFragment();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.main_layout, fragment)
                        .addToBackStack(sBackStackTag)
                        .commit();
            }
        }
    }
}
