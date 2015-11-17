package com.djkim.slap.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.group.GroupDetailsActivity;
import com.djkim.slap.group.GroupDetailsFragment;
import com.djkim.slap.menubar.MainActivity;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.GroupsCallback;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;

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

        User user = Utils.get_current_user();
        user.getGroupsInBackground(new GroupsCallback() {
            @Override
            public void done(List<Group> groups) {
                mGroupAdapter = new GroupAdapter(groups);
                mGroupRecyclerView.setAdapter(mGroupAdapter);
            }
        });
        return view;
    }

    // TODO(victorkwan): Refactor onCreateView to allow the fetch function to be overridden, and
    // rewrite MatchGroupListFragment's getGroupList to be asynchronous.
    protected List<Group> getGroupList() {
        User user = Utils.get_current_user();
        List<Group> groups = user.getGroups();
        return groups;
    }

    private class GroupHolder extends RecyclerView.ViewHolder {
        private Group mGroup;
        private ImageView mThumbnailImageView;
        private ImageView mCoverImageView;
        private TextView mTitleTextView;
        private TextView mSubheadTextView;
        private TextView mSupportingTextView;
        private Button mDetailsButton;
        private Button mRemainingSlotsButton;

        // TODO(victorkwan): Declare and assign the relevant fields.
        public GroupHolder(View itemView) {
            super(itemView);

            mThumbnailImageView =
                    (ImageView) itemView.findViewById(R.id.group_list_item_thumbnail_image_view);
            mCoverImageView =
                    (ImageView) itemView.findViewById(R.id.group_list_item_image_view);

            mTitleTextView = (TextView) itemView.findViewById(R.id.group_list_item_title_text_view);
            mTitleTextView.setText("Friday Night Soccer!");

            mSubheadTextView =
                    (TextView) itemView.findViewById(R.id.group_list_item_subhead_text_view);
            mSubheadTextView.setText("John Wooden Center");

            mSupportingTextView =
                    (TextView) itemView.findViewById(R.id.group_list_item_supporting_text_view);
            mSupportingTextView.setText(
                    "We're a group of three lads looking for two more to play soccer with us.");

            mDetailsButton = (Button) itemView.findViewById(R.id.group_list_item_details_button);
            mDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = new GroupDetailsFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GroupDetailsFragment.sGroupArgumentKey, mGroup);
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_layout, fragment)
                            .addToBackStack(MainActivity.sBackStackTag)
                            .commit();
                }
            });

            mRemainingSlotsButton =
                    (Button) itemView.findViewById(R.id.group_list_item_remaining_slots_button);
            mRemainingSlotsButton.setText("5 slots remaining");
            mRemainingSlotsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mGroup.isMember(Utils.get_current_user())) {
                        mGroup.addMember(Utils.get_current_user());
                        mGroup.save();
                    }
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = new GroupDetailsFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GroupDetailsFragment.sGroupArgumentKey, mGroup);
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_layout, fragment)
                            .addToBackStack(MainActivity.sBackStackTag)
                            .commit();
                }
            });
        }

        public void bindGroup(Group group) {
            mGroup = group;
            mTitleTextView.setText(group.get_name());
            mSubheadTextView.setText("Created by " + group.get_owner().get_name());
            mSupportingTextView.setText(group.get_description());

            int remainingSlots = group.get_capacity() - group.get_size();
            if(remainingSlots > 1)
                mRemainingSlotsButton.setText(remainingSlots + " slots remaining");
            else if (remainingSlots == 1)
                mRemainingSlotsButton.setText(remainingSlots + " slot remaining");
            else
                mRemainingSlotsButton.setText("Full");
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
