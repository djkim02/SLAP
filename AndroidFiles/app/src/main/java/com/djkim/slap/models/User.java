package com.djkim.slap.models;

import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.ParseException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjoonkim on 10/25/15.
 * This class is mainly a data structure to hold all the necessary data
 * for the users. It is used to communicate with Parse and to create a
 * profile for a user
 */
public class User implements Serializable {

    private String m_objectId;
    private String m_username;
    private Long m_facebookId;
    private ArrayList<Group> m_groups; // all groups the user is a part of
    private ArrayList<Group> m_groupsWhereUserIsOwner; // all groups the user is an owner of


    // Constructors
    public User(){}
    public User(String objectId, String username, Long facebookId)
    {
        m_objectId = objectId;
        m_username = username;
        m_facebookId = facebookId;
    }

    public void addGroup (Group group) {
        m_groups.add(group);
    }

    public void syncUsername(ParseUser parseUser) {
        m_username = parseUser.getUsername();
    }

    public void syncFacebookId(ParseUser parseUser) {
        m_facebookId = parseUser.getLong("facebookId");
    }

    public void syncGroups(ParseUser parseUser) {
        // get groups from relation
        // clear m_groupsWhereUserIsMember
        // fill m_groupsWhereUserIsMember
        ParseRelation<ParseUser> relation = parseUser.getRelation("groups");
        ParseQuery query = relation.getQuery();
    }

    // based on current m_object ID,
    // update the Java User's data with
    // the ParseUser's data from the database
    public void sync(){
        // query for user based on m_objectId
        // update username and other fields
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        try {
            ParseUser parseUser = query.get(m_objectId);
            syncGroups(parseUser);

        } catch (ParseException e) {
            return; // didn't find anything
        }
    }

    // Getters and Setters
    private String get_id() {
        return m_objectId;
    }

    public void set_id(String id) {
        m_objectId = id;
    }

    public String get_name(){
        return m_username;
    }

    public void set_name(String name){
        m_username = name;
    }

    public Long get_facebook_id() {
        return m_facebookId;
    }

    public void set_facebook_id(Long facebook_id){
        m_facebookId = facebook_id;
    }

    public boolean equals(User anotherUser){
        return (anotherUser.get_facebook_id().equals(this.m_facebookId));
    }

    public void setFieldsWithParseUser(ParseUser parseOwner) {
        m_objectId = parseOwner.getObjectId();
        m_username = parseOwner.getUsername();
        m_facebookId = parseOwner.getLong("facebookId");
        // TODO: set Arrays too!
    }

    public void saveGroupsToParse(ParseUser parseUser) {
        ParseRelation relation = parseUser.getRelation("groups");
        for (Group group: m_groups) {
            relation.add(group.toParseObject());
        }
    }

    public ParseUser toParseUser()
    {
        ParseUser parseUser = new ParseUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        try {
            parseUser = query.get(m_objectId);
            return parseUser;
        } catch (ParseException e) {
            return parseUser;
        }
    }

    private void saveAllFieldsToParse(ParseUser parseUser) {
        parseUser.put("username", m_username);
        parseUser.put("facebookId", m_facebookId);
        saveGroupsToParse(parseUser);
    }

    public void save(){
        ParseUser parseUser = new ParseUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("facebook_id", m_facebookId);
        try {
            parseUser = query.getFirst();
            saveAllFieldsToParse(parseUser);
            parseUser.saveInBackground();
        } catch (ParseException e) {
            saveAllFieldsToParse(parseUser);
            parseUser.saveInBackground();
            m_objectId = parseUser.getObjectId(); // do I need to do this?
        }
    }

}

