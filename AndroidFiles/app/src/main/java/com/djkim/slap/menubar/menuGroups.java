package com.djkim.slap.menubar;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.djkim.slap.R;

/**
 * Created by Calvin on 10/27/15.
 */
public class menuGroups extends Fragment {

    private View rootview;

    public menuGroups() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu_groups, container, false);
        return rootview;
    }
}