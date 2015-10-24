package com.djkim.slap.login;

/**
 * Created by dongjoonkim on 10/8/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.djkim.slap.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.facebook.FacebookSdk;

public class LoginActivity extends FragmentActivity {
    //The number of pictures / pages to show
    private static final int NUM_PAGES = 4;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new LoginPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(mPager);
    }

    private class LoginPagerAdapter extends FragmentPagerAdapter {
        public LoginPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SlapLoginFragment();
                case 1:
                    return new GroupsFragment();
                case 2:
                    return new HackerFragment();
                case 3:
                    return new AthleteFragment();
                default:
                    return new SlapLoginFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
