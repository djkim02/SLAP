package com.djkim.slap.models;

import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjoonkim on 10/25/15.
 * This class is mainly a data structure to hold all the necessary data
 * for the users. It is used to communicate with Parse and to create a
 * profile for a user
 */
public class User implements Serializable {
    private String user_id;
    private String user_name;
    private long user_facebook_id;

    public User() {
    }

    public void set_user (String id, String name, long facebook_id)
    {
        user_id = id;
        user_name = name;
        user_facebook_id = facebook_id;
    }

    public String get_id() {
        return user_id;
    }

    public void set_id(String id) {
        user_id = id;
        // I'm not sure if I can safely assume getCurrentUser is what we want to update
        ParseUser user = ParseUser.getCurrentUser();
        user.put("objectId", id);
        user.saveInBackground();
    }

    public String get_name() {
        return user_name;
    }

    public void set_name(String name) {
        user_name = name;
        // I'm not sure if I can safely assume getCurrentUser is what we want to update
        ParseUser user = ParseUser.getCurrentUser();
        user.put("username", name);
        user.saveInBackground();
    }

    public long get_user_facebook_id() {
        return user_facebook_id;
    }

    public void set_user_facebook_id(long facebook_id) {
        user_facebook_id = facebook_id;
        // I'm not sure if I can safely assume getCurrentUser is what we want to update
        ParseUser user = ParseUser.getCurrentUser();
        user.put("facebookId", facebook_id);
        user.saveInBackground();
    }

}

