package com.djkim.slap.models;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;


/**
 * Created by joannachen on 10/26/15.
 */
public class Utils {
    public Utils() {
    }

    public User get_user_by_facebook_id(long facebook_id) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("facebookId", facebook_id);
        ParseUser parseUser = query.getFirst();
        User u = new User();
        u.set_user(parseUser.getObjectId(), parseUser.getUsername(), parseUser.getLong("facebookId"));
        return u;
    }

    public User get_current_user() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        User u = new User();
        u.set_user(parseUser.getObjectId(), parseUser.getUsername(), parseUser.getLong("facebookId"));
        return u;
    }


    public Group get_group_by_name(String name)
    {
        return new Group();
    }

}
