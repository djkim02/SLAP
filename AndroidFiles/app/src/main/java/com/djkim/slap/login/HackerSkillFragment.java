package com.djkim.slap.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.djkim.slap.R;

/**
 * Created by ryan on 10/24/15.
 */
public class HackerSkillFragment extends Fragment {
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
                if (checked){}
                // Put some meat on the sandwich
                else{}
                // Remove the meat
                break;
            case R.id.checkbox_java:
                if (checked){}
                // Cheese me
                else{}
                // I'm lactose intolerant
                break;
        }
    }
}
