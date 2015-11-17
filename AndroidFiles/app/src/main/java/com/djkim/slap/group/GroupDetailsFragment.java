package com.djkim.slap.group;


import android.app.ActionBar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.djkim.slap.R;
import com.djkim.slap.messenger.MessagingActivity;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.widget.JoinAppGroupDialog;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

    private Context globalContext = null;

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

        globalContext = this.getActivity();

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
        private ImageView mThumbnailImageView;
        private TextView mTitleTextView;
        private TextView mSubheadTextView;
        private LinearLayout mUserItem;

        public UserHolder(View itemView) {
            super(itemView);


            mUserItem =
                    (LinearLayout) itemView.findViewById(R.id.group_details_item);
            mThumbnailImageView =
                    (ImageView) itemView.findViewById(R.id.group_details_item_thumbnail_image_view);
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

                    openConversation(mUser.get_id());
                }
            });
        }

        public void bindUser(com.djkim.slap.models.User user) {
            mUser = user;

            mTitleTextView.setText(user.get_name());
            mSubheadTextView.setText(mGroup.get_owner().equals(mUser) ? "Admin" : "Member");
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
//            // We only show the onClickListener if that person is in the SLAP group, but not in the facebook group
//            //TODO: Need to remove the slap card if the person left the group on Facebook
//            new GraphRequest(
//                    AccessToken.getCurrentAccessToken(),
//                    "/6EZmymlOoCjIFnPPnJ13XcpeyyoXNIVoXTq2RwMo/groups",
//                    null,
//                    HttpMethod.GET,
//                    new GraphRequest.Callback() {
//                        public void onCompleted(GraphResponse response) {
//                            Log.w("ALERT", response);
//                            response.getJSONObject().get("")
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
