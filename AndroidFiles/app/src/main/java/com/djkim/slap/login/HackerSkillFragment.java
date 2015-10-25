package com.djkim.slap.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.djkim.slap.R;
import com.parse.ParseObject;

import java.util.Map;

/**
 * Created by ryan on 10/24/15.
 */
public class HackerSkillFragment extends Fragment {
    private Map<String, Integer> m_hacker_skills;
    Integer True = new Integer(1);
    Integer False = new Integer(0);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hacker_skills_fragment_layout, container, false);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_cpp:
                if (checked){
                    m_hacker_skills.put("cpp", True);
                }
                else{
                    m_hacker_skills.put("cpp", False);
                }
                break;
            case R.id.checkbox_java:
                if (checked){
                    m_hacker_skills.put("java", True);
                }
                else{
                    m_hacker_skills.put("java", False);
                }
                break;
        }
    }

    public void onSubmitButtonClicked(View view) {
        ParseObject hacker_skills = new ParseObject("hacker_profile");
        for (String key : m_hacker_skills.keySet()) {
            hacker_skills.put(key, m_hacker_skills.get(key));
        }
    }
}
