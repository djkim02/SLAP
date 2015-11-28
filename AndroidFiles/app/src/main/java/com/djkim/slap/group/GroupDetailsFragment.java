package com.djkim.slap.group;


import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.menubar.MainActivity;
import com.djkim.slap.messenger.MessagingActivity;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.User;
import com.djkim.slap.models.UsersCallback;
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
public abstract class GroupDetailsFragment extends Fragment {
    private static int VIEW_TYPE_HEADER = 0;

    // Placeholder for the future "Convert to FB group" option.
    private static int VIEW_TYPE_ACTION = 1;
    private static int VIEW_TYPE_CONTENT = 2;

    protected final static int UPDATED_GROUP_REQUEST_CODE = 100;

    public final static String sGroupArgumentKey = "group_details_group_argument";

    public static final String RECIPIENT = "recipientName";
    public static final String CURRENTUSER = "currentUserName";

    private RecyclerView mGroupDetailsRecyclerView;
    protected UserAdapter mGroupDetailsAdapter;
    protected Group mGroup;
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

        mGroup.getMembersInBackground(new UsersCallback() {
            @Override
            public void done(List<User> users) {
                mGroupDetailsAdapter = new UserAdapter(users);
                mGroupDetailsRecyclerView.setAdapter(mGroupDetailsAdapter);
            }
        });
        globalContext = this.getActivity();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATED_GROUP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mGroup = (Group) data.getExtras().getSerializable(EditGroupActivity.EDIT_GROUP_EXTRA);
            Utils.get_current_user().updateGroup(mGroup);

            // In this case, we can specify that the header has changed.
            mGroupDetailsAdapter.notifyItemChanged(0, 1);
        }
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
        //private RelativeLayout mRelativeLayout;
        private ImageButton mMessageButton;


        public UserHolder(View itemView) {
            super(itemView);
            mUserItem = (LinearLayout) itemView.findViewById(R.id.group_details_item);
            mThumbnailImageView =
                    (ProfilePictureView) itemView.findViewById(R.id.group_details_item_thumbnail_image_view);
            mTitleTextView =
                    (TextView) itemView.findViewById(R.id.group_details_item_title_text_view);
            mSubheadTextView =
                    (TextView) itemView.findViewById(R.id.group_details_item_subhead_text_view);
            mMessageButton = (ImageButton) itemView.findViewById(R.id.message_button);

            //Set on-click listener for messaging with Sinch
            mMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Check if user is trying to click on himself/herself
                    if (!mUser.get_id().equals(ParseUser.getCurrentUser().getObjectId())) {
                        openConversation(mUser.get_id());
                    }
                }
            });
        }

        public void bindUser(com.djkim.slap.models.User user) {
            mUser = user;
            mThumbnailImageView.setProfileId(user.get_facebook_profile_id());
            mTitleTextView.setText(user.get_name());
            mSubheadTextView.setText(mGroup.isOwner(mUser) ? "Admin" : "Member");

            if (mUser.get_facebook_profile_id() != null) {
                mUserItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), OthersProfileActivity.class);
                        intent.putExtra("user", mUser);
                        startActivity(intent);
                    }
                });
            }

            //If it's the same user, do not show the button
            if (mUser.get_id().equals(ParseUser.getCurrentUser().getObjectId())) {
                mMessageButton.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Factory method that returns a ViewHolder for the header cell.
     * @return The ViewHolder to be returned.
     */
    protected abstract RecyclerView.ViewHolder createDetailsActivityHolder(View itemView);


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
                return createDetailsActivityHolder(view);
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
                        intent.putExtra(RECIPIENT, user.get(0).getUsername());
                        intent.putExtra(CURRENTUSER, ParseUser.getCurrentUser().getUsername());
                        startActivity(intent);
                    } else {
                        //show some sort of error
                    }
                }
            });
        }
}
