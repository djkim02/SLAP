package com.djkim.slap.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.djkim.slap.R;
import com.djkim.slap.home.GroupListFragment;

/**
 * Created by kylemn on 10/26/15.
 */
public class GroupDetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_details_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
        }
    }

}
