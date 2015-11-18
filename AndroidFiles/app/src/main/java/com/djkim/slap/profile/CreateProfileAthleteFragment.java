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

import java.util.ArrayList;

/**
 * Created by dongjoonkim on 10/25/15.
 */
public class CreateProfileAthleteFragment extends CreateProfileAbstractFragment {
    private ArrayList<Skill> athleteSkills = null;
    SkillsListAdapter skillsListAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        CreateProfileActivity createProfileActivity = (CreateProfileActivity) this.getActivity();
        createProfileActivity.updateAthleteSkills(athleteSkills);
        createProfileActivity.setTitleText("Programming Skills");
        createProfileActivity.setPrevButtonColorToPrimary();
        createProfileActivity.setNextButtonColorToPrimary();
    }

    public void onNextButtonClick() {
        CreateProfileActivity createProfileActivity = (CreateProfileActivity) this.getActivity();
        createProfileActivity.updateAthleteSkills(athleteSkills);
        createProfileActivity.setTitleText("Congratulations!");
        createProfileActivity.setNextButtonText("Done");
        createProfileActivity.setPrevButtonColorToBlack();
        createProfileActivity.setNextButtonColorToBlack();
        createProfileActivity.getCreateProfileLayout().setBackgroundResource(R.drawable.groupmountain);
    }
}