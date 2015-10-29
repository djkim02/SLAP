package com.djkim.slap.dispatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.djkim.slap.login.LoginActivity;
import com.djkim.slap.menubar.MainActivity;
import com.djkim.slap.profile.CreateProfileActivity;
import com.parse.ParseUser;

/**
 * Created by YooJung on 10/28/2015.
 */
public class DispatchActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: use Utils static method instead
        if (ParseUser.getCurrentUser() != null) {
            // User is logged in.
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // User has not logged in to our app.
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
