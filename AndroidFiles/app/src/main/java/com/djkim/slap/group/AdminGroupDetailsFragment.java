package com.djkim.slap.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.djkim.slap.R;
import com.djkim.slap.menubar.MainActivity;
import com.djkim.slap.models.GroupCallback;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.model.AppGroupCreationContent;
import com.facebook.share.widget.CreateAppGroupDialog;
import com.facebook.share.widget.JoinAppGroupDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victorkwan on 11/24/15.
 */
public class AdminGroupDetailsFragment extends GroupDetailsFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            mJoinGroupButton.setText("Edit Group Details");
            mJoinGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =
                            new Intent(AdminGroupDetailsFragment.this.getActivity(), EditGroupActivity.class);
                    intent.putExtra(EditGroupActivity.EDIT_GROUP_EXTRA, mGroup);
                    startActivityForResult(intent, UPDATED_GROUP_REQUEST_CODE);
                }
            });

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
    }

    @Override
    protected RecyclerView.ViewHolder createDetailsActivityHolder(View itemView) {
        return new DetailsActivityHolder(itemView);
    }


}
