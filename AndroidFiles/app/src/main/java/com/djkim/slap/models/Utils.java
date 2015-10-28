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

    public static User get_current_user() throws ParseException{
        ParseUser parseUser = ParseUser.getCurrentUser();
        // TO CHANGE
        User user = null;
        try {
            return new User(parseUser.getLong("facebookId"));
        } catch (ParseException e) {
            throw new ParseException(e);
        }
    }

    public static User get_user_by_facebook_id(Long facebook_id) throws ParseException{
        try {
            return new User(facebook_id);
        } catch (ParseException e){
            throw new ParseException(e);
        }
    }

}
