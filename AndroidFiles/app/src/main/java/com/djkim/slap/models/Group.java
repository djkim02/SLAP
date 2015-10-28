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
    private User m_owner;
    private int m_capacity;
    private ArrayList<User> m_members = new ArrayList<User>();
    private Hashtable<Long, Integer> m_membership = new Hashtable<Long, Integer>();
    private ParseObject m_parseGroup = new ParseObject("Group");

    private Integer True = new Integer(1);
    private Integer False = new Integer(0);

    public Group(ParseObject parseGroup) throws ParseException {
        m_name = (String) parseGroup.get("name");
        m_description = (String) parseGroup.get("description");
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
        m_parseGroup = new ParseObject("Group");

        try {
            createParseGroup(m_parseGroup, name, owner, capacity);
        } catch (ParseException e) {
            throw new ParseException(e);
        }
        m_description = "";
        m_parseGroup.put("description", m_description);

        m_parseGroup.saveInBackground();
    }

    public Group(String name, User owner, int capacity, String description) throws ParseException {
        m_parseGroup = new ParseObject("Group");

        try {
            createParseGroup(m_parseGroup, name, owner, capacity);
        } catch (ParseException e) {
            throw new ParseException(e);
        }
        m_description = description;
        m_parseGroup.put("description", m_description);

        m_parseGroup.saveInBackground();
    }

    public void createParseGroup(ParseObject parseGroup, String name, User owner, int capacity) throws ParseException {
        m_name = name;
        m_owner = owner;
        m_capacity = capacity;
        addMember(owner);

        parseGroup.put("name", m_name);
        ParseUser parseOwner = owner.getParseUser();
        parseGroup.put("owner", parseOwner);
        parseGroup.put("capacity", m_capacity);
        m_parseGroup = parseGroup;

        Log.e("MyApp", "New Group Saved!");

    }

    // Getter for the corresponding ParseGroup object
    public ParseObject getParseGroup(){
//        try {
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
//            return query.get(m_objectId);
//        }catch(ParseException e){
//            throw new ParseException(e);
//        }
        return m_parseGroup;
    }

    public void sync(){
            m_parseGroup.fetchInBackground();
    }

    public String get_name() {
        return m_name;
    }
    public void set_name(String name){
        m_name = name;
        getParseGroup().put("name", name);
    }

    public String get_description() {
        return m_description;
    }
    public void set_description(String description){
        m_description = description;
    }

    public String get_id() {
        return m_parseGroup.getObjectId();
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
        getParseGroup().put("capacity", capacity);
    }

    public boolean isOwner(User user){
        return m_owner.equals(user);
    }

    public boolean isMember(User user){
        return m_membership.containsKey(user.get_facebook_id());
    }

    public void addMember(User member){
        m_members.add(member);
        m_membership.put(member.get_facebook_id(), True);
        addParseMember(member.getParseUser());
    }

    public void addMembers(ArrayList<User> users){
        for (User user:users) {
            if (!isMember(user)) {
                m_members.add(user);
                m_membership.put(user.get_facebook_id(), True);
            }
        }
            addParseMembers(users);
    }

    public void addParseMember(ParseUser parseMember){
        ParseObject parseGroup = null;
        parseGroup = getParseGroup();
        ParseRelation<ParseUser> relation = parseGroup.getRelation("members");
        relation.add(parseMember);
    }

    public void addParseMembers(ArrayList<User> members){
        ParseObject parseGroup = getParseGroup();
        ParseRelation<ParseUser> relation = parseGroup.getRelation("members");
        for (User member: members) {
            relation.add(member.getParseUser());
        }
    }

    public ArrayList<User> getMembers(){
        return m_members;
    }

    public void save(){
        // TODO: exception in callback?
        m_parseGroup.saveInBackground();
    }

}
