package com.djkim.slap.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import bolts.Task;

/**
 * Created by joannachen on 10/26/15.
 */

/** was considering using the builder method for this... but got kind of weird with the ArrayList and all
 * maybe try this out later??
 */

public class Group implements Serializable {
    private final String HACKER_GROUP = "Hacker";
    private final String ATHLETE_GROUP = "Athlete";
    private final String GENERAL_GROUP = "General";
    private String m_objectId;
    private String m_name;
    private String m_description;
    private String m_facebookGroupId;
    private String m_type;
//    private User m_owner;
    private int m_capacity;
//    private Hashtable<Long, User> m_membership = new Hashtable<>();
    private String m_skills;    // comma-separated string of skills

    public Group(String name, User owner, int capacity, String type) {
        m_name = name;
//        m_owner = owner;
        m_capacity = capacity;
        m_description = "";

        m_type = type.equals(HACKER_GROUP) || type.equals(ATHLETE_GROUP) ? type : GENERAL_GROUP;
//        m_membership.put(owner.get_facebook_id(), owner);
    }

    public Group(ParseObject parseGroup) {
        updateAllFields(parseGroup);
    }

    private void updateAllFields(ParseObject parseGroup) {
        m_objectId = parseGroup.getObjectId();
        m_name = parseGroup.getString("name");
        m_description = parseGroup.getString("description");
//        try {
//            m_owner = new User(parseGroup.getParseUser("owner").fetchIfNeeded());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        m_facebookGroupId = parseGroup.getString("facebookGroupId");

        m_type = parseGroup.getString("type");
        m_skills = parseGroup.getString("skills");

//        ParseRelation<ParseUser> membersRelation = parseGroup.getRelation("members");
//        try {
//            List<ParseUser> parseUsers = membersRelation.getQuery().find();
//            m_membership = new Hashtable<>();
//            for (ParseUser parseUser : parseUsers) {
//                User user = new User(parseUser);
//                m_membership.put(user.get_facebook_id(), user);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        m_capacity = parseGroup.getInt("capacity");
    }

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
        ParseQuery query = ParseQuery.getQuery("Group");
        try {
            ParseObject parseGroup = query.get(m_objectId);
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.whereEqualTo("memberOf", parseGroup);
//            userQuery.whereEqualTo("memberOf", ParseObject.createWithoutData("Group", m_objectId))
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
        ParseQuery.getQuery("Group").getInBackground(m_objectId, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            ParseQuery<ParseUser> query = ParseUser.getQuery();
                            query.whereEqualTo("memberOf", object);
                            query.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> objects, ParseException e) {
                                    if (e == null) {
                                        List<User> users = new ArrayList<User>();
                                        for (ParseUser parseUser : objects) {
                                            User user = new User(parseUser);
                                            users.add(user);
                                        }
                                        callback.done(users);
                                    }
                                }
                            });
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
            ParseObject parseGroup = query.get(m_objectId);
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.whereEqualTo("ownerOf", parseGroup);
//            userQuery.whereEqualTo("memberOf", ParseObject.createWithoutData("Group", m_objectId))
            ParseUser parseUser = userQuery.getFirst();
            return new User(parseUser);
        } catch (ParseException e) {
            return null;
        }
    }

    public int get_size() {
        ParseQuery query = ParseQuery.getQuery("Group");
        try {
            ParseObject parseGroup = query.get(m_objectId);
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.whereEqualTo("memberOf", parseGroup);
//            userQuery.whereEqualTo("memberOf", ParseObject.createWithoutData("Group", m_objectId))
            // TODO(yjchoi): use query.count()
            List<ParseUser> parseUsers = userQuery.find();
            return parseUsers.size();
        } catch (ParseException e) {
            return 0;
        }
    }

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

//    public boolean isOwner(User user){
//        return m_owner.equals(user);
//    }

//    public boolean isMember(User user){
//        return m_membership.contains(user.get_id());
//    }

//    public void addMember(User member){
//        m_membership.put(member.get_facebook_id(), member);
//    }

//    public void addMembersToParseGroup(ParseObject parseGroup) {
//        ParseRelation<ParseUser> relation = parseGroup.getRelation("members");
//        for (User member : m_membership.values()) {
//            ParseObject parseUser = ParseObject.createWithoutData("_User", member.get_id());
//            relation.add((ParseUser) parseUser);
//        }
//    }

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
//        if (m_owner.get_id().equals(Utils.get_current_user().get_id())) {
//            ParseObject parseOwner = ParseObject.createWithoutData("_User", m_owner.get_id());
//            parseGroup.put("owner", parseOwner);
//        }

        parseGroup.put("name", m_name);
        parseGroup.put("description", m_description);
        parseGroup.put("type", m_type);
        parseGroup.put("capacity", m_capacity);
        parseGroup.put("skills", m_skills);
        if (m_facebookGroupId != null) {
            parseGroup.put("facebookGroupId", m_facebookGroupId);
        }

        // Iterates through and adds all members that are not already in the array.
//        addMembersToParseGroup(parseGroup);
//        if (m_membership.contains(Utils.get_current_user().get_id())) {
//            parseGroup.getRelation("members").add(Utils.get_current_user().toParseUser());
//        }
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
                    } else {
                        e.printStackTrace();
                        Group.this.sync();
                        if (callback != null) {
                            callback.done();
                        }
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
                        Group.this.m_objectId = parseGroup.getObjectId();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
