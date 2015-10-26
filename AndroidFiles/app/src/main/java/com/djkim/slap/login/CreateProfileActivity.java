package com.djkim.slap.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

        user = new User();
        Intent intent = getIntent();
        user.set_name(intent.getStringExtra("name"));
        user.set_user_profile_pic_uri(intent.getStringExtra("profilePictureUri"));
        mPager = (ViewPager) findViewById(R.id.profile_pager);
        fragmentManager = getSupportFragmentManager();

        TextView nameView = (TextView) findViewById(R.id.nametext);
        nameView.setText(user.get_name());
        TextView uriView = (TextView) findViewById(R.id.profileUriText);
        uriView.setText(user.get_user_profile_pic_uri());

    }
}
