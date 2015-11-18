package com.djkim.slap.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.models.SkillWithoutCheckbox;
import com.djkim.slap.models.SkillsWithoutCheckboxListAdapter;
import com.djkim.slap.models.User;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

/**
 * Created by dongjoonkim on 11/17/15.
 */
public class OthersProfileActivity extends Activity {
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_profile);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        TextView nameTextView = (TextView) findViewById(R.id.profile_name);
        nameTextView.setText(user.get_name());
        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profile_pic);
        profilePictureView.setProfileId(user.get_facebook_profile_id());

        ArrayList<SkillWithoutCheckbox> optionsList = SkillWithoutCheckbox.returnOthersProfileList();
        SkillsWithoutCheckboxListAdapter adapter = new SkillsWithoutCheckboxListAdapter(this, R.layout.skills_without_checkbox, optionsList);
        ListView listView = (ListView) findViewById(R.id.profile_skills);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (position == 0) {
                    intent = new Intent(OthersProfileActivity.this, OthersHackerSkillsActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else if (position == 1) {
                    intent = new Intent(OthersProfileActivity.this, OthersAthleteSkillsActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });
    }
}
