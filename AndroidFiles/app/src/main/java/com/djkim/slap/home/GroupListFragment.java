package com.djkim.slap.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.djkim.slap.match.MatchGroupActivity;
import com.djkim.slap.match.MatchGroupListFragment;
import com.djkim.slap.menubar.MainActivity;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.GroupCallback;
import com.djkim.slap.models.GroupsCallback;
import com.djkim.slap.models.User;
import com.djkim.slap.models.UserCallback;
import com.djkim.slap.models.Utils;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.Arrays;
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
        view = noGroupsSetup(view, inflater, container);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onResumeCalled();
    }

    // I think this motivates making GroupListFragment an abstract class and making the regular
    // GroupListFragment a subclass with its own behavior.
    protected void onResumeCalled() {
        mGroupAdapter.setGroups(Utils.get_current_user().getGroups());
        mGroupAdapter.notifyDataSetChanged();
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

    protected View noGroupsSetup(View view, LayoutInflater inflater, ViewGroup container) {
        if (mGroupAdapter.getItemCount() == 0) {
            view = inflater.inflate(R.layout.no_group_list_fragment_layout, container, false);
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "BebasNeue Bold.otf");
            TextView headingTextView = (TextView) view.findViewById(R.id.heading);
            headingTextView.setTypeface(tf);
            Button button = (Button) view.findViewById(R.id.match_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent matchGroupIntent = new Intent(GroupListFragment.this.getActivity(), MatchGroupActivity.class);
                    startActivityForResult(matchGroupIntent, 0);
                }
            });
            button.setTypeface(tf);
            tf = Typeface.createFromAsset(getActivity().getAssets(), "BebasNeue Book.otf");
            TextView subHeadingTextView = (TextView) view.findViewById(R.id.subheading);
            subHeadingTextView.setTypeface(tf);
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            FragmentManager fragmentManager = getFragmentManager();
            if (requestCode == 0) {
                Fragment fragment = new MatchGroupListFragment();
                fragment.setArguments(data.getExtras());
                fragmentManager.beginTransaction()
                        .replace(R.id.main_layout, fragment)
                        .addToBackStack("main_activity_back_stack")
                        .commit();
            }
        }
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

        public void bindGroup(Group group) {
            mGroup = group;
            mTitleTextView.setText(group.get_name());
            String text = group.get_type() + " Group";
            String ownerFacebookProfileId = group.get_owner_facebook_profile_id();
            mThumbnailImageView.setProfileId(ownerFacebookProfileId);
            if (Utils.get_current_user().get_facebook_profile_id().equals(ownerFacebookProfileId)) {
                text += " created by Me";
            } else {
                text += " created by " + group.get_owner_name();
            }
            mSubheadTextView.setText(text);
            mSkillsTextView.setText(group.get_skills());

            String tags = mGroup.get_tags();
            if (tags == null || tags.equals("")) {
                mTagsTextView.setText("#notags");
            } else {
                StringBuilder tagBuilder = new StringBuilder();
                for (String tag : mGroup.get_tags().replaceAll("\\s*,\\s*",",").split("[\\s,]")) {
                    tagBuilder.append("#");
                    tagBuilder.append(tag.toLowerCase());
                    tagBuilder.append(" ");
                }
                mTagsTextView.setText(tagBuilder.toString());
            }

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

        public void setGroups(List<Group> groups) { mGroups = groups; }

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
