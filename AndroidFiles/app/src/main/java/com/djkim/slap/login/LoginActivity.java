package com.djkim.slap.login;

/**
 * Created by dongjoonkim on 10/8/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.djkim.slap.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookButtonBase;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.viewpagerindicator.CirclePageIndicator;
import com.facebook.FacebookSdk;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
    private ParseUser parseUser;
    private String name = null;
    private String email = null;
    private ImageView profilePicture;
    private Profile mFbProfile;

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
        mFbProfile = Profile.getCurrentProfile();
        profilePicture = new ImageView(this);

        // setup login button
        loginButton = (ParseButton) findViewById(R.id.login_with_facebook);
        loginButton.setBackgroundColor(getResources().getColor(com.facebook.R.color.com_facebook_blue));
        loginButton.setText("Login with Facebook");
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
                            getUserDetailsFromFB();
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            getUserDetailsFromFB();
                        }
                    }
                });
            }
        });

    }

    private void getUserDetailsFromFB() {
        ProfilePhotoAsync profilePhotoAsync = new ProfilePhotoAsync(mFbProfile);
        profilePhotoAsync.execute();
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
           /* handle the result */
                        try {
                            //email = response.getJSONObject().getString("email");
                            name = response.getJSONObject().getString("name");
                            saveNewUser();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void saveNewUser() {
        parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(name);

        // Saving profile photo as a ParseFile
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] data = stream.toByteArray();
        String thumbName = parseUser.getUsername().replaceAll("\\s+", "");
        final ParseFile parseFile = new ParseFile(thumbName + "_thumb.jpg", data);

        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                parseUser.put("profileThumb", parseFile);
                //Finally save all the user details
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(LoginActivity.this, "New user:" + name + " Signed up", Toast.LENGTH_SHORT).show();
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
                    // return new HackerFragment();
                    return new HackerSkillFragment();
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

    private class ProfilePhotoAsync extends AsyncTask<String, String, String> {
        Profile profile;
        public Bitmap bitmap;
        public ProfilePhotoAsync(Profile profile) {
            this.profile = profile;
        }
        @Override
        protected String doInBackground(String... params) {
            // Fetching data from URI and storing in bitmap
            bitmap = DownloadImageBitmap(profile.getProfilePictureUri(200, 200).toString());
            profilePicture.setImageBitmap(bitmap);
            return null;
        }
    }

    public static Bitmap DownloadImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("IMAGE", "Error getting bitmap", e);
        }
        return bm;
    }

    public void onCheckboxClicked(View view) {
        Toast.makeText(this.getApplicationContext(), "onCheckboxClicked", Toast.LENGTH_SHORT).show();
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
//        switch(view.getId()) {
//            case R.id.checkbox_cpp:
//                if (checked){
//                    m_hacker_skills.put("cpp", True);
//                }
//                else{
//                    m_hacker_skills.put("cpp", False);
//                }
//                break;
//            case R.id.checkbox_java:
//                if (checked){
//                    m_hacker_skills.put("java", True);
//                }
//                else{
//                    m_hacker_skills.put("java", False);
//                }
//                break;
//        }
    }

    public void onSubmitButtonClicked(View view) {
        Toast.makeText(this.getApplicationContext(), "onSubmitButton", Toast.LENGTH_SHORT).show();
//        ParseObject hacker_skills = new ParseObject("hacker_profile");
//        for (String key : m_hacker_skills.keySet()) {
//            hacker_skills.put(key, m_hacker_skills.get(key));
//        }
    }
}
