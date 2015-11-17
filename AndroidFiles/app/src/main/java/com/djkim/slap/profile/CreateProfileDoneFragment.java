package com.djkim.slap.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djkim.slap.R;
import com.djkim.slap.menubar.MainActivity;

/**
 * Created by dongjoonkim on 10/25/15.
 */
public class CreateProfileDoneFragment extends CreateProfileAbstractFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_profile_done_fragment_layout, container, false);
    }

    public void onPrevButtonClick() {
        CreateProfileActivity createProfileActivity = (CreateProfileActivity) this.getActivity();
        createProfileActivity.setNextButtonText("Next");
        createProfileActivity.setTitleText("Athlete Skills");
    }

    public void onNextButtonClick() {
        CreateProfileActivity createProfileActivity = (CreateProfileActivity) this.getActivity();
        createProfileActivity.saveUser();
        startActivity(new Intent(createProfileActivity, MainActivity.class));
        this.getActivity().finish();
    }
}