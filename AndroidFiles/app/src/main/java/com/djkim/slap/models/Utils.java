package com.djkim.slap.models;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by joannachen on 10/26/15.
 */
public class Utils implements UtilsInterface{
    public Utils() {
    }

    public User get_current_user() throws ParseException{
        ParseUser parseUser = ParseUser.getCurrentUser();
        // TO CHANGE
        User user = null;
        try {
            user = new User(parseUser.getLong("facebookId"));
        } catch (ParseException e) {
            throw new ParseException(e);
        }

        return user;
    }

}
