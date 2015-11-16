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
import com.djkim.slap.models.Utils;

import java.util.ArrayList;

/**
 * Created by dongjoonkim on 10/25/15.
 */
public class EditProfileHackerFragment extends CreateProfileAbstractFragment {
    SkillsListAdapter skillsListAdapter = null;
    EditMyProfileActivity editMyProfileActivity;
    private ArrayList<Skill> hackerSkills;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        editMyProfileActivity = (EditMyProfileActivity) getActivity();
        hackerSkills = editMyProfileActivity.getUser().get_hacker_skills();
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
    }

    public void onNextButtonClick() {
        editMyProfileActivity.updateHackerSkills(hackerSkills);
        editMyProfileActivity.setTitleText("Edit My Athlete Skills");
        editMyProfileActivity.setPrevButtonText("Prev");
        editMyProfileActivity.setNextButtonText("Done");
    }
}