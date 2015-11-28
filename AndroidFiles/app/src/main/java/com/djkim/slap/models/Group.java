package com.djkim.slap.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joannachen on 10/26/15.
 */

public class Group implements Serializable {
    public static final String HACKER_GROUP = "Hacker";
    public static final String ATHLETE_GROUP = "Athlete";
    public static final String GENERAL_GROUP = "General";

    private String m_objectId;
    private String m_name;
    private String m_description;
    private String m_facebookGroupId;
    private String m_type;
    private int m_capacity;
    private String m_skills;    // comma-separated string of skills
    private String m_tags;      // comma-separated string of custom tags

    private String m_ownerName;
    private String m_ownerFacebookProfileId;
    private int m_size;

    public Group(String name, User owner, int capacity, String type) {
        m_name = name;
        m_capacity = capacity;
        m_description = "";
        m_type = type.equals(HACKER_GROUP) || type.equals(ATHLETE_GROUP) ? type : GENERAL_GROUP;
        m_ownerName = owner.get_name();
        m_ownerFacebookProfileId = owner.get_facebook_profile_id();
        m_size = 1;
    }

    public Group(ParseObject parseGroup) {
        updateAllFields(parseGroup);
    }

    private void updateAllFields(ParseObject parseGroup) {
        m_objectId = parseGroup.getObjectId();
        m_name = parseGroup.getString("name");
        m_description = parseGroup.getString("description");
        m_facebookGroupId = parseGroup.getString("facebookGroupId");
        m_type = parseGroup.getString("type");
        m_skills = parseGroup.getString("skills");
        m_tags = parseGroup.getString("tags");
        m_capacity = parseGroup.getInt("capacity");
        m_ownerName = parseGroup.getString("ownerName");
        m_ownerFacebookProfileId = parseGroup.getString("ownerFacebookProfileId");
        m_size = parseGroup.getInt("size");
    }

    public void set_tags(String tags) { m_tags = tags; }

    public void set_skills(String skills) {
        m_skills = skills;
    }

    // this will fetch the Group object from Parse
    // and update all fields in the Java Group object
    // so when you call a getter, it returns the most
    // updated values.
    // TODO: check timestamp to figure out if we need to sync or not
    public void sync(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
        try {
            ParseObject parseGroup = query.get(m_objectId);
            updateAllFields(parseGroup);
        } catch (ParseException e) {
            return; // nothing to update
        }
    }

    public List<User> getMembers() {
        try {
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.whereEqualTo("memberOf", ParseObject.createWithoutData("Group", m_objectId));
            List<ParseUser> parseUsers = userQuery.find();
            List<User> users = new ArrayList<User>();
            for (ParseUser parseUser : parseUsers) {
                User user = new User(parseUser);
                users.add(user);
            }
            return users;
        } catch (ParseException e) {
            return new ArrayList<User>();
        }
    }

    public void getMembersInBackground(final UsersCallback callback) {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("memberOf", ParseObject.createWithoutData("Group", m_objectId));
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    List<User> users = new ArrayList<User>();
                    for (ParseUser parseUser : objects) {
                        User user = new User(parseUser);
                        users.add(user);
                    }
                    callback.done(users);
                } else {
                    callback.done(new ArrayList<User>());
                }
            }
        });
    }

    public String get_name() {
        return m_name;
    }

    public void set_name(String name){
        m_name = name;
    }

    public String get_description() {
        return m_description;
    }
    public void set_description(String description){
        m_description = description;
    }

    public String get_id() {
        return m_objectId;
    }

    public User get_owner() {
        ParseQuery query = ParseQuery.getQuery("Group");
        try {
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.whereEqualTo("ownerOf", ParseObject.createWithoutData("Group", m_objectId));
            ParseUser parseUser = userQuery.getFirst();
            return new User(parseUser);
        } catch (ParseException e) {
            return null;
        }
    }

    public Boolean isOwner(User user) {
        return user.get_facebook_profile_id().equals(m_ownerFacebookProfileId);
    }

    public String get_owner_name() {
        return m_ownerName;
    }

    public String get_owner_facebook_profile_id() {
        return m_ownerFacebookProfileId;
    }

    public int get_size() {
        return m_size;
//        ParseQuery query = ParseQuery.getQuery("Group");
//        try {
//            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
//            userQuery.whereEqualTo("memberOf", ParseObject.createWithoutData("Group", m_objectId));
//            return userQuery.count();
//        } catch (ParseException e) {
//            return 0;
//        }
    }

    public void increment_size() {
        m_size++;
    }

    public String get_skills() {
        return m_skills;
    }

    public String get_tags() { return m_tags; }
    public int get_capacity() {
        return m_capacity;
    }
    public void set_capacity(int capacity){
        m_capacity = capacity;
    }

    public String get_type() { return m_type; }

    public String get_facebookGroupId() {
        return m_facebookGroupId;
    }

    public void set_facebookGroupId(String facebookGroupId) {
        m_facebookGroupId = facebookGroupId;
    }

    public ParseObject toParseObject()
    {
        ParseObject parseGroup = new ParseObject("Group");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
        try {
            parseGroup = query.get(m_objectId);
            return parseGroup;
        } catch (ParseException e) {
            return parseGroup;
        }
    }

    private void saveAllFieldsToParse(ParseObject parseGroup) {
        parseGroup.put("name", m_name);
        parseGroup.put("description", m_description);
        parseGroup.put("type", m_type);
        parseGroup.put("capacity", m_capacity);
        parseGroup.put("skills", m_skills);
        parseGroup.put("tags", m_tags == null ? "" : m_tags);
        if (m_facebookGroupId != null) {
            parseGroup.put("facebookGroupId", m_facebookGroupId);
        }
        parseGroup.put("ownerName", m_ownerName);
        parseGroup.put("ownerFacebookProfileId", m_ownerFacebookProfileId);
        parseGroup.put("size", m_size);
    }

    public void saveInBackground(final GroupCallback callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
        try {
            ParseObject parseGroup = query.get(m_objectId);
            saveAllFieldsToParse(parseGroup);
            parseGroup.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d("DEBUG", "Save completed successfully.");
                    }
                    if (callback != null) {
                        callback.done();
                    }
                }
            });
        } catch (ParseException e) {
            // m_objectId is null. Saving a new group
            final ParseObject parseGroup = new ParseObject("Group");
            saveAllFieldsToParse(parseGroup);
            parseGroup.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        ParseUser curUser = ParseUser.getCurrentUser();
                        String objectId = parseGroup.getObjectId();
                        curUser.getRelation("ownerOf").add(ParseObject.createWithoutData("Group", objectId));
                        curUser.getRelation("memberOf").add(ParseObject.createWithoutData("Group", objectId));
                        curUser.saveInBackground();
                        Utils.get_current_user().joinAsOwner(new Group(parseGroup));
                        Group.this.m_objectId = parseGroup.getObjectId();
//                        Group.this.sync();
                        if (callback != null) {
                            callback.done();
                        }
                    }
                }
            });
        }
    }

    public void save() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
        try {
            ParseObject parseGroup = query.get(m_objectId);
            saveAllFieldsToParse(parseGroup);
            parseGroup.save();
        } catch (ParseException e) {
            // object is null. Saving a new group
            final ParseObject parseGroup = new ParseObject("Group");
            saveAllFieldsToParse(parseGroup);
            parseGroup.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        ParseUser curUser = ParseUser.getCurrentUser();
                        String objectId = parseGroup.getObjectId();
                        curUser.getRelation("ownerOf").add(ParseObject.createWithoutData("Group", objectId));
                        curUser.getRelation("memberOf").add(ParseObject.createWithoutData("Group", objectId));
                        curUser.saveInBackground();
                        Utils.get_current_user().joinAsOwner(new Group(parseGroup));
                        Group.this.m_objectId = parseGroup.getObjectId();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
