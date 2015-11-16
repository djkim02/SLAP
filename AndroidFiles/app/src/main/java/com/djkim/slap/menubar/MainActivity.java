package com.djkim.slap.menubar;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.djkim.slap.R;
import com.djkim.slap.dispatch.DispatchActivity;
import com.djkim.slap.home.GroupListFragment;
import com.djkim.slap.createGroup.CreateGroupActivity;
import com.djkim.slap.match.MatchGroupActivity;
import com.djkim.slap.match.MatchGroupListFragment;
import com.parse.ParseUser;

public class MainActivity extends ActionBarActivity {
    public static final String sBackStackTag = "main_activity_back_stack";

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ImageView imageView1;
    RoundImage roundedImage;

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

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        roundedImage = new RoundImage(bm);
        //imageView1.setImageDrawable(roundedImage);

        Fragment fragment = new GroupListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.main_layout, fragment)
                .commit();
    }

    private void addDrawerItems() {
        final String[] osArray = {"Profile", "My Groups", "Create a Group", "Find Matches", "Settings", "Log Out"};
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
                        fragment = new menuProfile();
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
                        startActivity(createGroupIntent);
                        break;
                    case 3:     // Find Matches
                        Intent matchGroupIntent = new Intent(MainActivity.this, MatchGroupActivity.class);
                        startActivity(matchGroupIntent);
//                        fragment = new MatchGroupListFragment();
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.main_layout, fragment)
//                                .addToBackStack(sBackStackTag)
//                                .commit();
                        break;
                    case 5:     // Logout
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
}
