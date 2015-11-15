package com.djkim.slap.profile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djkim.slap.R;
import com.djkim.slap.models.Skill;
import com.djkim.slap.models.SkillWithoutCheckbox;
import com.djkim.slap.models.SkillsWithoutCheckboxListAdapter;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;

import java.util.ArrayList;

/**
 * Created by dongjoonkim on 11/15/15.
 */
public class MyHackerSkillsFragment extends Fragment {

    private View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.my_skills, container, false);
        User user = Utils.get_current_user();
        ArrayList<Skill> myHackerList = user.get_hacker_skills();
        ArrayList<SkillWithoutCheckbox> returnHackerList = new ArrayList<>();

        for (Skill skill : myHackerList) {
            if (skill.isSelected()) {
                returnHackerList.add(new SkillWithoutCheckbox(skill.getImageId(), skill.getName()));
            }
        }
        
        return rootview;
    }
}
