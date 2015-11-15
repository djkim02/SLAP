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
public class Utils{

    public static User get_current_user() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        User user = new User(
                parseUser.getObjectId(),
                parseUser.getUsername(),
                parseUser.getLong("facebookId"));
                //parseUser.getString("imageUrl"));
        return user;
    }

    public static User get_user_by_facebook_id(Long facebook_id) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("facebookId", facebook_id);
        ParseUser parseUser = null;
        try {
            parseUser = query.getFirst();
            User user = new User(parseUser.getObjectId(),
                    parseUser.getUsername(),
                    parseUser.getLong("facebookId"));
                    //parseUser.getString("imageUrl"));
            return user;
        } catch (ParseException e) {
            return null; // couldn't find a user?? is this okay?
        }
    }

}
