package com.djkim.slap.profile;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.menubar.MainActivity;
import com.djkim.slap.models.UserCallback;

/**
 * Created by dongjoonkim on 10/25/15.
 */
public class CreateProfileDoneFragment extends CreateProfileAbstractFragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_profile_done_fragment_layout, container, false);
        TextView heading = (TextView) view.findViewById(R.id.heading);
        TextView subheading = (TextView) view.findViewById(R.id.subheading);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "BebasNeue Bold.otf");
        heading.setTypeface(tf);
        tf = Typeface.createFromAsset(getContext().getAssets(), "BebasNeue Regular.otf");
        subheading.setTypeface(tf);
        return view;
    }

    public void onPrevButtonClick() {
        CreateProfileActivity createProfileActivity = (CreateProfileActivity) this.getActivity();
        createProfileActivity.setNextButtonText("Next");
        createProfileActivity.setTitleText("Athlete Skills");
        createProfileActivity.setPrevButtonColorToPrimary();
        createProfileActivity.setNextButtonColorToPrimary();
        createProfileActivity.getCreateProfileLayout().setBackgroundColor(0xffffffff);
    }

    public void onNextButtonClick() {
        final CreateProfileActivity createProfileActivity = (CreateProfileActivity) this.getActivity();
        createProfileActivity.saveUser(new UserCallback() {
            @Override
            public void done() {
                startActivity(new Intent(createProfileActivity, MainActivity.class));
                CreateProfileDoneFragment.this.getActivity().finish();
            }
        });
        startActivity(new Intent(createProfileActivity, MainActivity.class));
        this.getActivity().finish();
    }
}