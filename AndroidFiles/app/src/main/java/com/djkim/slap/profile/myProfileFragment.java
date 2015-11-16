package com.djkim.slap.profile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.models.SkillWithoutCheckbox;
import com.djkim.slap.models.SkillsWithoutCheckboxListAdapter;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

/**
 * Created by dongjoonkim on 11/14/15.
 */
public class MyProfileFragment extends Fragment {

    private View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu_profile, container, false);
        User currUser = Utils.get_current_user();
        TextView nameTextView = (TextView) rootview.findViewById(R.id.profile_name);
        nameTextView.setText(currUser.get_name());
        Profile profile = Profile.getCurrentProfile();
        ProfilePictureView profilePictureView = (ProfilePictureView) rootview.findViewById(R.id.profile_pic);
        profilePictureView.setProfileId(profile.getId());

        ArrayList<SkillWithoutCheckbox> optionsList = SkillWithoutCheckbox.returnMyProfileList();
        SkillsWithoutCheckboxListAdapter adapter = new SkillsWithoutCheckboxListAdapter(this.getActivity(), R.layout.skills_without_checkbox, optionsList);
        ListView listView = (ListView) rootview.findViewById(R.id.profile_skills);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (position == 0) {
                    intent = new Intent(getActivity(), MyHackerSkillsActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    intent = new Intent(getActivity(), MyAthleteSkillsActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    intent = new Intent(getActivity(), EditMyProfileActivity.class);
                    startActivity(intent);
                }
            }
        });
        return rootview;
    }
}