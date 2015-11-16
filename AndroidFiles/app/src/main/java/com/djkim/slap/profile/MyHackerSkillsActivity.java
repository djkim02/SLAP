package com.djkim.slap.profile;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
public class MyHackerSkillsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_skills);
        User user = Utils.get_current_user();
        TextView textView = (TextView) findViewById(R.id.my_skills_text);
        textView.setText("My Hacker Skills");

        ArrayList<Skill> myHackerList = user.get_hacker_skills();
        ArrayList<SkillWithoutCheckbox> returnHackerList = new ArrayList<>();

        for (Skill skill : myHackerList) {
            if (skill.isSelected()) {
                returnHackerList.add(new SkillWithoutCheckbox(skill.getImageId(), skill.getName()));
            }
        }
        SkillsWithoutCheckboxListAdapter adapter = new SkillsWithoutCheckboxListAdapter(this, R.layout.skills_without_checkbox, returnHackerList);
        ListView listView = (ListView) findViewById(R.id.my_skills_list);
        listView.setAdapter(adapter);
    }

    public void onBackClicked(View view) {
        this.finish();
    }
}
