package com.djkim.slap.match;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.djkim.slap.R;

/**
 * Created by YooJung on 11/15/2015.
 */
public class MatchGroupListActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO(djkim): use a layout with toolbar
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getFragmentManager();
        MatchGroupListFragment fragment = new MatchGroupListFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(android.R.id.content, fragment).commit();
    }
}
