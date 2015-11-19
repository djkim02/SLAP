package com.djkim.slap.login;

/**
 * Created by dongjoonkim on 10/8/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.djkim.slap.R;
import com.djkim.slap.menubar.MainActivity;
import com.djkim.slap.messenger.MessageService;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.ParseButton;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;
import com.djkim.slap.models.ZoomOutPageTransformer;
import com.djkim.slap.profile.CreateProfileActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.viewpagerindicator.CirclePageIndicator;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//TODO: There should be another activity that checks the cached login data so that we can skip this activity
public class LoginActivity extends FragmentActivity {
    //The number of pictures / pages to show
    private static final int NUM_PAGES = 4;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ParseButton loginButton;
    private ParseUser parseUser;
    private String name = null;
    private Long facebookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_activity);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new LoginPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        // Instantiate a page indicator
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(mPager);

        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                this.stopTracking();
                Profile.setCurrentProfile(currentProfile);

            }
        };
        profileTracker.startTracking();

        // setup login button
        loginButton = (ParseButton) findViewById(R.id.login_with_facebook);
        loginButton.setBackgroundColor(getResources().getColor(com.facebook.R.color.com_facebook_blue));
        final List<String> permissions = new ArrayList<>();
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
                            getUserDetailsFromFacebook(user);
                        } else {
                            //TODO: Implement this section for returning users
                            Log.d("MyApp", "User logged in through Facebook!");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, MessageService.class));
        super.onDestroy();
    }

    // This method fetches user details (name, profile picture) from Facebook
    private void getUserDetailsFromFacebook(final ParseUser parseUser) {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        if (jsonObject != null) {
                            try {
                                facebookId = new Long(jsonObject.getLong("id"));
                                name = jsonObject.getString("name");
                                Profile profile = Profile.getCurrentProfile();
                                //final JSONObject mPicture = jsonObject.getJSONObject("picture");
                                //final JSONObject mPictureData = jsonObject.getJSONObject("data");
                                //final String mImageUrl = mPictureData.getString("url");

                                User user = new User(parseUser.getObjectId(), name, facebookId, profile.getId());
                                Intent intent = new Intent(LoginActivity.this, CreateProfileActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                            catch (JSONException e) {
                                Log.d("", "Error parsing returned user data. " + e);
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private class LoginPagerAdapter extends FragmentStatePagerAdapter {
        public LoginPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        SlapLoginFragment slapLoginFragment = new SlapLoginFragment();
        GroupsFragment groupsFragment = new GroupsFragment();
        HackerFragment hackerFragment = new HackerFragment();
        AthleteFragment athleteFragment = new AthleteFragment();

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return slapLoginFragment;
                case 1:
                    return groupsFragment;
                case 2:
                    return hackerFragment;
                case 3:
                    return athleteFragment;
                default:
                    return slapLoginFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
