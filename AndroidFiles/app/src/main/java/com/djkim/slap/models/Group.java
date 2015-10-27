package com.djkim.slap.models;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Hashtable;

import bolts.Task;

/**
 * Created by joannachen on 10/26/15.
 */

/** was considering using the builder method for this... but got kind of weird with the ArrayList and all
 * maybe try this out later??
 */
public class Group implements GroupInterface{
    private String m_name;
    private String m_description;
    private String m_objectId;
    private User m_owner;
    private int m_capacity;
    private ArrayList<User> m_members = new ArrayList<User>();
    private Hashtable<Long, Integer> m_membership = new Hashtable<Long, Integer>();

    private Integer True = new Integer(1);
    private Integer False = new Integer(0);

    public Group(ParseObject parseGroup) throws ParseException {
        m_name = (String) parseGroup.get("name");
        m_description = (String) parseGroup.get("description");
        m_objectId = parseGroup.getObjectId();
        m_owner = (User) parseGroup.get("owner");
        m_capacity = (int) parseGroup.get("capacity");
        ArrayList<ParseUser> members = (ArrayList<ParseUser>) parseGroup.get("members");
        for (ParseUser member: members) {
            try {
                m_members.add(new User((Long) member.get("facebookId")));
            } catch (ParseException e) {
                throw new ParseException(e);
            }
        }

    }

    public Group(String name, User owner, int capacity) throws ParseException {
        ParseObject parseGroup = new ParseObject("Group");
        try {
            createParseGroup(parseGroup, name, owner, capacity);
        } catch (ParseException e) {
            throw new ParseException(e);
        }
        m_description = "";
        parseGroup.put("description", m_description);
    }
    public Group(String name, User owner, int capacity, String description) throws ParseException {
        ParseObject parseGroup = new ParseObject("Group");
        try {
            createParseGroup(parseGroup, name, owner, capacity);
        } catch (ParseException e) {
            throw new ParseException(e);
        }
        m_description = description;
        parseGroup.put("description", m_description);
    }

    public void createParseGroup(ParseObject parseGroup, String name, User owner, int capacity) throws ParseException {
        m_name = name;
        m_objectId = parseGroup.getObjectId();
        m_owner = owner;
        m_capacity = capacity;
        addMember(owner);

        parseGroup.put("name", m_name);
        ParseUser parseOwner = owner.getParseUser();
        parseGroup.put("owner", parseOwner);
        parseGroup.put("capacity", m_capacity);
        m_objectId = parseGroup.getObjectId();
        Log.e("MyApp", "New Group Saved!");

    }

    // Getter for the corresponding ParseGroup object
    public ParseObject getParseGroup() throws ParseException {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
            return query.get(m_objectId);
        }catch(ParseException e){
            throw new ParseException(e);
        }
    }

    public String get_name() {
        return m_name;
    }
    public void set_name(String name) throws ParseException {
        m_name = name;
        try {
            getParseGroup().put("name", name);
        } catch (ParseException e) {
            throw new ParseException(e);
        }
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
    public void set_capacity(int capacity) throws ParseException {
        m_capacity = capacity;
        try {
            getParseGroup().put("capacity", capacity);
        } catch (ParseException e) {
            throw new ParseException(e);
        }
    }

    public boolean isOwner(User user){
        return m_owner.equals(user);
    }

    public boolean isMember(User user){
        return m_membership.containsKey(user.get_facebook_id());
    }

    public void addMember(User member) throws ParseException {
        m_members.add(member);
        m_membership.put(member.get_facebook_id(), True);
        try {
            addParseMember(member.getParseUser());
        } catch (ParseException e) {
            throw new ParseException(e);
        }
    }

    public void addMembers(ArrayList<User> users) throws ParseException {
        for (User user:users) {
            if (!isMember(user)) {
                m_members.add(user);
                m_membership.put(user.get_facebook_id(), True);
            }
        }
        try {
            addParseMembers(users);
        } catch (ParseException e) {
            throw new ParseException(e);
        }
    }

    public void addParseMember(ParseUser parseMember) throws ParseException {
        ParseObject parseGroup = null;
        try {
            parseGroup = getParseGroup();
            ParseRelation<ParseUser> relation = parseGroup.getRelation("members");
            relation.add(parseMember);
            parseGroup.save();
        } catch (ParseException e) {
            throw new ParseException(e);
        }
    }

    public void addParseMembers(ArrayList<User> members) throws ParseException {
        ParseObject parseGroup = null;
        try {
            parseGroup = getParseGroup();
            ParseRelation<ParseUser> relation = parseGroup.getRelation("members");
            for (User member: members) {
                relation.add(member.getParseUser());
            }
            parseGroup.save();
        } catch (ParseException e) {
            throw new ParseException(e);
        }
    }

}
