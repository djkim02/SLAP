package com.djkim.slap.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

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
    private User m_owner;
    private int m_capacity;
    private ArrayList<User> m_members = new ArrayList<User>();
    private Set<String> m_membership = new HashSet<>();
    private String m_skills;    // comma-separated string of skills

    public Group() {}

    public Group(String name, User owner, int capacity, String type) {
        m_name = name;
        m_owner = owner;
        m_capacity = capacity;
        m_description = "";
        m_type = type.equals(HACKER_GROUP) || type.equals(ATHLETE_GROUP) ? type : GENERAL_GROUP;
        m_members.add(owner);
    }

    public Group(ParseObject parseGroup) {
        m_objectId = parseGroup.getObjectId();
        m_name = parseGroup.getString("name");
        m_description = parseGroup.getString("description");

        try {
            m_owner = new User();
            m_owner.setFieldsWithParseUser(parseGroup.getParseUser("owner").fetchIfNeeded());
        } catch (ParseException e) {
            Log.d("Debug", "Fetch failed!");
        }

        m_facebookGroupId = parseGroup.getString("facebookGroupId");
        m_type = parseGroup.getString("type");
        m_skills = parseGroup.getString("skills");

        ParseRelation<ParseUser> membersRelation = parseGroup.getRelation("members");
        try {
            List<ParseUser> parseUsers = membersRelation.getQuery().find();
            m_members = new ArrayList<>();
            for (ParseUser parseUser : parseUsers) {
                User user = new User();
                user.setFieldsWithParseUser(parseUser);
                m_members.add(user);
                m_membership.add(user.get_id());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        m_capacity = parseGroup.getInt("capacity");
    }

    private void updateAllFields(ParseObject parseGroup) {
        m_name = parseGroup.getString("name");
        m_description = parseGroup.getString("description");
        ParseUser parseOwner = parseGroup.getParseUser("owner");
        m_owner.setFieldsWithParseUser(parseOwner);
        m_capacity = parseGroup.getInt("capacity");
        m_facebookGroupId = parseGroup.getString("facebookGroupId");

        ParseRelation<ParseUser> membersRelation = parseGroup.getRelation("members");
        try {
            List<ParseUser> parseUsers = membersRelation.getQuery().find();
            m_members = new ArrayList<>();
            for (ParseUser parseUser : parseUsers) {
                User user = new User();
                user.setFieldsWithParseUser(parseUser);

                if (!m_membership.contains(user.get_id())) {
                    m_members.add(user);
                    m_membership.add(user.get_id());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public ArrayList<User> get_members() {
        return m_members;
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
        return m_owner;
    }

    public int get_size() {
        return m_members.size();
    }

    public int get_capacity() {
        return m_capacity;
    }
    public void set_capacity(int capacity){
        m_capacity = capacity;
    }

    public String get_facebookGroupId() {
        return m_facebookGroupId;
    }

    public void set_facebookGroupId(String facebookGroupId) {
        m_facebookGroupId = facebookGroupId;
    }

    public boolean isOwner(User user){
        return m_owner.equals(user);
    }

    public boolean isMember(User user){
        return m_membership.contains(user.get_id());
    }

    public void addMember(User member) {
        if (!m_membership.contains(member.get_id())) {
            m_members.add(member);
            m_membership.add(member.get_id());
        }
    }

    public ArrayList<User> getMembers(){
        return m_members;
    }

    public void addMembersToParseGroup(ParseObject parseGroup) {
        ParseRelation<ParseUser> relation = parseGroup.getRelation("members");
        for (User member : m_members) {
             relation.add(member.toParseUser());
//            ParseObject parseMember = ParseObject.createWithoutData("_User", member.get_id());
//            parseGroup.put("members", parseMember);
        }
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

        ParseObject parseOwner = ParseObject.createWithoutData("_User", m_owner.get_id());
        parseGroup.put("owner", parseOwner);

        parseGroup.put("type", m_type);

//        ParseRelation<ParseObject> relation = parseOwner.getRelation("groups");
//        relation.add(parseGroup);

        parseGroup.put("capacity", m_capacity);
        parseGroup.put("skills", m_skills);
        parseGroup.put("facebookGroupId", m_facebookGroupId);

        // iterate through and add all members that are not already in the array
        addMembersToParseGroup(parseGroup);
        // parseOwner.saveInBackground();
    }

    public void save(){
        ParseObject parseGroup = new ParseObject("Group");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
        try {
            parseGroup = query.get(m_objectId);
            saveAllFieldsToParse(parseGroup);
            parseGroup.saveInBackground();
        } catch (ParseException e) {
            saveAllFieldsToParse(parseGroup);
            parseGroup.saveInBackground();
            m_objectId = parseGroup.getObjectId();
        }
    }
}
