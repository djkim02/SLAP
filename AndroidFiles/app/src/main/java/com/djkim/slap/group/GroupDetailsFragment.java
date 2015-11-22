package com.djkim.slap.group;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.messenger.MessagingActivity;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    final User currentUser = Utils.get_current_user();

    private Context globalContext = null;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_details_fragment, container, false);

        mGroupDetailsRecyclerView = (RecyclerView) rootView.findViewById(R.id.group_recycler_view);
        mGroupDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = getArguments();
        mGroup = (Group) bundle.getSerializable(sGroupArgumentKey);

        List<com.djkim.slap.models.User> groupUsers = mGroup.getMembers();
        mGroupDetailsAdapter = new UserAdapter(groupUsers);
        mGroupDetailsRecyclerView.setAdapter(mGroupDetailsAdapter);

        globalContext = this.getActivity();

        return rootView;
    }

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
        private LinearLayout mUserItem;
        private RelativeLayout mRelativeLayout;


        public UserHolder(View itemView) {
            super(itemView);
                mUserItem =
                        (LinearLayout) itemView.findViewById(R.id.group_details_item);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.group_details_layout);
            mThumbnailImageView =
                    (ProfilePictureView) itemView.findViewById(R.id.group_details_item_thumbnail_image_view);
            mTitleTextView =
                    (TextView) itemView.findViewById(R.id.group_details_item_title_text_view);
            mSubheadTextView =
                    (TextView) itemView.findViewById(R.id.group_details_item_subhead_text_view);

            //Set on-click listener for messaging with Sinch
            mUserItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*JoinAppGroupDialog.show(getActivity(), fbGroupId);

                    if (!mGroup.isMember(Utils.get_current_user())) {
                        mGroup.addMember(Utils.get_current_user());
                        mGroup.save();
                    }*/

                    //Check if user is trying to click on himself/herself
                    if(!mUser.get_id().equals(ParseUser.getCurrentUser().getObjectId())) {
                        openConversation(mUser.get_id());
                    }
                }
            });
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
        private TextView mTitleTextView;
        private TextView mSubheadTextView;
        private TextView mDescriptionTextView;
        private Button mJoinGroupButton;

        public DetailsActivityHolder(View itemView) {
            super(itemView);
            mJoinGroupButton =
                    (Button) itemView.findViewById(R.id.group_details_action_join_group_button);

            // We only set the onClickListener if there is such a Facebook Group.
            // We only show the onClickListener if that person is in the SLAP group, but not in the facebook group
            // TODO: Need to remove the slap card if the person left the group on Facebook
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + mGroup.get_facebookGroupId() + "/members",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            try {
                                boolean hideButton = false;
                                JSONArray jsonDataArray = response.getJSONObject().getJSONArray("data");
                                for(int i = 0; i < jsonDataArray.length(); i++) {
                                    JSONObject jsonGroupMember = (JSONObject)jsonDataArray.get(i);
                                    if(currentUser.get_facebook_id().toString().equals(jsonGroupMember.getString("id"))) {
                                        hideButton = true;
                                        break;
                                    }
                                }
                                configureJoinButton(!hideButton);
                            } catch (JSONException e) {

                            }
                        }
                    }
            ).executeAsync();

            mTitleTextView =
                    (TextView) itemView.findViewById(R.id.group_details_action_title_text_view);
            mTitleTextView.setText(mGroup.get_name());
            mSubheadTextView =
                    (TextView) itemView.findViewById(R.id.group_details_action_subhead_text_view);

            String memberString = mGroup.get_size() == 1 ? " member" : " members";
            mSubheadTextView.setText(
                    mGroup.get_type() + " group • " + mGroup.get_size() + memberString);

            mDescriptionTextView = (TextView) itemView.findViewById(
                    R.id.group_details_action_description_text_view);
            mDescriptionTextView.setText(mGroup.get_description());
        }

        private void configureJoinButton(boolean shouldDisplay) {
            if (shouldDisplay) {
                if (currentUser.isMemberOf(mGroup)) {
                    mJoinGroupButton.setText("Join the Facebook Group!");
                } else {
                    mJoinGroupButton.setText("Join the group?");
                }

                final String fbGroupId = mGroup.get_facebookGroupId();
                if (fbGroupId != null) {
                    mJoinGroupButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            User curUser = Utils.get_current_user();
                            if (!curUser.isMemberOf(mGroup)) {
                                curUser.joinAsMember(mGroup);
                                curUser.save();
                            }
                            JoinAppGroupDialog.show(getActivity(), fbGroupId);
                        }
                    });
                }
            } else {
                mJoinGroupButton.setVisibility(View.GONE);
            }
        }
    }


    private class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<com.djkim.slap.models.User> mGroupUsers;

        public UserAdapter(List<com.djkim.slap.models.User> groupUsers) {
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

        public void openConversation(String userID) {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", userID);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> user, ParseException e) {
                    if (e == null) {
                        Intent intent = new Intent(globalContext.getApplicationContext(), MessagingActivity.class);
                        intent.putExtra("RECIPIENT_ID", user.get(0).getObjectId());
                        startActivity(intent);
                    } else {
                        //show some sort of error
                    }
                }
            });
        }
}
