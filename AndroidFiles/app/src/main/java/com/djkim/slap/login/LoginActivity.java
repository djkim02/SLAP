package com.djkim.slap.login;

/**
 * Created by dongjoonkim on 10/8/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.djkim.slap.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookButtonBase;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.viewpagerindicator.CirclePageIndicator;
import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LoginActivity extends FragmentActivity {
    //The number of pictures / pages to show
    private static final int NUM_PAGES = 4;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ParseButton loginButton;
    private CallbackManager callbackManager;
    private Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login_activity);
        currentActivity = this;

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new LoginPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(mPager);

        // setup login button
        loginButton = (ParseButton) findViewById(R.id.login_with_facebook);
        loginButton.setBackgroundColor(getResources().getColor(com.facebook.R.color.com_facebook_blue));
        loginButton.setText("Login with Facebook");
        final List<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        permissions.add("user_friends");
        permissions.add("email");
        loginButton.setText("Login with Facebook");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
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
