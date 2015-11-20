package com.djkim.slap.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.NonSwipeableViewPager;
import com.djkim.slap.models.Skill;
import com.djkim.slap.models.User;
import com.djkim.slap.models.UserCallback;
import com.djkim.slap.models.Utils;
import com.djkim.slap.models.ZoomOutPageTransformer;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by dongjoonkim on 10/25/15.
 */

public class CreateProfileActivity extends FragmentActivity {
    private NonSwipeableViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private FragmentManager fragmentManager;
    private User user;
    private ArrayList<CreateProfileAbstractFragment> fragments;
    private Button prevButton;
    private Button nextButton;
    private TextView createProfileTitle;
    private LinearLayout createProfileLayout;

    public void updateHackerSkills(ArrayList<Skill> skills) {
        user.set_hacker_skills(skills);
    }

    public void updateAthleteSkills(ArrayList<Skill> skills) {
        user.set_athlete_skills(skills);
    }

    public void setTitleText(String text) {
        createProfileTitle.setText(text);
    }

    public void setPrevButtonText(String text) {
        prevButton.setText(text);
    }

    public void setNextButtonText(String text) {
        nextButton.setText(text);
    }

    public void setPrevButtonColorToPrimary() {
        prevButton.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void setNextButtonColorToPrimary() {
        nextButton.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void setPrevButtonColorToBlack() {
        prevButton.setTextColor(getResources().getColor(R.color.vpi__background_holo_dark));
    }

    public void setNextButtonColorToBlack() {
        nextButton.setTextColor(getResources().getColor(R.color.vpi__background_holo_dark));
    }

    public void saveUser(final UserCallback callback) {
        user.saveInBackground(callback);
    }

    public LinearLayout getCreateProfileLayout() {
        return createProfileLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile_activity_layout);

        Intent intent = getIntent();
        // Make sure that user is non-null.
        // Current flow should ensure that CreateProfileActivity is only created from LoginActivity
        user = (User) intent.getSerializableExtra("user");

        mPager = (NonSwipeableViewPager) findViewById(R.id.profile_pager);
        fragmentManager = getSupportFragmentManager();
        mPagerAdapter = new profilePageAdapter(fragmentManager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        createProfileLayout = (LinearLayout) findViewById(R.id.create_profile_layout);

        fragments = new ArrayList<CreateProfileAbstractFragment>();
        fragments.add(new CreateProfileWelcomeFragment());
        fragments.add(new CreateProfileHackerFragment());
        fragments.add(new CreateProfileAthleteFragment());
        fragments.add(new CreateProfileDoneFragment());

        prevButton = (Button) findViewById(R.id.prev_button);
        nextButton = (Button) findViewById(R.id.next_button);
        createProfileTitle = (TextView) findViewById(R.id.edit_profile_text);
        setTitleText("Tell us about yourself");

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mPager.getCurrentItem();
                fragments.get(currentItem).onPrevButtonClick();
                mPager.setCurrentItem(currentItem - 1, true);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mPager.getCurrentItem();
                fragments.get(currentItem).onNextButtonClick();
                mPager.setCurrentItem(currentItem + 1, true);
            }
        });
    }

    private class profilePageAdapter extends FragmentPagerAdapter {
        public profilePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position)
        {
            if (position < fragments.size()) {
                return fragments.get(position);
            } else {
                return null;
            }
        }
    }
}