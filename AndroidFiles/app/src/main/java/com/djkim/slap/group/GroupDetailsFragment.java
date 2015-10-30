package com.djkim.slap.group;


import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;

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
        mGroup = (Group) bundle.getSerializable("group_list_group_argument");

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
        private ImageView mThumbnailImageView;
        private TextView mTitleTextView;
        private TextView mSubheadTextView;

        public UserHolder(View itemView) {
            super(itemView);

            mThumbnailImageView =
                    (ImageView) itemView.findViewById(R.id.group_details_item_thumbnail_image_view);
            mTitleTextView =
                    (TextView) itemView.findViewById(R.id.group_details_item_title_text_view);
            mSubheadTextView =
                    (TextView) itemView.findViewById(R.id.group_details_item_subhead_text_view);
        }

        public void bindUser(com.djkim.slap.models.User user) {
            mUser = user;

            mTitleTextView.setText(user.get_name());
            mSubheadTextView.setText(mGroup.get_owner().equals(mUser) ? "Admin" : "Member");
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
                // TODO(victorkwan): Add "Convert to Group" ViewHolder behavior here.
                return null;
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
                    return VIEW_TYPE_HEADER;
                default:
                    return VIEW_TYPE_CONTENT;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (position) {
                case 0: {
                    ((SectionHeaderHolder) holder).setText(
                            getResources().getString(R.string.group_details_group_members));
                }
                break;
                default: {
                    com.djkim.slap.models.User user = mGroupUsers.get(position - 1);
                    ((UserHolder) holder).bindUser(user);
                }
            }
        }

        @Override
        public int getItemCount() { return mGroupUsers.size() + 1; }
    }
}
