package com.djkim.slap.group;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.models.GroupCallback;
import com.djkim.slap.models.User;
import com.djkim.slap.models.UserCallback;
import com.djkim.slap.models.Utils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.widget.JoinAppGroupDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victorkwan on 11/24/15.
 */
public class MemberGroupDetailsFragment extends GroupDetailsFragment {
    private class DetailsActivityHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mSubheadTextView;
        private TextView mDescriptionTextView;
        private Button mJoinGroupButton;

        public DetailsActivityHolder(View itemView) {
            super(itemView);
            mJoinGroupButton =
                    (Button) itemView.findViewById(R.id.group_details_action_join_group_button);
            mJoinGroupButton.setVisibility(View.GONE);

            // We only set the onClickListener if there is such a Facebook Group.
            // We only show the onClickListener if that person is in the SLAP group, but not in the facebook group
            // TODO: Need to remove the slap card if the person left the group on Facebook
            String facebookGroupId = mGroup.get_facebookGroupId();
            if (facebookGroupId != null) {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + facebookGroupId + "/members",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try {
                                    boolean hideButton = false;
                                    JSONObject jsonResult = response.getJSONObject();
                                    if (jsonResult != null) {
                                        JSONArray jsonDataArray = jsonResult.getJSONArray("data");
                                        for(int i = 0; i < jsonDataArray.length(); i++) {
                                            JSONObject jsonGroupMember =
                                                    (JSONObject) jsonDataArray.get(i);
                                            if(currentUser.get_facebook_id().toString()
                                                    .equals(jsonGroupMember.getString("id"))) {
                                                hideButton = true;
                                                break;
                                            }
                                        }
                                        configureJoinButton(!hideButton);
                                    }
                                } catch (JSONException e) {

                                }
                            }
                        }
                ).executeAsync();
            }

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

        // TODO(victorkwan): Need to configure the button for an admin creating the group.
        private void configureJoinButton(boolean shouldDisplay) {
            if (shouldDisplay) {
                // TODO(victorkwan): Currently, this is quite jumpy. We should add an animation for
                // the button to "slide" down.
                mJoinGroupButton.setVisibility(View.VISIBLE);

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
                                curUser.saveInBackground(new UserCallback() {
                                    @Override
                                    public void done() {
                                        JoinAppGroupDialog.show(getActivity(), fbGroupId);
                                    }
                                });
                            } else {
                                JoinAppGroupDialog.show(getActivity(), fbGroupId);
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder createDetailsActivityHolder(View itemView) {
        return new DetailsActivityHolder(itemView);
    }
}
