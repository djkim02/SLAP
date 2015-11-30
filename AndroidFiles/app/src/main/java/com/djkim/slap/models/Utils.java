package com.djkim.slap.models;

import com.parse.FunctionCallback;
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
    private static User current_user = null;

    public static User get_current_user() {
        if (current_user == null) {
            ParseUser parseUser = ParseUser.getCurrentUser();
            current_user = new User(parseUser);
        }
        // TODO: TEST if sync is really necessary
//        user.sync();
        return current_user;
    }

    public static void getGroupsFromCloudInBackground(String type, String tags, final GroupsCallback callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        map.put("tags", tags);
        ParseCloud.callFunctionInBackground(
                "match", map, new FunctionCallback<List<ParseObject> >() {
                    @Override
                    public void done(List<ParseObject> parseGroups, ParseException e) {
                        List<Group> groups = new ArrayList<Group>();
                        for (ParseObject parseGroup : parseGroups) {
                            Group group = new Group(parseGroup);
                            groups.add(group);
                        }
                        callback.done(groups);
                    }
                });
    }

    public static List<Group> getGroupsFromCloud(String type, String tags) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", type);
            map.put("tags", tags);
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

    public static void getStringSearchGroupsFromCloudInBackground(String queryName, final GroupsCallback callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", queryName);
        ParseCloud.callFunctionInBackground(
                "partialStringSearch", map, new FunctionCallback<List<ParseObject> >() {
                    @Override
                    public void done(List<ParseObject> parseGroups, ParseException e) {
                        List<Group> groups = new ArrayList<Group>();
                        for (ParseObject parseGroup : parseGroups) {
                            Group group = new Group(parseGroup);
                            groups.add(group);
                        }
                        callback.done(groups);
                    }
                });
    }

}
