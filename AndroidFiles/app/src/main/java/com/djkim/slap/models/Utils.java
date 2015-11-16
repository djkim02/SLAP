package com.djkim.slap.models;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        user.sync();
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

    public static List<Group> getGroupsFromCloud(String type) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", type);
            List<ParseObject> parseGroups = ParseCloud.callFunction("match", map);
            List<Group> groups = new ArrayList<Group>();
            for (ParseObject parseGroup : parseGroups) {
                Group group = new Group(parseGroup);
                groups.add(group);
            }
            return groups;
        } catch (ParseException e) {
            // No group is found. Return an empty list.
            return new ArrayList<Group>();
        }
    }

}
