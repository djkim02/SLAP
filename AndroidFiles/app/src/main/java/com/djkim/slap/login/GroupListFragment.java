package com.djkim.slap.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djkim.slap.R;

import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment {
    private RecyclerView mGroupRecyclerView;
    private GroupAdapter mGroupAdapter;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_list_fragment, container, false);
        mGroupRecyclerView = (RecyclerView) view.findViewById(R.id.group_recycler_view);
        mGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // TODO(victorkwan): Provide the groups.
        List<Group> groups = new ArrayList<>();
        mGroupAdapter = new GroupAdapter(groups);
        mGroupRecyclerView.setAdapter(mGroupAdapter);
        return view;
    }

    private interface Group {}

    private class GroupHolder extends RecyclerView.ViewHolder {
        private Group mGroup;
        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private TextView mSecondaryDescriptionTextView;

        // TODO(victorkwan): Declare and assign the relevant fields.
        public GroupHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.group_list_item_group_name);
            mTitleTextView.setText("Friday Night Soccer!");

            mDescriptionTextView =
                    (TextView) itemView.findViewById(R.id.group_list_item_group_description);
            mDescriptionTextView.setText(
                    "Here's a long long description string. Let's see how it fares.");

            mSecondaryDescriptionTextView =
                    (TextView) itemView.findViewById(R.id.group_list_item_group_extra);
            mSecondaryDescriptionTextView.setText(
                    "Here's yet another long description string. How does this fare?");
        }

        // TODO(victorkwan): Configure the view for the given Group.
        public void bindGroup(Group group) {
            mGroup = group;
        }
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupHolder> {
        private List<Group> mGroups;

        public GroupAdapter(List<Group> groups) {
            mGroups = groups;
        }

        @Override
        public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.group_list_item, parent, false);
            return new GroupHolder(view);
        }

        @Override
        public void onBindViewHolder(GroupHolder holder, int position) {
            Group group = mGroups.get(position);
            holder.bindGroup(group);
        }

        @Override
        public int getItemCount() {
            return mGroups.size();
        }
    }
}
