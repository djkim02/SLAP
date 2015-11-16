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
import java.util.Hashtable;
import java.util.List;

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
    private ArrayList<User> m_membersToAdd = new ArrayList<User>();
    private Hashtable<Long, Integer> m_membership = new Hashtable<Long, Integer>();
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
        m_owner = new User();
        m_owner.setFieldsWithParseUser(parseGroup.getParseUser("owner"));
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
                m_members.add(user);
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
        return m_membership.containsKey(user.get_facebook_id());
    }

    public void addMember(User member){
        m_members.add(member);
//         m_membership.put(member.get_facebook_id(), True);
    }

    public void addMembers(ArrayList<User> users){
        for (User user:users) {
            m_membersToAdd.add(user);
//            check for duplicates?
//            if (!isMember(user)) {
//                m_members.add(user);
//                m_membership.put(user.get_facebook_id(), True);

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

// OUT WITH THE OLD
//    public void addParseMember(ParseUser parseMember){
//        ParseObject parseGroup = null;
//        parseGroup = getParseGroup();
//        ParseRelation<ParseUser> relation = parseGroup.getRelation("members");
//        relation.add(parseMember);
//    }
//
//    public void addParseMembers(ArrayList<User> members){
//        ParseObject parseGroup = getParseGroup();
//        ParseRelation<ParseUser> relation = parseGroup.getRelation("members");
//        for (User member: members) {
//            relation.add(member.getParseUser());
//        }
//    }


//    public Group(ParseObject parseGroup) throws ParseException {
//        m_name = (String) parseGroup.get("name");
//        m_description = (String) parseGroup.get("description");
//        m_owner = (User) parseGroup.get("owner");
//        m_capacity = (int) parseGroup.get("capacity");
//        ArrayList<ParseUser> members = (ArrayList<ParseUser>) parseGroup.get("members");
//        for (ParseUser member: members) {
//            try {
//                m_members.add(new User((Long) member.get("facebookId")));
//            } catch (ParseException e) {
//                throw new ParseException(e);
//            }
//        }
//
//    }

//    public Group(String name, User owner, int capacity) throws ParseException {
//        m_parseGroup = new ParseObject("Group");
//
//        try {
//            createParseGroup(m_parseGroup, name, owner, capacity);
//        } catch (ParseException e) {
//            throw new ParseException(e);
//        }
//        m_description = "";
//        m_parseGroup.put("description", m_description);
//
//        m_parseGroup.saveInBackground();
//    }
//
//    public Group(String name, User owner, int capacity, String description) throws ParseException {
//        m_parseGroup = new ParseObject("Group");
//
//        try {
//            createParseGroup(m_parseGroup, name, owner, capacity);
//        } catch (ParseException e) {
//            throw new ParseException(e);
//        }
//        m_description = description;
//        m_parseGroup.put("description", m_description);
//
//        m_parseGroup.saveInBackground();
//    }
//
//    public void createParseGroup(ParseObject parseGroup, String name, User owner, int capacity) throws ParseException {
//        m_name = name;
//        m_owner = owner;
//        m_capacity = capacity;
//        addMember(owner);
//
//        parseGroup.put("name", m_name);
//        ParseUser parseOwner = owner.getParseUser();
//        parseGroup.put("owner", parseOwner);
//        parseGroup.put("capacity", m_capacity);
//        m_parseGroup = parseGroup;
//
//        Log.e("MyApp", "New Group Saved!");
//
//    }
//
//    // Getter for the corresponding ParseGroup object
//    public ParseObject getParseGroup(){
////        try {
////            ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
////            return query.get(m_objectId);
////        }catch(ParseException e){
////            throw new ParseException(e);
////        }
//        return m_parseGroup;
//    }
//