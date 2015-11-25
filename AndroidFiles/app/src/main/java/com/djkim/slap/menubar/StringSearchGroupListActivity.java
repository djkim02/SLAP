package com.djkim.slap.menubar;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.djkim.slap.R;
import com.djkim.slap.selectionModel.ModelCallbacks;

/**
 * Created by joannachen on 11/24/15.
 */
public class StringSearchGroupListActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO(djkim): use a layout with toolbar
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getFragmentManager();
        StringSearchGroupListFragment fragment = new StringSearchGroupListFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(android.R.id.content, fragment).commit();
    }
}