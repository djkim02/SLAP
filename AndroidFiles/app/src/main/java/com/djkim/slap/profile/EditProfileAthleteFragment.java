package com.djkim.slap.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.djkim.slap.R;
import android.widget.ListView;

import com.djkim.slap.models.Skill;
import com.djkim.slap.models.SkillsListAdapter;
import com.djkim.slap.models.UserCallback;

import java.util.ArrayList;

/**
 * Created by dongjoonkim on 10/25/15.
 */
public class EditProfileAthleteFragment extends CreateProfileAbstractFragment {
    SkillsListAdapter skillsListAdapter = null;
    EditMyProfileActivity editMyProfileActivity;
    private ArrayList<Skill> athleteSkills;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        editMyProfileActivity = (EditMyProfileActivity) getActivity();
        athleteSkills = editMyProfileActivity.getUser().get_athlete_skills();
        View view = inflater.inflate(R.layout.create_profile_athlete_fragment_layout, container, false);
        if (athleteSkills == null) {
            athleteSkills = Skill.returnAthleteSkillsList();
        }
        skillsListAdapter = new SkillsListAdapter(this.getActivity(), R.layout.skills_info, athleteSkills);
        ListView listView = (ListView) view.findViewById(R.id.athlete_list_view);
        listView.setAdapter(skillsListAdapter);
        return view;
    }

    public void onPrevButtonClick() {
        EditMyProfileActivity editMyProfileActivity = (EditMyProfileActivity) this.getActivity();
        editMyProfileActivity.updateAthleteSkills(athleteSkills);
        editMyProfileActivity.setTitleText("Edit Hacker Skills");
        editMyProfileActivity.setPrevButtonText("");
    }

    public void onNextButtonClick() {
        final EditMyProfileActivity editMyProfileActivity = (EditMyProfileActivity) this.getActivity();
        editMyProfileActivity.updateAthleteSkills(athleteSkills);
        editMyProfileActivity.getUser().saveInBackground(new UserCallback() {
            @Override
            public void done() {
                Toast.makeText(EditProfileAthleteFragment.this.getActivity(), "Your profile has been edited!", Toast.LENGTH_SHORT).show();
                editMyProfileActivity.finish();
            }
        });
    }
}