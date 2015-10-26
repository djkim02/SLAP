package com.djkim.slap.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import com.djkim.slap.R;

/**
 * Created by dongjoonkim on 10/25/15.
 */

public class CreateProfileActivity extends FragmentActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private FragmentManager fragmentManager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity_layout);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        mPager = (ViewPager) findViewById(R.id.profile_pager);
        fragmentManager = getSupportFragmentManager();

        TextView nameView = (TextView) findViewById(R.id.nametext);
        String name = user.get_name();
        nameView.setText(name);
        TextView idView = (TextView) findViewById(R.id.profileUriText);
        String fbid = String.valueOf(user.get_user_facebook_id());
        idView.setText(fbid);
    }
}
