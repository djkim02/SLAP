package com.djkim.slap.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.djkim.slap.models.User;

/**
 * Created by dongjoonkim on 11/17/15.
 */
public class OthersProfileActivity extends Activity {
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
    }
}
