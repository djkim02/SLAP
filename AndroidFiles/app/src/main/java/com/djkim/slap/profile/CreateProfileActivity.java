package com.djkim.slap.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.djkim.slap.R;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.NonSwipeableViewPager;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;
import com.djkim.slap.models.ZoomOutPageTransformer;
import com.parse.ParseException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity_layout);

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

        Button prevButton = (Button) findViewById(R.id.prev_button);
        Button nextButton = (Button) findViewById(R.id.next_button);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
            }
        });

        TextView nameView = (TextView) findViewById(R.id.nametext);
        String name = user.get_name();
        nameView.setText(name);
        TextView idView = (TextView) findViewById(R.id.profileUriText);
        String fbid = String.valueOf(user.get_facebook_id());
        idView.setText(fbid);
    }

    private class profilePageAdapter extends FragmentPagerAdapter {
        public profilePageAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount()
        {
            return 4;
        }
        @Override
        public Fragment getItem(int position)
        {
            if (position == 0) {
                return welcomeFragment;
            } else if (position == 1) {
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
