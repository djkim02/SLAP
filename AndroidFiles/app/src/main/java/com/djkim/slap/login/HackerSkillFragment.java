package com.djkim.slap.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.djkim.slap.R;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ryan on 10/24/15.
 */
public class HackerSkillFragment extends Fragment {
    private static Map<String, Integer> m_hacker_skills = new HashMap<String, Integer>();
    Integer True = new Integer(1);
    Integer False = new Integer(0);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        CheckBox c = (CheckBox) getView().findViewById(R.id.checkbox_cpp);
        Toast.makeText(getActivity().getApplicationContext(), "onCreateView", Toast.LENGTH_SHORT).show();

//        c.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCheckboxClicked(v);
//            }
//        });
        return inflater.inflate(R.layout.hacker_skills_fragment_layout, container, false);
    }


}
