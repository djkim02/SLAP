package com.djkim.slap.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.group.AdminGroupDetailsFragment;
import com.djkim.slap.group.GroupDetailsActivity;
import com.djkim.slap.group.GroupDetailsFragment;
import com.djkim.slap.group.MemberGroupDetailsFragment;
import com.djkim.slap.menubar.MainActivity;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.GroupCallback;
import com.djkim.slap.models.GroupsCallback;
import com.djkim.slap.models.User;
import com.djkim.slap.models.UserCallback;
import com.djkim.slap.models.Utils;
import com.facebook.login.widget.ProfilePictureView;

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
        getGroupsInBackground();

        return view;
    }

    /**
     * Takes a List of Group objects and configures the RecyclerView to display these Groups via
     * the adapter.
     *
     * @param groups The List of Group objects.
     */
    protected void setAdapterWithGroups(List<Group> groups) {
        mGroupAdapter = new GroupAdapter(groups);
        mGroupRecyclerView.setAdapter(mGroupAdapter);
    }

    /**
     * Retrieves a List of Group objects asynchronously. If this function is overridden, it should
     * call setAdapterWithGroups to properly configure the fragment.
     */
    protected void getGroupsInBackground() {
        User user = Utils.get_current_user();
        setAdapterWithGroups(user.getGroups());
    }

    private class GroupHolder extends RecyclerView.ViewHolder {
        private Group mGroup;
        private ProfilePictureView mThumbnailImageView;
        private TextView mTitleTextView;
        private TextView mSubheadTextView;
        private TextView mSkillsTextView;
        private TextView mTagsTextView;
        private Button mDetailsButton;
        private Button mRemainingSlotsButton;
        private ImageView mSubtractedCircle;

        // TODO(victorkwan): Declare and assign the relevant fields.
        public GroupHolder(View itemView) {
            super(itemView);

            mSubtractedCircle = (ImageView) itemView.findViewById(R.id.group_list_item_subtracted_circle);
            int color = Color.parseColor("#009688");
            mSubtractedCircle.setColorFilter(color);

            mThumbnailImageView =
                    (ProfilePictureView) itemView.findViewById(R.id.group_list_item_thumbnail_image_view);

            mTitleTextView = (TextView) itemView.findViewById(R.id.group_list_item_title_text_view);
            mTitleTextView.setText("Friday Night Soccer!");

            mSubheadTextView =
                    (TextView) itemView.findViewById(R.id.group_list_item_subhead_text_view);
            mSubheadTextView.setText("John Wooden Center");

            mSkillsTextView =
                    (TextView) itemView.findViewById(R.id.group_list_item_skills_text_view);
            mTagsTextView =
                    (TextView) itemView.findViewById(R.id.group_list_item_tags_text_view);

            mDetailsButton = (Button) itemView.findViewById(R.id.group_list_item_details_button);
            mDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDetails();
                }
            });

            mRemainingSlotsButton =
                    (Button) itemView.findViewById(R.id.group_list_item_remaining_slots_button);
            mRemainingSlotsButton.setText("5 slots remaining");
            mRemainingSlotsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User curUser = Utils.get_current_user();
                    if (!curUser.isMemberOf(mGroup)) {
                        curUser.joinAsMember(mGroup);
                        curUser.saveInBackground(new UserCallback() {
                            @Override
                            public void done() {
                                openDetails();
                            }
                        });
                    } else {
                        openDetails();
                    }
                }
            });
        }

        private void openDetails() {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = mGroup.isOwner(Utils.get_current_user())
                    ? new AdminGroupDetailsFragment()
                    : new MemberGroupDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(GroupDetailsFragment.sGroupArgumentKey, mGroup);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_layout, fragment)
                    .addToBackStack(MainActivity.sBackStackTag)
                    .commit();
        }

        // TODO(yjchoi): this slows down main thread. Better way of getting owners?
        public void bindGroup(Group group) {
            mGroup = group;
            mTitleTextView.setText(group.get_name());
            String text = group.get_type() + " Group";
            String ownerFacebookProfileId = group.get_owner_facebook_profile_id();
            mThumbnailImageView.setProfileId(group.get_owner_facebook_profile_id());
            if (Utils.get_current_user().get_facebook_profile_id().equals(group.get_owner_facebook_profile_id())) {
                text += " created by me";
            } else {
                text += " created by " + group.get_owner_name();
            }
            mSubheadTextView.setText(text);
            mSkillsTextView.setText(group.get_skills());

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

        // TODO(yjchoi): maybe get all list of owners here?
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
