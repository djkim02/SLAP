package com.djkim.slap.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.djkim.slap.R;
import com.djkim.slap.models.Skill;
import com.djkim.slap.models.SkillsListAdapter;

import java.util.ArrayList;

/**
 * Created by dongjoonkim on 10/25/15.
 */
public class CreateProfileHackerFragment extends CreateProfileAbstractFragment {
    private ArrayList<Skill> hackerSkills = null;
    SkillsListAdapter skillsListAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_profile_hacker_fragment_layout, container, false);
        if (hackerSkills == null) {
            hackerSkills = Skill.returnHackerSkillsList();
        }
        skillsListAdapter = new SkillsListAdapter(this.getActivity(), R.layout.skills_info, hackerSkills);
        ListView listView = (ListView) view.findViewById(R.id.hacker_list_view);
        listView.setAdapter(skillsListAdapter);
        return view;
    }

    public void onPrevButtonClick() {
        CreateProfileActivity createProfileActivity = (CreateProfileActivity) this.getActivity();
        createProfileActivity.setPrevButtonText("");
        createProfileActivity.updateHackerSkills(hackerSkills);
        createProfileActivity.setTitleText("Tell us about yourself");
        createProfileActivity.setPrevButtonColorToBlack();
        createProfileActivity.setNextButtonColorToBlack();
        createProfileActivity.getCreateProfileLayout().setBackgroundResource(R.drawable.tellme);
    }

    public void onNextButtonClick() {
        CreateProfileActivity createProfileActivity = (CreateProfileActivity) this.getActivity();
        createProfileActivity.updateHackerSkills(hackerSkills);
        createProfileActivity.setTitleText("Athlete Skills");
        createProfileActivity.setPrevButtonColorToPrimary();
        createProfileActivity.setNextButtonColorToPrimary();
    }

//    public ArrayList<Skill> updateHackerSkills() {
//        return hackerSkills;
//    }
}