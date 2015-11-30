package com.djkim.slap.menubar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djkim.slap.home.GroupListFragment;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.GroupsCallback;
import com.djkim.slap.models.Utils;

import java.util.List;

/**
 * Created by joannachen on 11/24/15.
 */
public class StringSearchGroupListFragment extends GroupListFragment{
    private String mName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mName = this.getArguments().getString("name");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void onResumeCalled() {}

    @Override
    protected View noGroupsSetup(View view, LayoutInflater inflater, ViewGroup container) {
        return view;
    }

    protected void getGroupsInBackground() {
        Utils.getStringSearchGroupsFromCloudInBackground(mName, new GroupsCallback() {
            @Override
            public void done(List<Group> groups) {
                setAdapterWithGroups(groups);
            }
        });
    }
}