package com.djkim.slap.match;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djkim.slap.home.GroupListFragment;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.Utils;

import java.util.List;

/**
 * Created by YooJung on 11/15/2015.
 */
public class MatchGroupListFragment extends GroupListFragment {
    private String mType;
    private String mTags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mType = this.getArguments().getString("type");
        mTags = this.getArguments().getString("tags");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected List<Group> getGroupList() {
        return Utils.getGroupsFromCloud(mType);
    }
}
