package com.djkim.slap.group;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.djkim.slap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kylemn on 10/26/15.
 */
public class GroupDetailsFragment extends Fragment {
    private RecyclerView mGroupRecyclerView;
    private UserAdapter mGroupDetailsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_details_fragment, container, false);

        mGroupRecyclerView = (RecyclerView) rootView.findViewById(R.id.group_recycler_view);
        mGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<User> groupUsers = new ArrayList<>();
        mGroupDetailsAdapter = new UserAdapter(groupUsers);
        mGroupRecyclerView.setAdapter(mGroupDetailsAdapter);

        return rootView;
    }

    public class User {
    }

    private class UserHolder extends RecyclerView.ViewHolder {
        private User mUser;
        private ImageView mThumbnailImageView;
        private TextView mTitleTextView;

        public UserHolder(View itemView) {
            super(itemView);
        }

        public void bindUser(User user) { mUser = user; }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserHolder> {
        private List<User> mGroupUsers;

        public UserAdapter(List<User> groupUsers) { mGroupUsers = groupUsers; }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.group_details_item, parent, false);
            return new UserHolder(view);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, int position) {
            User user = mGroupUsers.get(position);
            holder.bindUser(user);
        }

        @Override
        public int getItemCount() { return mGroupUsers.size(); }
    }
}
