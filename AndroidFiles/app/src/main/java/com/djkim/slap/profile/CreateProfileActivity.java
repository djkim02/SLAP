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
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.NonSwipeableViewPager;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;
import com.djkim.slap.models.ZoomOutPageTransformer;

import java.util.List;

/**
 * Created by dongjoonkim on 10/25/15.
 */

public class CreateProfileActivity extends FragmentActivity {

    private NonSwipeableViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private FragmentManager fragmentManager;
    private User user;
    private CreateProfileWelcomeFragment welcomeFragment;
    private CreateProfileHackerFragment hackerFragment;
    private CreateProfileAthleteFragment athleteFragment;
    private CreateProfileDoneFragment doneFragment;
    private Button prevButton;
    private Button nextButton;
    private TextView createProfileTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile_activity_layout);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        mPager = (NonSwipeableViewPager) findViewById(R.id.profile_pager);
        fragmentManager = getSupportFragmentManager();
        mPagerAdapter = new profilePageAdapter(fragmentManager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        welcomeFragment = new CreateProfileWelcomeFragment();
        hackerFragment = new CreateProfileHackerFragment();
        athleteFragment = new CreateProfileAthleteFragment();
        doneFragment = new CreateProfileDoneFragment();

        prevButton = (Button) findViewById(R.id.prev_button);
        nextButton = (Button) findViewById(R.id.next_button);
        createProfileTitle = (TextView) findViewById(R.id.create_profile_title);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mPager.getCurrentItem();
                if (currentItem == 1) {
                    prevButton.setText("");
                    user.set_hacker_skills(hackerFragment.updateHackerSkills());
                    createProfileTitle.setText("Tell us about yourself");
                } else if (currentItem == 2) {
                    createProfileTitle.setText("Programming Skills");
                } else if (currentItem == 3) {
                    nextButton.setText("Next");
                    createProfileTitle.setText("Athlete Skills");
                }
                mPager.setCurrentItem(currentItem - 1, true);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mPager.getCurrentItem();
                if (currentItem == 0) {
                    prevButton.setText("Back");
                    createProfileTitle.setText("Programming Skills");
                } else if (currentItem == 1) {
                    user.set_hacker_skills(hackerFragment.updateHackerSkills());
                    createProfileTitle.setText("Athlete Skills");
                } else if (currentItem == 2) {
                    createProfileTitle.setText("Congratulations!");
                    nextButton.setText("Done");
                }
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
        public Fragment getItem(int position) {
            if (position == 0) {
                return welcomeFragment;
            } else if (position == 1) {
                User user = Utils.get_current_user();
                Group group = new Group("group2", user, 1001);
                group.save();
                // user.addGroup(group);
                // user.save();
                return hackerFragment;
            } else if (position == 2) {
                return athleteFragment;
            } else if (position == 3) {
                return doneFragment;
            }
            return welcomeFragment;
        }
    }
}