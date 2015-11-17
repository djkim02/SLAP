package com.djkim.slap.group;


import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;
import com.djkim.slap.profile.OthersProfileActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.widget.JoinAppGroupDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kylemn on 10/26/15.
 */
public class GroupDetailsFragment extends Fragment {
    private static int VIEW_TYPE_HEADER = 0;

    // Placeholder for the future "Convert to FB group" option.
    private static int VIEW_TYPE_ACTION = 1;
    private static int VIEW_TYPE_CONTENT = 2;

    public final static String sGroupArgumentKey = "group_details_group_argument";

    private RecyclerView mGroupDetailsRecyclerView;
    private UserAdapter mGroupDetailsAdapter;
    private Group mGroup;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_details_fragment, container, false);

        mGroupDetailsRecyclerView = (RecyclerView) rootView.findViewById(R.id.group_recycler_view);
        mGroupDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        Bundle bundle = getArguments();
        mGroup = (Group) bundle.getSerializable(sGroupArgumentKey);

        ArrayList<com.djkim.slap.models.User> groupUsers = mGroup.get_members();
        mGroupDetailsAdapter = new UserAdapter(groupUsers);
        mGroupDetailsRecyclerView.setAdapter(mGroupDetailsAdapter);

        return rootView;
    }

    public class User {}

    private class SectionHeaderHolder extends RecyclerView.ViewHolder {
        private TextView mSectionHeaderTextView;

        public SectionHeaderHolder(View itemView) {
            super(itemView);

            mSectionHeaderTextView = (TextView) itemView;
        }

        public void setText(String text) {
            mSectionHeaderTextView.setText(text);
        }
    }

    private class UserHolder extends RecyclerView.ViewHolder {
        private com.djkim.slap.models.User mUser;
        private ProfilePictureView mThumbnailImageView;
        private TextView mTitleTextView;
        private TextView mSubheadTextView;
        private RelativeLayout mRelativeLayout;

        public UserHolder(View itemView) {
            super(itemView);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.group_details_layout);
            mThumbnailImageView =
                    (ProfilePictureView) itemView.findViewById(R.id.group_details_item_thumbnail_image_view);
            mTitleTextView =
                    (TextView) itemView.findViewById(R.id.group_details_item_title_text_view);
            mSubheadTextView =
                    (TextView) itemView.findViewById(R.id.group_details_item_subhead_text_view);
        }

        public void bindUser(com.djkim.slap.models.User user) {
            mUser = user;
            mThumbnailImageView.setProfileId(user.get_facebook_profile_id());
            mTitleTextView.setText(user.get_name());
            mSubheadTextView.setText(mGroup.get_owner().equals(mUser) ? "Admin" : "Member");

            if (mUser.get_facebook_profile_id() != null) {
                mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), OthersProfileActivity.class);
                        intent.putExtra("user", mUser);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private class DetailsActivityHolder extends RecyclerView.ViewHolder {
        // TODO(victorkwan): Let's update this to be nicer.
        private LinearLayout mLinearLayout;
        private TextView mTitleTextView;
        private TextView mSubheadTextView;

        public DetailsActivityHolder(View itemView) {
            super(itemView);

            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.group_details_action_tile);

            // We only set the onClickListener if there is such a Facebook Group.
            // We only show the onClickListener if that person is in the SLAP group, but not in the facebook group
            //TODO: Need to remove the slap card if the person left the group on Facebook
//            new GraphRequest(
//                    AccessToken.getCurrentAccessToken(),
//                    "/6EZmymlOoCjIFnPPnJ13XcpeyyoXNIVoXTq2RwMo/groups",
//                    null,
//                    HttpMethod.GET,
//                    new GraphRequest.Callback() {
//                        public void onCompleted(GraphResponse response) {
//                            Log.w("ALERT", response.toString());
//                            try {
//                                response.getJSONObject().get("data");
//                            } catch (JSONException j) {
//                                Log.w("HERE:", response.toString());
//                            }
//                        }
//                    }
//            ).executeAsync();
            final String fbGroupId = mGroup.get_facebookGroupId();
            if (fbGroupId != null) {
                mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JoinAppGroupDialog.show(getActivity(), fbGroupId);

                        if (!mGroup.isMember(Utils.get_current_user())) {
                            mGroup.addMember(Utils.get_current_user());
                            mGroup.save();
                        }
                    }
                });
            }

            mTitleTextView =
                    (TextView) itemView.findViewById(R.id.group_details_action_title_text_view);
            mTitleTextView.setText(mGroup.get_name());
            mSubheadTextView =
                    (TextView) itemView.findViewById(R.id.group_details_action_subhead_text_view);

            String memberString = mGroup.get_size() == 1 ? " member." : " members.";
            mSubheadTextView.setText(
                    "You're in! This groups has " + mGroup.get_size() + memberString);
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<com.djkim.slap.models.User> mGroupUsers;

        public UserAdapter(ArrayList<com.djkim.slap.models.User> groupUsers) {
            mGroupUsers = groupUsers;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_HEADER) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.group_details_header, parent, false);
                return new SectionHeaderHolder(view);
            } else if (viewType == VIEW_TYPE_ACTION) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.group_details_action, parent, false);
                return new DetailsActivityHolder(view);
            } else {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.group_details_item, parent, false);
                return new UserHolder(view);
            }
        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case 0:
                    return VIEW_TYPE_ACTION;
                case 1:
                    return VIEW_TYPE_HEADER;
                default:
                    return VIEW_TYPE_CONTENT;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (position) {
                case 0: {}
                break;
                case 1: {
                    ((SectionHeaderHolder) holder).setText(
                            getResources().getString(R.string.group_details_group_members));
                }
                break;
                default: {
                    com.djkim.slap.models.User user = mGroupUsers.get(position - 2);
                    ((UserHolder) holder).bindUser(user);
                }
            }
        }

        @Override
        public int getItemCount() { return mGroupUsers.size() + 2; }
    }
}
