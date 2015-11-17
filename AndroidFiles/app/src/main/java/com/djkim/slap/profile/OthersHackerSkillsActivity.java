package com.djkim.slap.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.models.Skill;
import com.djkim.slap.models.SkillWithoutCheckbox;
import com.djkim.slap.models.SkillsWithoutCheckboxListAdapter;
import com.djkim.slap.models.User;

import java.util.ArrayList;

/**
 * Created by dongjoonkim on 11/17/15.
 */
public class OthersHackerSkillsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_skills);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        TextView textView = (TextView) findViewById(R.id.my_skills_text);
        String name = user.get_name();
        int endSpace = name.lastIndexOf(" ");
        textView.setText(name.substring(0, endSpace) + "'s Hacker Skills");
        ArrayList<Skill> myAthleteList = user.get_hacker_skills();
        ArrayList<SkillWithoutCheckbox> returnAthleteList = new ArrayList<>();

        for (Skill skill : myAthleteList) {
            if (skill.isSelected()) {
                returnAthleteList.add(new SkillWithoutCheckbox(skill.getImageId(), skill.getName()));
            }
        }
        SkillsWithoutCheckboxListAdapter adapter = new SkillsWithoutCheckboxListAdapter(this, R.layout.skills_without_checkbox, returnAthleteList);
        ListView listView = (ListView) findViewById(R.id.my_skills_list);
        listView.setAdapter(adapter);
    }

    public void onBackClicked(View view) {
        this.finish();
    }
}