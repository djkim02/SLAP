package com.djkim.slap.profile;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djkim.slap.R;

/**
 * Created by dongjoonkim on 10/25/15.
 */
public class CreateProfileWelcomeFragment extends CreateProfileAbstractFragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_profile_welcome_fragment_layout, container, false);
        TextView heading = (TextView) view.findViewById(R.id.heading);
        TextView subheading = (TextView) view.findViewById(R.id.subheading);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "BebasNeue Bold.otf");
        heading.setTypeface(tf);
        tf = Typeface.createFromAsset(getContext().getAssets(), "BebasNeue Regular.otf");
        subheading.setTypeface(tf);
        return view;
    }

    public void onPrevButtonClick() {}

    public void onNextButtonClick() {
        CreateProfileActivity createProfileActivity = (CreateProfileActivity) this.getActivity();
        createProfileActivity.setPrevButtonText("Back");
        createProfileActivity.setTitleText("Programming Skills");
        createProfileActivity.getCreateProfileLayout().setBackgroundColor(0xffffffff);
        createProfileActivity.setPrevButtonColorToPrimary();
        createProfileActivity.setNextButtonColorToPrimary();
    }
}