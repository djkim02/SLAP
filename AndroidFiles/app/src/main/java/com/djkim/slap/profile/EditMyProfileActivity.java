package com.djkim.slap.profile;

import android.app.Activity;
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
import com.djkim.slap.models.NonSwipeableViewPager;
import com.djkim.slap.models.Skill;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;
import com.djkim.slap.models.ZoomOutPageTransformer;

import java.util.ArrayList;

/**
 * Created by dongjoonkim on 11/15/15.
 */
public class EditMyProfileActivity extends FragmentActivity {
    private NonSwipeableViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private FragmentManager fragmentManager;
    private User user = Utils.get_current_user();
    private ArrayList<CreateProfileAbstractFragment> fragments;
    private Button prevButton;
    private Button nextButton;
    private TextView editProfileText;
    private LinearLayout editProfileLayout;

    public void updateHackerSkills(ArrayList<Skill> skills) {
        user.set_hacker_skills(skills);
    }

    public void updateAthleteSkills(ArrayList<Skill> skills) {
        user.set_athlete_skills(skills);
    }

    public void setPrevButtonText(String text) {
        prevButton.setText(text);
    }

    public void setNextButtonText(String text) {
        nextButton.setText(text);
    }

    public User getUser() {
        return user;
    }

    public void setTitleText(String text) {
        editProfileText.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile_activity_layout);

        mPager = (NonSwipeableViewPager) findViewById(R.id.profile_pager);
        fragmentManager = getSupportFragmentManager();
        mPagerAdapter = new profilePageAdapter(fragmentManager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        fragments = new ArrayList<CreateProfileAbstractFragment>();
        fragments.add(new EditProfileHackerFragment());
        fragments.add(new EditProfileAthleteFragment());

        prevButton = (Button) findViewById(R.id.prev_button);
        nextButton = (Button) findViewById(R.id.next_button);
        editProfileText = (TextView) findViewById(R.id.edit_profile_text);
        editProfileLayout = (LinearLayout) findViewById(R.id.create_profile_layout);
        editProfileLayout.setBackgroundColor(0xffffffff);

        setTitleText("Edit My Hacker Skills");

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
            return 2;
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
